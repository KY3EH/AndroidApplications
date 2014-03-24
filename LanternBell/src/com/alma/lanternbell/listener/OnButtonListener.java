package com.alma.lanternbell.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import com.alma.lanternbell.service.BellService;

/**
 * Mar 24, 2014
 * @author Maxim Kuzovlev
 */
public class OnButtonListener extends AbstractButtonListener implements View.OnClickListener
{
	public void onClick( View view_ )
	{
		Activity	activity	= Activity();
		Intent		intent		= new Intent( activity, BellService.class );
		
		activity.startService( intent );
			
	}

	public OnButtonListener( Activity activity_ )
	{
		super( activity_ );
		
	}

}
