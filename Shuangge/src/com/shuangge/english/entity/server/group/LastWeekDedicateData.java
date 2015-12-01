package com.shuangge.english.entity.server.group;

import java.util.Date;

import com.shuangge.english.entity.server.user.HonorData;

public class LastWeekDedicateData {

	private int authLevel;

	private Long userNo;
	private String userType;
	private String headUrl;
	private String name;

	private String signature;
	private Integer sex;
	private Integer age;
	private Date birthday;

	private Integer no;
	private Integer score;
	private Integer weekScore;
	private Integer lastWeekScore;

	private Integer classWeekScore;

	private HonorData honorData;

	private Date lastRequestTime;
	private Date joinDate;

	public int getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(int authLevel) {
		this.authLevel = authLevel;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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

	public Integer getWeekScore() {
		return weekScore;
	}

	public void setWeekScore(Integer weekScore) {
		this.weekScore = weekScore;
	}

	public Integer getLastWeekScore() {
		return lastWeekScore;
	}

	public void setLastWeekScore(Integer lastWeekScore) {
		this.lastWeekScore = lastWeekScore;
	}

	public Integer getClassWeekScore() {
		return classWeekScore;
	}

	public void setClassWeekScore(Integer classWeekScore) {
		this.classWeekScore = classWeekScore;
	}

	public HonorData getHonorData() {
		return honorData;
	}

	public void setHonorData(HonorData honorData) {
		this.honorData = honorData;
	}

	public Date getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(Date lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
}
