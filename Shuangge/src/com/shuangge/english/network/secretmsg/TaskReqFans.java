package com.shuangge.english.network.secretmsg;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.secretmsg.AttentionInfoResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqFans extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqFans(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		
		AttentionInfoResult result = HttpReqFactory.getServerResultByToken(AttentionInfoResult.class, ConfigConstants.SECRET_MY_FANS);
		
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setAttentionInfoResult(result);
			return true;
		}
		return false;

	}
	
}
