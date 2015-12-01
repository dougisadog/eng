package com.shuangge.english.entity.server.group;

import java.util.List;

import com.shuangge.english.entity.server.RestResult;

/**
 * 
 * @author Jeffrey
 *
 */
public class ClassResult extends RestResult {

	private ClassData classInfo;
	private List<ClassMemberData> members;
	
	public ClassData getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(ClassData classInfo) {
		this.classInfo = classInfo;	
	}

	public List<ClassMemberData> getMembers() {
		return members;
	}

	public void setMembers(List<ClassMemberData> members) {
		this.members = members;
	}
	
}