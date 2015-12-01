package com.shuangge.english.view.component.dialog;

import java.util.List;

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
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.view.component.EditTextWidthTips;

public class DialogWithContentFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogWithContent {
		
		public void notice(String msg,String wechatNo);
		
	}
	
	private CallBackDialogWithContent callback;
	private TextView txtDialogTitle;
	private EditTextWidthTips txtMsg,edWechat;
	private String title;
	private String tips;
	private String weChatNo;
	private ClassData data;
	
	public DialogWithContentFragment() {
		super();
	}
	
	public DialogWithContentFragment(CallBackDialogWithContent callback) {
		super();
		init(callback);
	}
	
	public DialogWithContentFragment(CallBackDialogWithContent callback, String title, String tips) {
		init(callback);
		this.title = title;
		this.tips = tips;
	}
	public DialogWithContentFragment(CallBackDialogWithContent callback, String title, String tips,String weChatNo) {
		init(callback);
		this.title = title;
		this.tips = tips;
		this.weChatNo = weChatNo;
	}
	
	private void init(CallBackDialogWithContent callback) {
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
		View v = inflater.inflate(R.layout.dialog_with_content, null);// 得到加载view
//		txtDialogTitle = (TextView) v.findViewById(R.id.txtDialogTitle);
//		if (!TextUtils.isEmpty(title))
//			txtDialogTitle.setText(title);
		txtMsg = (EditTextWidthTips) v.findViewById(R.id.txtMsg);
		edWechat = (EditTextWidthTips) v.findViewById(R.id.edWechat);
		if (!TextUtils.isEmpty(tips))
			txtMsg.setTips(tips);
		List<ClassData> listData = GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos();
		String weChatNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getWechatNo();
		if (listData.size() > 0) {
			data = listData.get(0);
		}
		
		if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() < ClassData.MANAGER && !TextUtils.isEmpty(data.getWechatNo())) {
			edWechat.setEnabled(false);
		}else {
			edWechat.setEnabled(true);
		}
		
		if (!TextUtils.isEmpty(data.getWechatNo())) {
			edWechat.setVal(data.getWechatNo().toString());
		}else {
			if (!TextUtils.isEmpty(weChatNo))
				edWechat.setVal(weChatNo);
		}
		v.findViewById(R.id.flDialogSubmit).setOnClickListener(this);
		v.findViewById(R.id.flDialogCancel).setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.flDialogCancel:
			dismiss();
			break;
		case R.id.flDialogSubmit:
			callback.notice(txtMsg.getVal(),edWechat.getVal());
			break;
		}
	}
	
}
