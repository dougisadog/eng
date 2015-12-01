package com.shuangge.english.network.read;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.PostListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqReadProgress extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqReadProgress(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = null;
		if (params.length == 3) {
			result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.READ_PROGRESS_URL,
					new HttpReqFactory.ReqParam("readNo", params[0]),
					new HttpReqFactory.ReqParam("progress", params[1]),
					new HttpReqFactory.ReqParam("notPassedWordIds", params[2]));
		}
		else if (params.length == 2) {
			result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.READ_PROGRESS_URL,
					new HttpReqFactory.ReqParam("readNo", params[0]),
					new HttpReqFactory.ReqParam("progress", params[1]));
		}
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
