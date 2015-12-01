package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.SearchClassResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取推荐班级
 * @author Jeffrey
 *
 */
public class TaskReqRecommendClasses extends BaseTask<Integer, Void, Boolean> {
	
	public TaskReqRecommendClasses(int tag, CallbackNoticeView<Void, Boolean> callback, Integer... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		SearchClassResult result = HttpReqFactory.getServerResultByToken(SearchClassResult.class, ConfigConstants.CLASS_RANDOM_URL);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setRecommendClasses(result);
			return true;
		}
		return false;
	}
	
}
