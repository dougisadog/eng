package com.shuangge.english.network.group;

import android.text.TextUtils;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 用户申请加入班级
 * @author Jeffrey
 *
 */
public class TaskReqUserApply extends BaseTask<String, Void, Boolean> {
	
	public TaskReqUserApply(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.CLASS_APPLY_USER_URL,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentClassNo()),
				new ReqParam("message", params[0]),
				new ReqParam("wechatNo",params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			//申请加入时如果个人微信为空则更新微信号信息
			InfoData infoData = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
			if (TextUtils.isEmpty(infoData.getWechatNo())) {
				infoData.setWechatNo(params[1].toString());
			}
			return true;
		}
		return false;
	}
	
}
