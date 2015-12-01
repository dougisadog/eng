package com.shuangge.english.task;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqHelper;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqRewardsTip extends BaseTask<Void, Void, String> {

	private String result;

	public TaskReqRewardsTip(int tag,
			CallbackNoticeView<Void, String> callback, Void... params) {
		super(tag, callback, params);
	}

	@Override
	protected String doInBackground(Void... params) {
		String url = ConfigConstants.RES_REWARDS_CLASS_MEMBER_TIP_URL;
		if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() == ClassData.MANAGER) {
			url = ConfigConstants.RES_REWARDS_ClASS_MANAGER_TIP_URL;
		}

		HttpReqFactory.createGetReq(new HttpReqHelper.HttpReqListener() {

			@Override
			public void errorHandler(Exception e, String errMsg) {
			}

			@Override
			public void completeHandler(String html) {
				result = html;
			}

		}, url);
		return result;
	}

}
