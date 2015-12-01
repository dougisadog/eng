package com.shuangge.english.entity.server.lesson;

import java.util.ArrayList;
import java.util.List;

public class UserScoreData {

	private int score = 0;
	private int weekScore = 0;
	private List<Integer> classWeekScores = new ArrayList<Integer>();
	private int schoolWeekScore = 0;
	private int money = 0;
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getWeekScore() {
		return weekScore;
	}
	
	public void setWeekScore(int weekScore) {
		this.weekScore = weekScore;
	}
	
	public List<Integer> getClassWeekScores() {
		return classWeekScores;
	}
	
	public void setClassWeekScores(List<Integer> classWeekScores) {
		this.classWeekScores = classWeekScores;
	}
	
	public int getSchoolWeekScore() {
		return schoolWeekScore;
	}
	
	public void setSchoolWeekScore(int schoolWeekScore) {
		this.schoolWeekScore = schoolWeekScore;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
}
