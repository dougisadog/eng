package com.shuangge.english.entity.server.shop;

import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class AddressListResult extends RestResult {

	private List<AddressData> addressDatas;

	public List<AddressData> getAddressDatas() {
		return addressDatas;
	}

	public void setAddressDatas(List<AddressData> addressDatas) {
		this.addressDatas = addressDatas;
	}

	
}
