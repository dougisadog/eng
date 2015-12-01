package com.shuangge.english.view.component.dialog;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DialogConfirmSaveFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirmSave {
		
		public void cancel();
		public void save();
		public void noSave();
		
	}
	
	private CallBackDialogConfirmSave callback;
	private TextView txtDialogTitle;
	
	public DialogConfirmSaveFragment() {
		super();
	}
	
	public DialogConfirmSaveFragment(CallBackDialogConfirmSave callback) {
		super();
		this.callback = callback;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
    	setStyle(style, theme);
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_confirm_save, null);// 得到加载view
		txtDialogTitle = (TextView) v.findViewById(R.id.txtDialogTitle);
		v.findViewById(R.id.flDialogYes).setOnClickListener(this);
		v.findViewById(R.id.flDialogNo).setOnClickListener(this);
		v.findViewById(R.id.flDialogCancel).setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.flDialogYes:
			callback.save();
			break;
		case R.id.flDialogNo:
			callback.noSave();
			break;
		case R.id.flDialogCancel:
			callback.cancel();
			break;
		}
	}
	
}
