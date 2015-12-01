package com.shuangge.english.support.utils;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.shuangge.english.GlobalApp;

public class SoundPoolMgr {
	
	private static SoundPoolMgr instance;
	
	public SoundPoolMgr() {
		initSnd();
	}
	
	public static SoundPoolMgr getInstance() {
		if (null == instance) {
			instance = new SoundPoolMgr();
		}
		return instance;
	}

	/***************************************************************************************************************************************/
	/**
	/**  SoundPool
	/**
	/***************************************************************************************************************************************/
	
	private static final int SOUND_DING = 1; 
	private static final int SOUND_RIGHT = 2; 
	private static final int SOUND_WRONG = 3; 
	private static final int SOUND_TAP_RIGHT = 4; 
	private static final int SOUND_TAP_WRONG = 5; 
	
	private SoundPool snd;
	private int prevStreamID = -1;
	private SparseIntArray sndMap;
	
	private void initSnd() {
		snd = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		sndMap = new SparseIntArray();
		sndMap.put(SOUND_DING, snd.load(GlobalApp.getInstance().getActivity(), R.raw.ding, 1));
		sndMap.put(SOUND_RIGHT, snd.load(GlobalApp.getInstance().getActivity(), R.raw.right, 1));
		sndMap.put(SOUND_WRONG, snd.load(GlobalApp.getInstance().getActivity(), R.raw.wrong, 1));
		sndMap.put(SOUND_TAP_RIGHT, snd.load(GlobalApp.getInstance().getActivity(), R.raw.tapright, 1));
		sndMap.put(SOUND_TAP_WRONG, snd.load(GlobalApp.getInstance().getActivity(), R.raw.tapwrong, 1));
	}
	
	public void destorySnd() {
		if (null == snd) {
			return;
		}
		stopSnd();
		snd.release();
		snd = null;
	}
	
	public void playSnd(int soundID) {
		stopSnd();
		prevStreamID = snd.play(soundID, 1, 1, 0, 0, 1);
	}
	
	public void stopSnd() {
		if (prevStreamID != -1) {
			snd.stop(prevStreamID);
		}
	}
	
	public void playTapRightSnd() {
		playSnd(SOUND_TAP_RIGHT);
	}
	
	public void playTapWrongSnd() {
		playSnd(SOUND_TAP_WRONG);
	}
	
	public void playRightSnd() {
		playSnd(SOUND_RIGHT);
	}
	
	public void playWrongSnd() {
		playSnd(SOUND_WRONG);
	}
	
	public void playDingSnd() {
		playSnd(SOUND_DING);
	}
	
}
