package com.shuangge.english.network.read;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.http.HttpProcess.DownloadState;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqHelper;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqReadIWordRes extends BaseTask<Object, Integer, Boolean> {

	private Exception e;
	private File file = null;
	private int errNum = 0;
	private String url; // 下载地址
	private boolean result = false;

	public TaskReqReadIWordRes(int tag,
			CallbackNoticeView<Integer, Boolean> callback, Object... params) {
		super(tag, callback, params);
		this.url = (String) params[0];
		this.file = new File((String) params[1]);
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return false;
		}
		try {
			File dir = file.getParentFile();
			if (!dir.exists())
				dir.mkdirs();
			if (!file.createNewFile()) {
				DebugPrinter.d("DownloadForPageAsyncTask 创建文件失败");
			}
		} catch (IOException e) {
			DebugPrinter.d("DownloadForPageAsyncTask 创建文件失败" + e.getMessage());
		}
		HttpReqFactory.createDownloadFileReq(
				new HttpReqHelper.DownloadFileListener() {

					@Override
					public void startHandler(long max) {
					}

					@SuppressLint("UseValueOf")
					@Override
					public void progressHandler(long progress, long max) {
						Integer[] v = { new Integer((int) progress),
								new Integer((int) max), };
						callback.onProgressUpdate(errNum, v);
					}

					@Override
					public void errorHandler(Exception e, String errMsg) {
						// 失败重新加载
						TaskReqReadIWordRes.this.e = e;
						if (file.exists()) {
							file.delete();
						}
						if (errNum++ > 2) {
							errNum = 0;
							return;
						}
						result = false;
					}

					@Override
					public void completeHandler(File file) {
						result = true;
						return;
					}

					@Override
					public void stopHandler(long progress, long max1) {
						// 停止删除文件
						if (file.exists())
							file.delete();
						result = false;
					}

				}, url, new DownloadState(), file);
		return result;
	}

}
