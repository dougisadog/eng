package com.shuangge.english.view.read.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
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
import com.shuangge.english.view.component.dialog.DialogVideoFragment;
import com.shuangge.english.view.read.AtyVideoTest;
import com.shuangge.english.view.read.adapter.AdapterVideoTest;
import com.shuangge.english.view.read.component.ReadLessonViewPager;
import com.shuangge.english.view.read.fragment.FragmentVideoTest.CallBackSelection;

public class DialogVideoQuestionFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onClose(int position);
		
		public void onVideocompleted();
		
	}
	
	private CallBackDialogConfirm callback;
	
	private TextView txtBarTitleCn ,txtBarTitleEn, restart;
	
	private AdapterVideoTest adapter;
	
	private ReadLessonViewPager vp;
	
	private VideoView videoview;
	
	private MyVideoViewPlayer myVideoPlayer;
	
	private List<String> videoUrls;
	
	private DialogVideoFragment dialogVideo;
	
	private List<BaseWordListFragment> fragments;
	
	private int index = 0;
	
	public DialogVideoQuestionFragment(List<String> videUrls) {
		super();
		this.videoUrls = videUrls;
//		int style = DialogFragment1.STYLE_NO_FRAME;
//    	setStyle(style, 0);
//    	setStyle(style, R.style.DialogRightToLeftTheme);
	}
	
	public DialogVideoQuestionFragment(CallBackDialogConfirm callback,List<String> videUrls) {
		super();
		this.callback = callback;
		this.videoUrls = videUrls;
		setCancelable(true);
		
//    	int style = DialogFragment1.STYLE_NO_FRAME; 
//    	setStyle(style, 0);
//    	setStyle(style, R.style.DialogAllScreenTheme);
    	
	}
	
	public void showDialog(FragmentManager fragmentManager,int index) {
		this.index = index-1;
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	public void hideDialog(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(this);
        ft.commitAllowingStateLoss();
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.aty_video_question, null);// 得到加载view
		txtBarTitleEn = (TextView) v.findViewById(R.id.txtBarTitleEn);
 		txtBarTitleEn.setText("Video Question");
 		txtBarTitleCn = (TextView) v.findViewById(R.id.txtBarTitleCn);
        txtBarTitleCn.setText("视频问题");
        restart = (TextView) v.findViewById(R.id.restart);
        restart.setOnClickListener(this);
    	videoview = (VideoView) v.findViewById(R.id.videoView);
    	vp = (ReadLessonViewPager) v.findViewById(R.id.vp);
    	String fragmentVideoURL = "http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/421771.mp4";
    	initAnswerDialog(fragmentVideoURL);
		initVideoPlayer();
		
		//url 可以加个校验
		if (!TextUtils.isEmpty(videoUrls.get(index))) {
			myVideoPlayer.initVideo(videoUrls.get(index));
		}
		adapter = new AdapterVideoTest(getChildFragmentManager());
		initDatas();
		vp.setCurrentItem(index);
		return v;
	}
	
	private void initAnswerDialog(String fragmentVideoURL) {
		
		dialogVideo = new DialogVideoFragment(new DialogVideoFragment.CallBackDialogConfirm() {

			@Override
			public void onClose(int position) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onVideocompleted() {
				dialogVideo.dismiss();
				hideDialog(getFragmentManager());
			}
		}, fragmentVideoURL);
	}
	private void initVideoPlayer(){
		myVideoPlayer = new MyVideoViewPlayer(getActivity(), videoview, new MyVideoViewPlayer.VideoCallBack() {
			
			@Override
			public void refreshProgressText(String text) {
				txtBarTitleCn.setText(text);
			}
			
			@Override
			public void progressUpdate(int progress) {
			}
			
			@Override
			public void prepare4Video() {
			}

			@Override
			public void onVideoCompleted() {
				vp.setCurrentItem(index);
				callback.onVideocompleted();
			}
		});
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
	
	/**
	 * 模拟数据 
	 */
	private void initDatas() {
		List<FragmentVideoTest> fragmentVideoTests = new ArrayList<FragmentVideoTest>();
		
		List<Answer> answers1 = new ArrayList<Answer>();
		List<Answer> answers2 = new ArrayList<Answer>();
		List<Answer> answers3 = new ArrayList<Answer>();
		for (int i = 1; i < 10; i++) {
			Answer answer = new Answer();
			String name = "1000" + i + ".jpg";
			answer.setImageURL("http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/img/" + name);
			answer.setName(name);
			answer.setSoundURL("http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/1000"+ i +".mp3");
			if (i%3 == 0) {
				answer.setRight(true);
			}
			if (i <4 && i >=1) {
				answer.setOt(FragmentVideoTest.OPTION_TYPE.TEXT);
				answers1.add(answer);
			}
			if (i <7 && i >=4) {
				answer.setOt(FragmentVideoTest.OPTION_TYPE.IMAGE);
				answers2.add(answer);
			}
			if (i >=7) {
				answer.setOt(FragmentVideoTest.OPTION_TYPE.MUTI_VIDEO);
				answers3.add(answer);
			}
		}
		
		CallBackSelection callBackSelection = new CallBackSelection() {
			
			@Override
			public void wrongSelected(Answer answer) {
				String s = null == answer ? "" : answer.getName();
				Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();;
			}
			
			@Override
			public void rightSelected(Answer answer) {
//				vp.setCurrentItem(0);
//				videoview.start();
				String fragmentVideoURL = "http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/421700.mp4";
				dialogVideo.setVideoUrl(fragmentVideoURL);
				dialogVideo.showDialog(getFragmentManager());
			}
		};
		fragmentVideoTests.add(new FragmentVideoTest(null));
		fragmentVideoTests.add(new FragmentVideoTest(answers1, callBackSelection));
		fragmentVideoTests.add(new FragmentVideoTest(answers2, callBackSelection));
		fragmentVideoTests.add(new FragmentVideoTest(answers3, callBackSelection));
		adapter.setDatas(getChildFragmentManager(), fragmentVideoTests);
//		addVideo(AppInfo.getScreenWidth());
		
		fragments = new ArrayList<BaseWordListFragment>();
		fragments.add(new FragmentHasLearnWords());
		
		vp.setAdapter(adapter);
		vp.setCurrentItem(0);
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
			callback.onVideocompleted();
			break;
		case R.id.restart:
			myVideoPlayer.seekTo(0);
			myVideoPlayer.startPlay();
			break;
		}
	}
}
