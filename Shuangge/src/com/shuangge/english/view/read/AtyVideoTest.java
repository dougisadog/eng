package com.shuangge.english.view.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogVideoFragment;
import com.shuangge.english.view.lesson.component.OptionVideo;
import com.shuangge.english.view.read.adapter.AdapterVideoTest;
import com.shuangge.english.view.read.component.ReadLessonViewPager;
import com.shuangge.english.view.read.fragment.Answer;
import com.shuangge.english.view.read.fragment.BaseWordListFragment;
import com.shuangge.english.view.read.fragment.DialogVideoQuestionFragment;
import com.shuangge.english.view.read.fragment.FragmentHasLearnWords;
import com.shuangge.english.view.read.fragment.FragmentVideoTest;
import com.shuangge.english.view.read.fragment.MyVideoViewPlayer;
import com.shuangge.english.view.read.fragment.FragmentVideoTest.CallBackSelection;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * 核心单词  
 * @author tovey
 *
 */
public class AtyVideoTest extends AbstractAppActivity implements OnClickListener {
	

	private TextView txtBarTitleCn ,txtBarTitleEn;
	
	private AdapterVideoTest adapter;
	
	private ReadLessonViewPager vp;
	
	public static enum OPTION_TYPE {
		TEXT, IMAGE, MUTI_VIDEO;
	}
	
	private VideoView videoview;
	
	private Map<Integer, Integer> mapDatas; // 停顿点  和  切换得fragment 位置
	
	private MyVideoViewPlayer myVideoViewPlayer;
	
	private DialogVideoQuestionFragment dialogVideoQuestion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.aty_video_test);
 		findViewById(R.id.btnBack).setOnClickListener(this);
 		txtBarTitleEn = (TextView) findViewById(R.id.txtBarTitleEn);
 		txtBarTitleEn.setText("Core Vocabulary");
 		txtBarTitleCn = (TextView) findViewById(R.id.txtBarTitleCn);
        txtBarTitleCn.setText("核心词汇");
       
    	videoview = (VideoView) findViewById(R.id.VideoView);
    	vp = (ReadLessonViewPager) findViewById(R.id.vp);
		
		String VideoURL = "http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/421746.mp4";
		
		String fragmentVideoURL2 = "http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/421700.mp4";
		String fragmentVideoURL = "http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/421771.mp4";
		List<String> urls = new ArrayList<String>();
		urls.add(VideoURL);
		urls.add(fragmentVideoURL2);
		urls.add(fragmentVideoURL);
		dialogVideoQuestion = new DialogVideoQuestionFragment(new DialogVideoQuestionFragment.CallBackDialogConfirm() {

			@Override
			public void onClose(int position) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onVideocompleted() {
			}
		}, urls);
		
        initDatas();
        
        myVideoViewPlayer = new MyVideoViewPlayer(this, videoview, new MyVideoViewPlayer.VideoCallBack() {
			
			@Override
			public void refreshProgressText(String text) {
				txtBarTitleCn.setText(text);
			}
			
			@Override
			public void progressUpdate(int progress) {
				Toast.makeText(AtyVideoTest.this, progress + "", Toast.LENGTH_SHORT).show();
				if (null != mapDatas.get(progress)) {
//					vp.setCurrentItem(mapDatas.get(progress));
					
					myVideoViewPlayer.pausePlay();
					dialogVideoQuestion.showDialog(getSupportFragmentManager(), mapDatas.get(progress));
				}
			}
			
			@Override
			public void prepare4Video() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onVideoCompleted() {
				
			}
		}, mapDatas);
        
        myVideoViewPlayer.initVideo(VideoURL);
	}
	
	
	@Override
	protected void onDestroy() {
		myVideoViewPlayer.setAtyVideoTestAlive(false);
		super.onDestroy();
	}
	
	
	
	/**
	 * 模拟数据 
	 */
	@SuppressLint("UseSparseArrays")
	private void initDatas() {
		mapDatas = new HashMap<Integer, Integer>();
		mapDatas.put(3, 1);
		mapDatas.put(8, 2);
		mapDatas.put(13, 3);
		/*
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
				Toast.makeText(AtyVideoTest.this, s, Toast.LENGTH_SHORT).show();;
			}
			
			@Override
			public void rightSelected(Answer answer) {
				vp.setCurrentItem(0);
//				videoview.start();
				String fragmentVideoURL = "http://res.happyge.com:8081/client/kechengceshi2/101/10101/101010100/snd/421700.mp4";
				dialogVideo.setVideoUrl(fragmentVideoURL);
				dialogVideo.showDialog(getSupportFragmentManager());
			}
		};
		fragmentVideoTests.add(new FragmentVideoTest(null));
		fragmentVideoTests.add(new FragmentVideoTest(answers1, callBackSelection));
		fragmentVideoTests.add(new FragmentVideoTest(answers2, callBackSelection));
		fragmentVideoTests.add(new FragmentVideoTest(answers3, callBackSelection));
		adapter.setDatas(getSupportFragmentManager(), fragmentVideoTests);
//		addVideo(AppInfo.getScreenWidth());
		
		fragments = new ArrayList<BaseWordListFragment>();
		fragments.add(new FragmentHasLearnWords());
		
		vp.setAdapter(adapter);
		vp.setCurrentItem(0);
		*/
	}
	
	private List<BaseWordListFragment> fragments;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myVideoViewPlayer.pausePlay();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		myVideoViewPlayer.pausePlay();
	}

	public static void startAty(Context context) {
		context.startActivity(new Intent(context, AtyVideoTest.class));
	}

}
