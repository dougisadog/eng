package com.shuangge.english.network.rewards;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.ShareRewardsResult;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRewardsPostDatas extends BaseTask<Object, Void, Integer> {
	
	public static final int STATUS_SCUCESS = 1;
	public static final int STATUS_ERR = 2;
	
	public TaskReqRewardsPostDatas(int tag, CallbackNoticeView<Void, Integer> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Integer doInBackground(Object... params) {
		ShareRewardsResult result = HttpReqFactory.getServerResultByToken(ShareRewardsResult.class, ConfigConstants.REWARDS_UPDATE,
				new HttpReqFactory.ReqParam("money", params[0]),
				new HttpReqFactory.ReqParam("wechatNo", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			InfoData infoData = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
			infoData.setMoney(result.getMoney());
			return STATUS_SCUCESS;
		}
		return STATUS_ERR;
	}
	
}
