package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.AddressListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqAddressList extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqAddressList(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		AddressListResult result = HttpReqFactory.getServerResultByToken(AddressListResult.class, ConfigConstants.SHOP_ADDRESS_LIST);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setAddressResult(result);
			return true;
		}
		return false;
	}
	
}
