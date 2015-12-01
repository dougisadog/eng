package com.shuangge.english.entity.server.msg;

import java.util.Date;

/**
 * @author Jeffrey
 *
 */
public class UserGroupMsgData {
	
	public static final int GROUP_TYPE_CLASS = 0;
	public static final int GROUP_TYPE_SCHOOL = 1;
	
	/*********************************************************************************************************/
	
	public static final int TYPE_CLASS_CREATE = 1;
	public static final int TYPE_CLASS_CHANGED = 3;
	public static final int TYPE_CLASS_TRANSFER = 35;
	
	public static final int TYPE_CLASS_JOIN = 21;
	public static final int TYPE_CLASS_LEAVE = 22;
	public static final int TYPE_CLASS_KILL = 23;
	
	public static final int TYPE_CLASS_MANAGER = 31;
	public static final int TYPE_CLASS_MANAGER_OFF = 32;
	public static final int TYPE_CLASS_SUB_MANAGER = 33;
	public static final int TYPE_CLASS_SUB_MANAGER_OFF = 34;
	
	public static final int TYPE_SCHOOL_CREATE = 41;
	public static final int TYPE_SCHOOL_DISSOLVE = 42;
	public static final int TYPE_SCHOOL_CHANGED = 43;
	public static final int TYPE_SCHOOL_UPGRADE = 44;
	
	public static final int TYPE_USER_APPLY = 51;
	public static final int TYPE_GROUP_APPLY = 52;
	public static final int TYPE_USER_REFUSE = 61;
	public static final int TYPE_GROUP_REFUSE = 62;
	
	/*********************************************************************************************************/
	
	public static final int STATUS_WAITING = 0;
	public static final int STATUS_AGREE = 1;
	public static final int STATUS_REFUSE = 2;
	public static final int STATUS_JOINED_OTHER = 3;
	public static final int STATUS_JOINED = 4;
	public static final int STATUS_BE_REFUSED = 5;
	public static final int STATUS_TIME_OUT = 10;
	
	/*********************************************************************************************************/

	private Long groupMsgNo;
	private Long userNo;
	private Long groupNo;
	private String groupName;
	private String groupHeadUrl;
	private int groupType;
	private Long targetNo1;
	private String targetName1;
	private String targetHeadUrl1;
	private Long targetNo2;
	private String targetName2;
	private String targetHeadUrl2;
	private String message;
	private int type = 0;
	private int status = 0;
	private Date createDate = new Date();
	private String wechatNo;

	public String getWechatNo() {
		return wechatNo;
	}

	public void setWechatNo(String wechatNo) {
		this.wechatNo = wechatNo;
	}

	public Long getGroupMsgNo() {
		return groupMsgNo;
	}

	public void setGroupMsgNo(Long groupMsgNo) {
		this.groupMsgNo = groupMsgNo;
	}

	public Long getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupHeadUrl() {
		return groupHeadUrl;
	}

	public void setGroupHeadUrl(String groupHeadUrl) {
		this.groupHeadUrl = groupHeadUrl;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}

	public Long getTargetNo1() {
		return targetNo1;
	}

	public void setTargetNo1(Long targetNo1) {
		this.targetNo1 = targetNo1;
	}

	public String getTargetName1() {
		return targetName1;
	}

	public void setTargetName1(String targetName1) {
		this.targetName1 = targetName1;
	}

	public String getTargetHeadUrl1() {
		return targetHeadUrl1;
	}

	public void setTargetHeadUrl1(String targetHeadUrl1) {
		this.targetHeadUrl1 = targetHeadUrl1;
	}

	public Long getTargetNo2() {
		return targetNo2;
	}

	public void setTargetNo2(Long targetNo2) {
		this.targetNo2 = targetNo2;
	}

	public String getTargetName2() {
		return targetName2;
	}

	public void setTargetName2(String targetName2) {
		this.targetName2 = targetName2;
	}

	public String getTargetHeadUrl2() {
		return targetHeadUrl2;
	}

	public void setTargetHeadUrl2(String targetHeadUrl2) {
		this.targetHeadUrl2 = targetHeadUrl2;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

}
