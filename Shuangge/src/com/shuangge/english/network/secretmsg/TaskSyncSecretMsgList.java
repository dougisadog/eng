package com.shuangge.english.network.secretmsg;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

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

public class TaskSyncSecretMsgList extends BaseTask<Void, Void, Boolean> {
	
	public static final int PAGE_SIZE = 100;
	
	public TaskSyncSecretMsgList(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		int errorNum = 0;
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		//本地私信列表数据初始化
		DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
		beans.setSecretListDatas(dao.getMsgList(beans.getLoginName()));
		beans.setSecretUniqueDatas(new ConcurrentHashMap<Long, SecretMsgDetailData>());
		for (SecretMsgDetailData data : beans.getSecretDetailsDatas()) {
			beans.getSecretUniqueDatas().put(data.getSecretMsgNo().longValue(), data);
		}
		Collections.sort(beans.getSecretListDatas());
		//获取本地最后一条记录的发送时间
		AtySecretMsgList.msgNoSendToServer = dao.getMaxMsgNo(beans.getLoginName());
		while (true) {
			SecretDetailsResult result = HttpReqFactory.getServerResultByToken(SecretDetailsResult.class, ConfigConstants.SECRET_DETAIL_LIST,
					new HttpReqFactory.ReqParam("secretMsgNo", AtySecretMsgList.msgNoSendToServer));
			
			if (null != result && result.getCode() == RestResult.C_SUCCESS) {
				Collections.reverse(result.getSecretMsgs());
				for (int i = 0; i < result.getSecretMsgs().size(); i++) {
					SecretMsgDetailData data = result.getSecretMsgs().get(i);
					if (beans.checkSecretUniqueDatas(data.getSecretMsgNo(), data)) {
						continue;
					}
					
					SecretMsgDataCache dataCache = new SecretMsgDataCache();
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
				}
				if (result.getSecretMsgs().size() < PAGE_SIZE) {
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
