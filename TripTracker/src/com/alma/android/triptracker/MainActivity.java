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
import android.widget.Switch;
import android.widget.TextView;
import com.alma.android.triptracker.itf.ListenerItf;
import com.alma.android.triptracker.itf.NotifyPropertiesItf;
import com.alma.android.triptracker.service.TrackerService;
import com.alma.android.triptracker.tool.GpsTools;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements ListenerItf, GpsStatus.Listener
{
	private static final String	TAG					= "MainActivity";
	private static final String	COORDINATE_FORMAT	= "#0.0000000";
	private static final String	VELOCITY_FORMAT		= "#0.00";
	private static final String	ALTITUDE_FORMAT		= "#0.0";
	private static final String	DISTANCE_FORMAT		= "#0.0000";
	private static final String	DATE_FORMAT			= "yyyy-MM-dd HH:mm:ss.SSS ZZZZZ";
	private static final double	KILO				= 1000.0d;
	private static final double	MPS_TO_KPH			= ( 60 * 60 ) / KILO;
	private static final int[]	SATELLITE_NUMBER_ID	= { R.id.txt_satellite001, R.id.txt_satellite002,
														R.id.txt_satellite003, R.id.txt_satellite004,
														R.id.txt_satellite005, R.id.txt_satellite006,
														R.id.txt_satellite007, R.id.txt_satellite008,
														R.id.txt_satellite009, R.id.txt_satellite010,
														R.id.txt_satellite011, R.id.txt_satellite012,
														R.id.txt_satellite013, R.id.txt_satellite014,
														R.id.txt_satellite015, R.id.txt_satellite016,
														R.id.txt_satellite017, R.id.txt_satellite018,
														R.id.txt_satellite019, R.id.txt_satellite020 };

	private static final int[]	IMAGE_ID			= { R.drawable.ic_level000,
														R.drawable.ic_level001, R.drawable.ic_level002,
														R.drawable.ic_level003, R.drawable.ic_level004,
														R.drawable.ic_level005, R.drawable.ic_level006,
														R.drawable.ic_level007, R.drawable.ic_level008,
														R.drawable.ic_level009, R.drawable.ic_level010,};
	private static final int	MAXIMUM_LEVEL		= IMAGE_ID.length - 1;
	private static final int[]	IMAGE_VIEW_ID		= { R.id.ic_level001, R.id.ic_level002,
														R.id.ic_level003, R.id.ic_level004,
														R.id.ic_level005, R.id.ic_level006,
														R.id.ic_level007, R.id.ic_level008,
														R.id.ic_level009, R.id.ic_level010,
														R.id.ic_level011, R.id.ic_level012,
														R.id.ic_level013, R.id.ic_level014,
														R.id.ic_level015, R.id.ic_level016,
														R.id.ic_level017, R.id.ic_level018,
														R.id.ic_level019, R.id.ic_level020 };
	private static final int	MAXIMUM_SATELLITES	= IMAGE_VIEW_ID.length;
	
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
		
		for( int i = 0 ; i < MAXIMUM_SATELLITES ; ++i )
		{
			m_satelliteLevel[ i ]	= (ImageView)findViewById( IMAGE_VIEW_ID[ i ] );
			m_satelliteId[ i ]		= (TextView)findViewById( SATELLITE_NUMBER_ID[ i ] );
			
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
		int			imageId		= IMAGE_ID[ 0 ];
		Resources	resources	= getResources();
		Bitmap		levelIcon	= BitmapFactory.decodeResource( resources, imageId );
		
		for( int i = 0 ; i < MAXIMUM_SATELLITES ; ++i )
		{
			m_satelliteLevel[ i ].setImageBitmap( levelIcon );
			m_satelliteId[ i ].setText( "" );

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
			int						number		= 0;

			for( GpsSatellite satellite : satellites )
			{

				float		signalNoise	= satellite.getSnr();
				int			satelliteId	= satellite.getPrn();
				int			level		= GpsTools.GetLevel( signalNoise, MAXIMUM_LEVEL );
				int			imageId		= IMAGE_ID[ level ];
				Resources	resources	= getResources();
				Bitmap		levelIcon	= BitmapFactory.decodeResource( resources, imageId );
				
				if( number < MAXIMUM_SATELLITES )
				{
					m_satelliteLevel[ number ].setImageBitmap( levelIcon );
					m_satelliteId[ number ].setText( Integer.toString( satelliteId ) );

				}
				
				number += 1;
				
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
	final private ImageView[]	m_satelliteLevel		= new ImageView[ MAXIMUM_SATELLITES ];
	final private TextView[]	m_satelliteId			= new TextView[ MAXIMUM_SATELLITES ];
	
}
