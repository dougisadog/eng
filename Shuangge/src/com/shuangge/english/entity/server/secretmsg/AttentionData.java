package com.shuangge.english.entity.server.secretmsg;

public class AttentionData implements Comparable<AttentionData> {

	private Long userNo;
	private String name;
	private String headUrl;
	private String signature;
	private Integer sex;
	private Integer age;
	private Integer level;
	private Integer currVersionNo = 0;
	
	private Boolean attention = false;
	private Boolean blackList = false;

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getCurrVersionNo() {
		return currVersionNo;
	}

	public void setCurrVersionNo(Integer currVersionNo) {
		this.currVersionNo = currVersionNo;
	}

	public Boolean getAttention() {
		return attention;
	}

	public void setAttention(Boolean attention) {
		this.attention = attention;
	}

	public Boolean getBlackList() {
		return blackList;
	}

	public void setBlackList(Boolean blackList) {
		this.blackList = blackList;
	}

	@Override
	public int compareTo(AttentionData another) {
		if (name.compareTo(another.getName()) > 0) 
			return -1;
		return 1;
	}


}

