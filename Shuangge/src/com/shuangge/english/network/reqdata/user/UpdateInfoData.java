package com.shuangge.english.network.reqdata.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

public class UpdateInfoData {

	private String name;
	private Integer sex;
	private Date birthday;
	private Integer age;
	private String signature;
	private String emotion;
	private String occupation;
	private String income;
	private String location;
	private String interest;

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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public void clear() {
		this.getPhotoNos().clear();
		this.getSortNos().clear();
		this.getFileNames().clear();
		this.getFileSortNos().clear();
	}

}
