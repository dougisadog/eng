package com.shuangge.english.network.secretmsg;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoSecretMsgDataCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqSecretMsgSetStatus extends BaseTask<Long, Void, Boolean> {
	
	public static final int PAGE_SIZE = 100;
	
	public TaskReqSecretMsgSetStatus(int tag, CallbackNoticeView<Void, Boolean> callback, Long... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Long... params) {
		Long friendNo = GlobalRes.getInstance().getBeans().getCurrentFriendNo();
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.SECRET_SET_STATUS,
				new HttpReqFactory.ReqParam("friendNo", friendNo),
				new HttpReqFactory.ReqParam("secretMsgNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
			dao.updateStatusByFriendId(GlobalRes.getInstance().getBeans().getLoginName(), friendNo);
			return true;
		}
		return false;
	}
	
}
