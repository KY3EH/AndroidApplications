package org.alma.android.test.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.alma.android.test.activity.R;

/**
 * May 22, 2014
 * @author Maxim Kuzovlev
 */
public class SatellitesIndicator extends LinearLayout
{
	private static final String	TAG					= "SatellitesIndicator";
	private static final String	DEFAULT_TEXT		= "";
	private static final int	PADDING				= 3;
	private static final int[]	IMAGE_ID			= { R.drawable.ic_level000,
														R.drawable.ic_level001, R.drawable.ic_level002,
														R.drawable.ic_level003, R.drawable.ic_level004,
														R.drawable.ic_level005, R.drawable.ic_level006,
														R.drawable.ic_level007, R.drawable.ic_level008,
														R.drawable.ic_level009, R.drawable.ic_level010,};
	
	private static final LayoutParams	TV_LAYOUT_PARAMS	= new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );;
	private static final LayoutParams	WRAP_LAYOUT_PARAMS	= new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );;

	private static Bitmap[]		s_levelBitmaps	= null;

	public void SatelliteId( String id_ )
	{
		Log.i( TAG, "SatelliteId::entry" );
		
		m_id.setText( id_ );
		
		Log.i( TAG, "SatelliteId::exit" );

	}
	
	public void SatelliteElevation( String elevation_ )
	{
		Log.i( TAG, "SatelliteElevation::entry" );
		
		m_elevation.setText( elevation_ );
		
		Log.i( TAG, "SatelliteElevation::exit" );

	}
	
	public void SatelliteAzimuth( String azimuth_ )
	{
		Log.i( TAG, "SatelliteAzimuth::entry" );
		
		m_azimuth.setText( azimuth_ );
		
		Log.i( TAG, "SatelliteAzimuth::exit" );

	}
	
	public void isSatelliteUsed( boolean isUsed_ )
	{
		Log.i( TAG, "SatelliteAzimuth::entry" );
		
		String	text	= isUsed_ ? "T" : "F";
		
		m_isUsed.setText( text );
		
		Log.i( TAG, "SatelliteAzimuth::exit" );

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
		view_.setText( DEFAULT_TEXT );
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
		m_levelBitmap.setImageBitmap( s_levelBitmaps[ 0 ] );
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
	
	public SatellitesIndicator( Context context_ )
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
		
		Log.i( TAG, "c-tor::exit" );

	}
	
	private final LinearLayout	m_layout;
	private final ImageView		m_levelBitmap;
	private final TextView		m_id;
	private final TextView		m_elevation;
	private final TextView		m_azimuth;
	private final TextView		m_isUsed;

}
