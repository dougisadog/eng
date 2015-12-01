package com.shuangge.english.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shuangge.english.support.app.PushManager;

public class RegistrationReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		PushManager.getInstance().init();
		PushManager.getInstance().register();
	}

}
