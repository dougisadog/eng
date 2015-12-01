package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.AddressListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqAddressNew extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqAddressNew(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		AddressListResult result = HttpReqFactory.getServerResultByToken(AddressListResult.class, ConfigConstants.SHOP_ADDRESS_NEW,
				new HttpReqFactory.ReqParam("addressId", params[0]),
				new HttpReqFactory.ReqParam("location", params[1]),
				new HttpReqFactory.ReqParam("detailed", params[2]),
				new HttpReqFactory.ReqParam("zipCode", params[3]),
				new HttpReqFactory.ReqParam("recipient", params[4]),
				new HttpReqFactory.ReqParam("phone", params[5]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setAddressResult(result);
			return true;
		}
		return false;
	}
	
}
