package com.shuangge.english.view.lesson.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.entity.lesson.GlobalResTypes.ILocalImg;
import com.shuangge.english.support.utils.ImageUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.MaskImage;

public class OptionImg extends RelativeLayout implements OnClickListener, ILocalImg {

	public static final int NORMAL = 0;
	public static final int GRAY = 1;
	private MaskImage img;
	private TextView txtSound;
	private int state = NORMAL;
	
	private String soundPath;
	
	public OptionImg(Context context, int maskSource, Bitmap bitmap, int width, int height) {
		super(context);
		if (null == bitmap) {
			bitmap = getDefaultPic(width, height);
		}
		img = new MaskImage(context, maskSource, bitmap, width, height);
		addView(img);
		this.setLayoutParams(ViewUtils.setLinearMargins(this, width, height, 0, 0, 0, 0));
	}

	public OptionImg(Context context, int maskSource, Bitmap bitmap, int width, int height, String path) {
		super(context);
		if (null == bitmap) {
			bitmap = getDefaultPic(width, height);
		}
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
		img.setBitmap(getDefaultPic((int) img.getImageWidth(), (int) img.getImageHeight()));
	}

	@Override
	public void onClick(View v) {
		MediaPlayerMgr.getInstance().playMp(soundPath, onCompletionListener);
	}

	public void setBitmap(Bitmap bitmap) {
		if (null == img) {
			return;
		}
//		img.clearColorFilter();
		clearAnimation();
		if (null == bitmap) {
			bitmap = getDefaultPic((int) img.getImageWidth(), (int) img.getImageHeight());
		}
		img.setBitmap(bitmap);
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setDuration(800);
		animation.setFillAfter(true);
		if (null != img.getAnimation()) {
			img.getAnimation().cancel();
		}
		img.clearAnimation();
		img.startAnimation(animation);
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
		clearAnimation();
		if (null != img) {
			img.clearAnimation();			
			img.recycle();
		}
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
//		img.clearColorFilter();
//		img.setAlpha(100);
		clearAnimation();
		AlphaAnimation animation = new AlphaAnimation(1, 0.35f);
		animation.setFillAfter(true);
		animation.setDuration(500);
		startAnimation(animation);
		if (null != txtSound)
			txtSound.setEnabled(false);
	}
	
	public void reset() {
		state = NORMAL;
		clearAnimation();
//		if (null != img) {
//			img.clearColorFilter();
//			img.setAlpha(255);
//		}
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
	
	private Bitmap getDefaultPic(int reqHeight, int reqWidth) {
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.default_lesson_pic, options);
        if (reqHeight > 0 && reqWidth > 0) {
            options.inSampleSize = ImageUtils.calculateInSampleSize(options, reqWidth, reqHeight);
        }
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inDither = false;  
		return BitmapFactory.decodeResource(getResources(), R.drawable.default_lesson_pic, options);
	}
	
}
