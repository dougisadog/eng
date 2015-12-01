package com.shuangge.english.entity.server.shop;

import com.shuangge.english.entity.server.RestResult;


public class GoodsResult extends RestResult {

	private GoodsData goodsData;

	public GoodsData getGoodsData() {
		return goodsData;
	}

	public void setGoodsData(GoodsData goodsData) {
		this.goodsData = goodsData;
	}

}
