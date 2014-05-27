package com.alma.android.triptracker.impl;

import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;

/**
 * May 27, 2014
 * @author Maxim Kuzovlev
 */
public class Remover implements Runnable
{
	public void run()
	{
		m_parent.removeView( m_view );
		
	}
	
	public Remover( ViewGroup parent_, View view_ )
	{
		m_parent	= parent_;
		m_view		= view_;
	}
	
	private final ViewGroup	m_parent;
	private final View		m_view;

}
