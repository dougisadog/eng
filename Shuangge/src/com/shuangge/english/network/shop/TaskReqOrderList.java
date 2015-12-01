package com.shuangge.english.network.shop;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.GoodsListResult;
import com.shuangge.english.entity.server.shop.OrderData;
import com.shuangge.english.entity.server.shop.OrderListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.shop.AtyShopList;
import com.shuangge.english.view.shop.AtyShopPurchaseRecords;

public class TaskReqOrderList extends BaseTask<Object, Void, Boolean> {
	
	public static final int PAGE_SIZE = 100;
	
	public TaskReqOrderList(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		CacheBeans beans = GlobalRes.getInstance().getBeans();
//		OrderListResult result = HttpReqFactory.getServerResultByToken(OrderListResult.class, ConfigConstants.SHOP_ORDER_LIST,
//				new HttpReqFactory.ReqParam("state", params[0]),
//				new HttpReqFactory.ReqParam("last", params[1]));
//		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
//			GlobalRes.getInstance().getBeans().setOrderListResult(result);
//			return true;
//		}
//		return false;
		
		while (true) {
			OrderListResult result = HttpReqFactory.getServerResultByToken(OrderListResult.class, ConfigConstants.SHOP_ORDER_LIST,
					new HttpReqFactory.ReqParam("state", params[0]),
					new HttpReqFactory.ReqParam("last", AtyShopPurchaseRecords.last));
 			if (null != result && result.getCode() == RestResult.C_SUCCESS) {
				
				for (OrderData data : result.getOrderDatas()) {
					beans.getOrderDatas().add(data);
					AtyShopPurchaseRecords.last = data.getOrderNo();
				}
				
				if (result.getOrderDatas().size() < PAGE_SIZE) {
					return true;
				}
			}
			return false;
		}
	}
	
}
