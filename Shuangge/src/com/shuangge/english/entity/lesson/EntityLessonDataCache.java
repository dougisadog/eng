package com.shuangge.english.entity.lesson;

import android.os.Parcel;

import com.shuangge.english.support.database.entity.BaseEntity;

public class EntityLessonDataCache extends BaseEntity {

	public void setData(String loginName, String cType5, String cType6, Integer status, 
			Integer heartNum, Integer rightNum, Integer wrongNum, String lessonTypes, String cType7AndStatus) {
		this.loginName = loginName;
		this.cType5 = cType5;
		this.cType6 = cType6;
		this.status = status;
		this.heartNum = heartNum;
		this.rightNum = rightNum;
		this.wrongNum = wrongNum;
		this.lessonTypes = lessonTypes;
		this.cType7AndStatus = cType7AndStatus;
	}
	
	private String loginName;
	private String cType5;
	private String cType6;
	private Integer status;
	private Integer heartNum;
	private Integer rightNum;
	private Integer wrongNum;
	private String lessonTypes;
	private String cType7AndStatus;
	
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getcType5() {
		return cType5;
	}

	public void setcType5(String cType5) {
		this.cType5 = cType5;
	}

	public String getcType6() {
		return cType6;
	}

	public void setcType6(String cType6) {
		this.cType6 = cType6;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getHeartNum() {
		return heartNum;
	}

	public void setHeartNum(Integer heartNum) {
		this.heartNum = heartNum;
	}

	public Integer getRightNum() {
		return rightNum;
	}

	public void setRightNum(Integer rightNum) {
		this.rightNum = rightNum;
	}

	public Integer getWrongNum() {
		return wrongNum;
	}

	public void setWrongNum(Integer wrongNum) {
		this.wrongNum = wrongNum;
	}

	public String getLessonTypes() {
		return lessonTypes;
	}

	public void setLessonTypes(String lessonTypes) {
		this.lessonTypes = lessonTypes;
	}

	public String getcType7AndStatus() {
		return cType7AndStatus;
	}

	public void setcType7AndStatus(String cType7AndStatus) {
		this.cType7AndStatus = cType7AndStatus;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(loginName);
		dest.writeString(cType5);
		dest.writeString(cType6);
		dest.writeInt(status);
		dest.writeInt(heartNum);
		dest.writeInt(rightNum);
		dest.writeInt(wrongNum);
		dest.writeString(lessonTypes);
		dest.writeString(cType7AndStatus);
	}

	@Override
	public BaseEntity getEntity() {
		return this;
	}
	
}
