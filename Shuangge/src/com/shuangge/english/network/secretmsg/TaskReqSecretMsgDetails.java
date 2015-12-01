package com.shuangge.english.network.secretmsg;

import java.util.Collections;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoSecretMsgDataCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.secretmsg.SecretDetailsResult;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDataCache;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.secretmsg.AtySecretMsgList;

public class TaskReqSecretMsgDetails extends BaseTask<Void, Void, Boolean> {
	
	public static final int PAGE_SIZE = 100;
	
	public TaskReqSecretMsgDetails(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		int errorNum = 0;
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		boolean inDetailView = null != beans.getCurrentFriendNo();
		while (true) {
			SecretDetailsResult result = HttpReqFactory.getServerResultByToken(SecretDetailsResult.class, ConfigConstants.SECRET_DETAIL_LIST,
					new HttpReqFactory.ReqParam("secretMsgNo", AtySecretMsgList.msgNoSendToServer));
			if (null != result && result.getCode() == RestResult.C_SUCCESS) {
				DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
				
				Collections.reverse(result.getSecretMsgs());
				for (int i = 0; i < result.getSecretMsgs().size(); i++) {
					SecretMsgDetailData data = result.getSecretMsgs().get(i);
					if (beans.checkSecretUniqueDatas(data.getSecretMsgNo(), data)) {
						continue;
					}
					
					SecretMsgDataCache dataCache = new SecretMsgDataCache();
					data.setStatus(SecretMsgDetailData.STATUS_READ);
					dataCache.setData(data);
					dataCache.setLoginName(GlobalRes.getInstance().getBeans().getLoginName());
					dao.save(dataCache);
					if (beans.getSecretListDatas() != null) {
						for (int j = 0; j < beans.getSecretListDatas().size(); j++) {
							if (data.getFriendNo().longValue() == beans.getSecretListDatas().get(j).getFriendNo().longValue()) {
								beans.getSecretListDatas().remove(j);
								break;
							}
						}
						beans.getSecretListDatas().add(data);
					} 
					
					if (null == AtySecretMsgList.msgNoSendToServer 
							|| AtySecretMsgList.msgNoSendToServer < data.getSecretMsgNo()) {
						AtySecretMsgList.msgNoSendToServer = data.getSecretMsgNo();
					}
					if (inDetailView && 
							beans.getCurrentFriendNo().longValue() == data.getFriendNo().longValue()) {
						beans.getSecretDetailsDatas().add(data);
					}
				}
				if (result.getSecretMsgs().size() < PAGE_SIZE) {
					if (inDetailView) {
						Collections.sort(beans.getSecretDetailsDatas());
						Collections.reverse(beans.getSecretDetailsDatas());
					}
					Collections.sort(beans.getSecretListDatas());
					return true;
				}
				continue;
			}
//			if (errorNum++ > 1)
				return false;
		}
	}
	
}