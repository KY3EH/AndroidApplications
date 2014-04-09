package com.alma.android.triptracker;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

		m_service	= (LocationManager)getSystemService( LOCATION_SERVICE );

		boolean	enabled	= m_service.isProviderEnabled( LocationManager.GPS_PROVIDER );

		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if( true == enabled )
		{
			
		}
		
	}
	
	private LocationManager	m_service;
	
}
