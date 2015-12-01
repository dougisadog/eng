package com.shuangge.english.entity.server.lesson;

public class LessonData46 {
	
	private String clientId;
	private String name;
	private String iconUrl1;
	private String iconUrl2;
	private String iconUrl3;
	private String iconUrl4;
	private Double sortNo;
	private boolean isFinished = false;

	private Boolean hasAuth = false;
	private Boolean enabled = false;
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUrl1() {
		return iconUrl1;
	}

	public void setIconUrl1(String iconUrl1) {
		this.iconUrl1 = iconUrl1;
	}

	public String getIconUrl2() {
		return iconUrl2;
	}

	public void setIconUrl2(String iconUrl2) {
		this.iconUrl2 = iconUrl2;
	}

	public String getIconUrl3() {
		return iconUrl3;
	}

	public void setIconUrl3(String iconUrl3) {
		this.iconUrl3 = iconUrl3;
	}

	public Double getSortNo() {
		return sortNo;
	}

	public void setSortNo(Double sortNo) {
		this.sortNo = sortNo;
	}

	public String getIconUrl4() {
		return iconUrl4;
	}

	public void setIconUrl4(String iconUrl4) {
		this.iconUrl4 = iconUrl4;
	}

	public Boolean getHasAuth() {
		return hasAuth;
	}

	public void setHasAuth(Boolean hasAuth) {
		this.hasAuth = hasAuth;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}


}
