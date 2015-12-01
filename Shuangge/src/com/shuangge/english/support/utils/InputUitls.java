package com.shuangge.english.support.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputUitls {

	public static void closeInputMethod(Context context, View ...vs) {
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	    	for (View v : vs) {
	    		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
	    		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
	    }
	}

	public static void showInputMethod(Context context, View ...vs) {
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	    	for (View v : vs) {
	    		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
	    		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);  
			}
	    }
	}
	
	
}
