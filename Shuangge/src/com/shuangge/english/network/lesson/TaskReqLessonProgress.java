package com.shuangge.english.network.lesson;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.UnlockResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取课程内容解锁
 * @author Jeffrey 
 * 
 */
public class TaskReqLessonProgress extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqLessonProgress(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		UnlockResult result = HttpReqFactory.getServerResultByToken(UnlockResult.class, ConfigConstants.GET_LESSON_PROGRESS_URL,
				new ReqParam("clientId", GlobalRes.getInstance().getBeans().getCurrentType2Id()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setUnlockDatas345(result.getLessonData());
			return true;
		}
		return false;
	}
	
}
