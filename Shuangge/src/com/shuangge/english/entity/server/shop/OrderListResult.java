package com.shuangge.english.entity.server.shop;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;


/**
 * 订单支付返回信息/订单详情
 * @author Administator
 *
 */
public class OrderListResult extends RestResult {
	
	private List<OrderData> orderDatas = new ArrayList<OrderData>();

	public List<OrderData> getOrderDatas() {
		return orderDatas;
	}

	public void setOrderDatas(List<OrderData> orderDatas) {
		this.orderDatas = orderDatas;
	}



}
