package com.shuangge.english.entity.server.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OtherInfoData {

	private Integer no;
	private String name;
	private String progress;
	private String honor;
	private Boolean attention = false;
	private Boolean blackList = false;
	private String userType;
	private String headUrl;
	private String phone;
	private String email;
	private Integer sex;
	private Integer age;
	private String address;
	
	private Date birthday;
	private String signature;
	private String emotion;
	private String occupation;
	private String income;
	private String location;
	private String interest;
	private String wechatNo;
	
	private Integer weekNo;
	private Integer score = 0;
	private Integer weekScore = 0;
	private Integer lastWeekScore = 0;
	
	private Integer level;
	private Long userNo;
	
	private List<Long> photoNos = new ArrayList<Long>();
	private List<String> photoUrls = new ArrayList<String>();
	private List<Integer> photoSortNos = new ArrayList<Integer>();

	private List<Long> classNos = new ArrayList<Long>();
	private List<String> classNames = new ArrayList<String>();
	
	private boolean repeatInvitation = false;
	
	public String getWechatNo() {
		return wechatNo;
	}

	public void setWechatNo(String wechatNo) {
		this.wechatNo = wechatNo;
	}

	
	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getHonor() {
		return honor;
	}

	public void setHonor(String honor) {
		this.honor = honor;
	}

	public Boolean getAttention() {
		return attention;
	}

	public void setAttention(Boolean attention) {
		this.attention = attention;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public Integer getWeekScore() {
		return weekScore;
	}

	public void setWeekScore(Integer weekScore) {
		this.weekScore = weekScore;
	}

	public List<Long> getPhotoNos() {
		return photoNos;
	}

	public void setPhotoNos(List<Long> photoNos) {
		this.photoNos = photoNos;
	}

	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	public List<Long> getClassNos() {
		return classNos;
	}

	public void setClassNos(List<Long> classNos) {
		this.classNos = classNos;
	}

	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}

	public boolean isRepeatInvitation() {
		return repeatInvitation;
	}

	public void setRepeatInvitation(boolean repeatInvitation) {
		this.repeatInvitation = repeatInvitation;
	}

	public List<Integer> getPhotoSortNos() {
		return photoSortNos;
	}

	public void setPhotoSortNos(List<Integer> photoSortNos) {
		this.photoSortNos = photoSortNos;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getWeekNo() {
		return weekNo;
	}

	public void setWeekNo(Integer weekNo) {
		this.weekNo = weekNo;
	}

	public Integer getLastWeekScore() {
		return lastWeekScore;
	}

	public void setLastWeekScore(Integer lastWeekScore) {
		this.lastWeekScore = lastWeekScore;
	}

	public Boolean getBlackList() {
		return blackList;
	}

	public void setBlackList(Boolean blackList) {
		this.blackList = blackList;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}


}
