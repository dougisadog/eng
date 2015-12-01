package com.shuangge.english.entity.server.post;

import com.shuangge.english.entity.server.RestResult;

/**
 * 
 * @author Jeffrey
 *
 */
public class PostResult extends RestResult {

	private PostData data;
	
	public PostData getData() {
		return data;
	}

	public void setData(PostData data) {
		this.data = data;
	}

}
