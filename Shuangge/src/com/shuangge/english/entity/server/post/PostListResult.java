package com.shuangge.english.entity.server.post;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;

/**
 * 
 * @author Jeffrey
 *
 */
public class PostListResult extends RestResult {

	private int pageCount = 0;
	
	private List<PostData> posts = new ArrayList<PostData>();

	public List<PostData> getPosts() {
		return posts;
	}

	public void setPosts(List<PostData> posts) {
		this.posts = posts;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}