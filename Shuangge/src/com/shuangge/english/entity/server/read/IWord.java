package com.shuangge.english.entity.server.read;

public interface IWord extends Comparable<IWord> {

	public Long getId();
	
	public String getWord();
	
	public String getImgUrl();
	
	public String getSoundUrl();
	
	public String getMnemonics();
	
	public int getFrequency();
	
	public String getTranslation();

}
