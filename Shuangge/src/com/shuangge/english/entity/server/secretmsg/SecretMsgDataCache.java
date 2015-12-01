package com.shuangge.english.entity.server.secretmsg;

import java.util.Date;

import android.os.Parcel;

import com.shuangge.english.support.database.entity.BaseEntity;

public class SecretMsgDataCache extends BaseEntity {

	private SecretMsgDetailData secretMsgDetaildata;
	private String loginName;
	private Long friendNo;

	public void setData(Long secretMsgNo, Long senderNo, Long receiverNo, String content, Integer status, 
			Date sendTime,  String picUrl) {
		
		SecretMsgDetailData data = new SecretMsgDetailData();
		
		data.setSecretMsgNo(secretMsgNo);
		data.setSenderNo(senderNo);
		data.setReceiverNo(receiverNo);
//		data.setUserNo(userNo);
//		data.setUserName(userName);
//		data.setUserHeadUrl(userHeadUrl);
//		data.setFriendNo(friendNo);
		data.setContent(content);
//		data.setType(type);
		data.setStatus(status);
//		data.setCount(count);
		data.setSendTime(sendTime);
//		data.setReadTime(readTime);
		data.setPicUrl(picUrl);
//		data.setFriendName(friendName);
//		data.setFriendHeadUrl(friendHeadUrl);
//		data.setListStatus(listStatus);
		
		this.secretMsgDetaildata = data;
	}
	
	public void setData(SecretMsgDetailData data) {
		
		secretMsgDetaildata = data;
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(secretMsgDetaildata.getSecretMsgNo());
		dest.writeLong(secretMsgDetaildata.getSenderNo());
		dest.writeLong(secretMsgDetaildata.getReceiverNo());
//		dest.writeLong(secretMsgdata.getUserNo());
//		dest.writeString(secretMsgdata.getUserName());
//		dest.writeString(secretMsgdata.getUserHeadUrl());
		dest.writeLong(secretMsgDetaildata.getFriendNo());
		dest.writeString(secretMsgDetaildata.getContent());
//		dest.writeInt(secretMsgdata.getType());
		dest.writeInt(secretMsgDetaildata.getStatus());
//		dest.writeInt(secretMsgdata.getCount());
		dest.writeValue(secretMsgDetaildata.getSendTime());
//		dest.writeValue(secretMsgdata.getReadTime());
		dest.writeString(secretMsgDetaildata.getPicUrl());
		dest.writeString(secretMsgDetaildata.getFriendName());
		dest.writeString(secretMsgDetaildata.getFriendHeadUrl());
//		dest.writeInt(secretMsgDetaildata.getListStatus());
	}

	@Override
	public BaseEntity getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	public SecretMsgDetailData getSecretMsgdata() {
		return secretMsgDetaildata;
	}

	public void setSecretMsgdata(SecretMsgDetailData secretMsgDetaildata) {
		this.secretMsgDetaildata = secretMsgDetaildata;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Long getFriendNo() {
		return friendNo;
	}

	public void setFriendNo(Long friendNo) {
		this.friendNo = friendNo;
	}

}