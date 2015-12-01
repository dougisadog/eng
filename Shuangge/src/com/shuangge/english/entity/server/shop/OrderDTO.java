package com.shuangge.english.entity.server.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shuangge.english.config.ConfigConstants.GoodsType;

public class OrderDTO {

	private Long id;
	private String orderNo;
	private String tag;
	private String address;
	private String goodsName;
	private String iosGoodsName;
	private String goodsPic;
	private String iosGoodsPic;
	private String expressNo;
	private String expressor;
	private String exchangeCode;
	private String appStoreId;
	private Long creatorId;
	private Long goodsId;
	private Long giverId;
	private String recipientPhone;
	private String giverName;
	private String giverHeadUrl;
	private Integer func;
	private int state;
	private Integer payChannel;
	private int amount; // 已购买数量
	private double cost;
	
	private List<String> addresses = new ArrayList<String>();
	
	private GoodsType type;
	
	/**
	 * 支持付款方式 积分
	 */
	private boolean supportedScore;
	/**
	 * 支持付款方式 奖学金
	 */
	private boolean supportedDeposit;
	/**
	 * 支持付款方式 现金
	 */
	private boolean supportedCash = true;

	private Date editTime;
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public GoodsType getType() {
		return type;
	}

	public void setType(GoodsType type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getIosGoodsName() {
		return iosGoodsName;
	}

	public void setIosGoodsName(String iosGoodsName) {
		this.iosGoodsName = iosGoodsName;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	public String getIosGoodsPic() {
		return iosGoodsPic;
	}

	public void setIosGoodsPic(String iosGoodsPic) {
		this.iosGoodsPic = iosGoodsPic;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getExpressor() {
		return expressor;
	}

	public void setExpressor(String expressor) {
		this.expressor = expressor;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getGiverId() {
		return giverId;
	}

	public void setGiverId(Long giverId) {
		this.giverId = giverId;
	}

	public String getRecipientPhone() {
		return recipientPhone;
	}

	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}

	public String getGiverName() {
		return giverName;
	}

	public void setGiverName(String giverName) {
		this.giverName = giverName;
	}

	public String getGiverHeadUrl() {
		return giverHeadUrl;
	}

	public void setGiverHeadUrl(String giverHeadUrl) {
		this.giverHeadUrl = giverHeadUrl;
	}

	public Integer getFunc() {
		return func;
	}

	public void setFunc(Integer func) {
		this.func = func;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Integer getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getAppStoreId() {
		return appStoreId;
	}

	public void setAppStoreId(String appStoreId) {
		this.appStoreId = appStoreId;
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public boolean isSupportedScore() {
		return supportedScore;
	}

	public void setSupportedScore(boolean supportedScore) {
		this.supportedScore = supportedScore;
	}

	public boolean isSupportedDeposit() {
		return supportedDeposit;
	}

	public void setSupportedDeposit(boolean supportedDeposit) {
		this.supportedDeposit = supportedDeposit;
	}

	public boolean isSupportedCash() {
		return supportedCash;
	}

	public void setSupportedCash(boolean supportedCash) {
		this.supportedCash = supportedCash;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
