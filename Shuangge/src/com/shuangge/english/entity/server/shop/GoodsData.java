package com.shuangge.english.entity.server.shop;

import java.util.Date;

public class GoodsData {
	
	public static final Integer OFF = 0;
	public static final Integer UP = 1;

	private Integer goodsId;
	private String name;
	private String details;
	private int payType = 0;
	private double price;
	private double payoff;
	private Date overTime;
	private Integer stock;
	private Integer scorePrice;
	private Integer type;
	private int limitCount; //  限购数量
	
	private int takenCount; //  已购买数量
	// private Integer minEPrice;
	// private Integer maxEPrice;

	private String appStoreId;

	private String url;

	private String introduction;

	private String goodsAuthority;
	
	private Integer gift;
	
	private int state;
	
	private Boolean postType;
	
	private Integer isCode; //0无激活码商品类型  1则为有
	private Integer func;
	
	private int cashPayType;
 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}


//	public Integer getMinEPrice() {
//		return minEPrice;
//	}
//
//	public void setMinEPrice(Integer minEPrice) {
//		this.minEPrice = minEPrice;
//	}
//
//	public Integer getMaxEPrice() {
//		return maxEPrice;
//	}
//
//	public void setMaxEPrice(Integer maxEPrice) {
//		this.maxEPrice = maxEPrice;
//	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	

	public Integer getScorePrice() {
		return scorePrice;
	}

	public void setScorePrice(Integer scorePrice) {
		this.scorePrice = scorePrice;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	public double getPayoff() {
		return payoff;
	}

	public void setPayoff(double payoff) {
		this.payoff = payoff;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getGoodsAuthority() {
		return goodsAuthority;
	}

	public void setGoodsAuthority(String goodsAuthority) {
		this.goodsAuthority = goodsAuthority;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public int getTakenCount() {
		return takenCount;
	}

	public void setTakenCount(int takenCount) {
		this.takenCount = takenCount;
	}

	public Integer getGift() {
		return gift;
	}

	public void setGift(Integer gift) {
		this.gift = gift;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getAppStoreId() {
		return appStoreId;
	}

	public void setAppStoreId(String appStoreId) {
		this.appStoreId = appStoreId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Boolean getPostType() {
		return postType;
	}

	public void setPostType(Boolean postType) {
		this.postType = postType;
	}

	public Integer getIsCode() {
		return isCode;
	}

	public void setIsCode(Integer isCode) {
		this.isCode = isCode;
	}

	public Integer getFunc() {
		return func;
	}

	public void setFunc(Integer func) {
		this.func = func;
	}

	public int getCashPayType() {
		return cashPayType;
	}

	public void setCashPayType(int cashPayType) {
		this.cashPayType = cashPayType;
	}

}
