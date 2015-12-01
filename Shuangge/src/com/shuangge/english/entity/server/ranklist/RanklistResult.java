package com.shuangge.english.entity.server.ranklist;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class RanklistResult extends RestResult {

	private int myNo = 0;
	private int myScore = 0;
	private List<RanklistData> ranklist = new ArrayList<RanklistData>();

	public List<RanklistData> getRanklist() {
		return ranklist;
	}

	public int getMyNo() {
		return myNo;
	}

	public void setMyNo(int myNo) {
		this.myNo = myNo;
	}

	public int getMyScore() {
		return myScore;
	}

	public void setMyScore(int myScore) {
		this.myScore = myScore;
	}

}
