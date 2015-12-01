package com.shuangge.english.entity.server.read;

import com.shuangge.english.entity.server.RestResult;

public class ReadInitResult extends RestResult{
	
	private Long readNo; //最近的文章
	//TODO: 已学单词数、阅读文章数 
	private Integer learnWordNum;
	private Integer wordNum;
	private Integer easyNum;
	private Integer normalNum;
	private Integer hardNum;
	private Integer learnEasyNum;
	private Integer learnNormalNum;
	private Integer learnHardNum;
	
	public static Long DEFAULT_READNO = -1L;
	
	public Integer getLearnWordNum() {
		return learnWordNum;
	}

	public void setLearnWordNum(Integer learnWordNum) {
		this.learnWordNum = learnWordNum;
	}

	public Integer getEasyNum() {
		return easyNum;
	}

	public void setEasyNum(Integer easyNum) {
		this.easyNum = easyNum;
	}

	public Integer getNormalNum() {
		return normalNum;
	}

	public void setNormalNum(Integer normalNum) {
		this.normalNum = normalNum;
	}

	public Integer getHardNum() {
		return hardNum;
	}

	public void setHardNum(Integer hardNum) {
		this.hardNum = hardNum;
	}

	public Integer getLearnEasyNum() {
		return learnEasyNum;
	}

	public void setLearnEasyNum(Integer learnEasyNum) {
		this.learnEasyNum = learnEasyNum;
	}

	public Integer getLearnNormalNum() {
		return learnNormalNum;
	}

	public void setLearnNormalNum(Integer learnNormalNum) {
		this.learnNormalNum = learnNormalNum;
	}

	public Integer getLearnHardNum() {
		return learnHardNum;
	}

	public void setLearnHardNum(Integer learnHardNum) {
		this.learnHardNum = learnHardNum;
	}

	public Integer getWordNum() {
		return wordNum;
	}

	public void setWordNum(Integer wordNum) {
		this.wordNum = wordNum;
	}

	public Long getReadNo() {
		return readNo;
	}

	public void setReadNo(Long readNo) {
		this.readNo = readNo;
	}


}
