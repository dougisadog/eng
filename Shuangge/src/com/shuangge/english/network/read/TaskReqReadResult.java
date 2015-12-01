package com.shuangge.english.network.read;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.read.ReadPassResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqReadResult extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqReadResult(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ReadPassResult result = HttpReqFactory.getServerResultByToken(ReadPassResult.class, ConfigConstants.READ_PASSED_URL,
				new HttpReqFactory.ReqParam("readNo", params[0]),
				new HttpReqFactory.ReqParam("rightNum", params[1]),
				new HttpReqFactory.ReqParam("answers", params[2]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setReadPassResult(result);
			return true;
		}
		return false;
	}
	
}
