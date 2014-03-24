package com.alma.lanternbell.listener;

import android.app.Activity;
import android.view.View;

/**
 * Mar 24, 2014
 * @author Maxim Kuzovlev
 */
public abstract class AbstractButtonListener implements View.OnClickListener
{
	public Activity Activity()
	{
		return m_activity;
		
	}
	
	public AbstractButtonListener( Activity activity_ )
	{
		m_activity	= activity_;
		
	}
	
	private Activity	m_activity;

}
