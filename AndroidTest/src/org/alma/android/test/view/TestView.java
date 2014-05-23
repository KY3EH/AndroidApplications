package org.alma.android.test.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * May 22, 2014
 * @author Maxim Kuzovlev
 */
public class TestView extends LinearLayout
{
	public void SetId( String id_ )
	{
		m_id.setText( id_ );
		
	}
	
	public void SetElevation( String elevation_ )
	{
		m_elevation.setText( elevation_ );
		
	}
	
	public void SetAzimuth( String azimuth_ )
	{
		m_azimuth.setText( azimuth_ );
		
	}
	
	public TestView( Context context_ )
	{
		super( context_ );
		this.setOrientation( VERTICAL );
		
		m_id		= new TextView( context_ );
		m_elevation	= new TextView( context_ );
		m_azimuth	= new TextView( context_ );
		
		AddElement( m_id );
		AddElement( m_elevation );
		AddElement( m_azimuth );

	}
	
	private void AddElement( TextView element_ )
	{
		this.addView( element_ );
		
	}
	
	private final TextView	m_id;
	private final TextView	m_elevation;
	private final TextView	m_azimuth;
	
}
