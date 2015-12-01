package com.shuangge.english.network.share;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.share.ShareResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

/**
 * @author Jeffrey
 *
 */
public class TaskReqShareResult extends BaseTask<Long, Void, Boolean> {
	
	public TaskReqShareResult(int tag, CallbackNoticeView<Void, Boolean> callback, Long... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Long... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ShareResult result = HttpReqFactory.getServerResultByToken(ShareResult.class, ConfigConstants.SHARE_RESULT);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setShareResult(result);
			return true;
		}
		return false;
	}
	
}
