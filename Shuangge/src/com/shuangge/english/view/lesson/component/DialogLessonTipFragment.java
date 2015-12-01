package com.shuangge.english.view.lesson.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DialogLessonTipFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialog {
		
		public void onOk();
		
	}
	
	private String titleStrEn, titleStrCn;
	private CallBackDialog callback;
	private TextView txtDialogTitleCn, txtDialogTitleEn;
	private FrameLayout flDialogOk;
	
	public DialogLessonTipFragment() {
		super();
	}
	
	public DialogLessonTipFragment(CallBackDialog callback, String titleStrEn, String titleStrCn) {
		super();
		this.callback = callback;
		this.titleStrEn = titleStrEn;
		this.titleStrCn = titleStrCn;
		setCancelable(false);
    	int style = DialogFragment1.STYLE_NO_TITLE; 
    	setStyle(style, R.style.DialogAllScreenTheme);
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		if (isAdded()) {
			return;
		}
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_lesson_tip, null);// 得到加载view
		txtDialogTitleEn = (TextView) v.findViewById(R.id.txtDialogTitleEn);
		txtDialogTitleEn.setText(titleStrEn);
		txtDialogTitleCn = (TextView) v.findViewById(R.id.txtDialogTitleCn);
		txtDialogTitleCn.setText(titleStrCn);
		flDialogOk = (FrameLayout) v.findViewById(R.id.flDialogOk);
		flDialogOk.setOnClickListener(this);
		return v;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		int gravity = Gravity.CENTER;
		if (getTheme() == R.style.DialogTopToBottomTheme) {
			gravity = Gravity.TOP;
		}
		else if (getTheme() == R.style.DialogBottomToTopTheme)  {
			gravity = Gravity.BOTTOM;
		}
		dialog.getWindow().setGravity(Gravity.LEFT | gravity);
		return dialog;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.flDialogOk:
			callback.onOk();
			break;
		}
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
	}
	
}
