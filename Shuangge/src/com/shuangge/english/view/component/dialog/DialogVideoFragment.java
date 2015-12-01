package com.shuangge.english.view.component.dialog;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.view.read.AtyVideoTest;
import com.shuangge.english.view.read.fragment.MyVideoViewPlayer;

public class DialogVideoFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onClose(int position);
		
		public void onVideocompleted();
		
	}
	
	private CallBackDialogConfirm callback;
	private ImageView btnClose;
	private int position;
	private VideoView videoView;
	private TextView txtTitle;
	
	private MyVideoViewPlayer myVideoPlayer;
	
	private String videoUrl;
	
	public DialogVideoFragment(String videoUrl) {
		super();
		this.videoUrl = videoUrl;
		int style = DialogFragment1.STYLE_NO_TITLE; 
//    	setStyle(style, R.style.DialogAllScreenTheme);
    	setStyle(style, R.style.TopToBottomAnimTheme);
	}
	
	public DialogVideoFragment(CallBackDialogConfirm callback, String videoUrl) {
		super();
		this.callback = callback;
		setCancelable(true);
		this.videoUrl = videoUrl;
		
    	int style = DialogFragment1.STYLE_NO_TITLE; 
//    	setStyle(style, R.style.DialogAllScreenTheme);
    	setStyle(style, R.style.DialogTopToBottomTheme);
    	
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		if (isAdded()) {
			return;
		}
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	public void setVideoUrl(String url) {
		this.videoUrl = url;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_video, null);// 得到加载view
		btnClose = (ImageView) v.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(this);
		videoView = (VideoView) v.findViewById(R.id.videoView);
		txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setText("title");
		myVideoPlayer = new MyVideoViewPlayer(getActivity(), videoView, new MyVideoViewPlayer.VideoCallBack() {
			
			@Override
			public void refreshProgressText(String text) {
				txtTitle.setText(text);
			}
			
			@Override
			public void progressUpdate(int progress) {
			}
			
			@Override
			public void prepare4Video() {
			}

			@Override
			public void onVideoCompleted() {
				callback.onVideocompleted();
			}
		});
		
		//url 可以加个校验
		if (!TextUtils.isEmpty(videoUrl)) {
			myVideoPlayer.initVideo(videoUrl);
		}
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
			callback.onClose(position);
			callback.onVideocompleted();
			break;
		}
	}
}
