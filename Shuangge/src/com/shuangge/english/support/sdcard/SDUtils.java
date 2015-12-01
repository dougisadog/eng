package com.shuangge.english.support.sdcard;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

public class SDUtils {
	public static String getFileDirByExt(Context ctx, String filePath) {
		String ext;
		if (checkSDCard()) {
			ext = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/" + filePath;
		} else {
			ext = "/" + filePath;
		}

		return ext;
	}

	public static String getDownloadZipDirByExt(Context context, String mLever) {
		String ext;
		PackageInfo info;
		String packageNames = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			packageNames = info.packageName;   
			Log.v("packageNames-->", packageNames+"--mLever_-->"+mLever);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		if (checkSDCard()) {
			ext = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/android/data/"+packageNames+"/cache/lesson/"
					+ mLever + ".zip";
		} else {
			ext = "/android/data/"+packageNames+"/cache/lesson/" + mLever
					+ ".zip";
		}

		return ext;
	}

	public static String getDownloadZipDirByExt(Context context) {
		String ext;
		PackageInfo info;
		String packageNames = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			packageNames = info.packageName;   
			Log.v("packageNames-->", packageNames);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		if (checkSDCard()) {
			ext = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/android/data/"+packageNames+"/cache/lesson/";

		} else {
			ext =  "/android/data/"+packageNames+"/cache/lesson/";
		}

		return ext;
	}

	/**
	 * 检测当前是否安装了sdcard
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return false;
		}
		return true;
	}
}
