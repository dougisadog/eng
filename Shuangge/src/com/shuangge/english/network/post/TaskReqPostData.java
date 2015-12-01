package com.shuangge.english.network.post;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.PostResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqPostData extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqPostData(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		PostResult result = HttpReqFactory.getServerResultByToken(PostResult.class, ConfigConstants.POST_BROWSE_URL,
				new HttpReqFactory.ReqParam("postNo", GlobalRes.getInstance().getBeans().getCurrentPostNo().intValue()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setPostData(result);
			return true;
		}
		return false;
	}
	
}
