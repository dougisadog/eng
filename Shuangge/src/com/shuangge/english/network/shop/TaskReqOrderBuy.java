package com.shuangge.english.network.shop;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqOrderBuy extends BaseTask<Object, Void, OrderSimpleResult> {
	
	public TaskReqOrderBuy(int tag, CallbackNoticeView<Void, OrderSimpleResult> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected OrderSimpleResult doInBackground(Object... params) {
		OrderSimpleResult result = HttpReqFactory.getServerResultByToken(OrderSimpleResult.class, ConfigConstants.SHOP_ORDER_BUY_WX_ORDER_DATA,
				new HttpReqFactory.ReqParam("orderNo", params[0]),
				new HttpReqFactory.ReqParam("paytype", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS)
			return result;
		return null;
	}
	
}
