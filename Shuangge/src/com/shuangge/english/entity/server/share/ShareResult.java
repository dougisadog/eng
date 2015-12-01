package com.shuangge.english.entity.server.share;

import com.shuangge.english.entity.server.RestResult;

public class ShareResult extends RestResult {

	private Long shareResultNo;
	private Integer shareScore = 0;

	public Long getShareResultNo() {
		return shareResultNo;
	}

	public void setShareResultNo(Long shareResultNo) {
		this.shareResultNo = shareResultNo;
	}

	public Integer getShareScore() {
		return shareScore;
	}

	public void setShareScore(Integer shareScore) {
		this.shareScore = shareScore;
	}

	
}
