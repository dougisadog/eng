package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.secretmsg.AttentionInfoResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqUserSearch extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqUserSearch(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		
		AttentionInfoResult result = HttpReqFactory.getServerResultByToken(AttentionInfoResult.class, ConfigConstants.USER_SEARCH,
				new HttpReqFactory.ReqParam("search", params[0]),
				new HttpReqFactory.ReqParam("pageNo", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setSearchUserDatas(result.getAttentions());
			return true;
		}
			
		return false;
	}
	
}
