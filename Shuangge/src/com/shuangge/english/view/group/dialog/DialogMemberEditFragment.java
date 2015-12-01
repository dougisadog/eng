package com.shuangge.english.view.group.dialog;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;

public class DialogMemberEditFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackClassMemberEdit {
		
		public void kill(int position);
		
		public void profile(int position);
		
		public void assigningAuthority(int position, int authorityLevel);
		
	}
	
	private int authLevel;
	private int position;
	private CallBackClassMemberEdit callback;
	private FrameLayout flClassMemberEditTip1, flClassMemberEditTip2, flClassMemberEditTip3, flClassMemberEditTip4;
	
	public DialogMemberEditFragment() {
		super();
	}
	
	public DialogMemberEditFragment(CallBackClassMemberEdit callback, int position, int authLevel) {
		super();
		this.callback = callback;
		this.position = position;
		this.authLevel = authLevel;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE, theme = R.style.DialogBottomToTopTheme; 
    	setStyle(style, theme);
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_member_edit, null);// 得到加载view
		flClassMemberEditTip1 = (FrameLayout) v.findViewById(R.id.flClassMemberEditTip1);
		flClassMemberEditTip2 = (FrameLayout) v.findViewById(R.id.flClassMemberEditTip2);
		flClassMemberEditTip1.setOnClickListener(this);
		flClassMemberEditTip2.setOnClickListener(this);
		if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() == ClassData.MANAGER) {
			if (authLevel == ClassData.NORMAL) {
				flClassMemberEditTip2.setVisibility(View.GONE);
			}
			else {
				flClassMemberEditTip1.setVisibility(View.GONE);
			}
		}
		else {
			flClassMemberEditTip1.setVisibility(View.GONE);
			flClassMemberEditTip2.setVisibility(View.GONE);
		}
		flClassMemberEditTip3 = (FrameLayout) v.findViewById(R.id.flClassMemberEditTip3);
		flClassMemberEditTip3.setOnClickListener(this);
		flClassMemberEditTip4 = (FrameLayout) v.findViewById(R.id.flClassMemberEditTip4);
		flClassMemberEditTip4.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.flClassMemberEditTip1:
			callback.assigningAuthority(position, ClassData.SUB_MANAGER);
			break;
		case R.id.flClassMemberEditTip2:
			callback.assigningAuthority(position, ClassData.NORMAL);
			break;
		case R.id.flClassMemberEditTip3:
			callback.kill(position);
			break;
		case R.id.flClassMemberEditTip4:
			callback.profile(position);
			break;
		}
	}
	
}
