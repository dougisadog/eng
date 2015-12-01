package com.shuangge.english.entity.server.applist;

public class AppData {

	private String name;
	private String headUrl;
	private String signature;
	private Integer status = 0;
	private Integer currVersionNo = 0;
	private String appUrl;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getCurrVersionNo() {
		return currVersionNo;
	}

	public void setCurrVersionNo(Integer currVersionNo) {
		this.currVersionNo = currVersionNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}


}

