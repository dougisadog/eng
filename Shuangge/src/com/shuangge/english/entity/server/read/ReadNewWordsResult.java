package com.shuangge.english.entity.server.read;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class ReadNewWordsResult extends RestResult {
	
	private Integer pageNo = 1;

	private List<UserNewWordsData> datas = new ArrayList<UserNewWordsData>();
	
	private List<UserNewWordsData> cacheDatas = new ArrayList<UserNewWordsData>();

	public List<UserNewWordsData> getDatas() {
		return datas;
	}

	public void setDatas(List<UserNewWordsData> datas) {
		this.datas = datas;
	}

	public List<UserNewWordsData> getCacheDatas() {
		return cacheDatas;
	}

	public void setCacheDatas(List<UserNewWordsData> cacheDatas) {
		this.cacheDatas = cacheDatas;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}

