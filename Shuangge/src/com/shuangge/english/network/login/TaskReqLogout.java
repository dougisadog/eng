package com.shuangge.english.network.login;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoUser;
import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.support.app.PushManager;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqLogout extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqLogout(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		DaoUser dao = new DaoUser();
		EntityUser user = dao.get("1");
		if (null != user) {
			user.setLogout(true);
			dao.update("1", user);
		}
		GlobalRes.getInstance().getBeans().setLoginData(null);
		GlobalRes.getInstance().getBeans().setSecretListDatas(null);
		PushManager.getInstance().logout();
		return true;
	}
	
}
