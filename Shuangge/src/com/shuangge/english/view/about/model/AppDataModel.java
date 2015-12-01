package com.shuangge.english.view.about.model;

public class AppDataModel {
	private String name;
	private String detail;
	private String imgUrl;
	private String appUrl;
	
	public AppDataModel() {
		super();
	}

	public AppDataModel(String name, String detail, String imgUrl, String appUrl, Integer status) {
		super();
		this.name = name;
		this.detail = detail;
		this.imgUrl = imgUrl;
		this.appUrl = appUrl;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", detail=" + detail + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

}
