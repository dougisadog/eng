package com.shuangge.english.network.lesson;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.share.ShareResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

/**
 * 设置初始Level
 * @author Jeffrey 
 * 
 * level 初判断英语水平的等级
 *
 */
public class TaskReqShare extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqShare(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ShareResult result = HttpReqFactory.getServerResultByToken(ShareResult.class, ConfigConstants.SHARE_LESSON_SUCCESS,
				new HttpReqFactory.ReqParam("shareNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setShareResult(result);
			return true;
		}
		return false;
	}
	
}
