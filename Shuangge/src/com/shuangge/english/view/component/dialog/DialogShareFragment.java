package com.shuangge.english.view.component.dialog;

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
import android.widget.ImageView;
import android.widget.TextView;

public class DialogShareFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogShare {
		
		public void onKeyBack();
		public void onQQ();
		public void onWx();
		public void onWxq();
		
	}
	
	private CallBackDialogShare callback;
	private ImageView imgQQ, imgWx,imgWxq;
	private TextView txtCancel;
	private String url;
	private int position;
	
	public DialogShareFragment() {
		super();
	}
	
//	public DialogShareFragment(CallBackDialogShare callback, int position) {
//		super();
//		this.callback = callback;
//		this.position = position;
//		setCancelable(true);
//		
//    	int style = DialogFragment.STYLE_NO_TITLE; 
//    	setStyle(style, 0);
//	}
	
	public DialogShareFragment(CallBackDialogShare callback, int position, int theme) {
		super();
		this.callback = callback;
		this.position = position;
		setCancelable(true);
		
		int style = DialogFragment1.STYLE_NO_TITLE; 
		setStyle(style, theme);
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
		View v = inflater.inflate(R.layout.dialog_share, null);// 得到加载view
		imgQQ = (ImageView) v.findViewById(R.id.imgQQ);
		imgWx = (ImageView) v.findViewById(R.id.imgWx);
		imgWxq = (ImageView) v.findViewById(R.id.imgWxq);
		txtCancel = (TextView) v.findViewById(R.id.txtCancel);
		imgQQ.setOnClickListener(this);
		imgWx.setOnClickListener(this);
		imgWxq.setOnClickListener(this);
		txtCancel.setOnClickListener(this);
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
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.imgQQ:
			callback.onQQ();
			break;
		case R.id.imgWx:
			callback.onWx();
			break;
		case R.id.imgWxq:
			callback.onWxq();
			break;
		case R.id.txtCancel:
			dismiss();
			break;
			
		}
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		if (null != callback) {
			callback.onKeyBack();
		}
	}
//	public void onQQ(){
//		
//	}
//	public void onWx(){
//		
//	}
//	public void onWxq(){
//		
//	}
}
