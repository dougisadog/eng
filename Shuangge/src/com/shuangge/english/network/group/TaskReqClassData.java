package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.ClassResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取选中班级信息
 * @author Jeffrey
 *
 */
public class TaskReqClassData extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqClassData(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ClassResult result = HttpReqFactory.getServerResultByToken(ClassResult.class, ConfigConstants.CLASS_GET_INFO_URL,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentClassNo()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setClassData(result);
			return true;
		}
		return false;
	}
	
}
