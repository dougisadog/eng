package com.shuangge.english.support.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

public class ConnectionChangeReceiver extends BroadcastReceiver {

	private Handler handler = new Handler(Looper.getMainLooper());
	private Runnable task = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

	}

}
