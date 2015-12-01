package com.shuangge.english.entity.server.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostData {

	private Long postNo;
	private String title;
	private String content;
	private String firstPic;
	private int type;
	private int niceNum;
	private int replyNum;
	private Boolean top = false;
	private Long userNo;
	private String userName;
	private String userHeadUrl;
	private Integer userSex;
	private String honorTitle;
	private Date createDate;
	private Date updateDate;
	
	private List<Long> picNos = new ArrayList<Long>();
	private List<String> picUrls = new ArrayList<String>();
	private List<Integer> picSortNos = new ArrayList<Integer>();
	
	private boolean niceForMe = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public Boolean getTop() {
		return top;
	}

	public void setTop(Boolean top) {
		this.top = top;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getPostNo() {
		return postNo;
	}

	public void setPostNo(Long postNo) {
		this.postNo = postNo;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getFirstPic() {
		return firstPic;
	}

	public void setFirstPic(String firstPic) {
		this.firstPic = firstPic;
	}

	public String getUserHeadUrl() {
		return userHeadUrl;
	}

	public void setUserHeadUrl(String userHeadUrl) {
		this.userHeadUrl = userHeadUrl;
	}

	public Integer getUserSex() {
		return userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	public String getHonorTitle() {
		return honorTitle;
	}

	public void setHonorTitle(String honorTitle) {
		this.honorTitle = honorTitle;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public List<Long> getPicNos() {
		return picNos;
	}

	public void setPicNos(List<Long> picNos) {
		this.picNos = picNos;
	}

	public List<String> getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(List<String> picUrls) {
		this.picUrls = picUrls;
	}

	public List<Integer> getPicSortNos() {
		return picSortNos;
	}

	public void setPicSortNos(List<Integer> picSortNos) {
		this.picSortNos = picSortNos;
	}

	public int getNiceNum() {
		return niceNum;
	}

	public void setNiceNum(int niceNum) {
		this.niceNum = niceNum;
	}

	public boolean isNiceForMe() {
		return niceForMe;
	}

	public void setNiceForMe(boolean niceForMe) {
		this.niceForMe = niceForMe;
	}

}
