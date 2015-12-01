package com.shuangge.english.network.lesson;

import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoLessonDataCache;
import com.shuangge.english.entity.lesson.EntityLessonDataCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.LessonDetailsResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取课程具体内容
 * @author Jeffrey 
 * 
 */
public class TaskReqLessonDetails extends BaseTask<Void, Void, List<EntityLessonDataCache>> {
	
	public TaskReqLessonDetails(int tag, CallbackNoticeView<Void, List<EntityLessonDataCache>> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected List<EntityLessonDataCache> doInBackground(Void... params) {
		LessonDetailsResult result = HttpReqFactory.getServerResultByToken(LessonDetailsResult.class, ConfigConstants.LESSON_DETAILS_URL,
				new ReqParam("cType5", GlobalRes.getInstance().getBeans().getCurrentType5Id()),
				new ReqParam("frequency", GlobalRes.getInstance().getBeans().getCurrentFrequency())
		);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setLessonDetails(result);
			GlobalRes.getInstance().getBeans().setCurrentFrequency(result.getFrequency());
			DaoLessonDataCache dao = new DaoLessonDataCache();
			List<EntityLessonDataCache> datas = dao.get(GlobalRes.getInstance().getBeans().getLoginName(), GlobalRes.getInstance().getBeans().getCurrentType5Id());
			return datas;
		}
		return null;
	}
	
}
