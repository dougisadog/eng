package com.shuangge.english.network.read;

import java.util.List;

import android.text.TextUtils;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.read.ReadNewWordsResult;
import com.shuangge.english.entity.server.read.UserNewWordsData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqReadWordList extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqReadWordList(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ReadNewWordsResult result = HttpReqFactory.getServerResultByToken(ReadNewWordsResult.class, ConfigConstants.READ_WORD_LIST_URL,
				new HttpReqFactory.ReqParam("pageNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS && result.getDatas().size() > 0) {
			setUrls(result.getDatas());
			result.setPageNo((int)params[0]);
			GlobalRes.getInstance().getBeans().setReadNewWordsResult(result);
			//空数据填填充用
			return true;
		}
		return false;
	}
	
	private void setUrls(List<UserNewWordsData> UserNewWordsDatas) {
		for (int i = 0; i < UserNewWordsDatas.size(); i++) {
			UserNewWordsData unwd = UserNewWordsDatas.get(i);
			if (!TextUtils.isEmpty(unwd.getImgUrl()))
				unwd.setImgUrl(unwd.getImgUrl().replace(" ", "%20"));
			else
				unwd.setImgUrl("http://112.124.9.127:8081/res/read/pic/people1_20150624082004_7.jpg");
			if (!TextUtils.isEmpty(unwd.getSoundUrl()))
				unwd.setSoundUrl(unwd.getSoundUrl().replace(" ", "%20"));
			else
				unwd.setSoundUrl("http://112.124.9.127:8081/res/read/sound/us_20150624154711_726.mp3");
		}
	}
	
}
