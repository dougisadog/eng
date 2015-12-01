package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.GoodsResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqGoodsDetail extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqGoodsDetail(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		GoodsResult result = HttpReqFactory.getServerResultByToken(GoodsResult.class, ConfigConstants.SHOP_GOODS_DETAIL,
				new HttpReqFactory.ReqParam("goodsId", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setGoodsResult(result);
			return true;
		}
		return false;
	}
	
}
