package com.shuangge.english.view.read.fragment;


import java.io.File;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.lesson.component.OptionImg;
import com.shuangge.english.view.read.AtyReadLesson;
import com.shuangge.english.view.read.AtyReadLesson.ReadLessonWord;


public class ReadLessonType02 extends BaseLessonType {
	
	public ReadLessonType02() {
		super();
	}
	
	public ReadLessonType02(ReadLessonWord wordData, CallbackLessonType callback) {
		super(wordData, callback);
		notPassWordsForLesson = GlobalRes.getInstance().getBeans().getNotPassWordsForLesson();
	}
	
	public ReadLessonType02(ReadLessonWord wordData, CallbackLessonType callback, Set<IWord> groupWords) {
		super(wordData, callback);
		notPassWordsForLesson = GlobalRes.getInstance().getBeans().getNotPassWordsForLesson();
		this.groupWords = groupWords;
	}
	
	private LinearLayout llImgsContainer, llImgsTopContainer, llImgsBottomContainer;
	private TextView txtQuestionTop;
	private ImageView imgQuestionTopPlay;
	private int margin = 10;
	private List<OptionImg> options;
	
	private Set<IWord> notPassWordsForLesson;
	private Set<IWord> groupWords;  //错题本进入 单词所属组（现默认4个 ）
	
	private final long maxWaitTime = 1000; //循环时间消耗时限
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
		
		mainView = inflater.inflate(R.layout.read_lesson_02, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		llImgsContainer = (LinearLayout) mainView.findViewById(R.id.llImgsContainer);
		llImgsTopContainer = (LinearLayout) mainView.findViewById(R.id.llImgsTopContainer);
		llImgsBottomContainer = (LinearLayout) mainView.findViewById(R.id.llImgsBottomContainer);
		
		margin = DensityUtils.dip2px(getActivity(), 10);
		
		initCurrentView();
		
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
	
	private void initCurrentView() {
		//top
		txtQuestionTop = (TextView) mainView.findViewById(R.id.txtQuestionTop);
		imgQuestionTopPlay = (ImageView) mainView.findViewById(R.id.imgQuestionTopPlay);
		
		//bottom
//		llImgsBottomContainer.setVisibility(View.GONE);
		txtQuestionTop.setText(wordData.getWord());
		txtQuestionTop.setVisibility(View.VISIBLE);
		imgQuestionTopPlay.setOnClickListener(this);
		imgQuestionTopPlay.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 05 08 13题型
	 * @param data
	 */
	private void init05And08And13(int w, int h) {
		anwserStrs.clear();
		anwserStrs.add(wordData.getWord());
	
		if (w > h) {
			w = h;
		}
		w = w / 2 - 10 * 3;
		h = w;
		IWord[] wordDatas;
		Set<IWord> wordSet = new HashSet<IWord>();
		
		//若拥有完整4个单词一组的单词 直接从 该组中获取选项内容
//		if (groupWords != null && groupWords.size() == 4) {
//			wordDatas = new IWord[4];
//			wordDatas = groupWords.toArray(wordDatas);
//		}
		if (groupWords != null) {
			wordDatas = new IWord[groupWords.size()];
			wordDatas = groupWords.toArray(wordDatas);
		}
		else {
			wordDatas = new IWord[GlobalRes.getInstance().getBeans().getNotPassWordsForRead().size()];
			wordDatas = GlobalRes.getInstance().getBeans().getNotPassWordsForRead().toArray(wordDatas);
		}
		if (wordDatas.length < 4 && null != GlobalRes.getInstance().getBeans().getReadData()) {
			wordDatas = new IWord[GlobalRes.getInstance().getBeans().getReadData().getCoreWords().size()];
			wordDatas = GlobalRes.getInstance().getBeans().getReadData().getCoreWords().toArray(wordDatas);
		}
		wordSet.add(wordData);
		long time =  System.currentTimeMillis();
		flag : while (true) {
			if (wordSet.size() == 4 || wordSet.size() >= wordDatas.length)
				break;
			while (true) {
				//运行超过1s 强行退出循环
				if (System.currentTimeMillis() - time > maxWaitTime) {
					break flag;
				}
				int i = (int) (Math.random() * wordDatas.length);
				IWord word = wordDatas[i];
				//选单词时判断其已经下载好了图片
				if (!wordData.getId().equals(word.getId()) && new File(getImgLocalPath(word.getId())).exists()) {
					wordSet.add(word);
					break;
				}
			}
			
		}
		wordDatas = new IWord[wordSet.size()];
		wordDatas = wordSet.toArray(wordDatas);
		for (int i = 0; i < wordDatas.length && i < 2; i++) {
			llImgsTopContainer.addView(createImgOption(wordDatas[i].getWord(), getImgLocalPath(wordDatas[i].getId()), w, h, i * margin));
		}
		if (wordDatas.length > 2) {
			for (int i = 2; i < wordDatas.length && i < 4; i++) {
				llImgsBottomContainer.addView(createImgOption(wordDatas[i].getWord(), getImgLocalPath(wordDatas[i].getId()), w, h, (i - 2) * margin));
			}
		}
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
	}
	
	private void hideOptions() {
	}
	
	private void initImgs(int w, int h) {
		init05And08And13(w, h);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgQuestionTopPlay:
			MediaPlayerMgr.getInstance().playMp(getSoundLocalPath(wordData.getId()), null);
			break;
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  start stop reset nextCurrentTypeStep
	/**
	/***************************************************************************************************************************************/
	
	@Override
	protected void onCurrentStepPrevStart() {
		// TODO Auto-generated method stub
		initCurrentView();
//		initImgs(llImgsContainer.getMeasuredWidth(), llImgsContainer.getMeasuredHeight());
	}
	
	
	@Override
	protected void onCurrentStepStart() {
		pass = false;
		showOptions();
		MediaPlayerMgr.getInstance().playMp(getSoundLocalPath(wordData.getId()), null);
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
    			ReadLessonType02.this.pass = pass;
    			MediaPlayerMgr.getInstance().playMp(getSoundLocalPath(wordData.getId()), onCompletionListener);
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
			else {
				targetStep(LessonData.RESULT_WRONG);
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
	//判断是否正确的标识
	private boolean pass = false;

	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null == getActivity() || ((AtyReadLesson) getActivity()).isMoving()) {
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
				for (OptionImg option : options) {
					option.setOnClickListener(null);
				}
//				if (answerIndex + 1 >= anwserStrs.size()) {
					notPassWordsForLesson.add(wordData);
					
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
		((AtyReadLesson) getActivity()).rightHandler();
		pass = true;
		waitToFinish(new Wait1SecondThread(true));
	}
	
	private void wrongHandler() {
		GlobalRes.getInstance().getBeans().getNotPassWordsForLesson().add(wordData);
		MediaPlayerMgr.getInstance().stopMp();
		setResult(LessonData.RESULT_WRONG);
		((AtyReadLesson) getActivity()).wrongHandler();
		SoundPoolMgr.getInstance().playWrongSnd();
		waitToFinish(new Wait1SecondThread(false));
	}
	
	/***************************************************************************************************************************************/
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		clearDatas();
	}

}
