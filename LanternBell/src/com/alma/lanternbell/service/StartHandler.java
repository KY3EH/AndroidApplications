package com.alma.lanternbell.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.IOException;

/**
 * Mar 26, 2014
 * @author Maxim Kuzovlev
 */
public class StartHandler extends BroadcastReceiver
{
	private static final String	TAG	= "StartHandler";
	
	@Override
	public void onReceive( Context context_, Intent intent_ )
	{
		Log.i( TAG, "onReceive" );
		
		try
		{
			PersistentBoolean	isRunning		= PersistentBoolean.CreateInstance( context_, BellService.VALUE_NAME );
			boolean				isRunningValue	= isRunning.Value();
			
			if( true == isRunningValue )
			{
				Intent			intent		= new Intent( context_, BellService.class );
				ComponentName	starResult	= context_.startService( intent );
			
				if( null == starResult )
				{
					Log.e( TAG, "onReceive: Error to start service BellService" );

				}
				
			}
			
		}
		catch( IOException ex_ )
		{
			Log.e( TAG, "onReceive", ex_ );
			
		}

	}

}
