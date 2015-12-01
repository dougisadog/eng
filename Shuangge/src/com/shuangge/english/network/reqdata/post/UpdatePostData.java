package com.shuangge.english.network.reqdata.post;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

public class UpdatePostData {

	private Long classNo;
	private String title;
	private String content;
	private Integer top = 0;

	private List<Long> photoNos = new ArrayList<Long>();
	private List<Integer> sortNos = new ArrayList<Integer>();
	private List<String> fileNames = new ArrayList<String>();
	private List<Integer> fileSortNos = new ArrayList<Integer>();

	private List<PhotoParam> photoDatas = new ArrayList<PhotoParam>();

	public Long getClassNo() {
		return classNo;
	}

	public void setClassNo(Long classNo) {
		this.classNo = classNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
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
