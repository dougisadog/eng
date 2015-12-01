package com.shuangge.english.network.login;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRegisterPhoneToken extends BaseTask<Void, Void, Boolean>{
	
	public TaskReqRegisterPhoneToken(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResult(RestResult.class, ConfigConstants.GET_REGISTER_PHONE_TOKEN_URL,
				new HttpReqFactory.ReqParam("phone", GlobalReqDatas.getInstance().getRequestLoginName()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
