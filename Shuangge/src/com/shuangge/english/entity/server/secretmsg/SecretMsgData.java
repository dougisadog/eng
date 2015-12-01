package com.shuangge.english.entity.server.secretmsg;

import java.util.Date;

public class SecretMsgData implements Comparable<SecretMsgData> {

	private Long secretMsgNo;
	private Long senderNo;
	private Long receiverNo;
	private Long friendNo;
	private String friendName;
	private String friendHeadUrl;
	private String content;
	private String picUrl;
	private Integer status;
	private Date sendTime;

	public Long getSecretMsgNo() {
		return secretMsgNo;
	}

	public void setSecretMsgNo(Long secretMsgNo) {
		this.secretMsgNo = secretMsgNo;
	}

	public Long getSenderNo() {
		return senderNo;
	}

	public void setSenderNo(Long senderNo) {
		this.senderNo = senderNo;
	}

	public Long getReceiverNo() {
		return receiverNo;
	}

	public void setReceiverNo(Long receiverNo) {
		this.receiverNo = receiverNo;
	}

	public Long getFriendNo() {
		return friendNo;
	}

	public void setFriendNo(Long friendNo) {
		this.friendNo = friendNo;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendHeadUrl() {
		return friendHeadUrl;
	}

	public void setFriendHeadUrl(String friendHeadUrl) {
		this.friendHeadUrl = friendHeadUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}



	@Override
	public int compareTo(SecretMsgData another) {
		// TODO Auto-generated method stub
		if (getSendTime().compareTo(another.getSendTime()) > 0) 
			return -1;
		return 1;
	}

}
