package com.shuangge.english.network.secretmsg;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoSecretMsgDataCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqSecretMsgDeleteDetail extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqSecretMsgDeleteDetail(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		int position  = (int) params[0];
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		SecretMsgDetailData data = (SecretMsgDetailData) params[1];
		SecretMsgDetailData prevData = (SecretMsgDetailData) params[3];
		
		DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
		
		//删除列表信息
		if ((boolean) params[2]) {
			for (int i = 0; i < beans.getSecretListDatas().size(); i++) {
				if (beans.getSecretListDatas().get(i).getFriendNo().longValue() != data.getFriendNo()) {
					continue;
				}
				if (null == prevData)
					prevData = dao.getLastMsg(beans.getLoginName(), data.getFriendNo());
				if (null == prevData) {
					beans.getSecretListDatas().remove(i);
					break;
				}
				beans.getSecretListDatas().set(i, prevData);
				break;
			}
		}
		
		//删除详情信息
		beans.getSecretDetailsDatas().remove(position);
		
		dao.delete(data.getId());
		
		if (null == params[0])
			return true;
		
		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.SECRET_DELETE_DETAIL,
				new HttpReqFactory.ReqParam("secretMsgNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			return true;
		}
		return false;
	}
	
}
