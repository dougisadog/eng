package com.shuangge.english.entity.server.post;

import com.shuangge.english.entity.server.RestResult;

/**
 * 
 * @author Jeffrey
 *
 */
public class ReplyResult extends RestResult {

	private ReplyData data;

	public ReplyData getData() {
		return data;
	}

	public void setData(ReplyData data) {
		this.data = data;
	}

}
