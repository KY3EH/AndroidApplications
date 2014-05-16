package com.alma.android.triptracker.tool;

/**
 * Apr 10, 2014
 * @author Maxim Kuzovlev
 */
public abstract class GpsTools
{
	private static final double	EARTH_RADIUS	= 6367.0d * 1000d;	// Earth radius in meters

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
		double	dlongitude		= Math.toRadians( oldLongitude_ - newLongitude_ );
		double	dlatitude		= Math.toRadians( oldLatitude_ - newLatitude_ );
		double	oldLatitudeRad	= Math.toRadians( oldLatitude_ );
		double	newLatitudeRad	= Math.toRadians( newLatitude_ );
		double	a				= Math.pow( Math.sin( dlatitude/2.0d ), 2.0d ) + Math.cos( oldLatitudeRad ) * Math.cos( newLatitudeRad ) * Math.pow( Math.sin( dlongitude/2.0d ), 2.0d );
		double	c				= 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1-a ) );
		double	result			= EARTH_RADIUS * c;

		return result;
		
	}
	
	public static int GetLevel( float signalNoise_, int maximum_ )
	{
		int	result	= ((int)signalNoise_) + 1 / 10;
		
		if( maximum_ > result )
		{
			result	= maximum_;
		}
				
		return result;
		
	}
	
 	private GpsTools()
	{
		
	}

}
