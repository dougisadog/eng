package com.shuangge.english.entity.server.read;

import com.shuangge.english.entity.server.RestResult;

public class ReadWordResult extends RestResult {

	private int wordNum = 0;

	public int getWordNum() {
		return wordNum;
	}

	public void setWordNum(int wordNum) {
		this.wordNum = wordNum;
	}
	
}
