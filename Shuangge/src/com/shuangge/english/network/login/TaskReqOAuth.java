package com.shuangge.english.network.login;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoUser;
import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.login.LoginResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.app.PushManager;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqOAuth extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqOAuth(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		LoginResult result = HttpReqFactory.getServerResult(LoginResult.class, ConfigConstants.LOGIN_OAUTH_URL,
				new HttpReqFactory.ReqParam("uid", GlobalReqDatas.getInstance().getRequestUID()),
				new HttpReqFactory.ReqParam("uidType", GlobalReqDatas.getInstance().getRequestUIDType()),
				new HttpReqFactory.ReqParam("name", GlobalReqDatas.getInstance().getRequestName()),
				new HttpReqFactory.ReqParam("headUrl", GlobalReqDatas.getInstance().getRequestImgHeadUrl()),
				new HttpReqFactory.ReqParam("sex", GlobalReqDatas.getInstance().getRequestSex()),
				new HttpReqFactory.ReqParam("phoneBrand", AppInfo.PHONE_BRAND),
				new HttpReqFactory.ReqParam("phoneModel", AppInfo.PHONE_MODEL),
				new HttpReqFactory.ReqParam("phoneVersion", AppInfo.ANDROID_VERSION),
				new HttpReqFactory.ReqParam("appVersion", AppInfo.APP_VERSION),
//				new HttpReqFactory.ReqParam("channel", "360"),
				new HttpReqFactory.ReqParam("clientType", "android"));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			DaoUser dao = new DaoUser();
			dao.update("1", new EntityUser(GlobalReqDatas.getInstance().getRequestUID(), GlobalReqDatas.getInstance().getRequestUIDType()));
			GlobalRes.getInstance().getBeans().setLoginData(result);
			PushManager.getInstance().login();
			return true;
		}
		return false;
	}
	
}
