package com.alma.lanternbell.listener;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * Mar 24, 2014
 * @author Maxim Kuzovlev
 */
public abstract class AbstractButtonListener implements View.OnClickListener
{
	private static final String	TAG	= "AbstractButtonListener";
	
	public Activity Activity()
	{
		Log.i( TAG, "Activity" );

		return m_activity;
		
	}
	
	public AbstractButtonListener( Activity activity_ )
	{
		Log.i( TAG, "AbstractButtonListener" );

		m_activity	= activity_;
		
	}
	
	private final Activity	m_activity;

}
