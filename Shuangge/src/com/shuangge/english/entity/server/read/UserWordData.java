package com.shuangge.english.entity.server.read;

public class UserWordData {

	private Long id;
	private boolean enable = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
