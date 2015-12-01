package com.shuangge.english.support.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class BaseEntity implements Parcelable {

	@Override
	public abstract int describeContents();

	@Override
	public abstract void writeToParcel(Parcel dest, int flags);
	
	public abstract BaseEntity getEntity();

}
