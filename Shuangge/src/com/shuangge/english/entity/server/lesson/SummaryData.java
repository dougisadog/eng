package com.shuangge.english.entity.server.lesson;

public class SummaryData {

	private Integer rightRate;
	private Integer heartNum;
	private Integer rightNum;
	private Integer wrongNum;
	
	//基础
	private Integer baseScore;
	//额外加成
	private Integer extraScore = 0;
	//首次过关
	private Integer fisrtFinishedScore = 0;
	//首次完美
	private Integer fisrtPerfectScore = 0;
	
	//下一课
	private String nextClientId;
	private String nextName;
	
	public Integer getRightRate() {
		return rightRate;
	}
	
	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}
	
	public Integer getHeartNum() {
		return heartNum;
	}
	
	public void setHeartNum(Integer heartNum) {
		this.heartNum = heartNum;
	}
	
	public Integer getRightNum() {
		return rightNum;
	}
	
	public void setRightNum(Integer rightNum) {
		this.rightNum = rightNum;
	}
	
	public Integer getWrongNum() {
		return wrongNum;
	}
	
	public void setWrongNum(Integer wrongNum) {
		this.wrongNum = wrongNum;
	}

	public Integer getBaseScore() {
		return baseScore;
	}

	public void setBaseScore(Integer baseScore) {
		this.baseScore = baseScore;
	}

	public Integer getFisrtFinishedScore() {
		return fisrtFinishedScore;
	}

	public void setFisrtFinishedScore(Integer fisrtFinishedScore) {
		this.fisrtFinishedScore = fisrtFinishedScore;
	}

	public Integer getFisrtPerfectScore() {
		return fisrtPerfectScore;
	}

	public void setFisrtPerfectScore(Integer fisrtPerfectScore) {
		this.fisrtPerfectScore = fisrtPerfectScore;
	}

	public Integer getExtraScore() {
		return extraScore;
	}

	public void setExtraScore(Integer extraScore) {
		this.extraScore = extraScore;
	}

	public String getNextClientId() {
		return nextClientId;
	}

	public void setNextClientId(String nextClientId) {
		this.nextClientId = nextClientId;
	}
	
	public String getNextName() {
		return nextName;
	}

	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	public Integer getAllScore() {
		return baseScore + extraScore + fisrtFinishedScore + fisrtPerfectScore;
	}
	
}
