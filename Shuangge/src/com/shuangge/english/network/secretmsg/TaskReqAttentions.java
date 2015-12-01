package com.shuangge.english.network.secretmsg;

import java.util.Collections;

import android.util.SparseArray;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoAttentionDataCache;
import com.shuangge.english.dao.DaoVersionNoCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.entity.server.secretmsg.AttentionDataCache;
import com.shuangge.english.entity.server.secretmsg.AttentionInfoResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.secretmsg.fragment.FragmentSecretAttentions;

public class TaskReqAttentions extends BaseTask<Object, Void, Boolean> {
	
	public static final int PAGE_SIZE = 100;
	
	public TaskReqAttentions(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		DaoAttentionDataCache dao = new DaoAttentionDataCache();
		DaoVersionNoCache localData = new DaoVersionNoCache();
		if (0 == beans.getAttentionDatas().size()) {
			beans.setAttentionDatas(dao.getList(beans.getLoginName()));
			beans.setAttentionUniqueDatas(new SparseArray<AttentionData>());
			for (AttentionData data : beans.getAttentionDatas()) {
				beans.getAttentionUniqueDatas().put(data.getUserNo().intValue(), data);
			}
			//TODO:Jordan 取本地versionNo的值
			FragmentSecretAttentions.versionNo = localData.getVersionNo(beans.getLoginName());
		}
		int errorNum = 0;
		while (true) {
			AttentionInfoResult result = HttpReqFactory.getServerResultByToken(AttentionInfoResult.class, ConfigConstants.SECRET_MY_ATTENTIONS,
				new HttpReqFactory.ReqParam("currVersionNo", FragmentSecretAttentions.versionNo));
			if (null != result && result.getCode() == RestResult.C_SUCCESS) {
				for (AttentionData data : result.getAttentions()) {
					if (null == FragmentSecretAttentions.versionNo || FragmentSecretAttentions.versionNo < data.getCurrVersionNo()) {
						FragmentSecretAttentions.versionNo = data.getCurrVersionNo();
					}
					if (data.getAttention()) {
						dao.update(beans.getLoginName(), new AttentionDataCache(data));
						beans.getAttentionUniqueDatas().put(data.getUserNo().intValue(), data);
					}
					else {
						dao.delete(beans.getLoginName(), data.getUserNo());
						if (null != beans.getAttentionUniqueDatas().get(data.getUserNo().intValue()))
							beans.getAttentionUniqueDatas().remove(data.getUserNo().intValue());
					}
				}
				//TODO:Jordan 保存本地versionNo的值
				localData.updateVersionNo(beans.getLoginName(), FragmentSecretAttentions.versionNo);
				
				if (result.getAttentions().size() < PAGE_SIZE) {
					beans.getAttentionDatas().clear();
					for (int i = 0; i < beans.getAttentionUniqueDatas().size(); i++) {
						beans.getAttentionDatas().add(beans.getAttentionUniqueDatas().valueAt(i));
					}
					Collections.sort(beans.getAttentionDatas());
					return true;
				}
			}
			return false;
		}
	}
	
}
