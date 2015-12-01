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

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.view.component.EditTextWidthTips;

public class DialogConfirmWithInviteNoFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirmWithInviteNo {
		
		public void cancel();
		public void submit(int position, String inviteNo);
		
	}
	
	private String inviteNo;
	private CallBackDialogConfirmWithInviteNo callback;
	private FrameLayout flDialogCancel, flDialogSubmit;
	private EditTextWidthTips edInviteNo;
	
	private int position;
	
	public DialogConfirmWithInviteNoFragment() {
		super();
	}
	
	public DialogConfirmWithInviteNoFragment(CallBackDialogConfirmWithInviteNo callback, int position) {
		super();
		this.callback = callback;
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
		View v = inflater.inflate(R.layout.dialog_input_inviteno, null);// 得到加载view
		edInviteNo = (EditTextWidthTips) v.findViewById(R.id.edInviteNo);
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
			if (TextUtils.isEmpty(edInviteNo.getVal()) || edInviteNo.getVal().length() < 5) {
				Toast.makeText(getActivity(), R.string.loginErrTip5, Toast.LENGTH_SHORT).show();
				break;
			}
			callback.submit(position, edInviteNo.getVal());
			break;
		case R.id.flDialogCancel:
			callback.cancel();
			break;
		}
	}
	
}
