package com.shuangge.english.entity.lesson;

import android.os.Parcel;
import android.text.TextUtils;

import com.shuangge.english.support.database.entity.BaseEntity;

public class EntityResType5 extends BaseEntity {
	
	public static final String FORMAT = ".txt";
	
	private String id;

	private String parentId;

	private String name;
	
	private String pageId;
	
	private String resId;
	
	private int isFinished;

	public EntityResType5(String id, String parentId, String name, String pageId, String resId) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.resId = resId;
		//核心课程 口语达人 写作达人 阅读达人 全能达人 单元重点 课前预习 课前预习A 课前预习B 单词达人 听说达人 复习检测 复习检测A 复习检测B
		if (!TextUtils.isEmpty(pageId)) {
			switch (pageId) {
			case "核心课程":
				break;
			case "口语达人":
				break;
			case "写作达人":
				break;
			case "阅读达人":
				break;
			case "全能达人":
				break;
			case "单元重点":
				break;
			case "课前预习":
				break;
			case "课前预习A":
				break;
			case "课前预习B":
				break;
			case "单词达人":
				break;
			case "听说达人 ":
				break;
			case "复习检测":
				break;
			case "复习检测A":
				break;
			case "复习检测B":
				break;
			default:
				break;
			}
			this.pageId = pageId;
		}
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

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public boolean isFinished() {
		return this.isFinished == 1;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(parentId);
		dest.writeString(name);
		dest.writeInt(isFinished);
	}

	@Override
	public BaseEntity getEntity() {
		return null;
	}
	
}