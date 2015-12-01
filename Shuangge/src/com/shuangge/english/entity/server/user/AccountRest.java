package com.shuangge.english.entity.server.user;

import com.shuangge.english.entity.server.RestResult;

public class AccountRest extends RestResult {

	private String loginName;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}
