package com.shuangge.english.support.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.os.Build;

import com.shuangge.english.GlobalApp;

public class ClipboardUtils {

	public static void copy(String str) {
		if (Build.VERSION.SDK_INT >= 11) {
			ClipboardManager c = (ClipboardManager) GlobalApp.getInstance().getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
			c.setText(str);
		}
	}
	
}
