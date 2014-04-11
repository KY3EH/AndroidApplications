package com.alma.android.triptracker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import com.alma.android.triptracker.itf.ListenerItf;
import com.alma.android.triptracker.itf.NotifyPropertiesItf;
import com.alma.android.triptracker.service.TrackerService;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends Activity implements ListenerItf
{
	private static final String TAG					= "MainActivity";
	private static final String COORDINATE_FORMAT	= "#0.0000000";
	private static final String VELOCITY_FORMAT		= "#0.00";
	private static final String ALTITUDE_FORMAT		= "#0.0";
	private static final String DISTANCE_FORMAT		= "#0.0000";
	private static final String DATE_FORMAT			= "yyyy-MM-dd HH:mm:ss.SSS ZZZZZ";
	private static final double NANOS_TO_HOURS		= 1000.0d * 1000.0d * 1000.0d * 60.0d * 60.0d;
	
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
		m_btReset			= (Button)findViewById( R.id.reset );

		m_btReset.setOnClickListener( new View.OnClickListener()
											{
												public void onClick( View v )
												{
													Reset();
													
												}
												
											}
			
									);

		m_txtLongitude.setText( "Not available" );
		m_txtLatitude.setText( "Not available" );
		m_txtAltitude.setText( "Not available" );
		m_txtVelocity.setText( "Not available" );
		m_txtDeltaDistance.setText( "Not available" );
		m_txtDistance.setText( "Not available" );
		m_txtAverageVelocity.setText( "Not available" );
		m_txtCurrentTime.setText( "Not available" );
			
		Log.i( TAG, "onCreate::exit" );
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		TrackerService	service		= TrackerService.GetInctance();
		boolean			isRunning	= (null != service);

		if( null != service )
		{
			service.AddListener( this );
			
		}
		
		m_serviceSwitch.setChecked( isRunning );
		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		TrackerService	service		= TrackerService.GetInctance();

		if( null != service )
		{
			service.RemoveListener( this );
			
		}
		
	}
	
//		Log.i( TAG, "::entry" );
//		Log.i( TAG, "::exit" );
	private void Reset()
	{
		Log.i( TAG, "onProviderDisabled::Reset" );
		
		m_txtLongitude.setText( "Not available" );
		m_txtLatitude.setText( "Not available" );
		m_txtAltitude.setText( "Not available" );
		m_txtVelocity.setText( "Not available" );
		m_txtDeltaDistance.setText( "Not available" );
		m_txtDistance.setText( "Not available" );
		m_txtAverageVelocity.setText( "Not available" );
		m_txtCurrentTime.setText( "Not available" );
		
		TrackerService	service	= TrackerService.GetInctance();
		
		if( null != service )
		{
			service.Reset();
			
		}
		
		Log.i( TAG, "onProviderDisabled::Reset" );

	}
	
	public void Notify( NotifyPropertiesItf data_ )
	{
		if( null != data_ )
		{
			Date	startTime		= data_.StartTime();
			Date	eventTime		= data_.EventTime();
			Double	longitude		= data_.Longitude();
			Double	latitude		= data_.Latitude();
			Double	altitude		= data_.Altitude();
			Double	distance		= data_.Distance();
			Double	deltaDistance	= data_.DeltaDistance();
			Double	instantVelocity	= data_.InstantVelocity();
			Double	averageVelocity	= data_.AverageVelocity();
			
		}
		
	}

	private synchronized void ProcessOnClick( View view_ ) throws IOException
	{
		Log.i( TAG, "ProcessOnClick" );
		
		TrackerService	service		= TrackerService.GetInctance();
		boolean			isRunning	= (null != service);
		Context			ctx			= getApplicationContext();
		
		if( true == isRunning )
		{
			TrackerService.StopService( ctx );
			m_serviceSwitch.setChecked( false );
			
		}
		else
		{
			ComponentName	starResult	= TrackerService.StartService( ctx );
			
			if( null != starResult )
			{
				m_serviceSwitch.setChecked( true );
				
			}
			
		}

	}
	
	private Switch				m_serviceSwitch;
	private TextView			m_txtLongitude;
	private TextView			m_txtLatitude;
	private TextView			m_txtAltitude;
	private TextView			m_txtVelocity;
	private TextView			m_txtDistance;
	private TextView			m_txtDeltaDistance;
	private TextView			m_txtCurrentTime;
	private TextView			m_txtAverageVelocity;
	private Button				m_btReset;
	
}
