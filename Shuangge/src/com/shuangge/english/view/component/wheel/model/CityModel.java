package com.shuangge.english.view.component.wheel.model;

import java.util.ArrayList;
import java.util.List;

public class CityModel {
	private String name;
	private List<DistrictModel> districtList;
	
	public CityModel() {
		super();
	}

	public CityModel(String name, List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}
	
	public CityModel(String name, DistrictModel district) {
		super();
		this.name = name;
		this.districtList = new ArrayList<DistrictModel>();
		this.districtList.add(district);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
