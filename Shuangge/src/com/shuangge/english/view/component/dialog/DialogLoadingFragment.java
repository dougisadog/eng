package com.shuangge.english.view.component.dialog;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuangge.english.GlobalApp;
public class DialogLoadingFragment extends DialogFragment1 {

	private static final int STATE_SHOW = 1;
	private static final int STATE_HIDE = 0;
	
	private TextView txtLoadingInfo;
	private String loadingInfo;
	private ICallbackDialog callback;
	private boolean isMCancelable;
	
	private FragmentManager fragmentManager;
	
	private int state = STATE_HIDE;
	
	public DialogLoadingFragment() {
		super();
	}
	
	public DialogLoadingFragment(boolean cancelable, ICallbackDialog callback) {
		super();
		setCancelable(false);
		this.isMCancelable = cancelable;
    	int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
    	setStyle(style, theme);
    	this.callback = callback;
	}
	
	public DialogLoadingFragment(String loadingInfo, boolean cancelable, ICallbackDialog callback) {
		super();
		setCancelable(false);
		this.isMCancelable = cancelable;
		int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
		setStyle(style, theme);
		this.loadingInfo = loadingInfo;
		this.callback = callback;
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		state = STATE_SHOW;
		this.fragmentManager = fragmentManager;
	}
	
	public void hideDialog() {
		state = STATE_HIDE;
		if (isAdded()) {
			dismissAllowingStateLoss();
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (state == STATE_HIDE) {
			dismissAllowingStateLoss();
		}
	}
	
	public void onResumeFragments() {
		if (!isAdded() && !isVisible() && !isRemoving() && state == STATE_SHOW) {
			show(fragmentManager, "dialog");
//			FragmentTransaction ft = fragmentManager.beginTransaction();
//			ft.add(this, "dialog");
//			ft.commitAllowingStateLoss();
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		initKeyListener(dialog);
		return dialog;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		txtLoadingInfo = (TextView) v.findViewById(R.id.txtLoadingInfo);
		if (!TextUtils.isEmpty(loadingInfo)) {
			txtLoadingInfo.setText(loadingInfo);
		}
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(GlobalApp.getInstance().getActivity(), R.anim.loading_animation);
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		return v;
	}
	
	public static interface ICallbackDialog {
		
		public boolean onKeyDown(int keyCode, KeyEvent event);
		
	}
	
	private void initKeyListener(Dialog dialog) {
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (null != callback) {
					return callback.onKeyDown(keyCode, event);
				}
				return false;
			}
       });
	}
	
	@Override
	public boolean isCancelable() {
		return isMCancelable;
	}
	
}
