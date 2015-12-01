package com.shuangge.english.network.lesson;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.UnlockResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 设置初始Level
 * @author Jeffrey 
 * 
 * level 初判断英语水平的等级
 *
 */
public class TaskReqSetLevel extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqSetLevel(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		UnlockResult result = HttpReqFactory.getServerResultByToken(UnlockResult.class, ConfigConstants.SET_LEVEL_URL,
				new ReqParam("level", GlobalReqDatas.getInstance().getRequestInitLevel()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
