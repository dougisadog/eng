package com.shuangge.english.entity.server.secretmsg;

import com.shuangge.english.entity.server.RestResult;

public class SecretMsgResult extends RestResult {

	private SecretMsgData secretMsgData;

	public SecretMsgData getSecretMsgData() {
		return secretMsgData;
	}

	public void setSecretMsgData(SecretMsgData secretMsgData) {
		this.secretMsgData = secretMsgData;
	}
	
}
