package com.alma.android.triptracker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements ListenerItf, GpsStatus.Listener
{
	private static final int	MAXIMUM_SATILLITES	= 10;
	private static final String	TAG					= "MainActivity";
	private static final String	SIGNAL_NOISE_FORMAT	= "00.0";
	private static final String	COORDINATE_FORMAT	= "#0.0000000";
	private static final String	VELOCITY_FORMAT		= "#0.00";
	private static final String	ALTITUDE_FORMAT		= "#0.0";
	private static final String	DISTANCE_FORMAT		= "#0.0000";
	private static final String	DATE_FORMAT			= "yyyy-MM-dd HH:mm:ss.SSS ZZZZZ";
	private static final double	KILO				= 1000.0d;
	private static final double	MPS_TO_KPH			= ( 60 * 60 ) / KILO;
	private static final int[]	SATILLITE_NUMBER_ID	= { R.id.txt_satillite001, R.id.txt_satillite002,
														R.id.txt_satillite003, R.id.txt_satillite004,
														R.id.txt_satillite005, R.id.txt_satillite006,
														R.id.txt_satillite007, R.id.txt_satillite008,
														R.id.txt_satillite009, R.id.txt_satillite010 };
	
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
		
		for( int i = 0 ; i < MAXIMUM_SATILLITES ; ++i )
		{
			m_satilliteNumber[ i ]	= (TextView)findViewById( SATILLITE_NUMBER_ID[ i ] );
			
		}

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
	
	@Override
	protected void onPause()
	{
		Log.i( TAG, "onPause::entry" );
		
		super.onPause();
		TrackerService	service		= TrackerService.GetInctance();

		if( null != service )
		{
			LocationManager	locationManager = service.GetLocationManager();
			
			locationManager.removeGpsStatusListener( this );
			service.RemoveListener( this );
			
		}
		
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
			String	startTimeValue			= FormatDate( DATE_FORMAT, startTime );
			Date	eventTime				= data_.EventTime();
			String	eventTimeValue			= FormatDate( DATE_FORMAT, eventTime );;
			Double	longitude				= data_.Longitude();
			String	longitudeValue			= FormatDouble( COORDINATE_FORMAT, longitude );
			Double	latitude				= data_.Latitude();
			String	latitudeValue			= FormatDouble( COORDINATE_FORMAT, latitude );
			Double	altitude				= data_.Altitude();
			String	altitudeValue			= FormatDouble( ALTITUDE_FORMAT, altitude );
			Double	distance				= data_.Distance() / KILO;							// km
			String	distanceValue			= FormatDouble( DISTANCE_FORMAT, distance );
			Double	deltaDistance			= data_.DeltaDistance() / KILO;						// km
			String	deltaDistanceValue		= FormatDouble( DISTANCE_FORMAT, deltaDistance );
			Double	instantVelocity			= data_.InstantVelocity() * MPS_TO_KPH;				// km/h
			String	instantVelocityValue	= FormatDouble( VELOCITY_FORMAT, instantVelocity );
			Double	averageVelocity			= data_.AverageVelocity() * MPS_TO_KPH;				// km/h
			String	averageVelocityValue	= FormatDouble( VELOCITY_FORMAT, averageVelocity );
			
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
	
	private String FormatDouble( String pattern_, Double value_ )
	{
		Log.i( TAG, "FormatDouble::entry" );
		
		String			result	= null;
		
		if( null != value_ )
		{
			DecimalFormat	format	= new DecimalFormat( pattern_ );
			
			result	= format.format( value_ );
			
		}
		
		Log.i( TAG, "FormatDouble::exit" );
		
		return result;
		
	}

	private String FormatDate( String pattern_, Date value_ )
	{
		Log.i( TAG, "FormatDate::entry" );
		
		String			result	= null;
		
		if( null != value_ )
		{
			SimpleDateFormat	format	= new SimpleDateFormat( pattern_ );
			
			result	= format.format( value_ );
			
		}
		
		Log.i( TAG, "FormatDate::exit" );
		
		return result;
		
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
	public void onGpsStatusChanged( int event_ )
	{
		Log.i( TAG, "onGpsStatusChanged::entry" );
		
		TrackerService	service			= TrackerService.GetInctance();
		LocationManager	locationManager = service.GetLocationManager();
		GpsStatus		gpsStatus		= locationManager.getGpsStatus( null );		

		if(null != gpsStatus )
		{
			Iterable<GpsSatellite>	satellites	= gpsStatus.getSatellites();

			for( GpsSatellite satellite : satellites )
			{
				int	number	= 0;

				float			signalNoise	= satellite.getSnr();
				DecimalFormat	format		= new DecimalFormat( SIGNAL_NOISE_FORMAT );
				String			value		= format.format( signalNoise );
				
				m_satilliteNumber[ number ].setText( value );

				number += 1;

				if( number > MAXIMUM_SATILLITES )
				{
					break;

				}
				
			}

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
	private TextView[]			m_satilliteNumber		= new TextView[ MAXIMUM_SATILLITES ];
	
	
}
