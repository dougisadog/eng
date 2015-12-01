package com.shuangge.english.entity.server.secretmsg;

import android.os.Parcel;

import com.shuangge.english.support.database.entity.BaseEntity;

public class LocalDataCache extends BaseEntity {
	
	private LocalData data = new LocalData();
	
	public LocalDataCache() {
	}
	
	public LocalDataCache(LocalData data) {
		this.data = data;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public BaseEntity getEntity() {
		return null;
	}

	public LocalData getData() {
		return data;
	}

	public void setData(LocalData data) {
		this.data = data;
	}




}
