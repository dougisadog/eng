package com.shuangge.english.entity;

import android.os.Parcel;

import com.shuangge.english.support.database.entity.BaseEntity;

/**
 * 对应数据存储对象 User
 * 
 * @author Jeffrey
 *
 */
public class EntityUser extends BaseEntity {

	public static final int NORMAL_USER = 0;
	public static final int VISITOR = 1;
	public static final int QQ = 2;
	public static final int WX = 3;
	
	public EntityUser() {
	}
	
	public EntityUser(String loginName, String password) {
		this.loginName = loginName;
		this.password = password;
		this.type = NORMAL_USER;
	}
	
	public EntityUser(String uid, int type) {
		this.uid = uid;
		this.type = type;
	}
	
	private String loginName;
	private String password;
	private String mac;
	private String uid;
	private int type;
	private boolean isLogout = false;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean isLogout() {
		return isLogout;
	}

	public void setLogout(boolean isLogout) {
		this.isLogout = isLogout;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(loginName);
		dest.writeString(password);
		dest.writeString(mac);
		dest.writeString(uid);
		dest.writeInt(type);
	}

	@Override
	public BaseEntity getEntity() {
		EntityUser entity = new EntityUser();
		entity.loginName = loginName;
		entity.password = password;
		entity.mac = mac;
		entity.uid = uid;
		entity.type = type;
		entity.isLogout = isLogout;
		return entity;
	}

}
