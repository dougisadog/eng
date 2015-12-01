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

public class DialogLessonUnlockTipFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialog {
		
		public void onOk();
		
	}
	
	private String title, dialog;
	private CallBackDialog callback;
	private TextView txtDialog, txtDialogTitle;
	private FrameLayout flDialogOk;
	
	public DialogLessonUnlockTipFragment() {
		super();
	}
	
	public DialogLessonUnlockTipFragment(CallBackDialog callback, String title, String dialog) {
		super();
		this.callback = callback;
		this.title = title;
		this.dialog = dialog;
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
//		View v = inflater.inflate(R.layout.dialog_lesson_unlock_tip, null);// 得到加载view
		View v = new View(getActivity());
		txtDialogTitle = (TextView) v.findViewById(R.id.txtDialogTitle);
		txtDialogTitle.setText(title);
//		txtDialog = (TextView) v.findViewById(R.id.txtDialog);
		txtDialog.setText(dialog);
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
