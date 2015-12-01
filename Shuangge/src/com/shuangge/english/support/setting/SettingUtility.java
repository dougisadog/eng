package com.shuangge.english.support.setting;

import android.content.Context;

import com.shuangge.english.GlobalApp;

public class SettingUtility {

	private static final String FIRSTSTART = "firststart";
	
	public static boolean firstStart() {
        boolean value = SettingHelper.getSharedPreferences(getContext(), FIRSTSTART, true);
        if (value) {
            SettingHelper.setEditor(getContext(), FIRSTSTART, false);
        }
        return value;
    }
	
	private static Context getContext() {
        return GlobalApp.getInstance();
    }
	
}
