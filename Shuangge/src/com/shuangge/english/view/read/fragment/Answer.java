package com.shuangge.english.view.read.fragment;

import com.shuangge.english.view.read.fragment.FragmentVideoTest.OPTION_TYPE;

public class Answer {
	
	public Answer() {
	}
	
	private boolean isRight = false;
	
	private OPTION_TYPE ot;
	
	private String name;
	
	private String ImageURL;
	
	private String SoundURL;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return ImageURL;
	}

	public void setImageURL(String imageURL) {
		ImageURL = imageURL;
	}


	public OPTION_TYPE getOt() {
		return ot;
	}

	public void setOt(OPTION_TYPE ot) {
		this.ot = ot;
	}

	public String getSoundURL() {
		return SoundURL;
	}

	public void setSoundURL(String soundURL) {
		SoundURL = soundURL;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}
}
