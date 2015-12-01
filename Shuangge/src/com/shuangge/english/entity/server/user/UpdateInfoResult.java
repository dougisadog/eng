package com.shuangge.english.entity.server.user;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class UpdateInfoResult extends RestResult {

	private List<Long> photoNos = new ArrayList<Long>();
	private List<String> photoUrls = new ArrayList<String>();
	private List<Integer> photoSortNos = new ArrayList<Integer>();

	public List<Long> getPhotoNos() {
		return photoNos;
	}

	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public List<Integer> getPhotoSortNos() {
		return photoSortNos;
	}

}
