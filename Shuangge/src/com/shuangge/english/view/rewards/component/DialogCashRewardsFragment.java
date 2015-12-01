package com.shuangge.english.view.rewards.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.support.utils.ClipboardUtils;

public class DialogCashRewardsFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onClose();
		public void onSubmit();
	}
	
	private CallBackDialogConfirm callback;
	private ImageView btnClose;
	private TextView txtRewardsInfo, txtScholarship;
	private String content;
	private FrameLayout flSubmit;
	private Double rewardsAccount;
	public DialogCashRewardsFragment() {
		super();
	}
	
	public DialogCashRewardsFragment(CallBackDialogConfirm callback, String content,double money) {
		super();
		this.callback = callback;
		setCancelable(true);
		this.content = content;
		this.rewardsAccount = money;
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
		View v = inflater.inflate(R.layout.dialog_reward_self, null);// 得到加载view
		btnClose = (ImageView) v.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(this);
		flSubmit = (FrameLayout) v.findViewById(R.id.flSubmit);
		flSubmit.setOnClickListener(this);
		
		txtScholarship = (TextView) v.findViewById(R.id.txtScholarship);
		String rewardsAccountStr = "￥0";
		if (null != rewardsAccount) {
			rewardsAccountStr = "￥" + rewardsAccount;
		}
		txtScholarship.setText(rewardsAccountStr + "");
		txtRewardsInfo = (TextView) v.findViewById(R.id.txtRewardsInfo);
		txtRewardsInfo.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动 
		
		parseContent(content);
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
		case R.id.btnClose:
			callback.onClose();
			break;
		case R.id.flSubmit:
			callback.onSubmit();
			break;
		}
	}
	
	//待重写
	private void parseContent(String content) {
		MyGroupListResult data = GlobalRes.getInstance().getBeans().getMyGroupDatas();
		if (null != data && null != data.getClassInfos() && data.getClassInfos().size() > 0 && !TextUtils.isEmpty(data.getClassInfos().get(0).getWechatNo())) {
			content = content.replace("xxxx", data.getClassInfos().get(0).getWechatNo());
		}
		else {
			content = content.replace("&xxxx&", "班长未填写微信");
		}
		String formatContent = content.replaceAll("&", "");
		Spanned ssb = Html.fromHtml(content);
		Spanned formatSsb = Html.fromHtml(formatContent);
		content = ssb.toString();
		int start = 0;
		int end = 0;
		int i = 0;
		while ((start = content.indexOf("&", start)) != -1) {
			end = content.indexOf("&", start + 1);
			if (end != -1) {
				RewardsClickableSpan cs = new RewardsClickableSpan(content.substring(start - i, end - (i + 1)));
				((SpannableStringBuilder) formatSsb).setSpan(cs, start - i, end - (i + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				start = end + 1;
				++i;
			}
		}
		txtRewardsInfo.setMovementMethod(LinkMovementMethod.getInstance());
		txtRewardsInfo.setText(formatSsb);
	}
	
	class RewardsClickableSpan extends ClickableSpan {

		private String content;
		
		public RewardsClickableSpan(String content) {
			this.content = content;
		}
		
		@Override
		public void onClick(View widget) {
			ClipboardUtils.copy(content);
			Toast.makeText(getActivity(), R.string.copyWechat, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void updateDrawState(TextPaint ds) {
//			ds.setColor(Color.BLACK);
			ds.setUnderlineText(false);
		}
		
	}
	
}
