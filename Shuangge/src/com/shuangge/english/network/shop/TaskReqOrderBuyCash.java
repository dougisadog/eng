package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.OrderResult;
import com.shuangge.english.entity.server.shop.WXOrderResult;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqOrderBuyCash extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqOrderBuyCash(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		OrderResult result = HttpReqFactory.getServerResultByToken(OrderResult.class, ConfigConstants.SHOP_ORDER_BUY_CASH,
				new HttpReqFactory.ReqParam("orderNo", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setCashOrderResult(result);
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setMoney(result.getRewards());
			return true;
		}
		return false;
	}
	
}
