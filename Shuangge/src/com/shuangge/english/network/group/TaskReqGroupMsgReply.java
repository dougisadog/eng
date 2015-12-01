package com.shuangge.english.network.group;

import android.text.TextUtils;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.msg.adapter.AdapterClassMsg;

/**
 * 班组消息回复
 * @author Jeffrey
 *
 */
public class TaskReqGroupMsgReply extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqGroupMsgReply(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.CLASS_REPLY_URL,
				new ReqParam("msgNo", params[0]),
				new ReqParam("status", params[1]),
				new ReqParam("message", params[2]),
				new ReqParam("wechatNo", params[3]));
				
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			//同意邀请如果个人微信为空则更新微信号信息
			InfoData infoData = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
			if (null != params[3]) {
				if (((int) params[4]) == AdapterClassMsg.TYPE_GROUP_APPLY && TextUtils.isEmpty(infoData.getWechatNo())) {
					infoData.setWechatNo(params[3].toString());
				}
			}
			
			return true;
		}
		return false;
	}
	
}
