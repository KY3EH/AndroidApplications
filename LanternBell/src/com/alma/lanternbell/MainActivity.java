package com.alma.lanternbell;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import com.alma.lanternbell.service.BellService;
import com.alma.lanternbell.service.PersistentBoolean;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener
{
	private static final String	TAG	= "MainActivity";
	
	@Override
	public void onCreate( Bundle savedInstanceState_ )
	{
		Log.i( TAG, "onCreate" );
		
		super.onCreate( savedInstanceState_ );
		
		try
		{
			setContentView( R.layout.main );

			m_serviceSwitch	= (Switch)findViewById( R.id.switchService );
			m_isRunning		= PersistentBoolean.CreateInstance( this, BellService.VALUE_NAME );

			boolean	isRunning	= isServiceRunning( BellService.class );
			
			m_serviceSwitch.setChecked( isRunning );
			m_isRunning.Value( isRunning );
			m_serviceSwitch.setOnClickListener( this );

		}
		catch( IOException ex_ )
		{
			Log.e( TAG, "onCreate", ex_ );
			
		}
		
	}
	
	public void onClick( View view_ )
	{
		Log.i( TAG, "onClick" );

		try
		{
			ProcessOnClick( view_ );

		}
		catch( IOException ex_ )
		{
			Log.e( TAG, "onClick", ex_ );

		}

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
	
	private synchronized void ProcessOnClick( View view_ ) throws IOException
	{
		Log.i( TAG, "ProcessOnClick" );
		
		Context	ctx			= getApplicationContext();
		boolean	isRunning	= m_isRunning.Value();
		
		if( true == isRunning )
		{
			BellService.StopService( ctx );
			m_isRunning.Value( false );
			m_serviceSwitch.setChecked( false );
			
		}
		else
		{
			ComponentName	starResult	= BellService.StartService( ctx );
			
			if( null != starResult )
			{
				m_isRunning.Value( true );
				m_serviceSwitch.setChecked( true );
				
			}
			
		}

	}

	private Switch				m_serviceSwitch;
	private PersistentBoolean	m_isRunning;
	
}
