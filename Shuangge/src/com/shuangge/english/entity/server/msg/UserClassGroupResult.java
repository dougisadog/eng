package com.shuangge.english.entity.server.msg;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class UserClassGroupResult extends RestResult {

	private List<UserGroupMsgData> classMsgs = new ArrayList<UserGroupMsgData>();

	public List<UserGroupMsgData> getClassMsgs() {
		return classMsgs;
	}

	public void setClassMsgs(List<UserGroupMsgData> classMsgs) {
		this.classMsgs = classMsgs;
	}

}
