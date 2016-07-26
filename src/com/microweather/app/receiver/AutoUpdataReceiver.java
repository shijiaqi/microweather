package com.microweather.app.receiver;

import com.microweather.app.service.AutoUpdataService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdataReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context,AutoUpdataService.class);
		context.startService(i);
	}
	
	

}
