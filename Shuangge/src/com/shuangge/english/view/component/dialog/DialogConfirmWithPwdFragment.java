package com.shuangge.english.view.component.dialog;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.view.component.EditTextWidthTips;

public class DialogConfirmWithPwdFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirmWithPwd {
		
		public void cancel();
		public void submit(int position, String password);
		
	}
	
	private String titleStrEn, titleStrCn;
	private CallBackDialogConfirmWithPwd callback;
	private TextView txtDialogTitleEn, txtDialogTitleCn;
	private FrameLayout flDialogCancel, flDialogSubmit;
	private EditTextWidthTips edPassword;
	
	private int position;
	
	public DialogConfirmWithPwdFragment() {
		super();
	}
	
	public DialogConfirmWithPwdFragment(CallBackDialogConfirmWithPwd callback, String titleStrEn, String titleStrCn, int position) {
		super();
		this.callback = callback;
		this.titleStrEn = titleStrEn;
		this.titleStrCn = titleStrCn;
		this.position = position;
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
		View v = inflater.inflate(R.layout.dialog_confirm_width_pwd, null);// 得到加载view
		txtDialogTitleEn = (TextView) v.findViewById(R.id.txtDialogTitleEn);
		txtDialogTitleEn.setText(titleStrEn);
		txtDialogTitleCn = (TextView) v.findViewById(R.id.txtDialogTitleCn);
		txtDialogTitleCn.setText(titleStrCn);
		edPassword = (EditTextWidthTips) v.findViewById(R.id.edPassword);
		flDialogSubmit = (FrameLayout) v.findViewById(R.id.flDialogSubmit);
		flDialogSubmit.setOnClickListener(this);
		flDialogCancel = (FrameLayout) v.findViewById(R.id.flDialogCancel);
		flDialogCancel.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.flDialogSubmit:
			if (TextUtils.isEmpty(edPassword.getVal()) || edPassword.getVal().length() < 6) {
				Toast.makeText(getActivity(), R.string.loginErrTip2, Toast.LENGTH_SHORT).show();
				break;
			}
			callback.submit(position, edPassword.getVal());
			break;
		case R.id.flDialogCancel:
			callback.cancel();
			break;
		}
	}
	
}
