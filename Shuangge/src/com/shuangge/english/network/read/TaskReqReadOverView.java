package com.shuangge.english.network.read;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.read.ReadInitResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqReadOverView extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqReadOverView(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ReadInitResult result = HttpReqFactory.getServerResultByToken(ReadInitResult.class, ConfigConstants.READ_OVERVIEW_URL);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setReadInitResult(result);
			return true;
		}
		return false;
	}
	
}
