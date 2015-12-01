package com.shuangge.english.entity.server.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoData {

	public static final String ROLE_METEN = "meten";
	public static final String ROLE_SG = "sg";
	
	private Long userNo;
	private String userType;
	private String loginName;
	private String headUrl;
	private String name;
	private String phone;
	private String email;
	private Integer sex;
	private Integer age;
	private String address;
	private HonorData honorData;

	private Date birthday;
	private String signature;
	private String emotion;
	private String occupation;
	private String income;
	private String location;
	private String interest;

	private Integer score = 0;
	private Integer weekScore = 0;
	private Integer lastWeekScore = 0;


	private String[] progressList;

	private List<Long> photoNos = new ArrayList<Long>();
	private List<String> photoUrls = new ArrayList<String>();
	private List<Integer> photoSortNos = new ArrayList<Integer>();

	private String lastRequestTime;

	private String alipay;
	private String weChat;

	private String type1ClientId;
	private String type2ClientId;
	private String type3ClientId;
	private String type2Name;
	private String type4ClientId;
	private String type4Name;
	private String type5ClientId;
	private String type5Name;
	private String type6ClientId;
	
	private String wechatNo;
	
	private Double money = 0D;

	private Integer eMoney = 0;
	private Integer money2 = 0;
	private String invitationCode;
	private String otherInvitationCode;
	
	private Integer beInvitation = 0;
	private Integer keyNum;

	public String getWechatNo() {
		return wechatNo;
	}

	public void setWechatNo(String wechatNo) {
		this.wechatNo = wechatNo;
	}

	public String getType5ClientId() {
		return type5ClientId;
	}

	public void setType5ClientId(String type5ClientId) {
		this.type5ClientId = type5ClientId;
	}
	public String getType3ClientId() {
		return type3ClientId;
	}

	public void setType3ClientId(String type3ClientId) {
		this.type3ClientId = type3ClientId;
	}

	public String getType6ClientId() {
		return type6ClientId;
	}

	public void setType6ClientId(String type6ClientId) {
		this.type6ClientId = type6ClientId;
	}

	public String getType4ClientId() {
		return type4ClientId;
	}

	public void setType4ClientId(String type4ClientId) {
		this.type4ClientId = type4ClientId;
	}

	private boolean visitor = false;
	private int level = 0;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public HonorData getHonorData() {
		return honorData;
	}

	public void setHonorData(HonorData honorData) {
		this.honorData = honorData;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String[] getProgressList() {
		return progressList;
	}

	public void setProgressList(String[] progressList) {
		this.progressList = progressList;
	}

	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
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

	public List<Long> getPhotoNos() {
		return photoNos;
	}

	public void setPhotoNos(List<Long> photoNos) {
		this.photoNos = photoNos;
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

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(String lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public String getType1ClientId() {
		return type1ClientId;
	}

	public void setType1ClientId(String type1ClientId) {
		this.type1ClientId = type1ClientId;
	}

	public String getType2ClientId() {
		return type2ClientId;
	}

	public void setType2ClientId(String type2ClientId) {
		this.type2ClientId = type2ClientId;
	}

	public boolean isVisitor() {
		return visitor;
	}

	public void setVisitor(boolean visitor) {
		this.visitor = visitor;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public String getType2Name() {
		return type2Name;
	}

	public void setType2Name(String type2Name) {
		this.type2Name = type2Name;
	}

	public String getType4Name() {
		return type4Name;
	}

	public void setType4Name(String type4Name) {
		this.type4Name = type4Name;
	}

	public String getType5Name() {
		return type5Name;
	}

	public void setType5Name(String type5Name) {
		this.type5Name = type5Name;
	}
	
	public Integer getLastWeekScore() {
		return lastWeekScore;
	}

	public void setLastWeekScore(Integer lastWeekScore) {
		this.lastWeekScore = lastWeekScore;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer geteMoney() {
		return eMoney;
	}

	public void seteMoney(Integer eMoney) {
		this.eMoney = eMoney;
	}

	public Integer getMoney2() {
		return money2;
	}

	public void setMoney2(Integer money2) {
		this.money2 = money2;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public Integer getBeInvitation() {
		return beInvitation;
	}

	public void setBeInvitation(Integer beInvitation) {
		this.beInvitation = beInvitation;
	}

	public String getOtherInvitationCode() {
		return otherInvitationCode;
	}

	public void setOtherInvitationCode(String otherInvitationCode) {
		this.otherInvitationCode = otherInvitationCode;
	}

	public Integer getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(Integer keyNum) {
		this.keyNum = keyNum;
	}


}