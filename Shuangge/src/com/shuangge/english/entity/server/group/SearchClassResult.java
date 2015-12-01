package com.shuangge.english.entity.server.group;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

/**
 * 
 * @author Jeffrey
 *
 */
public class SearchClassResult extends RestResult {

	private List<ClassData> classInfos = new ArrayList<ClassData>();

	public List<ClassData> getClassInfos() {
		return classInfos;
	}

	public void setClassInfos(List<ClassData> classInfos) {
		this.classInfos = classInfos;
	}

}