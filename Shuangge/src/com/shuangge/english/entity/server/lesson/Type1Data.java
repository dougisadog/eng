package com.shuangge.english.entity.server.lesson;

public class Type1Data {

	private String clientId;
	private Boolean hasAuth = false;
	private Boolean enabled = false;

//	private Boolean display = false;

	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

//	public Boolean getDisplay() {
//		return display;
//	}
//
//	public void setDisplay(Boolean display) {
//		this.display = display;
//	}

}
