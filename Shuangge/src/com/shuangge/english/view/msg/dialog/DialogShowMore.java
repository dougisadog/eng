package com.shuangge.english.view.msg.dialog;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.shuangge.english.entity.server.msg.NoticeData;
import com.shuangge.english.support.utils.DateUtils;

public class DialogShowMore extends DialogFragment1 implements OnClickListener {

	private String titleStr = "", contentStr = "", createDateStr = "";
	private TextView txtTitle, txtDetails, txtCreateDate;
	
	public DialogShowMore() {
		super();
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE, theme = R.style.CenterDialogTheme; 
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_sys_notice, null);// 得到加载view
		txtTitle = ((TextView) v.findViewById(R.id.txtTitle)); 
		txtDetails = ((TextView) v.findViewById(R.id.txtDetails)); 
		txtCreateDate = ((TextView) v.findViewById(R.id.txtCreateDate)); 
		((TextView) v.findViewById(R.id.txtClose)).setOnClickListener(this);
		refreshView();
//		flDialogCancel = (FrameLayout) v.findViewById(R.id.flDialogCancel);
//		flDialogCancel.setOnClickListener(this);
		return v;
	}
	
	public void setData(NoticeData entity) {
		if (!TextUtils.isEmpty(entity.getTitle())) {
			titleStr = entity.getTitle();
		}
		if (!TextUtils.isEmpty(entity.getDescription())) {
			contentStr = entity.getDescription();
		}
		if (null != entity.getCreateDate()) {
			createDateStr = DateUtils.convert(entity.getCreateDate());
		}
		refreshView();
	}
	
	public void refreshView() {
		if (null != txtTitle) {
			txtTitle.setText(titleStr);
		}
		if (null != txtDetails) {
			txtDetails.setText(contentStr);
		}
		if (null != txtCreateDate) {
			txtCreateDate.setText(createDateStr);
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().setGravity(Gravity.LEFT | Gravity.CENTER);
		return dialog;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtClose:
			dismiss();
			break;
		}
	}
	
}
