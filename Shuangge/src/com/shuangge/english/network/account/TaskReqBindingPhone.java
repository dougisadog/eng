package com.shuangge.english.network.account;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqBindingPhone extends BaseTask<String, Void, Boolean>{
	
	public TaskReqBindingPhone(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.ACCOUNT_BINDING_PHONE_URL,
				new HttpReqFactory.ReqParam("phone", params[0]),
				new HttpReqFactory.ReqParam("phoneToken", params[1])
				);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setPhone(params[0]);
			return true;
		}
		return false;
	}
	
}
