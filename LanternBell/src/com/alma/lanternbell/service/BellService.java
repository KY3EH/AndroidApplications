package com.alma.lanternbell.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.alma.lanternbell.MainActivity;
import com.alma.lanternbell.R;
import com.alma.lanternbell.lantern.ImpulsLantern;

/**
 * Mar 24, 2014
 * @author Maxim Kuzovlev
 */
public class BellService extends Service
{
	public static final String	VALUE_NAME	= "isRunning";
	private static final String	TAG			= "BellService";
	
	public static ComponentName StartService( Context context_ )
	{
		Log.i( TAG, "StartService" );
		
		Intent			intent	= new Intent( context_, BellService.class );
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
		
		Intent	intent	= new Intent( context_, BellService.class );
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

		Context				ctx					= getApplicationContext();
		TelephonyManager	telephonyManager	= (TelephonyManager) ctx.getSystemService( Context.TELEPHONY_SERVICE );
		
		telephonyManager.listen( m_stateListener, PhoneStateListener.LISTEN_CALL_STATE );
		
		return Service.START_STICKY;
		
	}
	
	@Override
	public void onDestroy()
	{
		Log.i( TAG, "onDestroy" );

		Context				ctx					= getApplicationContext();
		TelephonyManager	telephonyManager	= (TelephonyManager) ctx.getSystemService( Context.TELEPHONY_SERVICE );
		
		telephonyManager.listen( m_stateListener, PhoneStateListener.LISTEN_NONE );
		m_stateListener.StopLanternBell();
		
		super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
		
	}
	
	@Override
	public IBinder onBind( Intent intent_ )
	{
		Log.i( TAG, "onBind" );

		return null;	// This service does not support binding
		
	}
	
	private static void AddNotification( Context context_ )
	{
		Log.i( TAG, "AddNotification" );
		
		Intent					intent				= new Intent( context_, MainActivity.class );
		PendingIntent			pendingIntent		= PendingIntent.getActivity( context_ , 0, intent, 0 );
		Notification.Builder	notificationBuilder	= new Notification.Builder( context_ );

		notificationBuilder.setSmallIcon( R.drawable.ic_hang );
		notificationBuilder.setContentIntent( pendingIntent );
		notificationBuilder.setAutoCancel( false );
		notificationBuilder.setOngoing( true );
		notificationBuilder.setContentText( "Tap to start service manager" );
		notificationBuilder.setContentTitle( "Lantern bell is On" );
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
	
	private class CallStateListener extends PhoneStateListener
	{
		@Override
		public void onCallStateChanged( int state_, String incomingNumber_ )
		{
			Log.i( TAG, "onCallStateChanged" );

			switch( state_ )
			{
			case TelephonyManager.CALL_STATE_RINGING:
				StartLanternBell();
				break;
				
			case TelephonyManager.CALL_STATE_IDLE:
			case TelephonyManager.CALL_STATE_OFFHOOK:
				StopLanternBell();
				break;
				
			}
			
		}
		
		private void StartLanternBell()
		{
			Log.i( TAG, "StartLanternBell" );

			m_camera	= Camera.open();
			
			m_lantern.TurnOn( m_camera );
			

		}
		
		private void StopLanternBell()
		{
			Log.i( TAG, "StopLanternBell" );

			if( null != m_camera )
			{
				m_lantern.TurnOff();
				m_camera.release();
				
				m_camera	= null;
				
			}

		}
		
		private Camera				m_camera;
		private final ImpulsLantern	m_lantern	= new ImpulsLantern( 10, 1000 );
		
	}
	
	private final CallStateListener	m_stateListener	= new CallStateListener();

}
