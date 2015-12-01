package com.shuangge.english.entity.server.user;

import com.shuangge.english.entity.server.RestResult;

public class OtherInfoResult extends RestResult {

	private OtherInfoData infoData;

	public OtherInfoData getInfoData() {
		return infoData;
	}

	public void setInfoData(OtherInfoData infoData) {
		this.infoData = infoData;
	}

}
