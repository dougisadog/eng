package com.shuangge.english.view.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shuangge.english.entity.lesson.GlobalResTypes.ILocalImg;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.utils.ImageUtils;

public class MaskImage extends ImageView implements ILocalImg {

	private int imageSource = 0;
	private int maskSource = 0;
	private float imageWidth = 0;
	private float imageHeight = 0;
	RuntimeException e;

	public MaskImage(Context context, int maskSource, Bitmap bitmap, int width, int height) {
		super(context);
		imageWidth = width;
		imageHeight = height;
		this.maskSource = maskSource;
		setBitmap(bitmap);
	}
	
	public MaskImage(Context context, int maskSource, int imageSource, int width, int height) {
		super(context);
		this.imageSource = imageSource;
		imageWidth = width;
		imageHeight = height;
		this.maskSource = maskSource;
		
		// 获取图片的资源文件
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = null;
		options.outHeight = 1;
		Bitmap original = BitmapFactory.decodeResource(getResources(), imageSource, options);
		setBitmap(original);
	}
	
	public MaskImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray styleAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.jattrs, 0, 0);
		imageSource = styleAttrs.getResourceId(R.styleable.jattrs_image, 0);
		maskSource = styleAttrs.getResourceId(R.styleable.jattrs_mask, 0);
		imageWidth = styleAttrs.getDimension(R.styleable.jattrs_imageWidth, 0);
		imageHeight = styleAttrs.getDimension(R.styleable.jattrs_imageHeight, 0);

		if (imageSource == 0 || maskSource == 0) {
			e = new IllegalArgumentException(
					styleAttrs.getPositionDescription()
							+ ": The content attribute is required and must refer to a valid image.");
		}

		if (e != null)
			throw e;
		/**
		 * 主要代码实现
		 */
		// 获取图片的资源文件
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = null;
		Bitmap original = BitmapFactory.decodeResource(getResources(), imageSource, options);
		setBitmap(original);

		styleAttrs.recycle();
	}
	
	public void clear() {
		// 获取图片的资源文件
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = null;
		Bitmap original = BitmapFactory.decodeResource(getResources(), imageSource, options);
		setBitmap(original);
//		long time = System.currentTimeMillis(); 
//		setBackgroundResource(imageSource);
//		setBackground(new BitmapDrawable());
//		DebugPrinter.e("clear 消耗:" + (System.currentTimeMillis() - time) + "ms");
	}

	public void setBitmap2(Bitmap bitmap) {
		if (null == bitmap) {
			return;
		}
		try {
			// 获取遮罩层图片
			Bitmap mask = null;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = false;
			options.inPreferredConfig = null;
			mask = BitmapFactory.decodeResource(getResources(), maskSource, options);
			int width = mask.getWidth();
	        int height = mask.getHeight();
	        if (imageWidth > 0  && imageHeight > 0) {
	        	width = (int) imageWidth;
	        	height = (int) imageHeight;
	        }
			Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Bitmap maskBmp = Bitmap.createBitmap(mask, 0, 0, mask.getWidth(), mask.getHeight(), ImageUtils.getMatrix(mask, width, height), true);
			mask.recycle();
			Bitmap contentBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), ImageUtils.getMatrix(bitmap, width, height), true);
			bitmap.recycle();
			
			// 将遮罩层的图片放到画布中
			Canvas canvas = new Canvas(result);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// 设置两张图片相交时的模式
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			canvas.drawBitmap(contentBmp, 0, 0, null);
			canvas.drawBitmap(maskBmp, 0, 0, paint);
			paint.setXfermode(null);
			setImageBitmap(result);
			setScaleType(ScaleType.CENTER);
		} 
		catch (OutOfMemoryError ignored) {
            ignored.printStackTrace();
            return;
        }
	}
	
	public void setBitmap(Bitmap bitmap) {
		setBitmap(bitmap, false);
		
//		setImageBitmap(bitmap);
//		DebugPrinter.e("重绘图片消耗:" + (System.currentTimeMillis() - time) + "ms");
	}
	
	public void setBitmap(Bitmap bitmap, boolean noRecycle) {
		long time = System.currentTimeMillis();
		if (null == bitmap || bitmap.isRecycled()) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_male);
		}
		int width = (int) imageWidth;
    	int height = (int) imageHeight;
    	try {
	    	Bitmap contentBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), ImageUtils.getMatrix(bitmap, width, height), true);
		    Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		    Canvas canvas = new Canvas(output);
		 
		    final int color = 0xff424242;
		    final Paint paint = new Paint();
		    final Rect rect = new Rect(0, 0, width, height);
		    final RectF rectF = new RectF(rect);
		    final float roundPx = 12;
		 
		    paint.setAntiAlias(true);
		    canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(color);
		    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(contentBmp, rect, rect, paint);
		    time = System.currentTimeMillis(); 
		    setImageBitmap(output);
//		    setBackgroundDrawable(new BitmapDrawable(output));
//			setScaleType(ScaleType.CENTER);
//			System.gc();
			DebugPrinter.e("重绘图片消耗:" + (System.currentTimeMillis() - time) + "ms");
			if (!noRecycle)
				bitmap.recycle();
//			contentBmp.recycle();
		} 
		catch (OutOfMemoryError ignored) {
	        ignored.printStackTrace();
	    }
		
//		setImageBitmap(bitmap);
//		DebugPrinter.e("重绘图片消耗:" + (System.currentTimeMillis() - time) + "ms");
	  }
	
	public void recycle() {
		Drawable drawable = getDrawable();
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
		setImageDrawable(null);
	}

	public float getImageWidth() {
		return imageWidth;
	}

	public float getImageHeight() {
		return imageHeight;
	}
	
}
