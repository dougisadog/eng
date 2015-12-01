package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.ObtainLessonResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqObtainLesson extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqObtainLesson(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		int i = 0;
		while (true) {
			ObtainLessonResult result = HttpReqFactory.getServerResultByToken(ObtainLessonResult.class, ConfigConstants.LESSON_OBTAIN);
			if (null != result && result.getCode() == RestResult.C_SUCCESS) {
				GlobalRes.getInstance().getBeans().setObtainLesson(result.getLessonData());
				return true;
			}
			if (i++ >= 2)
				break;
		}
		return false;
	}
	
}
