package com.shuangge.english.support.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.text.TextUtils;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.support.debug.DebugPrinter;

public class MediaPlayerMgr {
	
	private static MediaPlayerMgr instance;
	
	public MediaPlayerMgr() {
		initMp();
	}
	
	public static MediaPlayerMgr getInstance() {
		if (null == instance) {
			instance = new MediaPlayerMgr();
		}
		return instance;
	}

	/***************************************************************************************************************************************/
	/**
	/**  MediaPlayer
	/**
	/***************************************************************************************************************************************/
	
	private MediaPlayer mp;
	private String path;
	private OnCompletionListener callback;
	
	private void initMp() {
	}
	
	public void destoryMp() {
		if (null == mp) {
			return;
		}
		stopMp();
		mp.reset();
		mp.release();
		mp = null;
	}
	
	public void playMp(String path) {
		playMp(path, null);
	}
	
	public void playMp(String path, OnCompletionListener callback) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		stopMp();
		this.callback = callback;
		this.path = path;
		if (null == mp) {
			mp = new MediaPlayer();
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			    try {
			        Class<?> cMediaTimeProvider = Class.forName( "android.media.MediaTimeProvider" );
			        Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
			        Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
			        Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );
	
			        Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
	
			        Object subtitleInstance = constructor.newInstance(GlobalApp.getInstance().getActivity(), null, null);
	
			        Field f = cSubtitleController.getDeclaredField("mHandler");
	
			        f.setAccessible(true);
			        try {
			            f.set(subtitleInstance, new Handler());
			        }
			        catch (IllegalAccessException e) {
		        	}
			        finally {
			            f.setAccessible(false);
			        }
	
			        Method setsubtitleanchor = mp.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);
	
			        setsubtitleanchor.invoke(mp, subtitleInstance, null);
			        //Log.e("", "subtitle is setted :p");
			    } catch (Exception e) {}
			}
			mp.setOnPreparedListener(onPreparedListener);
			mp.setOnErrorListener(onErrorListener);
		}
		else {
			mp.reset();
		}
		try {
			mp.setOnCompletionListener(this.onCompletionListener);
			mp.setDataSource(this.path);
		} 
		catch (IOException e) {
			DebugPrinter.e(e.getMessage());
		}
		catch (IllegalStateException e) {
			DebugPrinter.e(e.getMessage());
		}
		prepare();
	}
	
	/**
	 * 获取音频时长
	 * @param path
	 * @return
	 */
	public void getDuration(String path, final OnGetDurationListener callback) {
		MediaPlayer mp = new MediaPlayer();
		mp.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				int duration = mp.getDuration();
				mp.reset();
				mp.release();
				callback.onGetDuration(duration);
			}
		});
		try {
			mp.setDataSource(this.path);
			mp.prepareAsync();
		}
		catch (IOException e) {
			DebugPrinter.e(e.getMessage());
		}
		catch (IllegalStateException e) {
			DebugPrinter.e(e.getMessage());
		}
	}
	
	private OnPreparedListener onPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			if (mp == MediaPlayerMgr.this.mp) {
				mp.start();
            }
		}
		
	};
	
	private OnPreparedListener onPreparedListener2 = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.start();
		}
		
	};
	
	private OnErrorListener onErrorListener = new OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			mp.reset();
			return false;
		}
		
	};
	
	private android.media.MediaPlayer.OnCompletionListener onCompletionListener = 
			new android.media.MediaPlayer.OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
//			destoryMp();
			if (null != callback)
				callback.onCompletion();
		}
		
	};
	
	public interface OnCompletionListener {
		
		public void onCompletion();
		
	}
	
	public interface OnGetDurationListener {
		
		public void onGetDuration(int duration);
		
	}
	
	private void prepare() {
		try {
			mp.prepareAsync();
		} 
		catch (IllegalStateException e) {
			onCompletionListener.onCompletion(null);
			DebugPrinter.e(e.getMessage());
		}
	}
	
	public void stopMp() {
		try {
			if (null != mp && mp.isPlaying()) {
				mp.setOnCompletionListener(null);
				mp.stop();
			}
		}
		catch (IllegalStateException e) {
		}
	}
	
}
