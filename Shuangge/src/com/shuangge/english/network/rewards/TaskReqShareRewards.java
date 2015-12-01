package com.shuangge.english.network.rewards;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取选中班级信息
 * @author Jeffrey
 *
 */
public class TaskReqShareRewards extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqShareRewards(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		MyGroupListResult data = GlobalRes.getInstance().getBeans().getMyGroupDatas();
		if (null == data || null == data.getClassInfos() || data.getClassInfos().size() == 0) {
			return false;
		}
		HttpReqFactory.getServerResultByToken(null, ConfigConstants.REWARDS_GET, new ReqParam("classNo", data.getClassInfos().get(0).getClassNo()));
		return true;
	}
	
}
