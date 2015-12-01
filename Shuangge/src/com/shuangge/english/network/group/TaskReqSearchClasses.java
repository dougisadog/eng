package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.SearchClassResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 *
 * @author Jeffrey
 *
 */
public class TaskReqSearchClasses extends BaseTask<String, Void, Boolean> {
	
	public TaskReqSearchClasses(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		SearchClassResult result = HttpReqFactory.getServerResultByToken(SearchClassResult.class, ConfigConstants.CLASS_SEARCH_URL,
				new ReqParam("name", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setSearchClasses(result);
			return true;
		}
		return false;
	}
	
}
