package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.GoodsResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqGoodsLesson extends BaseTask<Object, Void, GoodsData> {
	
	public TaskReqGoodsLesson(int tag, CallbackNoticeView<Void, GoodsData> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected GoodsData doInBackground(Object... params) {
		GoodsResult result = HttpReqFactory.getServerResultByToken(GoodsResult.class, ConfigConstants.GOODS_LESSON,
				new HttpReqFactory.ReqParam("clientId", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setPayType(result.getGoodsData().getPayType());
			return result.getGoodsData();
		}
		return null;
	}
	
}
