package com.alma.android.triptracker.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.alma.android.triptracker.MainActivity;
import com.alma.android.triptracker.R;
import com.alma.android.triptracker.itf.ListenerItf;
import com.alma.android.triptracker.itf.NotifyPropertiesItf;
import com.alma.android.triptracker.itf.TrackerServiceItf;
import com.alma.android.triptracker.tool.GpsTools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Apr 11, 2014
 * @author Maxim Kuzovlev
 */
public class TrackerService extends Service implements LocationListener, TrackerServiceItf
{
	private static final String	TAG					= "TrackerService";
	private static final double NANOS_TO_SECUNDS	= 1000.0d * 1000.0d * 1000.0d;
	
	private static TrackerService	s_instance	= null;
	
	public static TrackerService GetInctance()
	{
		return s_instance;
		
	}
	
	public static ComponentName StartService( Context context_ )
	{
		Log.i( TAG, "StartService" );
		
		Intent			intent	= new Intent( context_, TrackerService.class );
		ComponentName	result	= context_.startService( intent );
		
		if( null != result )
		{
			AddNotification( context_ );
			
		}
		
		return result;
		
	}
	
	public static void StopService( Context context_ )
	{
		Log.i( TAG, "StopService" );
		
		Intent	intent	= new Intent( context_, TrackerService.class );
		boolean	result	= context_.stopService( intent );
		
		if( false == result )
		{
			Log.i( TAG, "Service is not running" );
			
		}
		
		RemoveNotification( context_ );
		
	}

	@Override
	public void onCreate()
	{
		Log.i( TAG, "onCreate" );

		super.onCreate(); //To change body of generated methods, choose Tools | Templates.
		
	}
	
	@Override
	public int onStartCommand( Intent intent_, int flags_, int startId_ )
	{
		Log.i( TAG, "onStartCommand" );

		LocationManager	gpsService		= (LocationManager)getSystemService( LOCATION_SERVICE );
		boolean			isEnabled		= gpsService.isProviderEnabled( LocationManager.GPS_PROVIDER );
		
		if( true == isEnabled )
		{
			gpsService.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0L, 0.1f, this );
			
			s_instance	= this;
			
			Intent	intent	= new Intent( this, MainActivity.class );
			
			intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
			startActivity( intent );			
			
		}
		else
		{
			stopSelf();
			
		}
		
		Log.i( TAG, "onCreate::exit" );
		
		return Service.START_STICKY;
		
	}
	
	@Override
	public void onDestroy()
	{
		Log.i( TAG, "onDestroy" );

		LocationManager	gpsService		= (LocationManager)getSystemService( LOCATION_SERVICE );
		boolean			isEnabled		= gpsService.isProviderEnabled( LocationManager.GPS_PROVIDER );
		
		if( true == isEnabled )
		{
			gpsService.removeUpdates( this );
			
		}
		
		s_instance	= null;
		
		super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
		
	}
	
	@Override
	public IBinder onBind( Intent intent )
	{
		Log.i( TAG, "onBind::entry" );
		
		Log.i( TAG, "onBind::exit" );
		
		return null;
		
	}

	public void onLocationChanged( Location location_ )
	{
		Log.i( TAG, "onLocationChanged::entry" );
		
		long	elapsedTime	= location_.getElapsedRealtimeNanos();
		
		if( null == m_elapsedStartTime )
		{
			m_elapsedStartTime	= Long.valueOf( elapsedTime );
			
		}
		
		long	currentTime	= location_.getTime();
		Date	eventTime	= new Date( currentTime  );
		
		if( null == m_startTime )
		{
			m_startTime	= eventTime;
			
		}
		
		double			longitude			= location_.getLongitude();
		double			latitude			= location_.getLatitude();
		double			altitude			= location_.getAltitude();
		double			velocity			= location_.getSpeed();
		double			deltaDistance		= GpsTools.GetDistance( m_latitude, m_longitude, latitude, longitude );
		
		if( false == Double.isNaN( deltaDistance ) )
		{
			m_distance	+= deltaDistance;
			
		}
		
		double	deltaTime		= (elapsedTime - m_elapsedStartTime) / NANOS_TO_SECUNDS;
		double	averageVelocity	= m_distance / deltaTime;
		
		m_longitude	= longitude;
		m_latitude	= latitude;
		m_lastProperties	= new NotifyPropertiesImpl( m_startTime,
														eventTime,
														longitude,
														latitude,
														altitude,
														m_distance,
														deltaDistance,
														velocity,
														averageVelocity );
		
		for( ListenerItf listener : m_listeners )
		{
			try
			{
				listener.Notify( m_lastProperties );
				
			}
			catch( Throwable th_ )
			{
				Log.e( TAG, "", th_ );
				
			}

		}
		
		Log.i( TAG, "onLocationChanged::exit" );

	}
	
	public void onStatusChanged( String provider, int status, Bundle extras )
	{
		Log.i( TAG, "onStatusChanged::entry" );
		
		Log.i( TAG, "onStatusChanged::exit" );

	}

	public void onProviderEnabled( String provider )
	{
		Log.i( TAG, "onProviderEnabled::entry" );
		
		Log.i( TAG, "onProviderEnabled::exit" );

	}

	public void onProviderDisabled( String provider )
	{
		Log.i( TAG, "onProviderDisabled::entry" );
		
		Log.i( TAG, "onProviderDisabled::exit" );

	}
	
	public void AddListener( ListenerItf listener_ )
	{
		Log.i( TAG, "AddListener::entry" );
		
		boolean	rc	= m_listeners.remove( listener_ );
		
		if( true == rc )
		{
			Log.i( TAG, "AddListener old instance of the same listener removed" );
			
		}
		
		m_listeners.add( listener_ );

		Log.i( TAG, "AddListener::exit" );

	}

	public boolean RemoveListener( ListenerItf listener_ )
	{
		Log.i( TAG, "RemoveListener::entry" );
		
		boolean	result	= m_listeners.remove( listener_ );
		
		Log.i( TAG, "RemoveListener::exit" );

		return result;

	}
	
	public NotifyPropertiesItf GetLastProperties()
	{
		Log.i( TAG, "GetLastProperties::entry" );
		
		Log.i( TAG, "GetLastProperties::exit" );

		return m_lastProperties;
		
	}
	
	public void Reset()
	{
		Log.i( TAG, "Reset::entry" );
		
		m_distance			= 0.0d;
		m_elapsedStartTime	= null;
		m_startTime			= null;
		m_lastProperties	= null;
		m_longitude			= Double.NaN;
		m_latitude			= Double.NaN;
		
		Log.i( TAG, "Reset::exit" );

	}
	
	private static void AddNotification( Context context_ )
	{
		Log.i( TAG, "AddNotification" );
		
		Intent					intent				= new Intent( context_, MainActivity.class );
		PendingIntent			pendingIntent		= PendingIntent.getActivity( context_ , 0, intent, 0 );
		Notification.Builder	notificationBuilder	= new Notification.Builder( context_ );
		Resources				resources			= context_.getResources();
		Bitmap					largeIcon			= BitmapFactory.decodeResource( resources, R.drawable.ic_launcher );
		String					applicationName		= context_.getResources().getString( R.string.app_name );
		String					contentText			= context_.getResources().getString( R.string.content_text );
	
		notificationBuilder.setSmallIcon( R.drawable.ic_arrow );
		notificationBuilder.setLargeIcon( largeIcon );
		notificationBuilder.setContentIntent( pendingIntent );
		notificationBuilder.setAutoCancel( false );
		notificationBuilder.setOngoing( true );
		notificationBuilder.setContentText( contentText );
		notificationBuilder.setContentTitle( applicationName );
		notificationBuilder.setShowWhen( false );
		
		Notification		notification		= notificationBuilder.build();
		NotificationManager	notificationManager = (NotificationManager) context_.getSystemService( Context.NOTIFICATION_SERVICE );

		notificationManager.notify( TAG, 0, notification );

	}
	
	private static void RemoveNotification( Context context_ )
	{
		Log.i( TAG, "RemoveNotification" );
		
		NotificationManager	notificationManager = (NotificationManager) context_.getSystemService( Context.NOTIFICATION_SERVICE );

		notificationManager.cancel( TAG, 0 );

	}
	
	private static class NotifyPropertiesImpl implements NotifyPropertiesItf
	{
		private static final String	TAG	= "TrackerService";
		
		public Date StartTime()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_startTime;
			
		}

		public Date EventTime()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_eventTime;
			
		}

		public Double Longitude()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_longitude;
			
		}

		public Double Latitude()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_latitude;
			
		}

		public Double Altitude()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_altitude;
			
		}

		public Double Distance()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_distance;
			
		}

		public Double DeltaDistance()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_deltaDistance;
			
		}

		public Double InstantVelocity()
		{
			Log.i( TAG, "::entry" );

			Log.i( TAG, "::exit" );
			
			return m_instantVelocity;
			
		}

		public Double AverageVelocity()
		{
			Log.i( TAG, "AverageVelocity::entry" );

			Log.i( TAG, "AverageVelocity::exit" );
			
			return m_averageVelocity;
			
		}
		
		private NotifyPropertiesImpl( Date startTime_,
										Date eventTime_,
										Double longitude_,
										Double latitude_,
										Double altitude_,
										Double distance_,
										Double deltaDistance_,
										Double instantVelocity_,
										Double averageVelocity_ )
		{
			m_startTime			= startTime_;
			m_eventTime			= eventTime_;
			m_longitude			= longitude_;
			m_latitude			= latitude_;
			m_altitude			= altitude_;
			m_distance			= distance_;
			m_deltaDistance		= deltaDistance_;
			m_instantVelocity	= instantVelocity_;
			m_averageVelocity	= averageVelocity_;
			
		}
		
		private final Date		m_startTime;
		private final Date		m_eventTime;
		private final Double	m_longitude;
		private final Double	m_latitude;
		private final Double	m_altitude;
		private final Double	m_distance;
		private final Double	m_deltaDistance;
		private final Double	m_instantVelocity;
		private final Double	m_averageVelocity;
		
	}
	
	private NotifyPropertiesImpl	m_lastProperties;
	
	private Date			m_startTime			= null;
	private Long			m_elapsedStartTime	= null;
	private Double			m_longitude			= Double.NaN;
	private Double			m_latitude			= Double.NaN;
	private double			m_distance			= 0.0;
	private final List<ListenerItf>	m_listeners	= Collections.synchronizedList( new ArrayList<ListenerItf>() );
	
}
