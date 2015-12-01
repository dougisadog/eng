package com.shuangge.english.view.read.fragment;

import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.support.utils.IOralEvalSDKMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.view.read.AtyReadLesson.ReadLessonWord;

public abstract class BaseLessonType extends Fragment implements OnClickListener {

	private Set<IWord> notPassWords; 
	protected IWord wordData;
	protected CallbackLessonType callback;
	protected View mainView;
	
	protected static final int STATE_START = 1;
	protected static final int STATE_PAUSE = 2;
	protected static final int STATE_STOP = 3;
	protected static final int STATE_FINISHED = 10;
	
	protected int state = 0;
	protected boolean initialized = false;
	
	protected int step = 0;
	
	protected int target = 0; // -1 默认  0为有返回
	public static int DEFAULT_TARGET_STEP = 0;
	public static int TARGET_STEP = 1;
	
	
	public static interface CallbackLessonType {
		
		public void onFramgentFinished(int result);
		public void onFirstWrong();
		
	}
	
	public BaseLessonType() {
		super();
	}
	
	public BaseLessonType(IWord wordData, CallbackLessonType callback) {
		super();
		this.callback = callback;
		this.wordData = wordData;
		notPassWords = GlobalRes.getInstance().getBeans().getNotPassWordsForLesson();
	}
	
	
	public void setTarget(int target) {
		this.target = target;
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
		step = 0;
		nextStep();
	}
	
	public abstract void reset();
	
	public void stop() {
		state = STATE_STOP;
		MediaPlayerMgr.getInstance().stopMp();
	}
	
	protected void nextStep() {
		if (state != STATE_START || !initialized) {
			return;
		}
		if (step++ >= 1) {
			onFramgentFinished(null);
			return;
		}
		onCurrentStepStart();
	}
	
	/**
	 * 指向性跳转
	 * @param result 2 为 type01 跳转回对应type02的标记 在atyReadLesson 的实现中进行判断
	 */
	protected void targetStep(int result) {
		if (state != STATE_START || !initialized) {
			return;
		}
		onFramgentFinished(result);
	}
	
	protected abstract void onCurrentStepPrevStart();
	
	protected abstract void onCurrentStepStart();
	
	private void onFramgentFinished(Integer result) {
		if (null != callback) {
			int r = null == result ? getResult():result;
			
			callback.onFramgentFinished(r);
		}
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
		onCurrentStepStart();
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

	private int result = 1;
	
	protected void setResult(int result) {
		this.result = result;
		if (result == LessonData.RESULT_WRONG) {
			if (notPassWords.contains(wordData)) {
				return;
			}
			notPassWords.add(wordData);
		}
		if (null != callback && result == LessonData.RESULT_WRONG)
			callback.onFirstWrong();
	}
	
	public int getResult() {
//		return notPassWords.contains(wordData) ? LessonData.RESULT_WRONG : LessonData.RESULT_RIGHT;
		return result;
	}
	
	public static String getSoundLocalPath(Long id) {
		return CacheDisk.getWordPath() + id + ".mp3";
//		return CacheDisk.getWordPath() + "word" + ".mp3";
	}
	
	public static String getImgLocalPath(Long id) {
		return CacheDisk.getWordPath() + id + ".jpg";
//		return CacheDisk.getWordPath() + "word" + ".jpg";
	}
}
