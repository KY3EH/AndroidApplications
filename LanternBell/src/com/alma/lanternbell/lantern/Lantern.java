package com.alma.lanternbell.lantern;

import android.hardware.Camera;

/**
 * Mar 3, 2014
 * @author Maxim Kuzovlev
 */
public class Lantern
{
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
		ProcessEvent( LanternEvent.EV_TURN_ON );
		
	}
	
	public void TurnOff()
	{
		ProcessEvent( LanternEvent.EV_TURN_OFF );
		
	}
	
	public boolean IsOn()
	{
		boolean result		= (LanternState.ST_ON == m_state);
		
		return result;
		
	}
	
	private void ProcessEvent( LanternEvent event_ )
	{
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
		switch( event_ )
		{
		case EV_TURN_OFF:
			Off();
			m_state	= LanternState.ST_OFF;
			break;
			
		}
	}
	
	private void ProcessStateOff( LanternEvent event_ )
	{
		switch( event_ )
		{
		case EV_TURN_ON:
			On();
			m_state	= LanternState.ST_ON;
			break;
			
		}
	}
	
	private void Off()
	{
		if( null != m_camera )
		{
			Camera.Parameters	params	= m_camera.getParameters();

			params.setFlashMode( Camera.Parameters.FLASH_MODE_OFF );
			m_camera.setParameters( params );
			m_camera.stopPreview();
			m_camera.release();
			
			m_camera	= null;
		
		}

	}
	
	private void On()
	{
		m_camera	= Camera.open();
		
		if( null != m_camera )
		{
			Camera.Parameters	params	= m_camera.getParameters();

			params.setFlashMode( Camera.Parameters.FLASH_MODE_TORCH );
			m_camera.setParameters( params );
			m_camera.startPreview();
			
		}

	}
	
	public Lantern( Camera camera_ )
	{
		m_state	= LanternState.ST_OFF;
		
	}
	
	private LanternState	m_state;
	private Camera			m_camera;

}
