package com.shuangge.english.network.post;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.ReplyListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqReplyDatas extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqReplyDatas(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ReplyListResult result = HttpReqFactory.getServerResultByToken(ReplyListResult.class, ConfigConstants.POST_REPLY_LIST_URL,
				new HttpReqFactory.ReqParam("postNo", GlobalRes.getInstance().getBeans().getCurrentPostNo().intValue()),
				new HttpReqFactory.ReqParam("timestamp", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setReplyDatas(result);
			return true;
		}
		return false;
	}
	
}
