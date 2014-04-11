package com.alma.android.triptracker.itf;

/**
 * Apr 11, 2014
 * @author Maxim Kuzovlev
 */
public interface TrackerServiceItf
{
	public void Reset();
	public NotifyPropertiesItf GetLastProperties();
	public void AddListener( ListenerItf listener_ );
	public boolean RemoveListener( ListenerItf listener_ );

}
