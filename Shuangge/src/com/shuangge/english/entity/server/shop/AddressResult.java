package com.shuangge.english.entity.server.shop;

import com.shuangge.english.entity.server.RestResult;

public class AddressResult extends RestResult {

	private AddressData addressData;

	public AddressData getAddressData() {
		return addressData;
	}

	public void setAddressData(AddressData addressData) {
		this.addressData = addressData;
	}

}
