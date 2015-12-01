package com.shuangge.english.entity.server.user;

import com.shuangge.english.entity.server.RestResult;

public class MyRanklistResult extends RestResult {

	private Integer no;
	private Integer weekNo;
	private Integer score;
	private Integer weekScore;
	private Integer money;
	private Integer keyNum;

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getWeekScore() {
		return weekScore;
	}

	public void setWeekScore(Integer weekScore) {
		this.weekScore = weekScore;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(Integer keyNum) {
		this.keyNum = keyNum;
	}

}
