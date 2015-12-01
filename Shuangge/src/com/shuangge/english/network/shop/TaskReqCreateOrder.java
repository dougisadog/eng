package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqCreateOrder extends BaseTask<Object, Void, OrderSimpleResult> {
	
	public TaskReqCreateOrder(int tag, CallbackNoticeView<Void, OrderSimpleResult> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected OrderSimpleResult doInBackground(Object... params) {
		OrderSimpleResult result = HttpReqFactory.getServerResultByToken(OrderSimpleResult.class, ConfigConstants.SHOP_ORDER_CREATE,
				new HttpReqFactory.ReqParam("amount", params[0]),
				new HttpReqFactory.ReqParam("goodsId", params[1]),
				new HttpReqFactory.ReqParam("addressId", params[2]),
				new HttpReqFactory.ReqParam("payType", params[3]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setMoney(result.getRewards());
			return result;
		}
		return null;
	}
	
}
