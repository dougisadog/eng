package com.shuangge.english.network.read;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.PostListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRemoveWord extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqRemoveWord(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		PostListResult result = HttpReqFactory.getServerResultByToken(PostListResult.class, ConfigConstants.PUSH_REGISTER,
				new HttpReqFactory.ReqParam("deviceToken", GlobalRes.getInstance().getBeans().getToken()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
