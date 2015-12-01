package com.shuangge.english.view.ranklist.adapter;

import com.shuangge.english.entity.server.ranklist.RanklistData;

public class AdapterUserRanklistData implements IAdapterData {
	
	private RanklistData data;
	
	public AdapterUserRanklistData(RanklistData data) {
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
		return data.getUserNo();
	}

	@Override
	public int getType() {
		return IAdapterData.TYPE_USER;
	}

}
