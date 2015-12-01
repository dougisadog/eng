package com.shuangge.english.network.user;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.OtherInfoResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取我的班级信息
 * @author Jeffrey
 *
 */
public class TaskReqOtherDataInfo extends BaseTask<Long, Void, Boolean> {
	
	public TaskReqOtherDataInfo(int tag, CallbackNoticeView<Void, Boolean> callback, Long... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Long... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		OtherInfoResult result = HttpReqFactory.getServerResultByToken(OtherInfoResult.class, ConfigConstants.USER_BROWSER_URL,
				new HttpReqFactory.ReqParam("userNo", GlobalRes.getInstance().getBeans().getCurrentUserNo()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setOtherInfoData(result);
			return true;
		}
		return false;
	}
	
}
