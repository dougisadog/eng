package com.shuangge.english.entity.server.secretmsg;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class SecretDetailsResult extends RestResult {

//	private int pageCount = 0;
//	
//	private List<SecretMsgData> secretMsgs = new ArrayList<SecretMsgData>();
//
//	public int getPageCount() {
//		return pageCount;
//	}
//
//	public void setPageCount(int pageCount) {
//		this.pageCount = pageCount;
//	}
//
//	public List<SecretMsgData> getSecretMsgs() {
//		return secretMsgs;
//	}
//
//	public void setSecretMsgs(List<SecretMsgData> secretMsgs) {
//		this.secretMsgs = secretMsgs;
//	}
	private List<SecretMsgDetailData> secretMsgs = new ArrayList<SecretMsgDetailData>();

	public List<SecretMsgDetailData> getSecretMsgs() {
		return secretMsgs;
	}

	public void setSecretMsgs(List<SecretMsgDetailData> secretMsgs) {
		this.secretMsgs = secretMsgs;
	}

}
