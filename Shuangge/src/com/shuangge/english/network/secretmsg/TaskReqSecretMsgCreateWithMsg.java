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

public class TaskReqSecretMsgCreateWithMsg extends BaseTask<Object, Void, Integer> {
	
	public static final int PAGE_SIZE = 100;
	
	public TaskReqSecretMsgCreateWithMsg(int tag, CallbackNoticeView<Void, Integer> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Integer doInBackground(Object... params) {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		
		SecretMsgDetailData lastData = (SecretMsgDetailData) params[2];
		
		int errorNum = 0;
		boolean inDetailView = null != beans.getCurrentFriendNo();
		SecretDetailsResult result = null;
		
		//存本地
		DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
		SecretMsgDataCache dataCache = new SecretMsgDataCache();
		lastData.setStatus(SecretMsgDetailData.STATUS_LOADING);
		dataCache.setData(lastData);
		dataCache.setLoginName(GlobalRes.getInstance().getBeans().getLoginName());
		Long id = dao.save(dataCache);
		lastData.setId(id);
		
		int k = 0;
		while (true) {
				result = k == 0
						? HttpReqFactory.getServerResultByToken(SecretDetailsResult.class, ConfigConstants.SECRET_CREATE_MSG,
						new HttpReqFactory.ReqParam("receiverNo", params[0]),
						new HttpReqFactory.ReqParam("content", params[1]),
						new HttpReqFactory.ReqParam("secretMsgNo", AtySecretMsgList.msgNoSendToServer))
						: HttpReqFactory.getServerResultByToken(SecretDetailsResult.class, ConfigConstants.SECRET_DETAIL_LIST,
								new HttpReqFactory.ReqParam("secretMsgNo", AtySecretMsgList.msgNoSendToServer));
			if (null != result && result.getCode() == RestResult.C_SUCCESS) {
				if (++k == 1 && beans.getSecretDetailsDatas().contains(lastData)) {
					beans.getSecretDetailsDatas().remove(lastData);
					dao.delete(id);
				}
				
				Collections.reverse(result.getSecretMsgs());
				for (int i = 0; i < result.getSecretMsgs().size(); i++) {
					SecretMsgDetailData data = result.getSecretMsgs().get(i);
					if (beans.checkSecretUniqueDatas(data.getSecretMsgNo(), data)) {
						continue;
					}
					
					dataCache = new SecretMsgDataCache();
					data.setStatus(SecretMsgDetailData.STATUS_READ);
					dataCache.setData(data);
					dataCache.setLoginName(GlobalRes.getInstance().getBeans().getLoginName());
					data.setId(dao.save(dataCache));
					for (int j = 0; j < beans.getSecretListDatas().size(); j++) {
						if (data.getFriendNo().longValue() == beans.getSecretListDatas().get(j).getFriendNo().longValue()) {
							beans.getSecretListDatas().remove(j);
							break;
						}
					}
					beans.getSecretListDatas().add(data);
					if (null == AtySecretMsgList.msgNoSendToServer 
							|| AtySecretMsgList.msgNoSendToServer < data.getSecretMsgNo()) {
						AtySecretMsgList.msgNoSendToServer = data.getSecretMsgNo();
					}
					if (inDetailView && 
							beans.getCurrentFriendNo().longValue() == data.getFriendNo()) {
						beans.getSecretDetailsDatas().add(data);
					}
				}
				if (result.getSecretMsgs().size() < PAGE_SIZE) {
					if (inDetailView) {
						Collections.sort(beans.getSecretDetailsDatas());
						Collections.reverse(beans.getSecretDetailsDatas());
					}
					Collections.sort(beans.getSecretListDatas());
					return result.getCode();
				}
				continue;
			}
			
//			if (errorNum++ <= 1) {
//				continue;
//			}
			
			if (k == 0 && id != -1) {
				lastData.setStatus(SecretMsgDetailData.STATUS_ERR);
				dataCache.setData(lastData);
				dataCache.setLoginName(GlobalRes.getInstance().getBeans().getLoginName());
				dao.update(id, dataCache);
			}
			
			if (null != result)
				return result.getCode();
			
			return null;
		}
	}
	
}
