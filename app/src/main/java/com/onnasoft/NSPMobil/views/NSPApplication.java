package com.onnasoft.NSPMobil.views;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.onnasoft.NSPMobil.PenClientCtrl;

public class NSPApplication extends android.app.Application{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		PenClientCtrl.getInstance( getApplicationContext() );
		PenClientCtrl.getInstance( getApplicationContext() ).setContext(getApplicationContext());
		PenClientCtrl.getInstance( getApplicationContext() ).registerBroadcastBTDuplicate();

	}

	public void onTerminate() {
		super.onTerminate();
		PenClientCtrl.getInstance( getApplicationContext() ).unregisterBroadcastBTDuplicate();
	};

}
