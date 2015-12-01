package com.shuangge.english.network.msg;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.msg.NoticeResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqSystemMsg extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqSystemMsg(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		NoticeResult result = HttpReqFactory.getServerResultByToken(NoticeResult.class, ConfigConstants.MSG_SYSTEM_URL,
				new HttpReqFactory.ReqParam("timestamp", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setSystemMsgData(result);
			return true;
		}
		return false;
	}
	
}
