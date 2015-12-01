package com.shuangge.english.entity.server.user;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class SearchUserResult extends RestResult {

	private List<OtherInfoData> infoData = new ArrayList<OtherInfoData>();

	public List<OtherInfoData> getInfoData() {
		return infoData;
	}

	public void setInfoData(List<OtherInfoData> infoData) {
		this.infoData = infoData;
	}
	
}
