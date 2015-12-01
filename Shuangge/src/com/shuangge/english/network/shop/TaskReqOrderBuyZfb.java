package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.PayListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqOrderBuyZfb extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqOrderBuyZfb(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		PayListResult result = HttpReqFactory.getServerResultByToken(PayListResult.class, ConfigConstants.SHOP_ORDER_BUY_ZFB,
				new HttpReqFactory.ReqParam("orderNos", params[0]),
				new HttpReqFactory.ReqParam("errCodes", params[1]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setPayResult(result);
			return true;
		}
		return false;
	}
	
}
