package com.shuangge.english.network.rewards;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.LastWeekDedicateResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取选中班级信息
 * @author Jeffrey
 *
 */
public class TaskReqRewardsLastweekRank extends BaseTask<Integer, Void, Boolean> {
	
	public TaskReqRewardsLastweekRank(int tag, CallbackNoticeView<Void, Boolean> callback, Integer... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		LastWeekDedicateResult result = HttpReqFactory.getServerResultByToken(LastWeekDedicateResult.class, ConfigConstants.REWARDS_LASTWEEK_RANK,
				new ReqParam("pageNo", params[0]),
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentClassNo()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setClassMemberRewardRankData(result);
			return true;
		}
		return false;
	}
	
}
