package com.shuangge.english.entity.server.user;

public class RewardsData {

	public static final int NO_AUTH = 0;
	public static final int HAVE_AUTH = 1;
	public static final int RECEIVED = 2;

	private Integer auth = 0;
	private Integer classRewards = 0;
	private Integer classRanking = 0;

	private Integer personRanking = 0;
	private Double personRewards = 0D;


	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

	public Integer getClassRewards() {
		return classRewards;
	}

	public void setClassRewards(Integer classRewards) {
		this.classRewards = classRewards;
	}

	public Integer getPersonRanking() {
		return personRanking;
	}

	public void setPersonRanking(Integer personRanking) {
		this.personRanking = personRanking;
	}

	public Double getPersonRewards() {
		return personRewards;
	}

	public void setPersonRewards(Double personRewards) {
		this.personRewards = personRewards;
	}

	public Integer getClassRanking() {
		return classRanking;
	}

	public void setClassRanking(Integer classRanking) {
		this.classRanking = classRanking;
	}

}