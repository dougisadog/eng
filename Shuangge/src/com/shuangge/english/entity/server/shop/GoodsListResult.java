package com.shuangge.english.entity.server.shop;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class GoodsListResult extends RestResult {

	private List<GoodsData> goodsDatas = new ArrayList<GoodsData>();

	public List<GoodsData> getGoodsDatas() {
		return goodsDatas;
	}

	public void setGoodsDatas(List<GoodsData> goodsDatas) {
		this.goodsDatas = goodsDatas;
	}


}
