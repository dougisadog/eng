package com.shuangge.english.network.post;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRemoveNice extends BaseTask<Long, Void, Boolean> {
	
	public TaskReqRemoveNice(int tag, CallbackNoticeView<Void, Boolean> callback, Long... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Long... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.POST_REMOVE_NICE_URL,
				new HttpReqFactory.ReqParam("postNo", params[0].intValue()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
