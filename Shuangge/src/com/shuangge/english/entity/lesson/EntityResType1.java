package com.shuangge.english.entity.lesson;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.view.component.tree.INode;

public class EntityResType1 implements INode {
	
	private String id;

	private String parentId;

	private String name;

	private long progress = 0l;
	
	private long max = 1l;
	
	private int status = INode.STATUS_NORMAL;
	
	private boolean base = false;
	
	List<EntityResType2> type2s = new ArrayList<EntityResType2>();

	public EntityResType1(String id, String name, boolean base) {
		super();
		this.id = id;
		this.name = name;
		this.base = base;
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

	public int getStatus() {
		return status;
	}

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<EntityResType2> getType2s() {
		return type2s;
	}

}
