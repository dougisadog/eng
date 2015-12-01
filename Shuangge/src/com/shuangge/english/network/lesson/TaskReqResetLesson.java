package com.shuangge.english.network.lesson;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.ResetResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 重做课程
 * @author Jeffrey 
 * 
 * level 初判断英语水平的等级
 *
 */
public class TaskReqResetLesson extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqResetLesson(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ResetResult result = HttpReqFactory.getServerResultByToken(ResetResult.class, ConfigConstants.LESSON_RESET_URL,
				new ReqParam("cType5", GlobalRes.getInstance().getBeans().getCurrentType5Id()),
				new ReqParam("frequency", GlobalRes.getInstance().getBeans().getCurrentFrequency())
				);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setCurrentFrequency(result.getFrequency());
			return true;
		}
		return false;
	}
	
}