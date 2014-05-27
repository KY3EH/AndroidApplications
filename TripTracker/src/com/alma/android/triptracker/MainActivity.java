package com.alma.android.triptracker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.alma.android.triptracker.impl.SatelliteIndicator;
import com.alma.android.triptracker.itf.ListenerItf;
import com.alma.android.triptracker.itf.NotifyPropertiesItf;
import com.alma.android.triptracker.service.TrackerService;
import com.alma.android.triptracker.tool.GpsTools;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity implements ListenerItf, GpsStatus.Listener
{
	private static final String	TAG					= "MainActivity";
	private static final String	COORDINATE_FORMAT	= "#0.0000000";
	private static final String	VELOCITY_FORMAT		= "#0.00";
	private static final String	ALTITUDE_FORMAT		= "#0.0";
	private static final String	DISTANCE_FORMAT		= "#0.0000";
	private static final String	DATE_FORMAT			= "yyyy-MM-dd HH:mm:ss.SSS ZZZZZ";
	private static final String	ELEVATION_FORMAT	= "00";
	private static final String	AZIMUTH_FORMAT		= "000";
	private static final double	KILO				= 1000.0d;
	private static final double	MPS_TO_KPH			= ( 60 * 60 ) / KILO;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState_ )
    {
		Log.i( TAG, "onCreate::entry" );
        super.onCreate( savedInstanceState_ );
        setContentView( R.layout.main );

		m_serviceSwitch	= (Switch)findViewById( R.id.switchService );

		m_serviceSwitch.setOnClickListener( new View.OnClickListener()
												{
													public void onClick( View view_ )
													{
														Log.i( TAG, "switchService::onClick" );

														try
														{
															ProcessOnClick( view_ );

														}
														catch( IOException ex_ )
														{
															Log.e( TAG, "switchService::onClick", ex_ );

														}

													}
												}
											);
		
		m_txtLongitude		= (TextView)findViewById( R.id.gpsLon );
		m_txtLatitude		= (TextView)findViewById( R.id.gpsLat );
		m_txtAltitude		= (TextView)findViewById( R.id.altitude );
		m_txtVelocity		= (TextView)findViewById( R.id.velocity );
		m_txtDeltaDistance	= (TextView)findViewById( R.id.deltaDistance );
		m_txtDistance		= (TextView)findViewById( R.id.distance );
		m_txtAverageVelocity= (TextView)findViewById( R.id.averageVelocity );
		m_txtCurrentTime	= (TextView)findViewById( R.id.curretTime );
		m_txtStartTime		= (TextView)findViewById( R.id.startTime );
		m_btReset			= (Button)findViewById( R.id.reset );
		m_indicatorsLyaout	= (LinearLayout)findViewById( R.id.indicators_lyaout );
		
		m_btReset.setOnClickListener( new View.OnClickListener()
											{
												public void onClick( View v )
												{
													Log.i( TAG, "OnClickListener::onClick::entry" );
													
													Reset();
													
													Log.i( TAG, "OnClickListener::onClick::exit" );
													
												}
												
											}
			
									);

		ResetText();
		
		Log.i( TAG, "onCreate::exit" );
		
	}
	
	private void ResetText()
	{
		Log.i( TAG, "ResetText::entry" );

		m_txtLongitude.setText( "Not available" );
		m_txtLatitude.setText( "Not available" );
		m_txtAltitude.setText( "Not available" );
		m_txtVelocity.setText( "Not available" );
		m_txtDeltaDistance.setText( "Not available" );
		m_txtDistance.setText( "Not available" );
		m_txtAverageVelocity.setText( "Not available" );
		m_txtCurrentTime.setText( "Not available" );
		m_txtStartTime.setText( "Not available" );
		
		Log.i( TAG, "ResetText::exit" );
		
	}
	
	@Override
	protected void onResume()
	{
		Log.i( TAG, "onResume::entry" );
		
		super.onResume();
		ClearSatelliteInfo();
		
		TrackerService	service		= TrackerService.GetInctance();
		boolean			isRunning	= (null != service);

		if( null != service )
		{
			service.AddListener( this );
			
			NotifyPropertiesItf	data	= service.GetLastProperties();
			
			ProcessData( data );
			
			LocationManager	locationManager = service.GetLocationManager();
			
			locationManager.addGpsStatusListener( this );
			
		}
		
		m_serviceSwitch.setChecked( isRunning );
		
		Log.i( TAG, "onResume::exit" );
		
	}
	
	private void StopStatusListener()
	{
		TrackerService	service		= TrackerService.GetInctance();

		if( null != service )
		{
			LocationManager	locationManager = service.GetLocationManager();
			
			locationManager.removeGpsStatusListener( this );
			service.RemoveListener( this );
			
		}
		
		
	}
	
	@Override
	protected void onPause()
	{
		Log.i( TAG, "onPause::entry" );
		
		StopStatusListener();
		
		super.onPause();
		
		Log.i( TAG, "onPause::exit" );
		
	}
	
	private void Reset()
	{
		Log.i( TAG, "onProviderDisabled::Reset" );
		
		ResetText();
		
		TrackerService	service	= TrackerService.GetInctance();
		
		if( null != service )
		{
			service.Reset();
			
		}
		
		Log.i( TAG, "onProviderDisabled::Reset" );

	}
	
	private void ProcessData( NotifyPropertiesItf data_ )
	{
		Log.i( TAG, "ProcessData::entry" );
		
		if( null != data_ )
		{
			Date	startTime				= data_.StartTime();
			String	startTimeValue			= GpsTools.FormatDate( DATE_FORMAT, startTime );
			Date	eventTime				= data_.EventTime();
			String	eventTimeValue			= GpsTools.FormatDate( DATE_FORMAT, eventTime );;
			Double	longitude				= data_.Longitude();
			String	longitudeValue			= GpsTools.FormatDouble( COORDINATE_FORMAT, longitude );
			Double	latitude				= data_.Latitude();
			String	latitudeValue			= GpsTools.FormatDouble( COORDINATE_FORMAT, latitude );
			Double	altitude				= data_.Altitude();
			String	altitudeValue			= GpsTools.FormatDouble( ALTITUDE_FORMAT, altitude );
			Double	distance				= data_.Distance() / KILO;							// km
			String	distanceValue			= GpsTools.FormatDouble( DISTANCE_FORMAT, distance );
			Double	deltaDistance			= data_.DeltaDistance() / KILO;						// km
			String	deltaDistanceValue		= GpsTools.FormatDouble( DISTANCE_FORMAT, deltaDistance );
			Double	instantVelocity			= data_.InstantVelocity() * MPS_TO_KPH;				// km/h
			String	instantVelocityValue	= GpsTools.FormatDouble( VELOCITY_FORMAT, instantVelocity );
			Double	averageVelocity			= data_.AverageVelocity() * MPS_TO_KPH;				// km/h
			String	averageVelocityValue	= GpsTools.FormatDouble( VELOCITY_FORMAT, averageVelocity );
			
			m_txtLongitude.setText( longitudeValue );
			m_txtLatitude.setText( latitudeValue );
			m_txtAltitude.setText( altitudeValue );
			m_txtVelocity.setText( instantVelocityValue );
			m_txtDeltaDistance.setText( deltaDistanceValue );
			m_txtDistance.setText( distanceValue );
			m_txtAverageVelocity.setText( averageVelocityValue );
			m_txtCurrentTime.setText( eventTimeValue );
			m_txtStartTime.setText( startTimeValue );
			
		}
		
		Log.i( TAG, "ProcessData::exit" );
		
	}
	
	public void Notify( NotifyPropertiesItf data_ )
	{
		Log.i( TAG, "Notify::entry" );
		
		ProcessData( data_ );
		
		Log.i( TAG, "Notify::exit" );
		
	}
	
	private synchronized void ProcessOnClick( View view_ ) throws IOException
	{
		Log.i( TAG, "ProcessOnClick" );
		
		TrackerService	service		= TrackerService.GetInctance();
		boolean			isRunning	= (null != service);
		Context			ctx			= getApplicationContext();
		
		if( true == isRunning )
		{
			StopStatusListener();
			ClearSatelliteInfo();
			TrackerService.StopService( ctx );
			m_serviceSwitch.setChecked( false );
			ResetText();
			
		}
		else
		{
			ComponentName	starResult	= TrackerService.StartService( ctx );
			
			if( null != starResult )
			{
				m_serviceSwitch.setChecked( true );
				
				Log.i( TAG, "Service started" );
			}
			else
			{
				m_serviceSwitch.setChecked( false );
				
				Log.e( TAG, "Failed to start service" );
				
			}
			
		}

	}
	
	private void ClearSatelliteInfo()
	{
		m_indicatorsLyaout.removeAllViews();
		m_indicatorMap.clear();
		m_satelliteMap.clear();
		
	}
	
	private void CleareSatelliteMap()
	{
		Set<Integer>	satelliteIds	= m_satelliteMap.keySet();

		for( Integer id : satelliteIds )
		{
			m_satelliteMap.put( id, Boolean.FALSE );

		}
			
	}
	
	private void AddSatellite( GpsSatellite satellite_ )
	{
		Log.i( TAG, "AddSatellite::entry" );
		
		int					satelliteNumber	= satellite_.getPrn();
		Integer				satelliteId		= Integer.valueOf( satelliteNumber );
		SatelliteIndicator	indicator		= m_indicatorMap.get( satelliteId );
		
		if( null == indicator )
		{
			indicator	= new SatelliteIndicator( this, satellite_ );
			
			m_indicatorMap.put( satelliteId, indicator );
			
		}
		else
		{
			indicator.Satellite( satellite_ );
			
		}

		m_satelliteMap.put( satelliteId, Boolean.TRUE );
		
		Log.i( TAG, "AddSatellite::exit" );
		
	}
	
	private void RemoveSatellites()
	{
		Set<Integer>	satelliteIds	= m_satelliteMap.keySet();

		for( Integer id : satelliteIds )
		{
			Boolean	isUpdated	= m_satelliteMap.get( id );
			
			if( false == isUpdated )
			{
				View	exparedView	= m_indicatorMap.get( id );
				
				m_indicatorsLyaout.removeView( exparedView );
				m_satelliteMap.remove( id );
				m_indicatorMap.remove( id );
				
			}

		}
			
		
	}
	
	public void onGpsStatusChanged( int event_ )
	{
		Log.i( TAG, "onGpsStatusChanged::entry" );
		
		TrackerService	service			= TrackerService.GetInctance();
		LocationManager	locationManager = service.GetLocationManager();
		GpsStatus		gpsStatus		= locationManager.getGpsStatus( null );		

		if( null != gpsStatus )
		{
			Iterable<GpsSatellite>	satellites	= gpsStatus.getSatellites();
			
			CleareSatelliteMap();

			for( GpsSatellite satellite : satellites )
			{
				AddSatellite( satellite );
				
			}
			
			RemoveSatellites();

		}
		
		Log.i( TAG, "onGpsStatusChanged::exit" );
		
	}
	
	private Switch				m_serviceSwitch;
	private TextView			m_txtLongitude;
	private TextView			m_txtLatitude;
	private TextView			m_txtAltitude;
	private TextView			m_txtVelocity;
	private TextView			m_txtDistance;
	private TextView			m_txtDeltaDistance;
	private TextView			m_txtCurrentTime;
	private TextView			m_txtStartTime;
	private TextView			m_txtAverageVelocity;
	private Button				m_btReset;
	private LinearLayout		m_indicatorsLyaout;
	
	private final Map<Integer, Boolean>				m_satelliteMap	= Collections.synchronizedMap( new HashMap<Integer, Boolean>() );
	private final Map<Integer, SatelliteIndicator>	m_indicatorMap	= Collections.synchronizedMap( new HashMap<Integer, SatelliteIndicator>() );
	
}
