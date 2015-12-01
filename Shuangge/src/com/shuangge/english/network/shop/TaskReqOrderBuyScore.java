package com.shuangge.english.network.shop;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqOrderBuyScore extends BaseTask<Object, Void, OrderSimpleResult> {
	
	public TaskReqOrderBuyScore(int tag, CallbackNoticeView<Void, OrderSimpleResult> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected OrderSimpleResult doInBackground(Object... params) {
		OrderSimpleResult result = HttpReqFactory.getServerResultByToken(OrderSimpleResult.class, ConfigConstants.SHOP_ORDER_BUY_SCORE,
				new HttpReqFactory.ReqParam("orderNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS)
			return result;
		return null;
	}
	
}
