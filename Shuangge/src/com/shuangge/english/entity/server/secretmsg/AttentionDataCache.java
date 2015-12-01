package com.shuangge.english.entity.server.secretmsg;

import android.os.Parcel;

import com.shuangge.english.support.database.entity.BaseEntity;

public class AttentionDataCache extends BaseEntity{
	
	private AttentionData data = new AttentionData();
	
	public AttentionDataCache() {
	}
	
	public AttentionDataCache(AttentionData data) {
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

	public AttentionData getAttentionData() {
		return data;
	}

	public void setAttentionData(AttentionData attentionData) {
		this.data = attentionData;
	}



}
