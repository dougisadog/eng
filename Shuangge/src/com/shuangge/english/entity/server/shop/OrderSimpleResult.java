package com.shuangge.english.entity.server.shop;

import com.shuangge.english.entity.server.RestResult;

/**
 * 订单支付返回信息/订单详情
 * @author Administator
 *
 */
public class OrderSimpleResult extends RestResult {
	
	private OrderData orderData;
	
	private Integer money;
	
	
	private Double rewards = 0D;
	
	public OrderData getOrderData() {
		return orderData;
	}

	public void setOrderData(OrderData orderData) {
		this.orderData = orderData;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Double getRewards() {
		return rewards;
	}

	public void setRewards(Double rewards) {
		this.rewards = rewards;
	}

}
