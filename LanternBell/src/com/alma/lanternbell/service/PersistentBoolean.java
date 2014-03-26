package com.alma.lanternbell.service;

import android.content.Context;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Mar 26, 2014
 * @author Maxim Kuzovlev
 */
public class PersistentBoolean
{
	private static final Map<String, PersistentBoolean>	VALUE_MAP	= Collections.synchronizedMap( new HashMap<String, PersistentBoolean>() );
	
	public static PersistentBoolean CreateInstance( Context context_, String valueName_ ) throws IOException
	{
		PersistentBoolean	result	= VALUE_MAP.get( valueName_ );
		
		if( null == result )
		{
			result	= CreateValue( context_, valueName_ );
			
			VALUE_MAP.put( valueName_, result );
			
		}
		
		return result;
		
	}
	
	public boolean Value() throws FileNotFoundException, IOException
	{
		boolean			result		= false;
		FileInputStream	valueFile	= m_context.openFileInput( m_valueName );
		
		try
		{
			DataInputStream	dataStream	= new DataInputStream( valueFile );

			result	= dataStream.readBoolean();
			
		}
		finally
		{
			valueFile.close();
			
		}
		
		return result;
		
	}
	
	public void Value( boolean value_ ) throws FileNotFoundException, IOException
	{
		FileOutputStream	valueFile	= m_context.openFileOutput( m_valueName, Context.MODE_PRIVATE );
		
		try
		{
			DataOutputStream	dataStream	= new DataOutputStream( valueFile );

			dataStream.writeBoolean( value_ );
			
		}
		finally
		{
			valueFile.close();
			
		}
		
	}
	
	private static PersistentBoolean CreateValue( Context context_, String valueName_ ) throws IOException
	{
		PersistentBoolean	result	= new PersistentBoolean( context_, valueName_ );
		
		try
		{
			result.Value();
			
		}
		catch( FileNotFoundException ex_ )
		{
			result.Value( false );
			
		}
				
		return result;
		
	}
	
	private PersistentBoolean( Context context_, String valueName_ )
	{
		m_valueName	= valueName_;
		m_context	= context_;
		
	}
	
	private final String	m_valueName;
	private final Context	m_context;

}
