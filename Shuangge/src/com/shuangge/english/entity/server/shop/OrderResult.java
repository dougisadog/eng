package com.shuangge.english.entity.server.shop;
import com.shuangge.english.entity.server.RestResult;

public class OrderResult extends RestResult {

	private Integer money;
	private Double rewards = 0D;
	
	private OrderDTO dto;

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

	public OrderDTO getDto() {
		return dto;
	}

	public void setDto(OrderDTO dto) {
		this.dto = dto;
	}
	
}
