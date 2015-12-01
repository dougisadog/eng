package com.shuangge.english.network.rewards;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqHelper;
import com.shuangge.english.support.service.BaseTask;

/**
 * 请求奖金选择的内容 部分
 * @author doug
 *
 */
public class TaskReqRewardsDetail extends BaseTask<Void, Void, String> {

	private String result;

	public TaskReqRewardsDetail(int tag,
			CallbackNoticeView<Void, String> callback, Void... params) {
		super(tag, callback, params);
	}

	@Override
	protected String doInBackground(Void... params) {
		//url待修改
		String url = ConfigConstants.RES_WITHDRAWALS_TIP_URL;
		//发送请求
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
