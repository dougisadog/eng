package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.ClassMemberResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 班级邀请成员
 * @author Jeffrey
 *
 */
public class TaskReqClassApply extends BaseTask<String, Void, Boolean> {
	
	public TaskReqClassApply(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ClassMemberResult result = HttpReqFactory.getServerResultByToken(ClassMemberResult.class, ConfigConstants.CLASS_APPLY_GROUP_URL,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentMyClassNo()),
				new ReqParam("userNo", GlobalRes.getInstance().getBeans().getCurrentUserNo()),
				new ReqParam("message", params[0]),
				new ReqParam("wechatNo", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setMemberData(result);
			return true;
		}
		return false;
	}
	
}
