package com.alma.lanternbell.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Mar 24, 2014
 * @author Maxim Kuzovlev
 */
public class BellService extends Service
{
	@Override
	public IBinder onBind( Intent intent )
	{
		return null;	// This service does not support binding
		
	}
	
	private static class CallStateListener extends PhoneStateListener
	{
		@Override
		public void onCallStateChanged( int state_, String incomingNumber_ )
		{
			switch( state_ )
			{
			case TelephonyManager.CALL_STATE_RINGING:
				break;
			}
			
		}
		
	}

}
