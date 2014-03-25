package com.alma.lanternbell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.alma.lanternbell.service.BellService;

public class MainActivity extends Activity
{
	private static final String	TAG	= "MainActivity";
	
	@Override
	public void onCreate( Bundle savedInstanceState_ )
	{
		Log.i( TAG, "onCreate" );
		
		super.onCreate( savedInstanceState_ );
		setContentView( R.layout.main );
		
		m_onButton	= (Button)findViewById( R.id.ButtonON );
		m_offButton	= (Button)findViewById( R.id.ButtonOFF );
		
		m_onButton.setOnClickListener( new OnClickListener()
										{
											public void onClick( View v )
											{
												Log.i( TAG, "onClick" );

												Context	ctx		= getApplicationContext();
												Intent	intent	= new Intent( ctx, BellService.class );

												startService( intent );

											}
										} );
		
		
		m_offButton.setOnClickListener( new OnClickListener()
										{
											public void onClick( View v )
											{
												Log.i( TAG, "onClick" );

												Context	ctx		= getApplicationContext();
												Intent	intent	= new Intent( ctx, BellService.class );

												stopService( intent );

											}
										} );
		
		
	}
	
	private Button	m_onButton;
	private Button	m_offButton;
	
}
