package com.shuangge.english.entity.server.shop;


public class ShopCartData {
	
	
	private int amount;  //商品数量
	
	private double price;
	
	private GoodsData goodsData;
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public GoodsData getGoodsData() {
		return goodsData;
	}

	public void setGoodsData(GoodsData goodsData) {
		this.goodsData = goodsData;
	}


}
