package com.shuangge.english.support.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.text.TextUtils;
import android.widget.Toast;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.HttpProcess.DownloadState;
import com.shuangge.english.support.task.MyAsyncTask;

/**
 * 队列下载 独立下载
 * @author Jeffrey
 *
 */
public class DownloadHelper {

	private Map<String, DownloadState> downloadStateMap = new HashMap<String, DownloadState>();
	private List<String> waitForDownloadQueue = new ArrayList<String>();
	private List<String> currentDownloadingQueue = new ArrayList<String>();
	
	private int maxSize = 5;
	
	//队列下载回调
	private CallbackDownload callBackQueueDownload;
	
	public DownloadHelper(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public int getQueueSize() {
		return waitForDownloadQueue.size();
	}
	
	
	/*************************************************************************************************************************************/
	/**
	/** 独立下载
	/**
	/*************************************************************************************************************************************/
	
	public void startSingleDownload(String url) {
		startQueueDownload(url, new DownloadState());
	}
	
	public void startSingleDownload(String url, DownloadState state) {
		new DownloadAsyncTask(url, state).executeOnWaitNetwork();
	}
	
	/*************************************************************************************************************************************/
	/**
	/** 队列下载
	/**
	/*************************************************************************************************************************************/
	
	public void setCallBackQueueDownload(CallbackDownload callBackQueueDownload) {
		this.callBackQueueDownload = callBackQueueDownload;
	}
	
	public void clearCallBackQueueDownload() {
		this.callBackQueueDownload = null;
	}

	public boolean isQueueDownloading(String url) {
		DownloadState state = downloadStateMap.get(url);
		return null != state && state.isAllowedTOdownload();
	}
	
	public void startQueueDownload(String url) {
		startQueueDownload(url, new DownloadState());
	}
	
	public void startQueueDownload(String url, DownloadState state) {
		if (!checkSDCard(url)) {
			return;
		}
		if (null != downloadStateMap.get(url) && downloadStateMap.get(url).isAllowedTOdownload())
			return;
		downloadStateMap.put(url, state);
		waitForDownloadQueue.add(url);
		downloadNext();
	}
	
	public void stopQueueDownload(String url) {
		DownloadState state = downloadStateMap.get(url);
		if (null == state) {
			return;
		}
		state.setAllowedTOdownload(false);
		downloadNext();
	}
	
	public void stopAllQueueDownload() {
		DownloadState state = null;
		for (String url : currentDownloadingQueue) {
			state = downloadStateMap.get(url);
			if (null == state) {
				continue;
			}
			state.setAllowedTOdownload(false);
		}
		currentDownloadingQueue.clear();
		waitForDownloadQueue.clear();
		downloadStateMap.clear();
	}
	
	/*************************************************************************************************************************************/
	
	private void downloadNext() {
		if (waitForDownloadQueue.size() == 0) {
			return;
		}
		String url = waitForDownloadQueue.get(0);
		DownloadState state = downloadStateMap.get(url);
		if (null == state || !state.isAllowedTOdownload()) {
			waitForDownloadQueue.remove(0);
			downloadStateMap.remove(url);
			downloadNext();
			return;
		}
		if (currentDownloadingQueue.size() >= maxSize) {
			if (null != callBackQueueDownload) {
				callBackQueueDownload.waitingHanler(waitForDownloadQueue.get(waitForDownloadQueue.size() - 1));
			}
			return;
		}
		waitForDownloadQueue.remove(0);
		currentDownloadingQueue.add(url);
		new DownloadAsyncTask(url, state, true).executeOnWaitNetwork();
	}

	private class DownloadAsyncTask extends MyAsyncTask<Void, Long, Void> {

		private String url;
		private DownloadState state;
		private boolean isFinished = false;
		private boolean isPause = false;
		private boolean queue = false;
		private Exception e;
		
		private Long max;
		private Long progress;
		
		public DownloadAsyncTask(String url, DownloadState state) {
			this.url = url;
			this.state = state;
		}
		
		public DownloadAsyncTask(String url, DownloadState state, boolean queue) {
			this.url = url;
			this.state = state;
			this.queue = queue;
		}

		@Override
		protected Void doInBackground(Void... params) {
			DebugPrinter.i("start download : " + url);
			File file = TextUtils.isEmpty(state.getFilePath()) ? CacheDisk.createNewLessonFileByUrl(url) : FileUtils.createNewFileInSDCard(state.getFilePath());
			HttpReqHelper.DownloadFileListener listener = new HttpReqHelper.DownloadFileListener() {

				@Override
				public void startHandler(long max) {
					state.startInBackground(max);
					publishProgress(max);
				}
				
				@Override
				public void progressHandler(long progress, long max) {
					publishProgress(progress, max);
				}

				@Override
				public void errorHandler(Exception e, String errMsg) {
					DebugPrinter.e("errer download : " + url + e);
					DownloadAsyncTask.this.e = e;
				}

				@Override
				public void completeHandler(File file) {
					DebugPrinter.i("finish download : " + url);
					isFinished = true;
				}

				@Override
				public void stopHandler(long progress1, long max1) {
					isPause = true;
					max = max1;
					progress = progress1;
				}
			};
			if (null == file) {
				String errMsg = GlobalApp.getInstance().getString(R.string.sd_card_in_not_mounted);
				GlobalApp.getInstance().showErrMsg(errMsg);
				listener.errorHandler(new Exception(errMsg), errMsg);
				return null;
			}
			HttpReqFactory.createDownloadFileReq(
					listener, url, state, file);
			if (isFinished) {
				state.finishInBackground();
			}
			return null; 
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//独立下载
			if (!queue) {
				return;
			}
			//队列下载
			if (null != downloadStateMap.get(url))
				downloadStateMap.remove(url);
			if (null != callBackQueueDownload) {
				if (null != e) {
					if (null != callBackQueueDownload)
						callBackQueueDownload.errorHandler(url);
				}
				else if (isFinished) {
					isFinished = false;
					if (null != callBackQueueDownload)
						callBackQueueDownload.finishedHandler(url);
				}
				else if (isPause) {
					isPause = false;
					if (null != callBackQueueDownload)
						callBackQueueDownload.stopHandler(url, progress, max);
				}
			}
			if (currentDownloadingQueue.size() > 0)
				currentDownloadingQueue.remove(0);
			downloadNext();
		}
		
		@Override
		protected void onProgressUpdate(Long... values) {
			super.onProgressUpdate(values);
			if (null != callBackQueueDownload && values.length == 1)
				callBackQueueDownload.startHandler(url, values[0]);
			else if (null != callBackQueueDownload && values.length == 2)
				callBackQueueDownload.progressHandler(url, values[0], values[1]);
		}
		
	}
	
	public int getCurrentDownloadsSize() {
		return currentDownloadingQueue.size();
	}

	public static interface CallbackDownload {
		
		public void startHandler(String url, long max);
		
		public void waitingHanler(String url);
	
		public void progressHandler(String url, long progress, long max);
		
		public void finishedHandler(String url);
		
        public void errorHandler(String url);
        
        public void stopHandler(String url, long progress, long max);
		
	}
	
	private boolean checkSDCard(String url) {
		if (TextUtils.isEmpty(FileUtils.getSdCardPath()) || TextUtils.isEmpty(url)) {
			if (null != callBackQueueDownload) {
				Toast.makeText(GlobalApp.getInstance(), "请检查sdcard是否正常插入", Toast.LENGTH_SHORT).show();
				callBackQueueDownload.errorHandler(url);
			}
			return false;
		}
		return true;
	}
	
}
