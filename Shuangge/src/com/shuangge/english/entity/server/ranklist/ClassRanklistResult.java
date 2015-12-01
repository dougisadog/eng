package com.shuangge.english.entity.server.ranklist;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;


/**
 * 
 * @author Jeffrey
 *
 */
public class ClassRanklistResult extends RestResult {

	private List<ClassRanklistData> ranklist = new ArrayList<ClassRanklistData>();
	private Integer myClassNo = 0;
	private Integer myClassScore = 0;

	public List<ClassRanklistData> getRanklist() {
		return ranklist;
	}

	public void setRanklist(List<ClassRanklistData> ranklist) {
		this.ranklist = ranklist;
	}

	public Integer getMyClassNo() {
		return myClassNo;
	}

	public void setMyClassNo(Integer myClassNo) {
		this.myClassNo = myClassNo;
	}

	public Integer getMyClassScore() {
		return myClassScore;
	}

	public void setMyClassScore(Integer myClassScore) {
		this.myClassScore = myClassScore;
	}

}
