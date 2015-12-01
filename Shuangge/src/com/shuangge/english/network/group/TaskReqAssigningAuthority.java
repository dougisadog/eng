package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 分配权限
 * @author Jeffrey
 *
 */
public class TaskReqAssigningAuthority extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqAssigningAuthority(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.CLASS_ASSIGN_AUTHORITY_URL,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentMyClassNo()),
				new ReqParam("authorityLevel", params[0]),
				new ReqParam("assigningAccount", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
