package com.shuangge.english.entity.lesson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;

import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.support.database.entity.BaseEntity;

public class EntityResType2 extends BaseEntity implements IResType {
	
	public static final String FORMAT = ".zip";
	
	private String id;

	private String parentId;

	private String name;

	private long progress = 0l;
	
	private long max = 1l;
	
	private double version;
	
	private int status = IResType.STATUS_NORMAL;
	private int downloadAllStatus = IResType.STATUS_NORMAL;

	private String desc;
	
	private String url;
	
	List<EntityResType4> type4s = new ArrayList<EntityResType4>();
	
	public EntityResType2(String id, String parentId, String name, String url, double version) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.url = url;
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getProgress() {
		return progress;
	}

	public void setProgress(long progress) {
		this.progress = progress;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isFinished() {
		return this.status == IResType.STATUS_FINISH;
	}
	
	public int getDownloadAllStatus() {
		return downloadAllStatus;
	}

	public void setDownloadAllStatus(int downloadAllStatus) {
		this.downloadAllStatus = downloadAllStatus;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(parentId);
		dest.writeLong(max);
		dest.writeDouble(version);
		dest.writeInt(status);
	}
	
	@Override
	public BaseEntity getEntity() {
		EntityResType2 entity = new EntityResType2(id, parentId, name, url, version);
		entity.max = max;
		entity.status = status;
		return entity;
	}
	
	public String getPath() {
		return this.getId() + FORMAT;
	}
	
	public String getLocalPath() {
		return CacheDisk.getLessonPath(getPath());
	}
	
	public File getLocalFile() {
		return CacheDisk.getLessonFileByPath(getPath());
	}

	public List<EntityResType4> getType4s() {
		return type4s;
	}

}
