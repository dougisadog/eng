package com.shuangge.english.network.secretmsg;

import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoAttentionDataCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqBlacklistCreate extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqBlacklistCreate(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.BLACKLIST_CREATE,
				new HttpReqFactory.ReqParam("attentionNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			DaoAttentionDataCache dao = new DaoAttentionDataCache();
			List<AttentionData> datas = dao.getList(GlobalRes.getInstance().getBeans().getLoginName());
			for (int j = 0; j < datas.size(); j++) {
				if (datas.get(j).getUserNo().longValue() == ((Long) params[0]).longValue()) {
					dao.delete(GlobalRes.getInstance().getBeans().getLoginName(), datas.get(j).getUserNo());
					break;
				}
			}
			return true;
		}
		return false;
	}
	
}
