package com.alma.android.triptracker.itf;

import java.util.Date;

/**
 * Apr 11, 2014
 * @author Maxim Kuzovlev
 */
public interface NotifyPropertiesItf
{
	public Date StartTime();
	public Date EventTime();
	public Double Longitude();
	public Double Latitude();
	public Double Altitude();
	public Double Distance();
	public Double DeltaDistance();
	public Double InstantVelocity();
	public Double AverageVelocity();

}
