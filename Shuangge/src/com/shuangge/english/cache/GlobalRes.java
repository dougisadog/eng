package com.shuangge.english.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.DownloadHelper;
import com.shuangge.english.support.http.DownloadHelper.CallbackDownload;
import com.shuangge.english.support.http.HttpProcess.DownloadState;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqHelper;
import com.shuangge.english.support.task.MyAsyncTask;
import com.shuangge.english.support.utils.ImageUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.MaskImage;

/**
 * 
 * @author Jeffrey
 *
 */
public class GlobalRes {

	private static GlobalRes instance = null;

	/**
	 * 所有数据Bead缓存
	 */
	private CacheBeans beans;
	private CacheDisk disk;
	private CacheMemory memory;

	private LinkedHashMap<Integer, LinkedHashMap<String, Bitmap>> emotionsPic = new LinkedHashMap<Integer, LinkedHashMap<String, Bitmap>>();
	
	private GlobalRes() {
	}

	public static GlobalRes getInstance() {
		if (null == instance) {
			instance = new GlobalRes();
		}
		return instance;
	}

	public void initCache() {
		beans = new CacheBeans();
		disk = new CacheDisk();
		memory = new CacheMemory();
		
		downloadHelper = new DownloadHelper(2);
	}
	
	/****************************************************************************************************************************************************************/
	/**
	/** 加载网络图片
	/**
	/****************************************************************************************************************************************************************/
	
	public void displayBitmap(DisplayBitmapParam ...displayBitmapParams) {
		List<DisplayBitmapParam> needLoadParams = null;
		for (DisplayBitmapParam param : displayBitmapParams) {
			if (TextUtils.isEmpty(param.getUrl())) {
				continue;
			}
			Bitmap bitmap = memory.getBitmapFromMem(param.url, param.width, + param.height);
			if (null != bitmap && !bitmap.isRecycled()) {
				param.setBitmap(bitmap);
				display(param);
				continue;
			}
			if (param.getImageView() instanceof MaskImage) {
				((MaskImage) param.getImageView()).clear();
			}
			else if (param.getImageView() instanceof CircleImageView) {
				((CircleImageView) param.getImageView()).clear();
			}
			if (null == needLoadParams) {
				needLoadParams = new ArrayList<DisplayBitmapParam>();
			}
			needLoadParams.add(param);
		}
		if (null != needLoadParams) {
			// 从文件缓存中获取，推荐在工作线程中进行
			new DonwloadBitmapTask(needLoadParams).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}
	
	private class DonwloadBitmapTask extends MyAsyncTask<Void, List<Bitmap>, List<DisplayBitmapParam>> {
		
		private List<DisplayBitmapParam> needLoadParams;
		
		public DonwloadBitmapTask(List<DisplayBitmapParam> params) {
			this.needLoadParams = params;
		}
		
		@Override
		protected List<DisplayBitmapParam> doInBackground(Void... params) {
			Bitmap bitmap = null;

			for (final DisplayBitmapParam param : needLoadParams) {
				bitmap = disk.getBitmapFromDisk(param.getUrl(), param.width, + param.height);
				if (null != bitmap) {
					memory.addBitmapToCache(param.getUrl(), param.width, + param.height, bitmap);
					param.setBitmap(bitmap);
					continue;
				}
				HttpReqHelper.DownloadBinaryFileListener helper = new HttpReqHelper.DownloadBinaryFileListener() {
					
					private String path;
					
					@Override
					public void progressHandler(long progress, long max) {
					}
					
					@Override
					public void errorHandler(Exception e, String errMsg) {
					}
					
					@Override
					public void completeHandler(byte[] bytes) {
						path = disk.addFileToCache(param.getUrl(), FileUtils.TYPE_FILE_PIC, bytes);
					}
					
					public String toString() {
						return path;
					}

				};
				HttpReqFactory.createDownloadFileReq(helper, param.getUrl());
				if (null != helper && !TextUtils.isEmpty(helper.toString())) {
					bitmap = ImageUtils.readNormalPic(helper.toString(), param.getWidth(), param.getHeight());
					memory.addBitmapToCache(param.url, param.width, + param.height, bitmap);
					param.setBitmap(bitmap);
				}
			}
			return needLoadParams;
		}

		@Override
		protected void onPostExecute(List<DisplayBitmapParam> params) {
			super.onPostExecute(params);
			for (DisplayBitmapParam param : params) {
				if (null == param.getBitmap()) {
					continue;
				}
				display(param);
			}
		}
	}

	private void display(final DisplayBitmapParam param) {
		if (null != param.getOnLoadImageForLvListener()) {
			param.getOnLoadImageForLvListener().onComplete(param.getPosition(), param.getBitmap());
			return;
		}
		ImageView imageView = param.getImageView();
		if (null == imageView) {
			return;
		}
		if (imageView instanceof MaskImage) {
			((MaskImage) imageView).setBitmap(param.getBitmap(), true);
		}
		else if (imageView instanceof CircleImageView) {
			((CircleImageView) imageView).setBitmap(param.getBitmap());
		}
		else {
			imageView.setImageBitmap(param.getBitmap());
		}
		if (null != param.getAnimation()) {
			imageView.startAnimation(param.getAnimation());
		}
		if (null != param.callback)
			param.callback.onComplete(param);
	}

	/**
	 * 加载Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public void loadBitmap(final String url,
			final DownloadBitampListener listener) {
		// 从内存缓存中获取，推荐在主UI线程中进行
		Bitmap bitmap = memory.getBitmapFromMem(url, 0, 0);
		if (null != bitmap) {
			listener.completeHandler(bitmap);
			return;
		}
		// 从文件缓存中获取，推荐在工作线程中进行
		bitmap = disk.getBitmapFromDisk(url, 0, 0);
		if (null != bitmap) {
			memory.addBitmapToCache(url, 0, 0, bitmap);
			listener.completeHandler(bitmap);
			return;
		}
		HttpReqFactory.createDownloadFileReq(
				new HttpReqHelper.DownloadBinaryFileListener() {

					@Override
					public void progressHandler(long progress, long max) {
						listener.progressHandler(progress, max);
					}

					@Override
					public void errorHandler(Exception e, String errMsg) {
						listener.errorHandler(e);
					}

					@Override
					public void completeHandler(byte[] bytes) {
						Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
								bytes.length);
						disk.addFileToCache(url, FileUtils.TYPE_FILE_PIC, bytes);
						memory.addBitmapToCache(url, 0, 0, bitmap);
						listener.completeHandler(bitmap);
					}

				}, url);
	}

	public static interface DownloadBitampListener {

		public void progressHandler(long progress, long max);

		public void completeHandler(Bitmap bitmap);

		public void errorHandler(Exception e);

	}
	
	public static interface OnLoadImageForLvListener {
		
		public void onComplete(int postion, Bitmap bitmap);
		
	}
	
	public static class DisplayBitmapParam {
		
		public DisplayBitmapParam(String url, ImageView imageView) {
			this.url = url;
			this.imageView = imageView;
			this.width = imageView.getWidth();
			this.height = imageView.getHeight();
			if ((width == 0 || height == 0) && imageView instanceof MaskImage) {
				MaskImage img = (MaskImage) imageView;
				this.width = (int) img.getImageWidth();
				this.height = (int) img.getImageHeight();
			}
		}
		
		public DisplayBitmapParam(String url, int position, OnLoadImageForLvListener mListener, int width, int height) {
			this.url = url;
			this.mListener = mListener;
			this.position = position;
			this.width = width;
			this.height = height;
		}
		
		public DisplayBitmapParam(String url, ImageView imageView, int width, int height) {
			this.url = url;
			this.imageView = imageView;
			this.width = width;
			this.height = height;
		}
		
		public DisplayBitmapParam(String url, ImageView imageView, int width, int height, Animation animation) {
			this.url = url;
			this.imageView = imageView;
			this.width = width;
			this.height = height;
			this.animation = animation;
		}
		
		private OnLoadImageForLvListener mListener;
		private int position;
		private ImageView imageView;
		private String url;
		private int height = 0;
		private int width = 0;
		private Animation animation;
		private Bitmap bitmap;
		private CallbackDisplayBitmap callback;

		public ImageView getImageView() {
			return imageView;
		}

		public void setImageView(ImageView imageView) {
			this.imageView = imageView;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public Animation getAnimation() {
			return animation;
		}

		public void setAnimation(Animation animation) {
			this.animation = animation;
		}

		public Bitmap getBitmap() {
			return bitmap;
		}

		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		public OnLoadImageForLvListener getOnLoadImageForLvListener() {
			return mListener;
		}

		public int getPosition() {
			return position;
		}

		public CallbackDisplayBitmap getCallback() {
			return callback;
		}

		public void setCallback(CallbackDisplayBitmap callback) {
			this.callback = callback;
		}
		
	}

	public static interface CallbackDisplayBitmap {
		
		public void onComplete(DisplayBitmapParam param);
		
	}
	
	/**
	 * 加载Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public void loadSound(final String url, final DownloadSoundListener listener) {
		// 从文件缓存中获取，推荐在工作线程中进行
		if (disk.getSoundFromDisk(url)) {
			listener.completeHandler();
			return;
		}
		HttpReqFactory.createDownloadFileReq(
				new HttpReqHelper.DownloadBinaryFileListener() {

					@Override
					public void progressHandler(long progress, long max) {
						listener.progressHandler(progress, max);
					}

					@Override
					public void errorHandler(Exception e, String errMsg) {
						listener.errorHandler(e);
					}

					@Override
					public void completeHandler(byte[] bytes) {
						disk.addFileToCache(url, FileUtils.TYPE_FILE_MP3, bytes);
						listener.completeHandler();
					}

				}, url);
	}

	public static interface DownloadSoundListener {

		public void progressHandler(long progress, long max);

		public void completeHandler();

		public void errorHandler(Exception e);

	}

	/*
	 * public synchronized Map<String, Bitmap> getEmotionsPics() { if
	 * (emotionsPic != null && emotionsPic.size() > 0) { return
	 * emotionsPic.get(SmileyMap.GENERAL_EMOTION_POSITION); } else {
	 * getEmotionsTask(); return
	 * emotionsPic.get(SmileyMap.GENERAL_EMOTION_POSITION); } }
	 * 
	 * public synchronized Map<String, Bitmap> getHuahuaPics() { if (emotionsPic
	 * != null && emotionsPic.size() > 0) { return
	 * emotionsPic.get(SmileyMap.HUAHUA_EMOTION_POSITION); } else {
	 * getEmotionsTask(); return
	 * emotionsPic.get(SmileyMap.HUAHUA_EMOTION_POSITION); } }
	 */

	public CacheBeans getBeans() {
		return beans;
	}
	
	/***********************************************************************************************************************************/
	/** 
	/** 下载
	/** 
	/***********************************************************************************************************************************/
	
	private DownloadHelper downloadHelper;
	
	/*************************************************************************************************************************************/
	/**
	/** 独立下载
	/**
	/*************************************************************************************************************************************/
	
	public void startSingleDownload(String url) {
		downloadHelper.startSingleDownload(url);
	}
	
	public void startSingleDownload(String url, DownloadState state) {
		downloadHelper.startSingleDownload(url, state);
	}
	
	/*************************************************************************************************************************************/
	/**
	/** 队列下载
	/**
	/*************************************************************************************************************************************/
	
	public void setCallBackQueueDownload(CallbackDownload callBackQueueDownload) {
		downloadHelper.setCallBackQueueDownload(callBackQueueDownload);
	}
	
	public void clearCallBackQueueDownload() {
		downloadHelper.clearCallBackQueueDownload();
	}

	public boolean isQueueDownloading(String url) {
		return downloadHelper.isQueueDownloading(url);
	}
	
	public void startQueueDownload(String url) {
		downloadHelper.startQueueDownload(url);
	}
	
	public void startQueueDownload(String url, DownloadState state) {
		downloadHelper.startQueueDownload(url, state);
	}
	
	public void stopQueueDownload(String url) {
		downloadHelper.stopQueueDownload(url);
	}
	
	public void stopAllQueueDownload() {
		downloadHelper.stopAllQueueDownload();
	}
	
	/*************************************************************************************************************************************/
}
