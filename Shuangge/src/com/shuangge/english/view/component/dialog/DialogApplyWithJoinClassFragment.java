package com.shuangge.english.view.component.dialog;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.DialogInterface;
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
import com.shuangge.english.support.utils.ClipboardUtils;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.msg.adapter.AdapterClassMsg;

public class DialogApplyWithJoinClassFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirmWithWeChatNo {
		
		public void cancel();
		public void submit(int position,String weChatNo);
		
	}
	
	private String titleStrEn, titleStrCn, classWechat;
	private CallBackDialogConfirmWithWeChatNo callback;
	private TextView txtDialogTitleEn, txtDialogTitleCn, txtWechat, txtWechatTitle;
	private FrameLayout flDialogCancel, flDialogSubmit;
	private EditTextWidthTips edWechat, edMsg;
	private int type;
	
	private int position;
	
	public DialogApplyWithJoinClassFragment() {
		super();
	}
	
	public DialogApplyWithJoinClassFragment(CallBackDialogConfirmWithWeChatNo callback, String classWechat, int position) {
		super();
		this.callback = callback;
		this.classWechat = classWechat;
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
		View v = inflater.inflate(R.layout.dialog_apply_join_class, null);// 得到加载view
		txtDialogTitleEn = (TextView) v.findViewById(R.id.txtDialogTitleEn);
//		txtDialogTitleEn.setText(titleStrEn);
		txtDialogTitleCn = (TextView) v.findViewById(R.id.txtDialogTitleCn);
//		txtDialogTitleCn.setText(titleStrCn);
		edWechat = (EditTextWidthTips) v.findViewById(R.id.edWechat);
		txtWechat = (TextView) v.findViewById(R.id.txtWechat);
		txtWechatTitle = (TextView) v.findViewById(R.id.txtWechatTitle);
		
		String weChatNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getWechatNo();
		if (!TextUtils.isEmpty(weChatNo)) {
			edWechat.setVal(weChatNo);
		}
		if (!TextUtils.isEmpty(classWechat)) {
			txtWechat.setText(classWechat + " (点击可复制)");
			txtWechat.setOnClickListener(this);
		}
		else{
			txtWechat.setText(R.string.classApplyForTip3);
		}
		
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
			callback.submit(position,edWechat.getVal());
			break;
		case R.id.flDialogCancel:
			callback.cancel();
//			dismiss();
			break;
		case R.id.txtWechat:
			ClipboardUtils.copy(classWechat);
			Toast.makeText(getActivity(), R.string.copyWechat, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	
}
