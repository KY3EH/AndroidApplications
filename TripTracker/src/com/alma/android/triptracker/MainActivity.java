package com.alma.android.triptracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alma.android.triptracker.tool.GpsTools;
import java.text.DecimalFormat;

public class MainActivity extends Activity implements LocationListener, View.OnClickListener
{
	private static final String TAG					= "MainActivity";
	private static final String COORDINATE_FORMAT	= "#0.0000000";
	private static final String VELOCITY_FORMAT		= "#0.00";
	private static final String ALTITUDE_FORMAT		= "#0.0";
	private static final String DISTANCE_FORMAT		= "#0.0000";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
		Log.i( TAG, "onCreate::entry" );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

		m_isGPSEnabledTxt	= (TextView)findViewById( R.id.isGPSEnabled );
		m_txtLongitude		= (TextView)findViewById( R.id.gpsLon );
		m_txtLatitude		= (TextView)findViewById( R.id.gpsLat );
		m_txtAltitude		= (TextView)findViewById( R.id.altitude );
		m_txtVelocity		= (TextView)findViewById( R.id.velocity );
		m_txtDeltaDistance	= (TextView)findViewById( R.id.deltaDistance );
		m_txtDistance		= (TextView)findViewById( R.id.distance );
		m_btReset			= (Button)findViewById( R.id.reset );
		m_gpsService		= (LocationManager)getSystemService( LOCATION_SERVICE );

		boolean	isEnabled		= m_gpsService.isProviderEnabled( LocationManager.GPS_PROVIDER );
		String	isEnabledTxt	= Boolean.toString( isEnabled );
		
		m_isGPSEnabledTxt.setText( isEnabledTxt );
		m_btReset.setOnClickListener( this );

		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if( true == isEnabled )
		{
			Criteria	criteria		= new Criteria();	
			String		providerName	= m_gpsService.getBestProvider( criteria, true );
			
			m_isGPSEnabledTxt.setText( isEnabledTxt + " " + providerName );
			m_txtLongitude.setText( "Not available" );
			m_txtLatitude.setText( "Not available" );
			m_txtAltitude.setText( "Not available" );
			m_txtVelocity.setText( "Not available" );
			m_txtDistance.setText( "Not available" );
			m_txtDeltaDistance.setText( "Not available" );
			
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
		DecimalFormat	altitudeFormat		= new DecimalFormat( ALTITUDE_FORMAT );
		DecimalFormat	distanceFormat		= new DecimalFormat( DISTANCE_FORMAT );
		double			longitude			= location_.getLongitude();
		double			latitude			= location_.getLatitude();
		double			altitude			= location_.getAltitude();
		float			velocity			= location_.getSpeed();
		String			lonValue			= coordinateFormat.format( longitude );
		String			latValue			= coordinateFormat.format( latitude );
		String			altitudeValue		= altitudeFormat.format( altitude );
		String			velocityValue		= velocityFormat.format( velocity );
		double			deltaDistance		= GpsTools.GetDistance( m_latitude, m_longitude, latitude, longitude );
		String			deltaDistanceValue	= distanceFormat.format( deltaDistance );
		
		if( false == Double.isNaN( deltaDistance ) )
		{
			m_distance	+= deltaDistance;
			
		}
		
		UpdateDistence();
		
		m_txtLongitude.setText( lonValue );
		m_txtLatitude.setText( latValue );
		m_txtAltitude.setText( altitudeValue );
		m_txtVelocity.setText( velocityValue );
		m_txtDeltaDistance.setText( deltaDistanceValue );
		
		m_longitude	= longitude;
		m_latitude	= latitude;
		m_altitude	= altitude;
		m_velocity	= velocity;
		
		Log.i( TAG, "onLocationChanged::exit" );

	}
	
	private void UpdateDistence()
	{
		DecimalFormat	distanceFormat		= new DecimalFormat( DISTANCE_FORMAT );
		String			distanceValue		= distanceFormat.format( m_distance );
	
		m_txtDistance.setText( distanceValue );
		
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
	
	private void Reset()
	{
		Log.i( TAG, "onProviderDisabled::Reset" );
		
		m_distance	= 0.0d;
		UpdateDistence();
		
		Log.i( TAG, "onProviderDisabled::Reset" );

	}
	
	public void onClick( View v )
	{
		Reset();
	}
	
	private LocationManager	m_gpsService;
	private TextView		m_isGPSEnabledTxt;
	private TextView		m_txtLongitude;
	private TextView		m_txtLatitude;
	private TextView		m_txtAltitude;
	private TextView		m_txtVelocity;
	private TextView		m_txtDistance;
	private TextView		m_txtDeltaDistance;
	private Button			m_btReset;
	private Double			m_longitude			= Double.NaN;
	private Double			m_latitude			= Double.NaN;
	private Double			m_altitude			= Double.NaN;
	private Float			m_velocity;
	private double			m_distance			= 0.0;
	
}
