package com.shuangge.english.network.msg;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.msg.UserClassGroupResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqGroupMsg extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqGroupMsg(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		UserClassGroupResult result = HttpReqFactory.getServerResultByToken(UserClassGroupResult.class, ConfigConstants.MSG_GROUP_URL,
				new HttpReqFactory.ReqParam("timestamp", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setGroupMsgData(result);
			return true;
		}
		return false;
	}
	
}
