package com.alma.lanternbell.lantern;

import android.hardware.Camera;
import android.util.Log;

/**
 * Mar 3, 2014
 * @author Maxim Kuzovlev
 */
public class Lantern
{
	private static final String	TAG	= "Lantern";
	
	private static enum LanternEvent
	{
		EV_TURN_ON,
		EV_TURN_OFF
		
	}
	
	private static enum LanternState
	{		
		ST_ON,
		ST_OFF

	}
	
	public void TurnOn()
	{
		Log.i( TAG, "TurnOn" );

		ProcessEvent( LanternEvent.EV_TURN_ON );
		
	}
	
	public void TurnOff()
	{
		Log.i( TAG, "TurnOff" );

		ProcessEvent( LanternEvent.EV_TURN_OFF );
		
	}
	
	public synchronized boolean IsOn()
	{
		Log.i( TAG, "IsOn" );

		boolean result		= (LanternState.ST_ON == m_state);
		
		return result;
		
	}
	
	private synchronized void ProcessEvent( LanternEvent event_ )
	{
		Log.i( TAG, "ProcessEvent" );

		switch( m_state )
		{
		case ST_ON:
			ProcessStateOn( event_ );
			break;
			
		case ST_OFF:
			ProcessStateOff( event_ );
			break;
			
		}
		
	}
	
	private void ProcessStateOn( LanternEvent event_ )
	{
		Log.i( TAG, "ProcessStateOn" );

		switch( event_ )
		{
		case EV_TURN_OFF:
			Off();
			break;
			
		}
	}
	
	private void ProcessStateOff( LanternEvent event_ )
	{
		Log.i( TAG, "ProcessStateOff" );

		switch( event_ )
		{
		case EV_TURN_ON:
			On();
			break;
			
		}
	}
	
	private void Off()
	{
		Log.i( TAG, "Off" );

		if( null != m_camera )
		{
			Camera.Parameters	params	= m_camera.getParameters();

			params.setFlashMode( Camera.Parameters.FLASH_MODE_OFF );
			m_camera.setParameters( params );
			m_camera.stopPreview();
			m_camera.release();
			
			m_camera	= null;
			m_state		= LanternState.ST_OFF;
		
		}

	}
	
	private void On()
	{
		Log.i( TAG, "On" );
		
		m_camera	= Camera.open();

		if( null != m_camera )
		{
			Camera.Parameters	params	= m_camera.getParameters();

			params.setFlashMode( Camera.Parameters.FLASH_MODE_TORCH );
			m_camera.setParameters( params );
			m_camera.startPreview();
			
			m_state	= LanternState.ST_ON;
			
		}

	}
	
	public Lantern()
	{
		Log.i( TAG, "Lantern" );

		m_state		= LanternState.ST_OFF;
		
	}
	
	private LanternState	m_state;
	private Camera			m_camera;

}
