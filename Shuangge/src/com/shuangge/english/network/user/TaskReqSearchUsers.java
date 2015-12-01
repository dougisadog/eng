package com.shuangge.english.network.user;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.SearchUserResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 *
 * @author Jeffrey
 *
 */
public class TaskReqSearchUsers extends BaseTask<String, Void, Boolean> {
	
	public TaskReqSearchUsers(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		SearchUserResult result = HttpReqFactory.getServerResultByToken(SearchUserResult.class, ConfigConstants.USER_SEARCH_URL,
				new ReqParam("name", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setSearchUsers(result);
			return true;
		}
		return false;
	}
	
}
