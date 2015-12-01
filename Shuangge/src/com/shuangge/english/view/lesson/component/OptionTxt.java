package com.shuangge.english.view.lesson.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.AutoResizeTextView;

public class OptionTxt extends RelativeLayout implements OnClickListener, OnTouchListener {

	private ImageView imgSound;
	private AutoResizeTextView txtContent;
	private OnCompletionListener onCompletionListener;
	private String soundPath;
	private static final int TEXT_SIZE = 20;
	
//	private static final int WRONG_COLOR = 0x3343454A;
	private static final int WRONG_COLOR = 0x33FFFFFF;
	private static final int NORMAL_COLOR = Color.WHITE;
	private static final int SELECTED_COLOR = 0xFF243F5A;
	
	/***************************************************************************************************************************************/
	/**
	/**  initialization
	/**
	/***************************************************************************************************************************************/
	
	public OptionTxt(Context context, String txt, int width, int height, int left, int top, String path, OnCompletionListener onCompletionListener) {
		super(context);
		if (!TextUtils.isEmpty(path)) {
			this.soundPath = path;
			createContentBtn(context, txt, width, 50);
			createSoundBtn(context);
		}
		else {
			createContentBtn(context, txt, width, 0);
		}
		this.onCompletionListener = onCompletionListener;
		this.setBackgroundResource(R.drawable.bg_lesson);
		this.setLayoutParams(ViewUtils.setRelativeMargins(this, width, height, left, top, 0, 0));
//		if (textSize == 0) {
//			textSize = DensityUtils.dip2px(context, 20f); 
//		}
		this.setOnTouchListener(this);
	}
	
	private void createContentBtn(Context context, String txt, int width, int marginRight) {
		txtContent = new AutoResizeTextView(context);
		txtContent.setTextSize(TEXT_SIZE);
		setContent(txt);
		txtContent.setTextColor(NORMAL_COLOR);
		txtContent.setGravity(Gravity.CENTER);
		LayoutParams layoutParams = ViewUtils.setRelativeMargins(txtContent, 
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, 0, 0, DensityUtils.dip2px(context, marginRight), 10);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		addView(txtContent);
		txtContent.setOnTouchListener(this);
	}
	
	private void createSoundBtn(Context context) {
		imgSound = new ImageView(context);
		imgSound.setImageDrawable(getResources().getDrawable(R.drawable.s_btn_lesson_play));
		LayoutParams layoutParams = ViewUtils.setRelativeMargins(imgSound, RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT, 0, 0, 10, 0);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		addView(imgSound);
		imgSound.setOnClickListener(this);
	}
	
	
	/***************************************************************************************************************************************/
	/**
	/**  click wrong reset
	/**
	/***************************************************************************************************************************************/
	
	public void setWrong() {
//		getBackground().setAlpha(100);
		clearAnimation();
		AlphaAnimation animation = new AlphaAnimation(1, 0.35f);
		animation.setFillAfter(true);
		animation.setDuration(500);
		startAnimation(animation);
		if (null != txtContent) {
			txtContent.setTextColor(WRONG_COLOR);
			txtContent.setOnTouchListener(null);
		}
		if (null != imgSound) {
			imgSound.setEnabled(false);
//			imgSound.setVisibility(View.GONE);
		}
		this.setOnTouchListener(null);
	}
	
	public void setRight() {
		clearAnimation();
//		getBackground().setAlpha(255);
		if (null != txtContent) {
			txtContent.setOnTouchListener(null);
		}
		this.setOnTouchListener(null);
	}
	
	public void setContent(String content) {
		txtContent.setText(content);
	}
	
	public void reset() {
		clearAnimation();
//		getBackground().setAlpha(255);
		this.setBackgroundResource(R.drawable.bg_lesson);
		if (null != txtContent) {
			txtContent.setTextColor(NORMAL_COLOR);
			txtContent.setOnTouchListener(this);
		}
		if (null != imgSound) {
			imgSound.setEnabled(true);
//			imgSound.setVisibility(View.VISIBLE);
		}
		this.setOnTouchListener(this);
	}
	
	@Override
	public void onClick(View v) {
		MediaPlayerMgr.getInstance().playMp(soundPath, onCompletionListener);
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.setBackgroundResource(R.drawable.bg_lesson_p);
			if (null != txtContent) {
				txtContent.setTextColor(SELECTED_COLOR);
			}
			break;
		case MotionEvent.ACTION_UP:
			this.setBackgroundResource(R.drawable.bg_lesson);
			if (null != txtContent) {
				txtContent.setTextColor(NORMAL_COLOR);
			}
			break;
		}
		return false;
	}
	
	/****************************************************************************************************************************************/
	/**
	/** recycle
	/**
	/****************************************************************************************************************************************/
	
	public void recycle() {
		clearAnimation();
		if (null != imgSound) {
			Drawable drawable = imgSound.getDrawable();
			if (null == drawable) {
				return;
			}
			if (drawable instanceof BitmapDrawable) {    
				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;    
				Bitmap bitmap = bitmapDrawable.getBitmap();    
				if (null != bitmap) {
					bitmap.recycle();
					System.gc();
				}
			}  
			imgSound.setImageDrawable(null);
			imgSound = null;
		}
		txtContent = null;
	}

}
