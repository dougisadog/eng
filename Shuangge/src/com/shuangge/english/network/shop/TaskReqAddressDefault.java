package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.AddressListResult;
import com.shuangge.english.entity.server.shop.AddressResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqAddressDefault extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqAddressDefault(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		AddressResult result = HttpReqFactory.getServerResultByToken(AddressResult.class, ConfigConstants.SHOP_ADDRESS_SETDEFAULT,
				new HttpReqFactory.ReqParam("addressId", params[0]));
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setDefaultAddress(result.getAddressData());
			return true;
		}
		return false;
	}
	
}
