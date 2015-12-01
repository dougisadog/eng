package com.shuangge.english.entity.server.group;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.OtherInfoData;

public class RandomResult extends RestResult {

	private List<OtherInfoData> otherInfoDatas = new ArrayList<OtherInfoData>();

	public List<OtherInfoData> getOtherInfoDatas() {
		return otherInfoDatas;
	}

}