package com.shuangge.english.network.settings;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqSettings extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqSettings(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		Object speechAccuracy = params[0];
		Object speechEnabled = params[1];
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.SETTINGS,
				new HttpReqFactory.ReqParam("speechAccuracy", speechAccuracy),
				new HttpReqFactory.ReqParam("secretMsgAcceptStatus", params[2]),
				new HttpReqFactory.ReqParam("speechEnabled", speechEnabled));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
