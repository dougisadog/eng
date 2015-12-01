package com.shuangge.english.entity.server.group;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class ClassMemberResult extends RestResult {

	private List<ClassMemberData> members = new ArrayList<ClassMemberData>();

	public List<ClassMemberData> getMembers() {
		return members;
	}

	public void setMembers(List<ClassMemberData> members) {
		this.members = members;
	}

}
