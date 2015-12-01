package com.shuangge.english.network.attention;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqAttentionCancel extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqAttentionCancel(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.ATTENTION_CANCEL_URL,
				new HttpReqFactory.ReqParam("attentionNo", GlobalRes.getInstance().getBeans().getCurrentUserNo()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
