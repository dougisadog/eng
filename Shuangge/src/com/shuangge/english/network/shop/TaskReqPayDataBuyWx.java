package com.shuangge.english.network.shop;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.shop.WXOrderResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.service.BaseTask;

/**
 * 从 [公司服务器] 请求 [微信支付数据] ,是继 [订单] 之后又一次请求公司服务器
 * 		[订单数据]和[微信支付数据]都分别请求一次服务器,原因:订单列表涉及到如果不支付就生成未支付订单,还有其他的逻辑,但会拖慢服务器
 * 
 * @author TOVEY
 *
 */
public class TaskReqPayDataBuyWx extends BaseTask<Object, Void, Boolean> {

	public TaskReqPayDataBuyWx(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		WXOrderResult result = HttpReqFactory.getServerResultByToken(WXOrderResult.class, ConfigConstants.WX_PAY_DATA,
				new HttpReqFactory.ReqParam("orderNo", params[0])); //订单号
		
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setOrderResult(result);
			return true;
		}
		return false;
	}
}
