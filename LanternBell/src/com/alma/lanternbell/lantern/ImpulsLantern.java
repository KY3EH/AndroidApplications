package com.alma.lanternbell.lantern;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Mar 3, 2014
 * @author Maxim Kuzovlev
 */
public class ImpulsLantern implements Runnable
{
	public void TurnOn()
	{
		m_isOn.set( true );
		m_lantern.TurnOn();
		m_executor.schedule( this, m_onTime, TimeUnit.MILLISECONDS );
		
	}
	
	public void TurnOff()
	{
		m_isOn.set( false );
		m_lantern.TurnOff();
		
	}
	
	public void run()
	{
		boolean	isOn	= m_isOn.get();
		
		if( true == isOn )
		{
			boolean	isLanternOn	= m_lantern.IsOn();

			if( true == isLanternOn )
			{
				m_lantern.TurnOff();
				m_executor.schedule( this, m_offTime, TimeUnit.MILLISECONDS );

			}
			else
			{
				m_lantern.TurnOn();
				m_executor.schedule( this, m_onTime, TimeUnit.MILLISECONDS );
				
			}
		
		}
		
	}
	
	public ImpulsLantern( Lantern lantern_, int onTime_, int offTime_ )
	{
		m_lantern	= lantern_;
		m_onTime	= onTime_;
		m_offTime	= offTime_;
		
	}
	
	private final Lantern					m_lantern;
	private final int						m_onTime;
	private final int						m_offTime;
	private final ScheduledExecutorService	m_executor	= Executors.newSingleThreadScheduledExecutor();
	private final AtomicBoolean				m_isOn		= new AtomicBoolean( false );

}
