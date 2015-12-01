package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.RandomResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 *
 * @author Jeffrey
 *
 */
public class TaskReqRecommendMembers extends BaseTask<Integer, Void, Boolean> {
	
	public TaskReqRecommendMembers(int tag, CallbackNoticeView<Void, Boolean> callback, Integer... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RandomResult result = HttpReqFactory.getServerResultByToken(RandomResult.class, ConfigConstants.CLASS_INVITE_RANDOM_URL,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentMyClassNo()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setRecommendMemberData(result);
			return true;
		}
		return false;
	}
	
}
