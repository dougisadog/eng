package com.shuangge.english.entity.server.user;

/**
 * 
 * @author Jeffrey
 *
 */
public class HonorData {

	private String title;
	private Integer score;
	private int level = 1;
	private String nextTitle;
	private Integer nextScore;
	private int nextLevel = 1;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNextTitle() {
		return nextTitle;
	}

	public void setNextTitle(String nextTitle) {
		this.nextTitle = nextTitle;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getNextScore() {
		return nextScore;
	}

	public void setNextScore(Integer nextScore) {
		this.nextScore = nextScore;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(int nextLevel) {
		this.nextLevel = nextLevel;
	}

}
