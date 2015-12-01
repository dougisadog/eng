package com.shuangge.english.view.lesson.fragment;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.yunzhisheng.oraleval.sdk.IOralEvalSDK.Error;
import cn.yunzhisheng.oraleval.sdk.IOralEvalSDK.OfflineSDKPreparationError;

import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.lesson.LessonFragment;
import com.shuangge.english.entity.lesson.Phrase;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.app.ScreenObserver;
import com.shuangge.english.support.utils.CheckUtils;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.IOralEvalSDKMgr;
import com.shuangge.english.support.utils.IOralEvalSDKMgr.CallbackIOralEvalSDK;
import com.shuangge.english.support.utils.MathUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.RatingBarView;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.wave.WaveformView;
import com.shuangge.english.view.lesson.AtyLesson;
import com.shuangge.english.view.lesson.component.OptionImg;
import com.shuangge.english.view.lesson.component.OptionTxt;


public class LessonType01_02_03_07_10_11 extends BaseLessonType {
	
	public LessonType01_02_03_07_10_11() {
		super();
	}
	
	public LessonType01_02_03_07_10_11(LessonFragment data, CallbackLessonType callback) {
		super(data, callback);
	}
	
	private RelativeLayout rlQuestionTop, rlQuestionBottom, rlOptions02, rlOptions03, rlOptions07, rlOptions10And11;
	private LinearLayout llImgsContainer, llLessonQuestionBottom;
	private TextView txtQuestionTop, txtQuestionBottom, rlOptions01;
	private ImageView imgQuestionTopPlay, imgQuestionBottomPlay;
	private WaveformView wv;
	private LinearLayout llRecordResult;
	private RatingBarView rbStar;
	private TextView txtRecordStar;
	
	private int margin = 10;
	private int btnH = 50;
	private List<OptionImg> imgs;
	private List<OptionTxt> options;
	
	
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
		currentWordSet = new HashSet<String>();
		nextRightWords = new ArrayList<String>();
		options = new ArrayList<OptionTxt>();
		imgs = new ArrayList<OptionImg>();
		
		mainView = inflater.inflate(R.layout.lesson_01_02_03_07_10_11, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		llImgsContainer = (LinearLayout) mainView.findViewById(R.id.llImgsContainer);
		llLessonQuestionBottom = (LinearLayout) mainView.findViewById(R.id.llLessonQuestionBottom);
		
		rlQuestionTop = (RelativeLayout) mainView.findViewById(R.id.rlQuestionTop);
		txtQuestionTop = (TextView) mainView.findViewById(R.id.txtQuestionTop);
		imgQuestionTopPlay = (ImageView) mainView.findViewById(R.id.imgQuestionTopPlay);
		
		btnH = DensityUtils.dip2px(getActivity(), 50);
		margin = DensityUtils.dip2px(getActivity(), 10);
		
		if (data.getDatas().size() > 1) {
			llImgsContainer.setGravity(Gravity.TOP|Gravity.CENTER);
			llImgsContainer.setPadding(0, 20, 0, 0);
		}
		else {
			llImgsContainer.setGravity(Gravity.CENTER);
			llImgsContainer.setPadding(0, 0, 0, 0);
		}
		
		initCurrentView(data.getDatas().get(0));
		
		//判断是否上移
		if (rlQuestionTop.getVisibility() == View.GONE) {
			boolean visible = false;
			for (LessonData data1 : data.getDatas()) {
				String type = data1.getType().toUpperCase(Locale.getDefault());
				//需要显示顶部的题型
				if (NO_TOP_TYPE_SET.contains(type)) {
					visible = true;
				}
			}
			if (visible) {
				rlQuestionTop.setVisibility(View.INVISIBLE);
			}
			else {
//				ViewUtils.setRelativeMargins(llImgsContainer, LinearLayout.LayoutParams.MATCH_PARENT, 
//						LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0);
			}
		}
		
		ViewTreeObserver vto = mainView.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!initialized) {
					if (llImgsContainer.getMeasuredWidth() <= 0
							|| mainView.getMeasuredHeight() <= 0
							|| (rlQuestionTop.getVisibility() != View.GONE && rlQuestionTop.getMeasuredHeight() <= 0) 
							|| (llLessonQuestionBottom.getVisibility() != View.GONE && llLessonQuestionBottom.getMeasuredWidth() <= 0)) {
						return true;
					}
					initialized = true;
					initImgs(llImgsContainer.getMeasuredWidth(), mainView.getMeasuredHeight() - 
							rlQuestionTop.getMeasuredHeight() - llLessonQuestionBottom.getMeasuredHeight());
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
	
	private void clearDatas() {
		if (null != options) {
			for (OptionTxt option : options) {
				option.recycle();
			}
			options.clear();
			options = null;
		}
		if (null != imgs) {
			for (OptionImg img : imgs) {
				img.recycle();
			}
			imgs.clear();
			imgs = null;
		}
		if (null != rlOptions02) {
			rlOptions02.removeAllViews();
			rlOptions02 = null;
		}
		if (null != rlOptions03) {
			rlOptions03.removeAllViews();
			rlOptions03 = null;
		}
		if (null != rlOptions07) {
			rlOptions07.removeAllViews();
			rlOptions07 = null;
		}
		if (null != rlOptions10And11) {
			rlOptions10And11.removeAllViews();
			rlOptions10And11 = null;
		}
		clearWait();
		anwserStrs = null;
		currentWordSet = null;
		nextRightWords = null;
	}
	
	//初始化当前题目片段
	private void initCurrentView(LessonData currentData) {
		currentType = currentData.getType().toUpperCase(Locale.getDefault());
		if (data.getDatas().size() > 1) {
			//top
			rlQuestionTop.setVisibility(View.INVISIBLE);
			//bottom
			llLessonQuestionBottom.setVisibility(View.INVISIBLE);
		}
		else {
			//top
			rlQuestionTop.setVisibility(View.GONE);
			//bottom
			llLessonQuestionBottom.setVisibility(View.GONE);
		}
		
		
		rlQuestionBottom = (RelativeLayout) mainView.findViewById(R.id.rlQuestionBottom);
		txtQuestionBottom = (TextView) mainView.findViewById(R.id.txtQuestionBottom);
		imgQuestionBottomPlay = (ImageView) mainView.findViewById(R.id.imgQuestionBottomPlay);
		rlQuestionBottom.setVisibility(View.GONE);
		
		rlOptions01 = (TextView) mainView.findViewById(R.id.rlOptions01);
		rlOptions01.setOnClickListener(onClickListener);
		rlOptions01.setVisibility(View.GONE);
		rlOptions02 = (RelativeLayout) mainView.findViewById(R.id.rlOptions02);
		rlOptions02.setVisibility(View.GONE);
		wv = (WaveformView) mainView.findViewById(R.id.wv);
		wv.setVisibility(View.GONE);
		llRecordResult = (LinearLayout) mainView.findViewById(R.id.llRecordResult);
		llRecordResult.setVisibility(View.GONE);
		rbStar = (RatingBarView) mainView.findViewById(R.id.rbStar);
		txtRecordStar = (TextView) mainView.findViewById(R.id.txtRecordStar);
		
		rlOptions03 = (RelativeLayout) mainView.findViewById(R.id.rlOptions03);
		rlOptions03.removeAllViews();
		rlOptions03.setVisibility(View.GONE);
		rlOptions07 = (RelativeLayout) mainView.findViewById(R.id.rlOptions07);
		rlOptions07.removeAllViews();
		rlOptions07.setVisibility(View.GONE);
		rlOptions10And11 = (RelativeLayout) mainView.findViewById(R.id.rlOptions10And11);
		rlOptions10And11.removeAllViews();
		rlOptions10And11.setVisibility(View.GONE);
		
		if (!speechEnabled) {
			if (currentType.indexOf("02") != -1) {
				currentType = "01A";
			}
		}
		if ("01A".equals(currentType)) {
			txtQuestionTop.setText(currentData.getAnswer().getContent());
			imgQuestionTopPlay.setOnClickListener(this);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions01.setVisibility(View.INVISIBLE);
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
		}
		else if ("01B".equals(currentType)) {
			txtQuestionTop.setText(currentData.getAnswer().getContent());
			imgQuestionTopPlay.setVisibility(View.GONE);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions01.setVisibility(View.INVISIBLE);
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
		}
		else if ("01C".equals(currentType)) {
			txtQuestionTop.setVisibility(View.GONE);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions01.setVisibility(View.INVISIBLE);
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
		}
		else if ("01D".equals(currentType)) {
			rlOptions01.setVisibility(View.INVISIBLE);
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
		}
		else if ("02A".equals(currentType)) {
			txtQuestionTop.setText(currentData.getAnswer().getContent());
			imgQuestionTopPlay.setOnClickListener(this);
			rlQuestionTop.setVisibility(View.VISIBLE);
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.VISIBLE);
			init02(currentData, true);
		}
		else if ("02B".equals(currentType)) {
			txtQuestionTop.setText("");
			imgQuestionTopPlay.setOnClickListener(this);
			rlQuestionTop.setVisibility(View.VISIBLE);
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.VISIBLE);
			init02(currentData, true);
		}
		else if ("02C".equals(currentType)) {
			txtQuestionTop.setText("");
			rlQuestionTop.setVisibility(View.VISIBLE);
			imgQuestionTopPlay.setVisibility(View.GONE);
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.VISIBLE);
			init02(currentData, false);
		}
		else if ("03A".equals(currentType)) {
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlOptions03.setVisibility(View.VISIBLE);
			init03(currentData, true);
		}
		else if ("03B".equals(currentType)) {
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlOptions03.setVisibility(View.VISIBLE);
			init03(currentData, false);
		}
		else if ("07A".equals(currentType)) {
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlQuestionBottom.setVisibility(View.VISIBLE);
			imgQuestionBottomPlay.setOnClickListener(this);
			rlOptions07.setVisibility(View.VISIBLE);
			txtQuestionBottom.setText(currentData.getPhrases().get(0).getContent());
			init07(currentData);
		}
		else if ("10A".equals(currentType)) {
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlQuestionBottom.setVisibility(View.VISIBLE);
			imgQuestionBottomPlay.setOnClickListener(this);
			rlOptions10And11.setVisibility(View.VISIBLE);
			init10(currentData);
		}
		else if ("11A".equals(currentType)) {
			llLessonQuestionBottom.setVisibility(View.VISIBLE);
			rlQuestionBottom.setVisibility(View.VISIBLE);
			imgQuestionBottomPlay.setOnClickListener(this);
			rlOptions10And11.setVisibility(View.VISIBLE);
			init11(currentData);
		}
	}
	
	private void init02(LessonData data, boolean hasSound) {
		anwserStrs.clear();
		anwserStrs.add(data.getAnswer().getContent());
		if (hasSound) {
			anwserSoundPath = data.getAnswer().getLocalSoundPath();
		}
	}
	
	/**
	 * 03题型
	 * @param data
	 * @param hasSound
	 */
	private void init03(LessonData data, boolean hasSound) {
		anwserStrs.clear();
		anwserSoundPath = data.getAnswer().getLocalSoundPath();
		anwserStrs.add(data.getAnswer().getContent());
		int w = RelativeLayout.LayoutParams.MATCH_PARENT;
		Phrase phrase = null;
		MathUtils.ramdomList(data.getPhrases());
		if (data.getPhrases().size() > 0 && data.getPhrases().size() < 4) {
			phrase = data.getPhrases().get(0);
			rlOptions03.addView(createTxtOption(phrase, w, btnH, 0, 0, hasSound));
			if (data.getPhrases().size() > 1) {
				phrase = data.getPhrases().get(1);
				rlOptions03.addView(createTxtOption(phrase, w, btnH, 0, margin + btnH, hasSound));
				if (data.getPhrases().size() > 2) {
					phrase = data.getPhrases().get(2);
					rlOptions03.addView(createTxtOption(phrase, w, btnH, 0, margin * 2 + btnH * 2, hasSound));
				}
			}
		}
		else if (data.getPhrases().size() == 4) {
			w = (AppInfo.getScreenWidth() - margin * 3) / 2;
			phrase = data.getPhrases().get(0);
			rlOptions03.addView(createTxtOption(phrase, w, btnH, 0, 0, hasSound));
			phrase = data.getPhrases().get(1);
			rlOptions03.addView(createTxtOption(phrase, w, btnH, margin + w, 0, hasSound));
			phrase = data.getPhrases().get(2);
			rlOptions03.addView(createTxtOption(phrase, w, btnH, 0, margin + btnH, hasSound));
			phrase = data.getPhrases().get(3);
			rlOptions03.addView(createTxtOption(phrase, w, btnH, margin + w, margin + btnH, hasSound));
		}
	}
	
	/**
	 * 07题型
	 * @param data
	 */
	private void init07(LessonData data) {
		anwserStrs.clear();
		anwserSoundPath = data.getAnswer().getLocalSoundPath();
		anwserStrs.add(data.getPhrases().get(0).getBlanks().getOptions1()[0]);
		showAnwserStr = data.getAnswer().getContent();
		String[] strs = data.getPhrases().get(0).getBlanks().getOptions1();
		String[] optionStrs = MathUtils.ramdomStringArray(strs);
		if (optionStrs.length > 0 && optionStrs.length < 4) {
			int w = (AppInfo.getScreenWidth() - (margin * (optionStrs.length + 1))) / optionStrs.length;
			rlOptions07.addView(createTxtOption(optionStrs[0], w, btnH, 0, 0));
			if (optionStrs.length > 1) {
				rlOptions07.addView(createTxtOption(optionStrs[1], w, btnH, margin + w, 0));
				if (optionStrs.length > 2) {
					rlOptions07.addView(createTxtOption(optionStrs[2], w, btnH, (margin + w) * 2, 0));
				}
			}
		}
		else if (optionStrs.length == 4) {
			int w = (AppInfo.getScreenWidth() - margin * 3) / 2;
			rlOptions07.addView(createTxtOption(optionStrs[0], w, btnH, 0, 0));
			rlOptions07.addView(createTxtOption(optionStrs[1], w, btnH, margin + w, 0));
			rlOptions07.addView(createTxtOption(optionStrs[2], w, btnH, 0, margin + btnH));
			rlOptions07.addView(createTxtOption(optionStrs[3], w, btnH, margin + w, margin + btnH));
		}
	}
	
	/**
	 * 10题型
	 * @param data
	 */
	private void init10(LessonData data) {
		currentWordSet.clear();
		nextRightWords.clear();
		anwserStrs.clear();
		rlOptions10And11.removeAllViews();
		anwserSoundPath = data.getAnswer().getLocalSoundPath();
		int i = 0;
		String str = "";
		String c = ""; 
		for (String word : data.getPhrases().get(0).getWriteBlanks().getWords()) {
			for (i = 0; i < word.length(); i++) {
				c = word.charAt(i) + "";
				if (!CheckUtils.isABC(c)) {
					str += c;
					continue;
				}
				anwserStrs.add(c);
				str += "_";
			}
			if (TextUtils.isEmpty(showAnwserStr)) {
				showAnwserStr = data.getAnswer().getContent();
			}
			showAnwserStr = showAnwserStr.replaceAll(word, str);
			str = "";
		}
		String[] optionStrs = get8Words();
		optionStrs = MathUtils.ramdomStringArray(optionStrs);
		int w = (AppInfo.getScreenWidth() - margin * 5) / 4;
		for (i = 0; i < 4; i++) {
			rlOptions10And11.addView(createTxtOption(optionStrs[i], w, btnH, i * (margin + w), 0));
		}
		for (i = 0; i < 4; i++) {
			rlOptions10And11.addView(createTxtOption(optionStrs[i + 4], w, btnH, i * (margin + w), margin + btnH));
		}
		txtQuestionBottom.setText(showAnwserStr);
		
		for (String string : nextRightWords) {
			str += " " + string;
		}
		android.util.Log.e("test", "next words: " + str);
		
	}
	
	/**
	 * 11题型
	 * @param data
	 */
	private void init11(LessonData data) {
		anwserStrs.clear();
		currentWordSet.clear();
		nextRightWords.clear();
		rlOptions10And11.removeAllViews();
		anwserSoundPath = data.getAnswer().getLocalSoundPath();
		int i = 0;
		showAnwserStr = "";
		String word = data.getAnswer().getContent();
		String str = "";
		String c = "";
		for (i = 0; i < word.length(); i++) {
			c = word.charAt(i) + "";
			if (CheckUtils.isABC(c)) {
				anwserStrs.add(c);
				str += "_";
				continue;
			}
			str += c;
		}
		showAnwserStr = str;
		String[] optionStrs = get8Words();
		optionStrs = MathUtils.ramdomStringArray(optionStrs);
		int w = (AppInfo.getScreenWidth() - margin * 5) / 4;
		for (i = 0; i < 4; i++) {
			rlOptions10And11.addView(createTxtOption(optionStrs[i], w, btnH, i * (margin + w), 0));
		}
		for (i = 0; i < 4; i++) {
			rlOptions10And11.addView(createTxtOption(optionStrs[i + 4], w, btnH, i * (margin + w), margin + btnH));
		}
		txtQuestionBottom.setText(showAnwserStr);
	}
	
	public static String[] ABC = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	
	private String[] get8Words() {
		currentWordSet.clear();
		nextRightWords.clear();
		for (String word : anwserStrs) {
			word = word.toLowerCase(Locale.getDefault());
			if (!currentWordSet.contains(word) && currentWordSet.size() < 8) {
				currentWordSet.add(word);
				continue;
			}
			nextRightWords.add(word);
		}
		while (currentWordSet.size() < 8) {
			addAWord(true);
		}
		String[] strs = new String[8];
		return currentWordSet.toArray(strs);
	}
	
	private String addAWord(boolean newWord) {
//		String word = anwserStrs.get(answerIndex);
		String word = "";
		if (nextRightWords.size() > 0 && !newWord) {
			word = nextRightWords.get(0);
			if (!currentWordSet.contains(word)) {
				nextRightWords.remove(0);
				currentWordSet.add(word);
//				android.util.Log.e("test", "new word right: " + word);
				return word;
			}
		}
		while (true) {
			word = ABC[new Random().nextInt(26)];
			if (currentWordSet.contains(word)) {
				continue;
			}
			if (nextRightWords.contains(word)) {
				continue;
			}
//			android.util.Log.e("test", "new word random: " + word);
			currentWordSet.add(word);
			return word;
		}
	}
	
	private OptionTxt createTxtOption(String content, int w, int h, int l, int t, String soundPath) {
		OptionTxt option = new OptionTxt(getActivity(), content, w, h, l, t, soundPath, onCompletionListener);
		option.setOnClickListener(onClickListener);
		option.setTag(content);
		option.setVisibility(View.INVISIBLE);
		options.add(option);
		return option;
	}
	
	private OptionTxt createTxtOption(String content, int w, int h, int l, int t) {
		return createTxtOption(content, w, h, l, t, null);
	}
	
	private OptionTxt createTxtOption(Phrase phrase, int w, int h, int l, int t, boolean hasSound) {
		String soundPath = null;
		if (hasSound)
			soundPath = phrase.getLocalSoundPath();
		return createTxtOption(phrase.getContent(), w, h, l, t, soundPath);
	}
	
	private void showOptions() {
		Animation animation = null;
		if (null != options && options.size() > 0) {
			for (OptionTxt option : options) {
				option.setVisibility(View.VISIBLE);
				animation = new AlphaAnimation(0, 1);
				animation.setDuration(500);
				animation.setFillAfter(true);
				option.clearAnimation();
				option.startAnimation(animation);
			}
		}
		if (currentType.indexOf("01") != -1) {
			rlOptions01.setVisibility(View.VISIBLE);
			animation = new AlphaAnimation(0, 1);
			animation.setDuration(500);
			animation.setFillAfter(true);
			rlOptions01.clearAnimation();
			rlOptions01.startAnimation(animation);
		}
	}
	
	private void hideOptions() {
		if (null != options && options.size() > 0) {
			for (OptionTxt option : options) {
				option.clearAnimation();
				option.setVisibility(View.INVISIBLE);
			}
		}
		rlOptions01.clearAnimation();
		if (rlOptions01.getVisibility() == View.VISIBLE) {
			rlOptions01.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initImgs(int w, int h) {
		w = (w - (data.getDatas().size() + 1) * margin) / data.getDatas().size();
		if (w > h) {
			w = h;
		}
		Bitmap bitmap = null;
		OptionImg img = null;
		String path = null;
		for (int i = 0; i < data.getDatas().size(); ++i) {
			path = data.getDatas().get(i).getAnswer().getLocalImgPath();
			bitmap = GlobalResTypes.getInstance().getLessonBitmap(path);
			img = new OptionImg(getActivity(), R.drawable.bg_head, bitmap, w, w);
			if (i != 0) {
				ViewUtils.setLinearMargins(img, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, margin, 0, 0, 0);
			}
			imgs.add(img);
			if (null == bitmap) {
				GlobalResTypes.getInstance().displayLessonBitmapNoCache(path, img);
			}
			llImgsContainer.addView(img);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgQuestionTopPlay: case R.id.imgQuestionBottomPlay:
			stop02();
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

	//实现BaselessonType 的 onCurrentStepPrevStart  课程片段的预备处理
	@Override
	protected void onCurrentStepPrevStart(LessonData currentData) {
		initCurrentView(currentData);
	}
	
	public static Set<String> NO_PLAYING_SOUND_TYPE_SET = new HashSet<String>();
	public static Set<String> WAITING_TYPE_SET = new HashSet<String>();
	public static Set<String> NO_TOP_TYPE_SET = new HashSet<String>();
	
	static {
		NO_PLAYING_SOUND_TYPE_SET.add("03A");
		NO_PLAYING_SOUND_TYPE_SET.add("03B");
		NO_PLAYING_SOUND_TYPE_SET.add("05B");
		NO_PLAYING_SOUND_TYPE_SET.add("07A");
		NO_PLAYING_SOUND_TYPE_SET.add("13C");
		NO_PLAYING_SOUND_TYPE_SET.add("14C");
		
		WAITING_TYPE_SET.add("01B");
		WAITING_TYPE_SET.add("01D");
		
		NO_TOP_TYPE_SET.add("01A");
		NO_TOP_TYPE_SET.add("01B");
		NO_TOP_TYPE_SET.add("01C");
		NO_TOP_TYPE_SET.add("02A");
		NO_TOP_TYPE_SET.add("02B");
		NO_TOP_TYPE_SET.add("02C");
	}
	
	//实现BaseLessonType 的 onCurrentStepStart 在 nextStep 时被调用
	@Override
	protected void onCurrentStepStart(LessonData currentData) {
		wv.setVisibility(View.GONE);
		llRecordResult.setVisibility(View.GONE);
		pass = false;
		hideOptions();
		showOptions();
		if (!speechEnabled) {
			if (currentType.indexOf("02") != -1) {
				currentType = "01A";
			}
		}
		//不需要播音频的题型
		if ("02C".equals(currentType)) {
			waitToStart02(1000);
			return;
		}
		//等待2秒过的题型
		if (WAITING_TYPE_SET.contains(currentType)) {
			wait2Seconds();
			return;
		}
		if (NO_PLAYING_SOUND_TYPE_SET.contains(currentType)) {
			return;
		}
		MediaPlayerMgr.getInstance().playMp(getCurrentData().getAnswer().getLocalSoundPath(), onCompletionListener);
	}
	
	@Override
	public void stop() {
		super.stop();
		clearWait();
		hideOptions();
		stop02();
		MediaPlayerMgr.getInstance().stopMp();
	}
	
	//根据题目类型重置选项 并设置选项点击事件
	public void reset() {
		if (!isRunning()) {
			return;
		}
		answerIndex = 0;
		pass = false;
		//02题型
		type02Count = 0;
		stop02();
		clearWait();
		if ("10A".equals(currentType)) {
			init10(getCurrentData());
			return;
		}
		if ("11A".equals(currentType)) {
			init11(getCurrentData());
			return;
		}
		if ("02A".equals(currentType)) {
			if (rlQuestionTop.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(getCurrentData().getAnswer().getContent())) {
				txtQuestionTop.setText(getCurrentData().getAnswer().getContent());
			}
			return;
		}
		if ("02B".equals(currentType) || "02C".equals(currentType)) {
			txtQuestionTop.setText("");
			return;
		}
		if ("07A".equals(currentType)) {
			txtQuestionBottom.setText(getCurrentData().getPhrases().get(0).getContent());
		}
		if (null != options) {
			for (OptionTxt option : options) {
				option.reset();
				option.setOnClickListener(onClickListener);
			}
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  Thread wait 1 second, 2 seconds
	/**
	/***************************************************************************************************************************************/
	
	private Handler handler = new Handler();
	
	private void clearWait() {
		handler.removeCallbacks(rNext);
		handler.removeCallbacks(r02C);
		handler.removeCallbacks(r);
		if (null != wait1SecondThread)
			handler.removeCallbacks(wait1SecondThread);
	}
	
	private Runnable r = new Runnable() {
		
		@Override
		public void run() {
			if (!isRunning()) {
				return;
			}
			nextStep();
		}
		
	};
	
	
	//页面加载动画
	private Runnable r02C = new Runnable() {
		
		@Override
		public void run() {
			if (!isRunning()) {
				return;
			}
			try {
				SoundPoolMgr.getInstance().playDingSnd();
				if (null != wv) {
					Animation animation = new AlphaAnimation(0, 0.8f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					wv.startAnimation(animation);
					wv.setVisibility(View.VISIBLE);
				}
				Thread.sleep(200);
				start02();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	};
	
	public void waitToNext(int time) {
		handler.removeCallbacks(rNext);
		handler.postDelayed(rNext, time);
	}
	
	private Runnable rNext = new Runnable() {
		
		@Override
		public void run() {
			if (!isRunning()) {
				return;
			}
			nextStep();
		}
		
	};
	
	public void wait2Seconds() {
		handler.removeCallbacks(r);
		handler.postDelayed(r, 2000);
	}
	
	public void waitToStart02() {
		waitToStart02(0);
	}
	
	//执行题目view加载动画
	public void waitToStart02(int time) {
		llRecordResult.setVisibility(View.GONE);
		handler.removeCallbacks(r02C);
		handler.postDelayed(r02C, time);
	}
	
	public void waitToFinish(Wait1SecondThread r) {
		if (null != wait1SecondThread)
			handler.removeCallbacks(wait1SecondThread);
		wait1SecondThread = r;
		handler.postDelayed(wait1SecondThread, 1000);
	}
	
	private Wait1SecondThread wait1SecondThread;
	
	//执行选项正确处理
    private class Wait1SecondThread extends Thread {
    	
    	private boolean pass = false;
    	
    	public Wait1SecondThread(boolean pass) {
    		this.pass = pass;
    	}
    	
    	public void run() {
    		if (!isRunning() || ScreenObserver.isScreenLocked(getActivity())) {
    			return;
    		}
    		try {
    			//02C 题型特殊处理
    			if ("02C".equals(currentType)) {
    				if (!pass)
    					waitToStart02();
    				else
    					nextStep();
					return;
				}
    			LessonType01_02_03_07_10_11.this.pass = pass;
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
			// 01题 直接过
			if (currentType.indexOf("01") != -1) {
				return;
//				waitToNext(1200);
			}
			if (pass || (!speechEnabled && currentType.indexOf("02") != -1)) {
				nextStep();
				return;
			}
			// 02题型 直接播音频
			if (currentType.indexOf("02") != -1) {
				waitToStart02();
				return;
			}
		}
		
	};
	
	/***************************************************************************************************************************************/
	/**
	/**  IOralEvalSDKMgr 语音识别 02题型
	/**
	/***************************************************************************************************************************************/
	
	private int type02Count = 0;
	private boolean isType02Finished = false;
	
	private void start02() {
		isType02Finished = false;
		String rightAnwser = anwserStrs.get(answerIndex);
		IOralEvalSDKMgr.getInstance().start(rightAnwser, getActivity(), new CallbackIOralEvalSDK() {
			
			@Override
			public void onResult(boolean pass, int finalScore, String htmlStr) {
				if (isType02Finished || !isRunning()) {
					return;
				}
				stop02();
				showAnwserStr = htmlStr;
				if (!TextUtils.isEmpty(showAnwserStr)) {
					txtQuestionTop.setText(Html.fromHtml(showAnwserStr));
				}
				if (pass) {
					rightHandler();
					SoundPoolMgr.getInstance().playRightSnd();
					show02Result(pass, finalScore);
				}
				else {
					wrongHandler();
					SoundPoolMgr.getInstance().playWrongSnd();
					show02Result(pass, finalScore);
				}
				//错3次 直接过
				if (++type02Count >= 3 && !pass) {
					pass = true;
				}
				waitToFinish(new Wait1SecondThread(pass));
			}
			
			@Override
			public void onError(Error error, OfflineSDKPreparationError ofError) {
				if (!isRunning()) {
					return;
				}
				stop02();
				if (null != getActivity()) {
					AtyLesson atyLesson = (AtyLesson) getActivity();
					if (error.toString().equals("Network")) {
						
						showSettingNetwork(getString(R.string.speechTip1), " ");
//						Toast.makeText(getActivity(), R.string.speechTip1, Toast.LENGTH_SHORT).show();
					}
					else if (error.toString().equals("AudioDevice")) {
						showSetting(getString(R.string.speechTip2), " ");
//						Toast.makeText(getActivity(), R.string.speechTip2, Toast.LENGTH_SHORT).show();
					}
				}
//				speechEnabled = false;
//				reset();
//				onCurrentStepStart(getCurrentData());
			}

			@Override
			public void onVoice(int num) {
				if (null != wv) {
					wv.updateAmplitude(num);
				}
			}
			
		});
	}
	
	public void stop02() {
		isType02Finished = true;
		IOralEvalSDKMgr.getInstance().stop();
		if (null != wv) {
			wv.clearAnimation();
			wv.setVisibility(View.GONE);
		}
	}
	
	private void show02Result(boolean pass, int score) {
		wv.setVisibility(View.GONE);
		llRecordResult.clearAnimation();
		Animation animation = new AlphaAnimation(0, 0.8f);
		animation.setDuration(300);
		animation.setFillAfter(false);
		llRecordResult.startAnimation(animation);
		llRecordResult.setVisibility(View.VISIBLE);
		if (!pass) {
			txtRecordStar.setText("Try Again!");
			rbStar.setStar(1);
		}
		else if (score < 80) {
			txtRecordStar.setText("Great!");
			rbStar.setStar(2);
		}
		else {
			txtRecordStar.setText("Perfect!");
			rbStar.setStar(3);
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  Grade onClickListener
	/**
	/***************************************************************************************************************************************/
	
	//10 11题型  的单词 获取器
	private Set<String> currentWordSet;
	private List<String> nextRightWords;
	//目前step的正确答案列表
	private List<String> anwserStrs;
	//当前正确答案的索引
	private int answerIndex = 0;
	//当前显示内容
	private String showAnwserStr;
	//回答正确后音频
	private String anwserSoundPath;
	//判断是否正确的标识
	private boolean pass = false;

	private OnClickListener onClickListener = new OnClickListener() {
		
		//页面中题目的点击事件 同理其他的lessonType 不同题型的判断方式不同
		@Override
		public void onClick(View v) {
			if (null == getActivity() || ((AtyLesson) getActivity()).isMoving()) {
				return;
			}
			//点击next
			if (v.getId() == R.id.rlOptions01) {
				nextStep();
				return;
			}
			
			String str = (String) v.getTag();
			// right
			String rightAnwser = anwserStrs.get(answerIndex);
			if (rightAnwser.toUpperCase(Locale.getDefault()).equals(str.toUpperCase(Locale.getDefault()))) {
				//全部正确
				if (answerIndex + 1 >= anwserStrs.size()) {
					if ("10A".equals(currentType) || "11A".equals(currentType)) {
						showAnwserStr = showAnwserStr.replaceFirst("_", rightAnwser);
						for (OptionTxt option : options) {
							option.setOnClickListener(null);
						}
					}
					else {
						for (OptionTxt option : options) {
							option.setOnClickListener(null);
							if (option.equals(v)) {
								option.setRight();
								continue;
							}
							option.setWrong();
						}
					}
					if (rlQuestionBottom.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(showAnwserStr)) {
						txtQuestionBottom.setText(showAnwserStr);
					}
					rightHandler();
					return;
				}
				++answerIndex;
				if ("07A".equals(currentType)) {
					txtQuestionBottom.setText(showAnwserStr);
				}
				else if ("10A".equals(currentType) || "11A".equals(currentType)) {
					showAnwserStr = showAnwserStr.replaceFirst("_", rightAnwser);
					txtQuestionBottom.setText(showAnwserStr);
					currentWordSet.remove(str);
					String word = addAWord(false);
					OptionTxt option = ((OptionTxt) v);
					option.setContent(word);
					option.setTag(word);
					SoundPoolMgr.getInstance().playTapRightSnd();
				}
			}
			// wrong
			else {
//				if (answerIndex + 1 >= anwserStrs.size()) {
				if ("10A".equals(currentType) || "11A".equals(currentType)) {
					setResult(LessonData.RESULT_WRONG);
					SoundPoolMgr.getInstance().playTapWrongSnd();
					return;
				}
				if (anwserStrs.size() == 1) {
					pass = false;
					wrongHandler();
					return;
				}
			}
		}
		
	};
	//选项点击正确 背景和声音动作
	private void rightHandler() {
		MediaPlayerMgr.getInstance().stopMp();
		setResult(LessonData.RESULT_RIGHT);
		SoundPoolMgr.getInstance().playRightSnd();
		((AtyLesson) getActivity()).rightHandler();
		pass = true;
		waitToFinish(new Wait1SecondThread(true));
	}
	
	//选项点击错误 背景和声音动作
	private void wrongHandler() {
		MediaPlayerMgr.getInstance().stopMp();
		setResult(LessonData.RESULT_WRONG);
		((AtyLesson) getActivity()).wrongHandler();
		SoundPoolMgr.getInstance().playWrongSnd();
	}
	
	/***************************************************************************************************************************************/
	private void hideSettingDialog() {
		if (null == settingDialog) {
			return;
		}
		settingDialog.dismiss();
		settingDialog = null;
	}
//	private DialogAlertFragment finishDialog;
//	private void finishSettingDialog() {
//		 finishDialog = new DialogAlertFragment(new DialogAlertFragment.CallBackDialogConfirm() {
//				@Override
//				public void onSubmit(int position) {
//					if (null == finishDialog) {
//						return;
//					}
//					finishDialog.dismiss();
//					finishDialog = null;
//					reset();
//					onCurrentStepStart(getCurrentData());
//				}
//				
//				@Override
//				public void onKeyBack() {
//
//				}
//			},getString(R.string.settingTip),  " ", 0);
//		 if (finishDialog.isVisible()) {
//				return;
//			}
//		 finishDialog.showDialog(getActivity().getSupportFragmentManager());
//	}
	
	private DialogConfirmFragment settingDialog;
	
	public void showSettingNetwork(String str,String strEn) {
		if (null == settingDialog) {
			settingDialog = new DialogConfirmFragment(new CallBackDialogConfirm() {
				
				@Override
				public void onSubmit(int position) {
					Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
//					intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
					startActivity(intent);
					hideSettingDialog();
//					finishSettingDialog();
//					toFinish();
					
				}
				
				@Override
				public void onCancel() {
					hideSettingDialog();
					((AtyLesson) getActivity()).toFinish();
				}

				@Override
				public void onKeyBack() {
					onCancel();
				}
				
			}, strEn,  str, 0,getString(R.string.btnStrSubmit),getString(R.string.btnStrExit));
		}
		if (settingDialog.isVisible()) {
			return;
		}
		settingDialog.showDialog(getActivity().getSupportFragmentManager());
		settingDialog.setCancelable(false);
	}
	
	public void showSetting(String str,String strEn) {
		if (null == settingDialog) {
			settingDialog = new DialogConfirmFragment(new CallBackDialogConfirm() {
				
				@Override
				public void onSubmit(int position) {
					Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
//					intent.setClassName("com.android.settings", "com.android.settings.Settings");
					startActivity(intent);
					hideSettingDialog();
//					toFinish();
				}
				
				@Override
				public void onCancel() {
					hideSettingDialog();
					((AtyLesson) getActivity()).toFinish();
				}

				@Override
				public void onKeyBack() {
					onCancel();
				}
				
			}, strEn,  str, 0,getString(R.string.btnStrSubmit),getString(R.string.btnStrExit));
		}
		if (settingDialog.isVisible()) {
			return;
		}
		settingDialog.showDialog(getActivity().getSupportFragmentManager());
		settingDialog.setCancelable(false);
	}
	
	/***************************************************************************************************************************************/
	@Override
	public void onDestroy() {
		super.onDestroy();
		clearDatas();
	}
	
}
