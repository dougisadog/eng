package com.shuangge.english.network.reqdata;

import com.shuangge.english.entity.lesson.EntityLessonDataCache;
import com.shuangge.english.network.reqdata.group.UpdateClassData;
import com.shuangge.english.network.reqdata.post.UpdatePostData;
import com.shuangge.english.network.reqdata.post.UpdateReplyData;
import com.shuangge.english.network.reqdata.user.UpdateInfoData;

/**
 * 
 * @author Jeffrey
 *
 */
public class GlobalReqDatas {

	private static GlobalReqDatas instance = null;
	
	private GlobalReqDatas() {
	}
	
	public static GlobalReqDatas getInstance() {
		if (null == instance) {
			instance = new GlobalReqDatas();
		}
		return instance;
	}
	
	/**********************************************************************************************************************/
	
	private String requestUID;
	private int requestUIDType;
	private String requestLoginName;
	private String requestName;
	private String requestPassword;
	private String requestPhoto;
	private String requestPhotoToken;
	private String requestImgHeadUrl;
	private Integer requestSex;
	
	private UpdateInfoData updateInfoData = new UpdateInfoData();
	private UpdateClassData updateClassData = new UpdateClassData();
	private UpdatePostData updatePostData = new UpdatePostData();
	private UpdateReplyData updateReplyData = new UpdateReplyData();
	
	//初判断level
	private Integer requestInitLevel;
	private String otherInvitationCode;

	public UpdateInfoData getUpdateInfoData() {
		return updateInfoData;
	}

	public UpdateClassData getUpdateClassData() {
		return updateClassData;
	}

	public UpdatePostData getUpdatePostData() {
		return updatePostData;
	}

	public UpdateReplyData getUpdateReplyData() {
		return updateReplyData;
	}

	public String getRequestLoginName() {
		return requestLoginName;
	}

	public void setRequestLoginName(String requestLoginName) {
		this.requestLoginName = requestLoginName;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getRequestPassword() {
		return requestPassword;
	}

	public void setRequestPassword(String requestPassword) {
		this.requestPassword = requestPassword;
	}

	public String getRequestPhotoToken() {
		return requestPhotoToken;
	}

	public void setRequestPhotoToken(String requestPhotoToken) {
		this.requestPhotoToken = requestPhotoToken;
	}

	public String getRequestPhoto() {
		return requestPhoto;
	}

	public void setRequestPhoto(String requestPhoto) {
		this.requestPhoto = requestPhoto;
	}

	public String getRequestImgHeadUrl() {
		return requestImgHeadUrl;
	}

	public void setRequestImgHeadUrl(String requestImgHeadUrl) {
		this.requestImgHeadUrl = requestImgHeadUrl;
	}

	public Integer getRequestSex() {
		return requestSex;
	}

	public void setRequestSex(Integer requestSex) {
		this.requestSex = requestSex;
	}

	public String getRequestUID() {
		return requestUID;
	}

	public void setRequestUID(String requestUID) {
		this.requestUID = requestUID;
	}

	public int getRequestUIDType() {
		return requestUIDType;
	}

	public void setRequestUIDType(int requestUIDType) {
		this.requestUIDType = requestUIDType;
	}

	public Integer getRequestInitLevel() {
		return requestInitLevel;
	}

	public void setRequestInitLevel(Integer requestInitLevel) {
		this.requestInitLevel = requestInitLevel;
	}

	public String getOtherInvitationCode() {
		return otherInvitationCode;
	}

	public void setOtherInvitationCode(String otherInvitationCode) {
		this.otherInvitationCode = otherInvitationCode;
	}

}
