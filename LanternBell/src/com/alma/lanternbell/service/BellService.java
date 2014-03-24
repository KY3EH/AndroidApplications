package com.alma.lanternbell.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Mar 24, 2014
 * @author Maxim Kuzovlev
 */
public class BellService extends Service
{
	private static final String	TAG	= "BellService";
	
	@Override
	public void onCreate()
	{
		super.onCreate(); //To change body of generated methods, choose Tools | Templates.
		
		Log.e( TAG, "onCreate" );
		
	}
	
	@Override
	public int onStartCommand( Intent intent_, int flags_, int startId_ )
	{
		Log.e( TAG, "onStartCommand" );
		
		Context				ctx					= getApplicationContext();
		TelephonyManager	telephonyManager	= (TelephonyManager) ctx.getSystemService( Context.TELEPHONY_SERVICE );
		
		telephonyManager.listen( m_stateListener, PhoneStateListener.LISTEN_CALL_STATE );
		
		return Service.START_STICKY;
		
	}
	
	@Override
	public void onDestroy()
	{
		Log.e( TAG, "OnDestroy" );
		
		Context				ctx					= getApplicationContext();
		TelephonyManager	telephonyManager	= (TelephonyManager) ctx.getSystemService( Context.TELEPHONY_SERVICE );
		
		telephonyManager.listen( m_stateListener, PhoneStateListener.LISTEN_NONE );
		
		super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
		
	}
	
	@Override
	public IBinder onBind( Intent intent )
	{
		Log.e( TAG, "onBind" );
		
		return null;	// This service does not support binding
		
	}
	
	private static class CallStateListener extends PhoneStateListener
	{
		@Override
		public void onCallStateChanged( int state_, String incomingNumber_ )
		{
			Log.e( TAG, "onCallStateChanged" );
			
			switch( state_ )
			{
			case TelephonyManager.CALL_STATE_RINGING:
				Log.d( TAG, "CALL_STATE_RINGING" );
				break;
				
			case TelephonyManager.CALL_STATE_IDLE:
				Log.d( TAG, "CALL_STATE_IDLE" );
				break;
				
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.d( TAG, "CALL_STATE_OFFHOOK" );
				break;
				
			}
			
		}
		
	}
	
	private final CallStateListener	m_stateListener	= new CallStateListener();

}
