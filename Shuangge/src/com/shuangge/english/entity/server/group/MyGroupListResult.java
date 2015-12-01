package com.shuangge.english.entity.server.group;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

/**
 * 
 * @author Jeffrey
 *
 */
public class MyGroupListResult extends RestResult {

	private int myClassAuth = -1;
	private List<ClassData> classInfos = new ArrayList<ClassData>();
	private SchoolData schoolData;

	public List<ClassData> getClassInfos() {
		return classInfos;
	}

	public void setClassInfos(List<ClassData> classInfos) {
		this.classInfos = classInfos;
	}

	public SchoolData getSchoolData() {
		return schoolData;
	}

	public void setSchoolData(SchoolData schoolData) {
		this.schoolData = schoolData;
	}

	public int getMyClassAuth() {
		return myClassAuth;
	}

	public void setMyClassAuth(int myClassAuth) {
		this.myClassAuth = myClassAuth;
	}

}
