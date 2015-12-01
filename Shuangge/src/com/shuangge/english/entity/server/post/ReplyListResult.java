package com.shuangge.english.entity.server.post;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

/**
 * 
 * @author Jeffrey
 *
 */
public class ReplyListResult extends RestResult {

	private int pageCount = 0;
	
	private List<ReplyData> replys = new ArrayList<ReplyData>();

	public List<ReplyData> getReplys() {
		return replys;
	}

	public void setReplys(List<ReplyData> replys) {
		this.replys = replys;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}
