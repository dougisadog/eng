package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 踢人
 * @author Jeffrey
 *
 */
public class TaskReqKill extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqKill(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.CLASS_KICK_URL,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentMyClassNo()),
				new ReqParam("kickAccount", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0).setNum(GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0).getNum() - 1);
			return true;
		}
		return false;
	}
	
}
