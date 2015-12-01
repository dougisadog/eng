package com.shuangge.english.network.post;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.PostListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqMyPostDatas extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqMyPostDatas(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		PostListResult result = HttpReqFactory.getServerResultByToken(PostListResult.class, ConfigConstants.POST_MY_LIST_URL,
				new HttpReqFactory.ReqParam("timestamp", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setPostDatas(result);
			return true;
		}
		return false;
	}
	
}
