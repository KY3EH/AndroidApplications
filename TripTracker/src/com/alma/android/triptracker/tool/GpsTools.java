package com.alma.android.triptracker.tool;

import android.util.Log;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Apr 10, 2014
 * @author Maxim Kuzovlev
 */
public abstract class GpsTools
{
	private static final String	TAG				= "GpsTools";
	private static final double	EARTH_RADIUS	= 6367.0d * 1000d;	// Earth radius in meters
	private static final double	LINEAR			= 20.0;
	private static final double	LOG_BASIS		= Math.pow( (99.0 + LINEAR)/LINEAR, 1.0/10.0 ); //Root 

	/**
	 *
	 * @param oldLatitude_
	 * @param oldLongitude_
	 * @param newLatitude_
	 * @param newLongitude_
	 * @return Distance since start in meters
	 */
	public static double GetDistance(	double oldLatitude_,
										double oldLongitude_,
										double newLatitude_,
										double newLongitude_ )
	{
		Log.i( TAG, "GetDistance::entry" );
		
		double	dlongitude		= Math.toRadians( oldLongitude_ - newLongitude_ );
		double	dlatitude		= Math.toRadians( oldLatitude_ - newLatitude_ );
		double	oldLatitudeRad	= Math.toRadians( oldLatitude_ );
		double	newLatitudeRad	= Math.toRadians( newLatitude_ );
		double	a				= Math.pow( Math.sin( dlatitude/2.0d ), 2.0d ) + Math.cos( oldLatitudeRad ) * Math.cos( newLatitudeRad ) * Math.pow( Math.sin( dlongitude/2.0d ), 2.0d );
		double	c				= 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1-a ) );
		double	result			= EARTH_RADIUS * c;

		Log.i( TAG, "GetDistance::exit" );
		
		return result;
		
	}
	
	public static int GetLevel( float signalNoise_, int maximum_ )
	{
		Log.i( TAG, "GetLevel::entry" );
		
		long	result	= Math.round( Math.log( ( ( signalNoise_ + LINEAR ) / LINEAR ) ) / Math.log( LOG_BASIS ) );
		
		if( maximum_ < result )
		{
			result	= maximum_;
		}
				
		Log.i( TAG, "GetLevel::exit" );
		
		return (int)result;
		
	}
	
	public static String FormatDouble( String pattern_, Double value_ )
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

	public static String FormatDate( String pattern_, Date value_ )
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

 	private GpsTools()
	{
		Log.i( TAG, "c-tor::entry" );
		
		Log.i( TAG, "c-tor::exit" );
		
	}

}
