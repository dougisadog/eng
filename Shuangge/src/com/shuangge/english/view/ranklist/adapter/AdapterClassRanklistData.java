package com.shuangge.english.view.ranklist.adapter;

import com.shuangge.english.entity.server.ranklist.ClassRanklistData;

public class AdapterClassRanklistData implements IAdapterData {
	
	private ClassRanklistData data;
	
	public AdapterClassRanklistData(ClassRanklistData data) {
		this.data = data;
	}

	@Override
	public Integer getNo() {
		return data.getNo();
	}

	@Override
	public String getName() {
		return data.getName();
	}

	@Override
	public Integer getScore() {
		return data.getScore();
	}

	@Override
	public String getHeadUrl() {
		return data.getHeadUrl();
	}

	@Override
	public Long getIndex() {
		return data.getClassNo();
	}

	@Override
	public int getType() {
		return IAdapterData.TYPE_CLASS;
	}

}
