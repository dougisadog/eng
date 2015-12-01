package com.shuangge.english.view.read.fragment;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.view.lesson.component.OptionImg;
import com.shuangge.english.view.read.AtyReadLesson;
import com.shuangge.english.view.read.AtyReadLesson.ReadLessonWord;


public class ReadLessonType01 extends BaseLessonType {
	
	public ReadLessonType01() {
		super();
	}
	
	public ReadLessonType01(IWord wordData, CallbackLessonType callback) {
		super(wordData, callback);
	}
	
	private RelativeLayout rlQuestionTop;
	private LinearLayout llImgsContainer, llLessonQuestionBottom;
	private TextView txtQuestionTop, rlOptions01;
	private ImageView imgQuestionTopPlay;
	private int margin = 10;
	private List<OptionImg> imgs;
	
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initialized = false;
		imgs = new ArrayList<OptionImg>();
		
		mainView = inflater.inflate(R.layout.read_lesson_01, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		llImgsContainer = (LinearLayout) mainView.findViewById(R.id.llImgsContainer);
		llLessonQuestionBottom = (LinearLayout) mainView.findViewById(R.id.llLessonQuestionBottom);
		
		rlQuestionTop = (RelativeLayout) mainView.findViewById(R.id.rlQuestionTop);
		txtQuestionTop = (TextView) mainView.findViewById(R.id.txtQuestionTop);
		imgQuestionTopPlay = (ImageView) mainView.findViewById(R.id.imgQuestionTopPlay);
		
		margin = DensityUtils.dip2px(getActivity(), 10);
		
		llImgsContainer.setGravity(Gravity.CENTER);
		llImgsContainer.setPadding(0, 0, 0, 0);
		
		initCurrentView();
		
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
					if (target == DEFAULT_TARGET_STEP) {
						nextStep();
					}
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
		if (null != imgs) {
			for (OptionImg img : imgs) {
				img.recycle();
			}
			imgs.clear();
			imgs = null;
		}
	}
	
	private void initCurrentView() {
		rlOptions01 = (TextView) mainView.findViewById(R.id.rlOptions01);
		rlOptions01.setOnClickListener(onClickListener);
		
		String mnemonics = "";
		if (!TextUtils.isEmpty(wordData.getMnemonics())) {
			mnemonics = wordData.getMnemonics();
		}
		
		txtQuestionTop.setText(wordData.getWord() + "\n" + mnemonics);
		imgQuestionTopPlay.setOnClickListener(this);
		rlQuestionTop.setVisibility(View.VISIBLE);
		rlOptions01.setVisibility(View.INVISIBLE);
		llLessonQuestionBottom.setVisibility(View.VISIBLE);
	}
	
	private void showOptions() {
		Animation animation = null;
		rlOptions01.setVisibility(View.VISIBLE);
		animation = new AlphaAnimation(0, 1);
		animation.setDuration(500);
		animation.setFillAfter(true);
		rlOptions01.clearAnimation();
		rlOptions01.startAnimation(animation);
	}
	
	private void hideOptions() {
		rlOptions01.clearAnimation();
		if (rlOptions01.getVisibility() == View.VISIBLE) {
			rlOptions01.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initImgs(int w, int h) {
		w = (w - 2 * margin);
		if (w > h) {
			w = h;
		}
		String path = getImgLocalPath(wordData.getId());
		
		//TODO:图片地址
		Bitmap bitmap = GlobalResTypes.getInstance().getLessonBitmap(path);
		OptionImg img = new OptionImg(getActivity(), R.drawable.bg_head, bitmap, w, w);
		imgs.add(img);
		if (null == bitmap) {
			GlobalResTypes.getInstance().displayLessonBitmapNoCache(path, img);
		}
		llImgsContainer.addView(img);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgQuestionTopPlay: case R.id.imgQuestionBottomPlay:
			//TODO:音频地址
			MediaPlayerMgr.getInstance().playMp(getSoundLocalPath(wordData.getId()), onCompletionListener);
			break;
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  start stop reset nextCurrentTypeStep
	/**
	/***************************************************************************************************************************************/

	public static Set<String> NO_TOP_TYPE_SET = new HashSet<String>();
	
	static {
		NO_TOP_TYPE_SET.add("01A");
	}
	
	@Override
	protected void onCurrentStepPrevStart() {
		// TODO Auto-generated method stub
		initCurrentView();
//		initImgs(llImgsContainer.getMeasuredWidth(), mainView.getMeasuredHeight() - 
//				rlQuestionTop.getMeasuredHeight() - llLessonQuestionBottom.getMeasuredHeight());
	}
	
	@Override
	protected void onCurrentStepStart() {
		hideOptions();
		showOptions();
		//TODO:音频地址
		MediaPlayerMgr.getInstance().playMp(getSoundLocalPath(wordData.getId()), onCompletionListener);
	}
	
	@Override
	public void stop() {
		super.stop();
		hideOptions();
		MediaPlayerMgr.getInstance().stopMp();
	}
	
	public void reset() {
		if (!isRunning()) {
			return;
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  MediaPlayer onCompletionListener
	/**
	/***************************************************************************************************************************************/
	
	private OnCompletionListener onCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion() {
		}
		
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null == getActivity() || ((AtyReadLesson) getActivity()).isMoving()) {
				return;
			}
			if (v.getId() == R.id.rlOptions01) {
				//判断是否存在指向性跳转
				if (target == DEFAULT_TARGET_STEP) {
					nextStep();
				}
				else if (target == TARGET_STEP) {
					targetStep(2);
				}
				
				return;
			}
		}
		
	};

	/***************************************************************************************************************************************/
	
}
