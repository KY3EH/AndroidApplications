package org.alma.android.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.concurrent.atomic.AtomicInteger;
import org.alma.android.test.view.SatelliteIndicator;

public class TestActivity extends Activity
{
	private static final String			TAG		= "TestActivity";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i( TAG, "onCreate::entry" );

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		m_btAdd			= (Button)findViewById( R.id.btAdd );
		m_btRemove		= (Button)findViewById( R.id.btRemove );
		m_btRemoveLast	= (Button)findViewById( R.id.btRemoveLast );
		m_ltAcceptor	= (LinearLayout)findViewById( R.id.ltAcceptor );

		m_btAdd.setOnClickListener( new View.OnClickListener()
											{
												public void onClick( View v )
												{
													Log.i( TAG, "OnClickListener::onClick::entry" );

													Add();

													Log.i( TAG, "OnClickListener::onClick::exit" );

												}

											}

									);
		
		m_btRemove.setOnClickListener( new View.OnClickListener()
											{
												public void onClick( View v )
												{
													Log.i( TAG, "OnClickListener::onClick::entry" );

													Remove();

													Log.i( TAG, "OnClickListener::onClick::exit" );

												}

											}

									);
		
		m_btRemoveLast.setOnClickListener( new View.OnClickListener()
											{
												public void onClick( View v )
												{
													Log.i( TAG, "OnClickListener::onClick::entry" );

													RemoveLast();

													Log.i( TAG, "OnClickListener::onClick::exit" );

												}

											}

									);
		
		Log.i( TAG, "onCreate::exit" );

	}
	
	private void Add()
	{
		Log.i( TAG, "Add::entry" );
		
		SatelliteIndicator	item	= new SatelliteIndicator( this );

		m_ltAcceptor.addView( item );
		
		m_lastView	= item;

		Log.i( TAG, "Add::exit" );

	}

	private void Remove()
	{
		Log.i( TAG, "Remove::entry" );
		
		int	child	= m_ltAcceptor.getChildCount();
		
		if( 0 < child )
		{
			m_ltAcceptor.removeViews( child - 1, 1 );
			
			if( 1 < child )
			{
				m_lastView	= m_ltAcceptor.getChildAt( child - 2 );
				
			}
			else
			{
				m_lastView	= null;
				
			}
			
			
		}
		
		Log.i( TAG, "Remove::exit" );
		
	}
	
	private void RemoveLast()
	{
		Log.i( TAG, "Remove::entry" );
		
		if( null != m_lastView )
		{
			m_ltAcceptor.removeView( m_lastView );
			
			int	child	= m_ltAcceptor.getChildCount();
		
			if( 0 < child )
			{
				m_lastView	= m_ltAcceptor.getChildAt( child - 1 );
				
			}
			else
			{
				m_lastView	= null;
				
			}
			
		}
		
		Log.i( TAG, "Remove::exit" );
		
	}
	
	private Button			m_btAdd;
	private Button			m_btRemove;
	private Button			m_btRemoveLast;
	private LinearLayout	m_ltAcceptor;
	private View			m_lastView		= null;

}
