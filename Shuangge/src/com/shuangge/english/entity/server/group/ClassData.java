package com.shuangge.english.entity.server.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author Jeffrey
 * 
 */
public class ClassData {
	
	public static final int NORMAL = 0;
	public static final int SUB_MANAGER = 2;
	public static final int MANAGER = 3;

	private Long classNo;
	private String name;
	private String headUrl;
	private String description;
	private String location;
	private String signature;
	private Integer score;
	private int num = 0;
	private int maxNum = 50;
	private String creater;
	private Long createrId;
	private int level;

	private Integer no;
	private Integer lastWeekNo;

	private Integer weekScore;
	private Integer lastWeekScore;

	private Integer lastWeekRewards;

	private Integer type;
	private Integer lastWeekType;
	
	private Integer postNum;
	private Date createDate;

	private List<Long> photoNos = new ArrayList<Long>();
	private List<String> photoUrls = new ArrayList<String>();
	private List<Integer> photoSortNos = new ArrayList<Integer>();
	
	private String wechatNo;
	
	private Integer joinRule;
	
	public String getWechatNo() {
		return wechatNo;
	}

	public void setWechatNo(String wechatNo) {
		this.wechatNo = wechatNo;
	}

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

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Integer getLastWeekNo() {
		return lastWeekNo;
	}

	public void setLastWeekNo(Integer lastWeekNo) {
		this.lastWeekNo = lastWeekNo;
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

	public Integer getLastWeekRewards() {
		return lastWeekRewards;
	}

	public void setLastWeekRewards(Integer lastWeekRewards) {
		this.lastWeekRewards = lastWeekRewards;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLastWeekType() {
		return lastWeekType;
	}

	public void setLastWeekType(Integer lastWeekType) {
		this.lastWeekType = lastWeekType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getPostNum() {
		return postNum;
	}

	public void setPostNum(Integer postNum) {
		this.postNum = postNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
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

	public List<Integer> getPhotoSortNos() {
		return photoSortNos;
	}

	public void setPhotoSortNos(List<Integer> photoSortNos) {
		this.photoSortNos = photoSortNos;
	}

	public Integer getJoinRule() {
		return joinRule;
	}

	public void setJoinRule(Integer joinRule) {
		this.joinRule = joinRule;
	}

}
