package com.shuangge.english.network.ranklist;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.ranklist.RanklistResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRanklistAttentionWeek extends BaseTask<Integer, Void, Boolean> {
	
	public TaskReqRanklistAttentionWeek(int tag, CallbackNoticeView<Void, Boolean> callback, Integer... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RanklistResult result = HttpReqFactory.getServerResultByToken(RanklistResult.class, ConfigConstants.RANK_LIST_ATTENTION_WEEK_URL,
				new HttpReqFactory.ReqParam("pageNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setAttentionWeekRanklistData(result);
			return true;
		}
		return false;
	}
	
}
