package com.shuangge.english.support.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;

import com.shuangge.english.GlobalApp;

/**
 * App的版本相关信息
 * 
 * @author Jeffrey
 *
 */
public class AppInfo {

	public static String APP_VERSION_NAME = null;
	public static String APP_VERSION = null;
	public static String APP_PACKAGE = null;
	public static String ANDROID_VERSION = null;
	public static String PHONE_BRAND = null;
	public static String PHONE_MODEL = null;
	public static String PHONE_MANUFACTURER = null;

	/**
	 * 设备ID
	 */
	public static String DEVICE_ID;

	/**
	 * 显示通用信息 分辨率相关信息
	 */
	private DisplayMetrics displayMetrics = null;

	/*************************************************************************************************************************/

	public void getAppInfo(Context context) {
		AppInfo.ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
		AppInfo.PHONE_BRAND = android.os.Build.BRAND;
		AppInfo.PHONE_MODEL = android.os.Build.MODEL;
		AppInfo.PHONE_MANUFACTURER = android.os.Build.MANUFACTURER;
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			AppInfo.APP_VERSION_NAME = packageInfo.versionName;
			AppInfo.APP_VERSION = "" + packageInfo.versionCode;
			AppInfo.APP_PACKAGE = packageInfo.packageName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		TelephonyManager telephoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		DEVICE_ID = telephoneManager.getDeviceId();
	}

	/*************************************************************************************************************************/

	public DisplayMetrics getDisplayMetrics(Activity activity) {
		if (null != displayMetrics) {
			return displayMetrics;
		}
		if (null != activity) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			this.displayMetrics = metrics;
			return metrics;
		}
		// default screen is 800x480
		DisplayMetrics metrics = new DisplayMetrics();
		metrics.widthPixels = 480;
		metrics.heightPixels = 800;
		return metrics;
	}

	public static int getScreenWidth() {
		Activity activity = GlobalApp.getInstance().getActivity();
		if (activity != null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			return metrics.widthPixels;
		}

		return 480;
	}

	public static int getScreenHeight() {
		Activity activity = GlobalApp.getInstance().getActivity();
		if (activity != null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			return metrics.heightPixels;
		}
		return 800;
	}

}
