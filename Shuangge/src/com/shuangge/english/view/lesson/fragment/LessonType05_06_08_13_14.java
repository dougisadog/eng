package com.shuangge.english.view.lesson.fragment;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.lesson.LessonFragment;
import com.shuangge.english.entity.lesson.Phrase;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.MathUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.lesson.AtyLesson;
import com.shuangge.english.view.lesson.component.OptionImg;


public class LessonType05_06_08_13_14 extends BaseLessonType {
	
	public LessonType05_06_08_13_14() {
		super();
	}
	
	public LessonType05_06_08_13_14(LessonFragment data, CallbackLessonType callback) {
		super(data, callback);
	}
	
	private LinearLayout llImgsContainer, llImgsTopContainer, llImgsBottomContainer;
	private TextView txtQuestionTop;
	private ImageView imgQuestionTopPlay;
	private int margin = 10;
	private List<OptionImg> options;
	
	
	/***************************************************************************************************************************************/
	/**
	/**  initialization
	/**
	/***************************************************************************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initialized = false;
		anwserStrs = new ArrayList<String>();
		options = new ArrayList<OptionImg>();
		
		mainView = inflater.inflate(R.layout.lesson_05_06_08_13_14, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		llImgsContainer = (LinearLayout) mainView.findViewById(R.id.llImgsContainer);
		llImgsTopContainer = (LinearLayout) mainView.findViewById(R.id.llImgsTopContainer);
		llImgsBottomContainer = (LinearLayout) mainView.findViewById(R.id.llImgsBottomContainer);
		
		margin = DensityUtils.dip2px(getActivity(), 10);
		
		initCurrentView(data.getDatas().get(0));
		
		ViewTreeObserver vto = llImgsContainer.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!initialized) {
					if (llImgsContainer.getMeasuredWidth() <= 0 || llImgsContainer.getMeasuredHeight() <= 0) {
						return true;
					}
					initialized = true;
					initImgs(llImgsContainer.getMeasuredWidth(), llImgsContainer.getMeasuredHeight());
					nextStep();
				}
				return true;
			}

		});
		return mainView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		clearDatas();
	}
	
	private void initCurrentView(LessonData data) {
		//top
		txtQuestionTop = (TextView) mainView.findViewById(R.id.txtQuestionTop);
		imgQuestionTopPlay = (ImageView) mainView.findViewById(R.id.imgQuestionTopPlay);
		txtQuestionTop.setVisibility(View.GONE);
		imgQuestionTopPlay.setVisibility(View.GONE);
		
		//bottom
//		llImgsBottomContainer.setVisibility(View.GONE);
		String type = data.getType().toUpperCase(Locale.getDefault());
		if ("05A".equals(type)) {
			txtQuestionTop.setText(data.getAnswer().getContent());
			txtQuestionTop.setVisibility(View.VISIBLE);
			imgQuestionTopPlay.setOnClickListener(this);
			imgQuestionTopPlay.setVisibility(View.VISIBLE);
		}
		else if ("05B".equals(type)) {
			txtQuestionTop.setText(data.getAnswer().getContent());
			txtQuestionTop.setVisibility(View.VISIBLE);
		}
		//相当于05C
		else if ("06A".equals(type)) {
			imgQuestionTopPlay.setOnClickListener(this);
			imgQuestionTopPlay.setVisibility(View.VISIBLE);
		}
		else if ("08A".equals(type)) {
			txtQuestionTop.setText(data.getAnswer().getContent());
			txtQuestionTop.setVisibility(View.VISIBLE);
			imgQuestionTopPlay.setOnClickListener(this);
			imgQuestionTopPlay.setVisibility(View.VISIBLE);
		}
		else if ("13A".equals(type)) {
			txtQuestionTop.setText(data.getAnswer().getContent());
			txtQuestionTop.setVisibility(View.VISIBLE);
			imgQuestionTopPlay.setOnClickListener(this);
			imgQuestionTopPlay.setVisibility(View.VISIBLE);
		}
		else if ("13B".equals(type)) {
			imgQuestionTopPlay.setOnClickListener(this);
			imgQuestionTopPlay.setVisibility(View.VISIBLE);
		}
		else if ("13C".equals(type)) {
			txtQuestionTop.setText(data.getAnswer().getContent());
			txtQuestionTop.setVisibility(View.VISIBLE);
		}
		else if ("14A".equals(type)) {
			txtQuestionTop.setText(data.getAnswer().getContent());
			txtQuestionTop.setVisibility(View.VISIBLE);
			imgQuestionTopPlay.setOnClickListener(this);
			imgQuestionTopPlay.setVisibility(View.VISIBLE);
		}
		else if ("14B".equals(type)) {
			imgQuestionTopPlay.setOnClickListener(this);
			imgQuestionTopPlay.setVisibility(View.VISIBLE);
		}
		else if ("14C".equals(type)) {
			txtQuestionTop.setText(data.getAnswer().getContent());
			txtQuestionTop.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 05 08 13题型
	 * @param data
	 */
	private void init05And08And13(LessonData data, int w, int h) {
		anwserSoundPath = data.getAnswer().getLocalSoundPath();
		anwserStrs.clear();
		anwserStrs.add(data.getAnswer().getContent());
	
		if (w > h) {
			w = h;
		}
		w = w / 2 - 10 * 3;
		h = w;
		MathUtils.ramdomList(data.getPhrases());
		Phrase phrase = null;
		for (int i = 0; i < data.getPhrases().size() && i < 2; i++) {
			phrase = data.getPhrases().get(i);
			llImgsTopContainer.addView(createImgOption(phrase.getContent(), phrase.getLocalImgPath(), w, h, i * margin));
		}
		if (data.getPhrases().size() > 2) {
			for (int i = 2; i < data.getPhrases().size() && i < 4; i++) {
				phrase = data.getPhrases().get(i);
				llImgsBottomContainer.addView(createImgOption(phrase.getContent(), phrase.getLocalImgPath(), w, h, (i - 2) * margin));
			}
		}
	}
	
	/**
	 * 14题型
	 * @param data
	 */
	private void init14(LessonData data, int w, int h) {
		anwserSoundPath = data.getAnswer().getLocalSoundPath();
		anwserStrs.clear();
		anwserStrs.add(data.getAnswer().getContent());
		
		if ((h - margin) / 2 * 1.46 > w) {
			h = (int) (w * 2 / 1.46 + margin);
		}
		else {
			w = (int) ((h - margin) / 2 * 1.46);
		}
		w -= margin * 2;
		h = (int) (w / 1.46);
		MathUtils.ramdomList(data.getPhrases());
		Phrase phrase = data.getPhrases().get(0);
		llImgsTopContainer.addView(createImgOption(phrase.getContent(), phrase.getLocalImgPath(), w, h, 0));
		phrase = data.getPhrases().get(1);
		llImgsBottomContainer.addView(createImgOption(phrase.getContent(), phrase.getLocalImgPath(), w, h, 0));
	}
	
	private OptionImg createImgOption(String tag, String path, int w, int h, int l) {
		long time = System.currentTimeMillis();
		Bitmap bitmap = GlobalResTypes.getInstance().getLessonBitmap(path);
		OptionImg option = new OptionImg(getActivity(), R.drawable.bg_head, bitmap, w, h);
		ViewUtils.setLinearMargins(option, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, l, 0, 0, 0);
		option.setTag(tag);
		option.setOnClickListener(onClickListener);
//		option.setVisibility(View.GONE);
		options.add(option);
		if (null == bitmap) {
			GlobalResTypes.getInstance().displayLessonBitmapNoCache(path, option);
		}
		DebugPrinter.e("每张图片创建耗时:" + (System.currentTimeMillis() - time) + "ms");
//		option.setVisibility(View.INVISIBLE);
		return option;
	}
	
	private void showOptions() {
		if (false && null != options) {
			Animation animation = null;
			for (OptionImg option : options) {
				option.setVisibility(View.VISIBLE);
				animation = new AlphaAnimation(0, 1);
				animation.setDuration(500);
				animation.setFillAfter(true);
				option.clearAnimation();
				option.startAnimation(animation);
			}
		}
	}
	
	private void hideOptions() {
		if (false && null != options) {
			for (OptionImg option : options) {
				option.clearAnimation();
				option.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	private void initImgs(int w, int h) {
		long time = System.currentTimeMillis();
		LessonData lessonData = data.getDatas().get(0);
		String type = lessonData.getType().toUpperCase(Locale.getDefault());		
		if (type.indexOf("14") != -1) {
			init14(lessonData, w, h);
		}
		else {
			init05And08And13(lessonData, w, h);
		}
//		case "05A": case "05B": case "06A":
//		case "08A":
//		case "13A": case "13B": case "13C":
//			init05And08And13(lessonData, w, h);
//			break;
//		case "14A": case "14B": case "14C":
//			init14(lessonData, w, h);
//			break;
//		}
		DebugPrinter.i("耗时图片计算耗时 :" + (System.currentTimeMillis() - time) + "ms");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgQuestionTopPlay:
			if (null != getCurrentData()) {
				MediaPlayerMgr.getInstance().playMp(getCurrentData().getAnswer().getLocalSoundPath(), onCompletionListener);
			}
			break;
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  start stop reset nextCurrentTypeStep
	/**
	/***************************************************************************************************************************************/

	@Override
	protected void onCurrentStepPrevStart(LessonData currentData) {
//		initCurrentView(currentData);
	}
	
	public static Set<String> NO_PLAYING_SOUND_TYPE_SET = new HashSet<String>();
	
	static {
		NO_PLAYING_SOUND_TYPE_SET.add("02C");
		NO_PLAYING_SOUND_TYPE_SET.add("03A");
		NO_PLAYING_SOUND_TYPE_SET.add("03B");
		NO_PLAYING_SOUND_TYPE_SET.add("05B");
		NO_PLAYING_SOUND_TYPE_SET.add("07A");
		NO_PLAYING_SOUND_TYPE_SET.add("10A");
		NO_PLAYING_SOUND_TYPE_SET.add("13C");
		NO_PLAYING_SOUND_TYPE_SET.add("14C");
	}
	
	@Override
	protected void onCurrentStepStart(LessonData currentData) {
		pass = false;
		showOptions();
		//不需要播音频的题型
		String type = getCurrentData().getType().toUpperCase(Locale.getDefault());
		if (NO_PLAYING_SOUND_TYPE_SET.contains(type)) {
			return;
		}
		MediaPlayerMgr.getInstance().playMp(getCurrentData().getAnswer().getLocalSoundPath(), onCompletionListener);
	}
	
	@Override
	public void stop() {
		super.stop();
		clearWait();
		hideOptions();
		MediaPlayerMgr.getInstance().stopMp();
	}
	
	public void reset() {
		if (!isRunning()) {
			return;
		}
		answerIndex = 0;
		pass = false;
		clearWait();
		if (null != options) {
			for (OptionImg option : options) {
				option.setOnClickListener(onClickListener);
				option.reset();
			}
		}
	}
	
	private void clearDatas() {
		clearWait();
		if (null != llImgsTopContainer) {
			llImgsTopContainer.removeAllViews();
			llImgsTopContainer = null;
		}
		if (null != llImgsBottomContainer) {
			llImgsBottomContainer.removeAllViews();
			llImgsBottomContainer = null;
		}
		if (null != options) {
			for (OptionImg img : options) {
				img.recycle();
			}
			options.clear();
			options = null;
		}
		anwserStrs = null;
		answerIndex = 0;
		pass = false;
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  Thread wait 1 second
	/**
	/***************************************************************************************************************************************/
	
	private Handler handler = new Handler();
	
	private void clearWait() {
		if (null != wait1SecondThread)
			handler.removeCallbacks(wait1SecondThread);
	}
	
	public void waitToFinish(Wait1SecondThread r) {
		if (null != wait1SecondThread)
			handler.removeCallbacks(wait1SecondThread);
		wait1SecondThread = r;
		handler.postDelayed(wait1SecondThread, 1000);
	}
	
	private Wait1SecondThread wait1SecondThread;
	
    private class Wait1SecondThread extends Thread {
    	
    	private boolean pass = false;
    	
    	public Wait1SecondThread(boolean pass) {
    		this.pass = pass;
    	}
    	
    	public void run() {
    		if (!isRunning()) {
    			return;
    		}
    		try {
    			LessonType05_06_08_13_14.this.pass = pass;
    			MediaPlayerMgr.getInstance().playMp(anwserSoundPath, onCompletionListener);
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    };
	
	/***************************************************************************************************************************************/
	/**
	/**  MediaPlayer onCompletionListener
	/**
	/***************************************************************************************************************************************/
	
	private OnCompletionListener onCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion() {
			if (!isRunning()) {
				return;
			}
			if (pass) {
				nextStep();
			}
		}
		
	};
	
	/***************************************************************************************************************************************/
	/**
	/**  Grade onClickListener
	/**
	/***************************************************************************************************************************************/
	
	//目前step的正确答案列表
	private List<String> anwserStrs;
	//当前正确答案的索引
	private int answerIndex = 0;
	//回答正确后音频
	private String anwserSoundPath;
	//判断是否正确的标识
	private boolean pass = false;

	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null == getActivity() || ((AtyLesson) getActivity()).isMoving()) {
				return;
			}
			String str = (String) v.getTag();
			// right
			String rightAnwser = anwserStrs.get(answerIndex);
			if (rightAnwser.toUpperCase(Locale.getDefault()).equals(str.toUpperCase(Locale.getDefault()))) {
				//全部正确
				if (answerIndex + 1 >= anwserStrs.size()) {
					for (OptionImg option : options) {
						option.setOnClickListener(null);
						if (option.equals(v)) {
							continue;
						}
						option.setWrong();
					}
					rightHandler();
					return;
				}
				++answerIndex;
				SoundPoolMgr.getInstance().playTapRightSnd();
			}
			// wrong
			else {
//				if (answerIndex + 1 >= anwserStrs.size()) {
				if (anwserStrs.size() == 1) {
					pass = false;
					wrongHandler();
					return;
				}
				SoundPoolMgr.getInstance().playTapWrongSnd();
			}
		}
		
	};
	
	private void rightHandler() {
		MediaPlayerMgr.getInstance().stopMp();
		setResult(LessonData.RESULT_RIGHT);
		SoundPoolMgr.getInstance().playRightSnd();
		((AtyLesson) getActivity()).rightHandler();
		pass = true;
		waitToFinish(new Wait1SecondThread(true));
	}
	
	private void wrongHandler() {
		MediaPlayerMgr.getInstance().stopMp();
		setResult(LessonData.RESULT_WRONG);
		((AtyLesson) getActivity()).wrongHandler();
		SoundPoolMgr.getInstance().playWrongSnd();
	}
	
	/***************************************************************************************************************************************/
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		clearDatas();
	}
	
}
