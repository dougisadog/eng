package com.shuangge.english.entity.server.read;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class ReadListResult extends RestResult {

	private List<ReadListData> datas = new ArrayList<ReadListData>();

	public List<ReadListData> getDatas() {
		return datas;
	}

	public void setDatas(List<ReadListData> datas) {
		this.datas = datas;
	}
	
}
