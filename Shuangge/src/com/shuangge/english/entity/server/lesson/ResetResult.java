package com.shuangge.english.entity.server.lesson;

import com.shuangge.english.entity.server.RestResult;

public class ResetResult extends RestResult{
	
	private Integer frequency = 1;

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

}
