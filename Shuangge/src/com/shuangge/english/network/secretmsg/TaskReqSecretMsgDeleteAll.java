package com.shuangge.english.network.secretmsg;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoSecretMsgDataCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqSecretMsgDeleteAll extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqSecretMsgDeleteAll(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		//删除内存中数据
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		for (int i = 0; i < beans.getSecretListDatas().size(); i++) {
			if (((Long) params[0]).longValue() == beans.getSecretListDatas().get(i).getFriendNo().longValue()) {
				beans.getSecretListDatas().remove(i);
				break;
			}
		}
		//删除sdcard中数据
		DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
		dao.deleteAll(GlobalRes.getInstance().getBeans().getLoginName(), Integer.valueOf(GlobalRes.getInstance().getBeans().getCurrentFriendNo().toString()));
		
		//删除服务器中数据
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.SECRET_DELETE_ALL,
				new HttpReqFactory.ReqParam("friendNo", beans.getCurrentFriendNo()));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
