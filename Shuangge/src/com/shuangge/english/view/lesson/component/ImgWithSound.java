package com.shuangge.english.view.lesson.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.entity.lesson.GlobalResTypes.ILocalImg;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.MaskImage;

public class ImgWithSound extends RelativeLayout implements OnClickListener, ILocalImg {

	public static final int NORMAL = 0;
	public static final int GRAY = 1;
	private MaskImage img;
	private TextView txtSound;
	private int state = NORMAL;
	
	private String soundPath;
	
	public ImgWithSound(Context context, int maskSource, Bitmap bitmap, int width, int height) {
		super(context);
		img = new MaskImage(context, maskSource, bitmap, width, height);
		addView(img);
		this.setLayoutParams(ViewUtils.setLinearMargins(this, width, height, 0, 0, 0, 0));
	}

	public ImgWithSound(Context context, int maskSource, Bitmap bitmap, int width, int height, String path) {
		super(context);
		this.soundPath = path;
		img = new MaskImage(context, maskSource, bitmap, width, height);
		addView(img);
		txtSound = new TextView(context);
		txtSound.setText("播放");
		LayoutParams layoutParams = ViewUtils.setRelativeMargins(txtSound, RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT, 0, 0, 10, 10);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		addView(txtSound);
		txtSound.setOnClickListener(this);
		this.setLayoutParams(ViewUtils.setLinearMargins(this, width, height, 0, 0, 0, 0));
	}
	
	public void clear() {
		img.clear();
	}

	@Override
	public void onClick(View v) {
		MediaPlayerMgr.getInstance().playMp(soundPath, onCompletionListener);
	}

	public void setBitmap(Bitmap bitmap) {
		if (null == img) {
			return;
		}
		img.clearColorFilter();
		img.setBitmap(bitmap);
		switch (state) {
		case NORMAL:
			reset();
			break;
		case GRAY:
			setWrong();
			break;
		}
	}

	public MaskImage getImg() {
		return img;
	}
	
	public void recycle() {
		if (null != img)
			img.recycle();
		img = null;
		soundPath = null;
		txtSound = null;
	}
	
	public float getImageWidth() {
		if (null == img) {
			return 0;
		}
		return img.getImageWidth();
	}

	public float getImageHeight() {
		if (null == img) {
			return 0;
		}
		return img.getImageHeight();
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  click wrong reset
	/**
	/***************************************************************************************************************************************/
	
	public void setWrong() {
		state = GRAY;
		img.clearColorFilter();
		img.setColorFilter(0xFF333333, PorterDuff.Mode.MULTIPLY);
		if (null != txtSound)
			txtSound.setEnabled(false);
	}
	
	public void reset() {
		state = NORMAL;
		if (null != img)
			img.clearColorFilter();
		if (null != txtSound)
			txtSound.setEnabled(true);
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
	
}
