package com.shuangge.english.support.service;

import com.shuangge.english.support.task.MyAsyncTask;

/**
 * 
 * @author Jeffrey
 *
 * @param <Params> 后台运行时参数
 * @param <Progress> 更新进度 参数
 * @param <Result> 更新结果参数
 */
public abstract class BaseTask<Params, Progress, Result> extends MyAsyncTask<Params, Progress, Result> {
	
	protected int tag = 0;
	protected CallbackNoticeView<Progress, Result> callback;
	
	public BaseTask(int tag, CallbackNoticeView<Progress, Result> callback, Params[] params) {
		super();
		this.tag = tag;
		this.callback = callback;
		
//        execute(params);
		executeOnNormal2(params);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onProgressUpdate(Progress... values) {
		callback.onProgressUpdate(tag, values);
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		callback.refreshView(tag, result);
	}
	
	public static interface CallbackNoticeView<Progress, Result> {
		
		public void refreshView(int tag, Result result);
		
		public void onProgressUpdate(int tag, Progress[] values);
		
	}
	
	
}
