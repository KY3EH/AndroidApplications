package com.alma.android.triptracker.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsSatellite;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alma.android.triptracker.R;
import com.alma.android.triptracker.tool.GpsTools;

/**
 * May 22, 2014
 * @author Maxim Kuzovlev
 */
public class SatelliteIndicator extends LinearLayout
{
	private static final String	TAG					= "SatellitesIndicator";
	private static final String	ELEVATION_FORMAT	= "00";
	private static final String	AZIMUTH_FORMAT		= "000";
	private static final String	DEFAULT_TEXT		= "";
	private static final int	PADDING				= 3;
	private static final int[]	IMAGE_ID			= { R.drawable.ic_level000,
														R.drawable.ic_level001, R.drawable.ic_level002,
														R.drawable.ic_level003, R.drawable.ic_level004,
														R.drawable.ic_level005, R.drawable.ic_level006,
														R.drawable.ic_level007, R.drawable.ic_level008,
														R.drawable.ic_level009, R.drawable.ic_level010,};
	private static final int	MAXIMUM_LEVEL		= IMAGE_ID.length - 1;
	
	private static final LinearLayout.LayoutParams	TV_LAYOUT_PARAMS	= new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );;
	private static final LinearLayout.LayoutParams	WRAP_LAYOUT_PARAMS	= new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );;

	private static Bitmap[]		s_levelBitmaps	= null;

	public void Satellite( GpsSatellite satellite_ )
	{
		Log.i( TAG, "Satellite::entry" );
		
		float			signalNoise	= satellite_.getSnr();
		int				level		= GpsTools.GetLevel( signalNoise, MAXIMUM_LEVEL );
		int				satelliteId	= satellite_.getPrn();
		float			azimuth		= satellite_.getAzimuth();
		float			elevation	= satellite_.getElevation();
		boolean			isUsed		= satellite_.usedInFix();
		
		Level( level );
		Id( satelliteId );
		Elevation( elevation );
		Azimuth( azimuth );
		IsUsed( isUsed );
		
		Log.i( TAG, "Satellite::exit" );
		
	}
	
	private void Level( int level_ )
	{
		Log.i( TAG, "Level::entry" );
		
		Bitmap	levelBitmap	= s_levelBitmaps[ level_ ];
			
		m_levelBitmap.setImageBitmap( levelBitmap );
	
		Log.i( TAG, "Level::exit" );
		
	}
	
	private void Id( int id_ )
	{
		Log.i( TAG, "Id::entry" );
		
		String	id	= Integer.toString( id_ );
		
		m_id.setText( id );
		
		Log.i( TAG, "Id::exit" );

	}
	
	private void Elevation( float elevation_ )
	{
		Log.i( TAG, "Elevation::entry" );
		
		Double	elevationValue	= Double.valueOf( elevation_ );
		String	elevation		= GpsTools.FormatDouble( ELEVATION_FORMAT, elevationValue );
		
		m_elevation.setText( elevation );
		
		Log.i( TAG, "Elevation::exit" );

	}
	
	private void Azimuth( float azimuth_ )
	{
		Log.i( TAG, "Azimuth::entry" );
		
		Double	azimuthValue	= Double.valueOf( azimuth_ );
		String	azimuth			= GpsTools.FormatDouble( AZIMUTH_FORMAT, azimuthValue );
		
		m_azimuth.setText( azimuth );
		
		Log.i( TAG, "Azimuth::exit" );

	}
	
	private void IsUsed( boolean isUsed_ )
	{
		Log.i( TAG, "IsUsed::entry" );
		
		String	text	= isUsed_ ? "T" : "F";
		
		m_isUsed.setText( text );
		
		Log.i( TAG, "IsUsed::exit" );

	}
	
	private void LoadLevelBitmaps()
	{
		Log.i( TAG, "LoadLevelBitmaps::entry" );
		
		if( null == s_levelBitmaps )
		{
			Log.i( TAG, "LoadLevelBitmaps: Loading bitmaps..." );
			Resources	resources	= getResources();

			s_levelBitmaps	= new Bitmap[ IMAGE_ID.length ];
			
			for( int i = 0 ; i < IMAGE_ID.length ; ++i )
			{
				int	bitmapId	= IMAGE_ID[ i ];

				s_levelBitmaps[ i ]	= BitmapFactory.decodeResource( resources, bitmapId );

			}
			
		}	
		
		Log.i( TAG, "LoadLevelBitmaps::exit" );
		
	}
	
	private void InitializeTextView( TextView view_ )
	{
		Log.i( TAG, "InitializeTextView::entry" );
		
		view_.setGravity( Gravity.CENTER );
		view_.setLayoutParams( TV_LAYOUT_PARAMS );
		m_layout.addView( view_ );
		
		Log.i( TAG, "InitializeTextView::exit" );
		
	}
	
	private void InitializeLayout()
	{
		Log.i( TAG, "InitializeLayout::entry" );
		
		m_layout.setOrientation( VERTICAL );
		m_layout.setLayoutParams( WRAP_LAYOUT_PARAMS );
		
		this.addView( m_layout );
		
		Log.i( TAG, "InitializeLayout::exit" );
		
	}
	
	private void InitializeBitmap()
	{
		Log.i( TAG, "SetDefaultInfo::entry" );
		
		m_levelBitmap.setLayoutParams( WRAP_LAYOUT_PARAMS );
		m_levelBitmap.setPaddingRelative( PADDING, PADDING, PADDING, PADDING );
		m_layout.addView( m_levelBitmap );

		Log.i( TAG, "SetDefaultInfo::exit" );
		
	}
	
	private void Initialize()
	{
		Log.i( TAG, "Initialize::entry" );
		
		LoadLevelBitmaps();
		InitializeLayout();
		InitializeBitmap();
		
		InitializeTextView( m_id );
		InitializeTextView( m_elevation );
		InitializeTextView( m_azimuth );
		InitializeTextView( m_isUsed );

		Log.i( TAG, "Initialize::exit" );

	}
	
	public SatelliteIndicator( Context context_, GpsSatellite satellite_ )
	{
		super( context_ );
		
		Log.i( TAG, "c-tor::entry" );
		
		m_layout		= new LinearLayout( context_ );
		m_levelBitmap	= new ImageView( context_ );
		m_id			= new TextView( context_ );
		m_elevation		= new TextView( context_ );
		m_azimuth		= new TextView( context_ );
		m_isUsed		= new TextView( context_ );
		
		Initialize();
		
		if( null != satellite_ )
		{
			Satellite( satellite_ );
			
		}
		
		Log.i( TAG, "c-tor::exit" );

	}
	
	private final LinearLayout	m_layout;
	private final ImageView		m_levelBitmap;
	private final TextView		m_id;
	private final TextView		m_elevation;
	private final TextView		m_azimuth;
	private final TextView		m_isUsed;

}
