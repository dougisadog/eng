package com.shuangge.english.network.account;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.MyRanklistResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqMyRanklist extends BaseTask<Void, Void, Boolean>{
	
	public TaskReqMyRanklist(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		MyRanklistResult result = HttpReqFactory.getServerResultByToken(MyRanklistResult.class, ConfigConstants.ACCOUNT_MYINFO);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setMyRanklistData(result);
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setKeyNum(result.getKeyNum());
			return true;
		}
		return false;
	}
	
}
