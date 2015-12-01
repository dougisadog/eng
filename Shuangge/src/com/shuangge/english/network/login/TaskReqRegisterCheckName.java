package com.shuangge.english.network.login;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRegisterCheckName extends BaseTask<Void, Void, Boolean>{
	
	public TaskReqRegisterCheckName(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResult(RestResult.class, ConfigConstants.CHECK_LOGIN_NAME,
				new HttpReqFactory.ReqParam("loginName", GlobalReqDatas.getInstance().getRequestLoginName()),
				new HttpReqFactory.ReqParam("name", GlobalReqDatas.getInstance().getRequestName())
		);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
