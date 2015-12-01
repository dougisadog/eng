package com.shuangge.english.network.account;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoUser;
import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.AccountRest;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqBindingAccount extends BaseTask<String, Void, Boolean> {
	
	public TaskReqBindingAccount(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		AccountRest result = HttpReqFactory.getServerResultByToken(AccountRest.class, ConfigConstants.ACCOUNT_SET_ACCOUNT_URL,
				new HttpReqFactory.ReqParam("newLoginName", GlobalReqDatas.getInstance().getRequestLoginName()),
				new HttpReqFactory.ReqParam("password", GlobalReqDatas.getInstance().getRequestPassword()),
				new HttpReqFactory.ReqParam("phoneToken", GlobalReqDatas.getInstance().getRequestPhotoToken()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setLoginName(result.getLoginName());
			InfoData data = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
			data.setLoginName(result.getLoginName());
			data.setVisitor(false);
			
			DaoUser dao = new DaoUser();
			dao.update("1", new EntityUser(GlobalReqDatas.getInstance().getRequestLoginName(), 
					GlobalReqDatas.getInstance().getRequestPassword()));
			return true;
		}
		return false;
	}
	
}
