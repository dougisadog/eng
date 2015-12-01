package com.shuangge.english.entity.server.group;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class LastWeekDedicateResult extends RestResult {
	
	private Integer myNo;
	private Integer myScore;
	private List<LastWeekDedicateData> datas = new ArrayList<LastWeekDedicateData>();

	public Integer getMyNo() {
		return myNo;
	}

	public void setMyNo(Integer myNo) {
		this.myNo = myNo;
	}

	public Integer getMyScore() {
		return myScore;
	}

	public void setMyScore(Integer myScore) {
		this.myScore = myScore;
	}

	public List<LastWeekDedicateData> getDatas() {
		return datas;
	}

	public void setDatas(List<LastWeekDedicateData> datas) {
		this.datas = datas;
	}

}