package com.shuangge.english.entity.lesson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.lesson.Type4Data;
import com.shuangge.english.entity.server.lesson.Type5Data;
import com.shuangge.english.support.database.entity.BaseEntity;

public class EntityResType4 extends BaseEntity implements IResType {
	
	public static final String FORMAT = ".zip";
	
	private String id;
	private String parentId;
	private String name;
	private long progress = 0L;
	private long max = 1L;
	private double version;
	private int status = IResType.STATUS_NORMAL;
	private String desc;
	private String url;
	private int isCore;
	private List<EntityResType5> type5s = new ArrayList<EntityResType5>(); 
	
	public EntityResType4(String id, String parentId, String name, String url, double version) {
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
	
	public boolean isCore() {
		return isCore == 1;
	}

	public void setCore(boolean isCore) {
		this.isCore = isCore ? 1 : 0;
	}
	
	public List<EntityResType5> getType5s() {
		return type5s;
	}

	public void setType5s(List<EntityResType5> type5s) {
		this.type5s = type5s;
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
		dest.writeInt(isCore);
		dest.writeList(type5s);
	}
	
	@Override
	public BaseEntity getEntity() {
		EntityResType4 entity = new EntityResType4(id, parentId, name, url, version);
		entity.max = max;
		entity.status = status;
		entity.isCore = isCore;
		entity.status = status;
		entity.type5s = type5s;
		return entity;
	}
	
	public String getPath() {
		return this.getParentId() + File.separator + this.getId() + FORMAT;
	}
	
	public String getLocalPath() {
		return CacheDisk.getLessonPath(getPath());
	}
	
	public String getLocalTempDirPath() {
		return this.getParentId() + File.separator + this.getId() + File.separator;
	}
	
	public File getLocalTempDir() {
		return CacheDisk.getLessonFileByPath(getLocalTempDirPath());
	}
	
	public File getLocalFile() {
		return CacheDisk.getLessonFileByPath(getPath());
	}
	
	public void refreshStar() {
		int profectNum = 0;
		int finishedNum = 0;
		int starNum = 0;
		
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		Type4Data type4 = beans.getUnlockDatas().getType4s().get(getId());
		if (null == type4) {
			return;
		}
		
		Type5Data type5 = null;
		EntityResType5 entityResType5 = null;
		for (int i = 0; i < type5s.size(); ++i) {
			entityResType5 = type5s.get(i);
			type5 = beans.getUnlockDatas().getType5s().get(entityResType5.getId());
			if (null == type5 || !type5.getIsFinished()) {
				continue;
			}
			++finishedNum;
			if (null == type5.getRightRate() || type5.getRightRate() < 100) {
				continue;
			}
			++profectNum;
			if (isCore()) {
				if (profectNum > 0) {
					++starNum;
				}
				else if (profectNum > 1) {
					++starNum;
				}
			}
			else {
				if (i < 2) {
					if (profectNum > 1) {
						++starNum;
					}
					if (i == 1) {
						profectNum = 0;
					}
					continue;
				}
				if (profectNum == type5s.size() - 2) {
					++starNum;
				}
			}
		}
		if (finishedNum == type5s.size()) {
			++starNum;
		}
		type4.setStarNum(starNum);
	}

}
