package com.shuangge.english.network.read;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.read.ReadListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqReadList extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqReadList(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ReadListResult result = HttpReqFactory.getServerResultByToken(ReadListResult.class, ConfigConstants.READ_LIST_URL);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setReadListData(result);
			return true;
		}
		return false;
	}
	
}
