package com.alma.android.triptracker;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends Activity implements LocationListener
{
	private static final String TAG					= "MainActivity";
	private static final String COORDINATE_FORMAT	= "#0.0000000";
	private static final String VELOCITY_FORMAT		= "#0.00";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
		Log.i( TAG, "onCreate::entry" );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

		m_isGPSEnabledTxt	= (TextView)findViewById( R.id.isGPSEnabled );
		m_longitude			= (TextView)findViewById( R.id.gpsLon );
		m_latitude			= (TextView)findViewById( R.id.gpsLat );
		m_velocity			= (TextView)findViewById( R.id.velocity );
		m_gpsService		= (LocationManager)getSystemService( LOCATION_SERVICE );

		boolean	isEnabled		= m_gpsService.isProviderEnabled( LocationManager.GPS_PROVIDER );
		String	isEnabledTxt	= Boolean.toString( isEnabled );
		
		m_isGPSEnabledTxt.setText( isEnabledTxt );

		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if( true == isEnabled )
		{
			Criteria	criteria		= new Criteria();	
			String		providerName	= m_gpsService.getBestProvider( criteria, true );
			
			m_isGPSEnabledTxt.setText( isEnabledTxt + " " + providerName );
			m_longitude.setText( "Not available" );
			m_latitude.setText( "Not available" );
			m_velocity.setText( "Not available" );
			
		}
		else
		{
			finish();
			
		}
		
		Log.i( TAG, "onCreate::exit" );
		
	}
	
	@Override
	protected void onResume()
	{
		Log.i( TAG, "onResume::entry" );
		super.onResume();
		// The activity has become visible (it is now "resumed").
		m_gpsService.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this );
		Log.i( TAG, "onResume::exit" );

	}

	@Override
	protected void onPause()
	{
		Log.i( TAG, "onPause::entry" );
		super.onPause();
		// Another activity is taking focus (this activity is about to be "paused").
		m_gpsService.removeUpdates( this );
		Log.i( TAG, "onPause::exit" );

	}
//		Log.i( TAG, "::entry" );
//		Log.i( TAG, "::exit" );
	public void onLocationChanged( Location location_ )
	{
		Log.i( TAG, "onLocationChanged::entry" );
		DecimalFormat	coordinateFormat	= new DecimalFormat( COORDINATE_FORMAT );
		DecimalFormat	velocityFormat		= new DecimalFormat( VELOCITY_FORMAT );
		double			lon					= location_.getLongitude();
		double			lat					= location_.getLatitude();
		float			velocity			= location_.getSpeed();
		String			lonValue			= coordinateFormat.format( lon );
		String			latValue			= coordinateFormat.format( lat );
		String			velocityValue		= velocityFormat.format( velocity );
		
		m_longitude.setText( lonValue );
		m_latitude.setText( latValue );
		m_velocity.setText( velocityValue );
		
		Log.i( TAG, "onLocationChanged::exit" );

	}

	public void onStatusChanged( String provider, int status, Bundle extras )
	{
		Log.i( TAG, "onStatusChanged::entry" );
		
		Log.i( TAG, "onStatusChanged::exit" );

	}

	public void onProviderEnabled( String provider )
	{
		Log.i( TAG, "onProviderEnabled::entry" );
		
		Log.i( TAG, "onProviderEnabled::exit" );

	}

	public void onProviderDisabled( String provider )
	{
		Log.i( TAG, "onProviderDisabled::entry" );
		
		Log.i( TAG, "onProviderDisabled::exit" );

	}
	
	private LocationManager	m_gpsService;
	private TextView		m_isGPSEnabledTxt;
	private TextView		m_longitude;
	private TextView		m_latitude;
	private TextView		m_velocity;
	
	
}
