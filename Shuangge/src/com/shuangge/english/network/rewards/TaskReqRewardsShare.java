package com.shuangge.english.network.rewards;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.login.LoginResult;
import com.shuangge.english.entity.server.post.ShareRewardsResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRewardsShare extends BaseTask<Object, Void, Integer> {
	
	public static final int STAUTS_NETWROK_ERR = 0;
	public static final int STAUTS_ERR = 1;
	public static final int STAUTS_SUCCESS = 2;
	
	public TaskReqRewardsShare(int tag, CallbackNoticeView<Void, Integer> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Integer doInBackground(Object... params) {
		ShareRewardsResult result = HttpReqFactory.getServerResultByToken(ShareRewardsResult.class, ConfigConstants.REWARDS_SHARE);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			LoginResult loginResult = GlobalRes.getInstance().getBeans().getLoginData();
			if (loginResult.getInfoData().getMoney() == result.getMoney()) {
				return STAUTS_ERR;
			}
			loginResult.getInfoData().setMoney(result.getMoney());
			return STAUTS_SUCCESS;
		}
		return STAUTS_NETWROK_ERR;
	}
	
}
