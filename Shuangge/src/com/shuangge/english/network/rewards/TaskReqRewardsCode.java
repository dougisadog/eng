package com.shuangge.english.network.rewards;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.config.ConfigConstants.RewardsCode;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取选中班级信息
 * @author Jeffrey
 *
 */
public class TaskReqRewardsCode extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqRewardsCode(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.REWARDS_CODE,
				new ReqParam("rewardsCode", RewardsCode.invitationCode),
				new ReqParam("code", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
