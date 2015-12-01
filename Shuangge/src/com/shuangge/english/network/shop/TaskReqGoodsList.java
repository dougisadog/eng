package com.shuangge.english.network.shop;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.GoodsListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.shop.AtyShopList;

public class TaskReqGoodsList extends BaseTask<Object, Void, Boolean> {
	
	public static final int PAGE_SIZE = 100;
	
	public TaskReqGoodsList(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
//		GoodsListResult result = HttpReqFactory.getServerResultByToken(GoodsListResult.class, ConfigConstants.SHOP_GOODS_LIST,
//				new HttpReqFactory.ReqParam("type", params[0]),
//				new HttpReqFactory.ReqParam("paytype", params[1]),
//				new HttpReqFactory.ReqParam("last", params[2]));
//		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
//			GlobalRes.getInstance().getBeans().setGoodsListResult(result);
//			return true;
//		}
		while (true) {
			GoodsListResult result = HttpReqFactory.getServerResultByToken(GoodsListResult.class, ConfigConstants.SHOP_GOODS_LIST,
					new HttpReqFactory.ReqParam("type", params[0]),
					new HttpReqFactory.ReqParam("payType", params[1]),
					new HttpReqFactory.ReqParam("pageNo", 1));
			if (null != result && result.getCode() == RestResult.C_SUCCESS) {
				if ((int) params[1] == 0) {
					for (GoodsData data : result.getGoodsDatas()) {
						beans.getGoodsDatasCash().add(data);
						AtyShopList.last = data.getGoodsId();
					}
				} else {
					for (GoodsData data : result.getGoodsDatas()) {
						beans.getGoodsDatasCredits().add(data);
						AtyShopList.last = data.getGoodsId();
					}
				}
				
				if (result.getGoodsDatas().size() < PAGE_SIZE) {
					return true;
				}
			}
			return false;
		}
		
	}
}
