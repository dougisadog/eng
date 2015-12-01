package com.shuangge.english.entity.server.post;

import java.util.Date;

public class ReplyData {

	private Long replyNo;
	private int replyFloorsNo;
	private String replyContent;
	private Long replyUserNo;
	private String replyUserName;
	private boolean replyDeleted = false;

	private Long userNo;
	private String userName;
	private String userHeadUrl;
	private Integer userSex;

	private int floorsNo;
	private String content;
	private String picUrl;
	private String picLocation;
	private boolean deleted = false;
	private Date createDate;

	public Long getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(Long replyNo) {
		this.replyNo = replyNo;
	}

	public int getReplyFloorsNo() {
		return replyFloorsNo;
	}

	public void setReplyFloorsNo(int replyFloorsNo) {
		this.replyFloorsNo = replyFloorsNo;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Long getReplyUserNo() {
		return replyUserNo;
	}

	public void setReplyUserNo(Long replyUserNo) {
		this.replyUserNo = replyUserNo;
	}

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}


	public boolean isReplyDeleted() {
		return replyDeleted;
	}

	public void setReplyDeleted(boolean replyDeleted) {
		this.replyDeleted = replyDeleted;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getFloorsNo() {
		return floorsNo;
	}

	public void setFloorsNo(int floorsNo) {
		this.floorsNo = floorsNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicLocation() {
		return picLocation;
	}

	public void setPicLocation(String picLocation) {
		this.picLocation = picLocation;
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

}

