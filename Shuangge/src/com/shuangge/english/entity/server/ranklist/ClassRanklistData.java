package com.shuangge.english.entity.server.ranklist;

public class ClassRanklistData {

	private Long classNo;
	private String name;
	private String description;
	private int num = 0;
	private String creater;
	private Long createrId;

	private Integer no = 0;
	private String headUrl;
	private Integer score = 0;
	private double weekReward = 0d;

	public Long getClassNo() {
		return classNo;
	}

	public void setClassNo(Long classNo) {
		this.classNo = classNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(Long createrId) {
		this.createrId = createrId;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public double getWeekReward() {
		return weekReward;
	}

	public void setWeekReward(double weekReward) {
		this.weekReward = weekReward;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	
}