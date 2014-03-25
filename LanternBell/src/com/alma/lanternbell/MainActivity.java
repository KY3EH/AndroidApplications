package com.alma.lanternbell;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.alma.lanternbell.service.BellService;
import java.util.List;

public class MainActivity extends Activity
{
	private static final String	TAG			= "MainActivity";
	private static final String	CAPTION_ON	= "On";
	private static final String	CAPTION_OFF	= "Off";
	
	@Override
	public void onCreate( Bundle savedInstanceState_ )
	{
		Log.i( TAG, "onCreate" );
		
		super.onCreate( savedInstanceState_ );
		setContentView( R.layout.main );
		
		m_isRunning		= isServiceRunning( BellService.class );
		m_serviceButton	= (Button)findViewById( R.id.ButtonSERVICE );
		
		String	caption	= true == m_isRunning ? CAPTION_OFF : CAPTION_ON;
		
		m_serviceButton.setText( caption );
		
		m_serviceButton.setOnClickListener( new OnClickListener()
											{
												public void onClick( View view_ )
												{
													Log.i( TAG, "onClick" );

													ProcessOnClick( view_ );

												}
												
											} );		

	}
	
	private boolean isServiceRunning( Class class_ )
	{
		Log.i( TAG, "isServiceRunning" );
		
		boolean										result			= false;
		String										className		= class_.getName();
		ActivityManager								manager			= (ActivityManager) getSystemService( Context.ACTIVITY_SERVICE );
		List<ActivityManager.RunningServiceInfo>	runningServices	= manager.getRunningServices( Integer.MAX_VALUE );
		
		for( ActivityManager.RunningServiceInfo service : runningServices )
		{
			String	serviceName	= service.service.getClassName();
			
			if( className.equals( serviceName ) )
			{
				result	= true;
				
				break;
				
			}
			
		}
    
		return result;
		
	}	
	
	private synchronized void ProcessOnClick( View view_ )
	{
		Log.i( TAG, "ProcessOnClick" );
		
		Context			ctx			= getApplicationContext();
		Intent			intent		= new Intent( ctx, BellService.class );
		
		if( true == m_isRunning )
		{
			stopService( intent );
			m_isRunning	= false;
			m_serviceButton.setText( CAPTION_ON );
			
		}
		else
		{
			ComponentName	starResult	= startService( intent );
			
			if( null != starResult )
			{
				m_isRunning	= true;
				m_serviceButton.setText( CAPTION_OFF );
				
			}
			
		}

	}
	
	private Button	m_serviceButton;
	private boolean	m_isRunning;
	
}
