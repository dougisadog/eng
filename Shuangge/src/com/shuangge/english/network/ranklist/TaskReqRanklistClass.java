package com.shuangge.english.network.ranklist;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.ranklist.ClassRanklistResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRanklistClass extends BaseTask<Integer, Void, Boolean> {
	
	public TaskReqRanklistClass(int tag, CallbackNoticeView<Void, Boolean> callback, Integer... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ClassRanklistResult result = HttpReqFactory.getServerResultByToken(ClassRanklistResult.class, ConfigConstants.RANK_LIST_CLASS_ALL_URL,
				new HttpReqFactory.ReqParam("pageNo", params[0]),
				new HttpReqFactory.ReqParam("classNo", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setClassRanklistData(result);
			return true;
		}
		return false;
	}
	
}
