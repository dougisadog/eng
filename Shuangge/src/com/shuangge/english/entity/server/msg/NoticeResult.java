
package com.shuangge.english.entity.server.msg;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

public class NoticeResult extends RestResult {
	
	private List<NoticeData> notices = new ArrayList<NoticeData>();

	public List<NoticeData> getNotices() {
		return notices;
	}

	public void setNotices(List<NoticeData> notices) {
		this.notices = notices;
	}
	
}