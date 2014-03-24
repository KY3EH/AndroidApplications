package com.alma.lanternbell;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import com.alma.lanternbell.listener.AbstractButtonListener;
import com.alma.lanternbell.listener.OffButtonListener;
import com.alma.lanternbell.listener.OnButtonListener;

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
		
		AbstractButtonListener	onListener	= new OnButtonListener( this );
		AbstractButtonListener	offListener	= new OffButtonListener( this );
		
		m_onButton.setOnClickListener( onListener );
		m_offButton.setOnClickListener( offListener );
		
	}
	
	private Button	m_onButton;
	private Button	m_offButton;
	
}
