package com.shuangge.english.entity.server.lesson;

import java.util.Date;

public class Type5Data {

	private String clientId;
	private Integer score;
	private Integer rightNum;
	private Integer wrongNum;
	private Integer rightRate;
	private Integer currentScore;
	private Integer currentRightNum;
	private Integer currentWrongNum;
	private Integer currentRightRate;
	private Integer heartNum;
	private Integer frequency;
	private String type6ClientId;
	private Date createTime;
	private Boolean isFinished = false;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getHeartNum() {
		return heartNum;
	}

	public void setHeartNum(Integer heartNum) {
		this.heartNum = heartNum;
	}

	public String getType6ClientId() {
		return type6ClientId;
	}

	public void setType6ClientId(String type6ClientId) {
		this.type6ClientId = type6ClientId;
	}

	public Boolean getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(Integer currentScore) {
		this.currentScore = currentScore;
	}

	public Integer getCurrentRightNum() {
		return currentRightNum;
	}

	public void setCurrentRightNum(Integer currentRightNum) {
		this.currentRightNum = currentRightNum;
	}

	public Integer getCurrentWrongNum() {
		return currentWrongNum;
	}

	public void setCurrentWrongNum(Integer currentWrongNum) {
		this.currentWrongNum = currentWrongNum;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public Integer getCurrentRightRate() {
		return currentRightRate;
	}

	public void setCurrentRightRate(Integer currentRightRate) {
		this.currentRightRate = currentRightRate;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

}
