package com.shuangge.english.network.lesson;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.LessonKeyResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqLessonUnlock extends BaseTask<Object, Void, Integer> {
	
	public TaskReqLessonUnlock(int tag, CallbackNoticeView<Void, Integer> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Integer doInBackground(Object... params) {
		LessonKeyResult result = HttpReqFactory.getServerResultByToken(LessonKeyResult.class, ConfigConstants.LESSON_UNLOCK,
				new HttpReqFactory.ReqParam("lessonId", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setKeyNum(result.getKeyNum());
			GlobalRes.getInstance().getBeans().setUnlockDatas345(result.getLessonData());
			return 0;
		}
		if (result == null) {
			return -1;
		}
		return result.getCode();
	}
	
}
