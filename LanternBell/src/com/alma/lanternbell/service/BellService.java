package com.alma.lanternbell.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.alma.lanternbell.lantern.ImpulsLantern;

/**
 * Mar 24, 2014
 * @author Maxim Kuzovlev
 */
public class BellService extends Service
{
	@Override
	public void onCreate()
	{
		super.onCreate(); //To change body of generated methods, choose Tools | Templates.
		
	}
	
	@Override
	public int onStartCommand( Intent intent_, int flags_, int startId_ )
	{
		Context				ctx					= getApplicationContext();
		TelephonyManager	telephonyManager	= (TelephonyManager) ctx.getSystemService( Context.TELEPHONY_SERVICE );
		
		telephonyManager.listen( m_stateListener, PhoneStateListener.LISTEN_CALL_STATE );
		
		return Service.START_STICKY;
		
	}
	
	@Override
	public void onDestroy()
	{
		Context				ctx					= getApplicationContext();
		TelephonyManager	telephonyManager	= (TelephonyManager) ctx.getSystemService( Context.TELEPHONY_SERVICE );
		
		telephonyManager.listen( m_stateListener, PhoneStateListener.LISTEN_NONE );
		m_stateListener.StopLanternBell();
		
		super.onDestroy(); //To change body of generated methods, choose Tools | Templates.
		
	}
	
	@Override
	public IBinder onBind( Intent intent )
	{
		return null;	// This service does not support binding
		
	}
	
	private class CallStateListener extends PhoneStateListener
	{
		@Override
		public void onCallStateChanged( int state_, String incomingNumber_ )
		{
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
			m_camera	= Camera.open();
			m_lantern	= new ImpulsLantern( m_camera, 50, 1000 );
			
			m_lantern.TurnOn();
			

		}
		
		private void StopLanternBell()
		{
			if( null != m_lantern )
			{
				m_lantern.TurnOff();
				m_camera.release();
				
				m_camera	= null;
				m_lantern	= null;
			}

		}
		
		private Camera			m_camera;
		private ImpulsLantern	m_lantern;
		
	}
	
	private final CallStateListener	m_stateListener	= new CallStateListener();

}
