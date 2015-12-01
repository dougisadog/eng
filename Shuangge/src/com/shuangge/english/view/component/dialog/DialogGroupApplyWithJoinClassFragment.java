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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shuangge.english.view.component.EditTextWidthTips;

public class DialogGroupApplyWithJoinClassFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirmWithWeChatNo {
		
		public void cancel();
		public void submit(int position,String msg);
		
	}
	
	private String titleStrEn, titleStrCn;
	private CallBackDialogConfirmWithWeChatNo callback;
	private TextView txtDialogTitleEn, txtDialogTitleCn, txtWechat;
	private FrameLayout flDialogCancel, flDialogSubmit;
	private EditTextWidthTips edMsg;
	private int type;
	
	private int position;
	
	public DialogGroupApplyWithJoinClassFragment() {
		super();
	}
	
	public DialogGroupApplyWithJoinClassFragment(CallBackDialogConfirmWithWeChatNo callback, int position) {
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
		View v = inflater.inflate(R.layout.dialog_group_apply_join_class, null);// 得到加载view
		txtDialogTitleEn = (TextView) v.findViewById(R.id.txtDialogTitleEn);
//		txtDialogTitleEn.setText(titleStrEn);
		txtDialogTitleCn = (TextView) v.findViewById(R.id.txtDialogTitleCn);
//		txtDialogTitleCn.setText(titleStrCn);
		edMsg = (EditTextWidthTips) v.findViewById(R.id.edMsg);
		
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
			callback.submit(position,edMsg.getVal());
			break;
		case R.id.flDialogCancel:
			callback.cancel();
//			dismiss();
			break;
		}
	}
	
	
}
