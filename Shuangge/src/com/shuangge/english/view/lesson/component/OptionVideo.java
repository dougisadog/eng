package com.shuangge.english.view.lesson.component;

import java.io.IOException;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.utils.ViewUtils;

public class OptionVideo extends RelativeLayout implements OnClickListener {

	private String url;
	private MediaPlayer mp;
	private SurfaceView sv;
	private ImageView ivStart, ivStop, ivPause;
	
	private VideoState state = VideoState.stop;
	
	private int size;
	
	private enum VideoState {
		start, stop, pause;
	}
	
	public OptionVideo(Context context, final String url, int size) {
		super(context);
		
		this.url = url;
		this.size = size;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_optionvideo, this);
        
//        sv = (SurfaceView) findViewById(R.id.sv);
        ivStart = (ImageView) findViewById(R.id.ivStart);
        ivStop = (ImageView) findViewById(R.id.ivStop);
        ivPause = (ImageView) findViewById(R.id.ivPause);
        ivStart.setOnClickListener(this);
		ivStop.setOnClickListener(this);
		ivPause.setOnClickListener(this);
		ivStart.setVisibility(View.VISIBLE);
		ivStop.setVisibility(View.INVISIBLE);
		ivPause.setVisibility(View.INVISIBLE);
		
//		sv.setVisibility(View.INVISIBLE);
	}
	
	private void prepare() {
		mp = new MediaPlayer();
        try {
//        	mp.setDataSource("http://forum.ea3w.com/coll_ea3w/attach/2008_10/12237832415.3gp");
//        	mp.setDataSource("http://res.happyge.com:8081/client/test2/101/10101/101010100/img/400000.mp4");
			mp.setDataSource(url);
			mp.prepareAsync();
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			e.printStackTrace();
		}
        mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
			}
		});
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				state = VideoState.stop;
				ivStart.setVisibility(View.VISIBLE);
				ivStop.setVisibility(View.INVISIBLE);
				ivPause.setVisibility(View.INVISIBLE);
			}
			
		});
        mp.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				mIsVideoReadyToBePlayed = true;
				mp.seekTo(0);
				startVideoPlayback();
			}
			
		});
        mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				
				if (width == 0 || height == 0) {
		            return;
		        }
//				if (width > AppInfo.getScreenWidth()) {
					double d = (double) AppInfo.getScreenWidth() / width;
					width = AppInfo.getScreenWidth();
					height = (int) (height * d);
//				}
		        mIsVideoSizeKnown = true;
		        mVideoWidth = width;
		        mVideoHeight = height;
		        
		        startVideoPlayback();
			}
			
		});
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}
	
	public void start() {
		if (state.equals(VideoState.start)) {
			return;
		}
		if (null == mp) {
			prepare();
		}
		state = VideoState.start;
		ivStart.setVisibility(View.INVISIBLE);
		ivStop.setVisibility(View.VISIBLE);
		ivPause.setVisibility(View.VISIBLE);
		startVideoPlayback();
	}
	
	public void stop() {
		if (state.equals(VideoState.stop)) {
			return;
		}
		state = VideoState.stop;
		if (mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
		mIsVideoSizeKnown = false;
		ivStart.setVisibility(View.VISIBLE);
		ivStop.setVisibility(View.INVISIBLE);
		ivPause.setVisibility(View.INVISIBLE);
	}
	
	public void pause() {
		if (!state.equals(VideoState.start)) {
			return;
		}
		state = VideoState.pause;
		if (mp != null) {
			mp.pause();
		}
		ivStart.setVisibility(View.VISIBLE);
		ivStop.setVisibility(View.VISIBLE);
		ivPause.setVisibility(View.INVISIBLE);
	}
	
	public void clear() {
		releaseMediaPlayer();
	}
	
	
	private void releaseMediaPlayer() {
        if (mp != null) {
        	mp.release();
        	mp = null;
        }
    }
	
	private int mVideoWidth = 0;
	private int mVideoHeight = 0;
	private boolean mIsVideoReadyToBePlayed = false;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoCreated = false;

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
        mIsVideoCreated = true;
    }

    private void startVideoPlayback() {
    	if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown && state.equals(VideoState.start)) {
    		if (null == sv) {
    			sv = new SurfaceView(getContext());
    			LayoutParams lp = ViewUtils.setRelativeMargins(sv, mVideoWidth, mVideoHeight);
    			lp.addRule(CENTER_VERTICAL);
    			lp.addRule(CENTER_HORIZONTAL);
    			
    			((ViewGroup) findViewById(R.id.rlOptionVideoBg)).addView(sv, 0);
    			
    			SurfaceHolder h = sv.getHolder();
    			sv.getHolder().setFixedSize(mVideoWidth, mVideoHeight);
    			h.addCallback(new Callback() {
    				
    				@Override
    				public void surfaceDestroyed(SurfaceHolder holder) {
    				}
    				
    				@Override
    				public void surfaceCreated(SurfaceHolder holder) {
    					mIsVideoCreated = true;
    					startVideoPlayback();
    				}
    				
    				@Override
    				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    				}
    				
    			});
    			return;
    		}
    		if (!mIsVideoCreated) {
    			return;
    		}
    		mp.start();
    		mp.setDisplay(sv.getHolder());
//    		sv.setVisibility(View.VISIBLE);
    	}
    }

	@Override
	public void onClick(View v) {
		if (v.equals(ivStart)) {
			start();
			return;
		}
		if (v.equals(ivStop)) {
			stop();
			return;
		}
		if (v.equals(ivPause)) {
			pause();
			return;
		}
	}

}
