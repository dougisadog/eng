package com.shuangge.english.support.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

public class ServiceUtils {

	public static interface ServiceIntentCallBack {
		public void setServiceIntent(Intent intent);
	}

	/**
	 * 查看该service是否存在 不存在则开启
	 * 
	 * @param context
	 * @param clazz
	 *            service的class
	 */
	public static <T> boolean checkService(Context context, final Class<T> clazz,
			ServiceIntentCallBack si) {
		boolean isServiceRunning = false;

		// 检查Service状态

		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (clazz.getName().equals(service.service.getClassName())) {
				isServiceRunning = true;
			}
		}
		if (!isServiceRunning) {
			Intent i = new Intent(context, clazz);
			if (null != si) {
				si.setServiceIntent(i);
			}
			context.startService(i);
		}
		return isServiceRunning;
	}
}
