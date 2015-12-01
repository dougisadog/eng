package com.shuangge.english.view.lesson.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.lesson.LessonFragment;
import com.shuangge.english.support.utils.IOralEvalSDKMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundPoolMgr;

public abstract class BaseLessonType extends Fragment implements OnClickListener {

	protected LessonFragment data;
	protected CallbackLessonType callback;
	protected View mainView;
	protected int step = 0;
	protected String currentType;
	protected LessonData currentData;
	
	protected static final int STATE_START = 1;
	protected static final int STATE_PAUSE = 2;
	protected static final int STATE_STOP = 3;
	protected static final int STATE_FINISHED = 10;
	
	protected int state = 0;
	
	protected boolean initialized = false;
	
	protected boolean speechEnabled = false;
	
	public static interface CallbackLessonType {
		
		public void onFramgentFinished(String types, int rightNum, int wrongNum);
		
		public void onFirstWrong();
	}
	
	public BaseLessonType() {
		super();
	}
	
	public BaseLessonType(LessonFragment data, CallbackLessonType callback) {
		super();
		this.data = data;
		this.callback = callback;
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		speechEnabled = beans.isSpeechEnabled();
		if (data.getDatas().size() > 0) {
			currentData = data.getDatas().get(0);
			currentType = currentData.getType().toUpperCase(Locale.getDefault());
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) mainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        }
		return mainView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		state = STATE_FINISHED;
	} 
	
	public void start() {
		state = STATE_START;
		nextStep();
	}
	
	public abstract void reset();
	
	public void stop() {
		state = STATE_STOP;
		step = 0;
		wrongNum = 0;
		MediaPlayerMgr.getInstance().stopMp();
	}
	
	//执行下一步
	protected void nextStep() {
		if (state != STATE_START || !initialized) {
			return;
		}
		if (step > 0) {
			String type = currentData.getType().toUpperCase(Locale.getDefault());
			if (!speechEnabled && type.indexOf("02") != -1) {
				currentData.setResult(LessonData.RESULT_WRONG);
			}
			else {
				setResult(LessonData.RESULT_RIGHT);
			}
		}
		//若无 课程片段 判断当前题目完成
		if (step >= data.getDatas().size()) {
			onFramgentFinished();
			return;
		}
		currentData = data.getDatas().get(step++);
		if (step > 1) {
			onCurrentStepPrevStart(currentData);
		}
		onCurrentStepStart(currentData);
	}
	
	protected abstract void onCurrentStepPrevStart(LessonData currentData);
	
	protected abstract void onCurrentStepStart(LessonData currentData);
	
	private void onFramgentFinished() {
		if (null != callback) {
			Object[] objs = getResult();
			callback.onFramgentFinished((String) objs[0], (Integer) objs[1], (Integer) objs[2]);
		}
	}
	
	protected LessonData getCurrentData() {
		return currentData;
	}
	
	/***************************************************************************************************************************************/
	
	public void pause() {
		state = STATE_PAUSE;
		IOralEvalSDKMgr.getInstance().stop();
		MediaPlayerMgr.getInstance().stopMp();
		SoundPoolMgr.getInstance().stopSnd();
	}
	
	public void resume() {
		if (state == STATE_START || !initialized || state == STATE_STOP) {
			return;
		}
		state = STATE_START;
		if (null != currentData)
			onCurrentStepStart(currentData);
	}
	
	/***************************************************************************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
	}

	/***************************************************************************************************************************************/
	
	public boolean isRunning() {
		return state < STATE_FINISHED;
	}
	
	/***************************************************************************************************************************************/

	private int wrongNum = 0;
	
	protected void setResult(int result) {
		if (result == LessonData.RESULT_WRONG) {
			if (++wrongNum < 3) {
				String type = currentData.getType().toUpperCase(Locale.getDefault());
				if (type.indexOf("02") != -1 || type.indexOf("10") != -1 || type.indexOf("11") != -1) {
					return;
				}
			}
		}
		if (null != currentData.getResult()) {
			return;
		}
		currentData.setResult(result);
		if (null != callback && result == LessonData.RESULT_WRONG)
			callback.onFirstWrong();
	}
	
	protected void setResult2(int result) {
		if (null != currentData.getResult()) {
			return;
		}
		currentData.setResult(result);
		if (null != callback && result == LessonData.RESULT_WRONG)
			callback.onFirstWrong();
	}
	
	private Object[] getResult() {
		String type = "";
		String rightTypes = "";
		int rightNum = 0;
		int wrongNum = 0;
		for (LessonData lessonData : data.getDatas()) {
			switch(lessonData.getResult().intValue()) {
			case LessonData.RESULT_RIGHT:
				type = lessonData.getType().toUpperCase(Locale.getDefault());
				if (!TextUtils.isEmpty(rightTypes)) {
					rightTypes = "_";
				}
				rightTypes += type;
				++rightNum;
				break;
			case LessonData.RESULT_WRONG:
				++wrongNum;
				break;
			}
		}
		return new Object[] {rightTypes, rightNum, wrongNum};
	}

	/************************************************************************************************************************************/
	
	public String getCurrentType() {
		return currentType;
	}
	
}
