package com.shuangge.english.network.reqdata.group;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

public class UpdateClassData {

	private String name;
	private String location;
	private String signature;
	private String description;
	private String password;
	private String wechatNo;
	private Integer joinRule;
	
	public String getWechatNo() {
		return wechatNo;
	}

	public void setWechatNo(String wechatNo) {
		this.wechatNo = wechatNo;
	}

	private List<Long> photoNos = new ArrayList<Long>();
	private List<Integer> sortNos = new ArrayList<Integer>();
	private List<String> fileNames = new ArrayList<String>();
	private List<Integer> fileSortNos = new ArrayList<Integer>();
	
	private List<PhotoParam> photoDatas = new ArrayList<PhotoParam>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Long> getPhotoNos() {
		return photoNos;
	}

	public void setPhotoNos(List<Long> photoNos) {
		this.photoNos = photoNos;
	}

	public List<Integer> getSortNos() {
		return sortNos;
	}

	public void setSortNos(List<Integer> sortNos) {
		this.sortNos = sortNos;
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public List<Integer> getFileSortNos() {
		return fileSortNos;
	}

	public void setFileSortNos(List<Integer> fileSortNos) {
		this.fileSortNos = fileSortNos;
	}

	public List<PhotoParam> getPhotoDatas() {
		return photoDatas;
	}

	public void setPhotoDatas(List<PhotoParam> photoDatas) {
		this.photoDatas = photoDatas;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void clear() {
		this.getPhotoNos().clear();
		this.getSortNos().clear();
		this.getFileNames().clear();
		this.getFileSortNos().clear();
	}

	public Integer getJoinRule() {
		return joinRule;
	}

	public void setJoinRule(Integer joinRule) {
		this.joinRule = joinRule;
	}

}
