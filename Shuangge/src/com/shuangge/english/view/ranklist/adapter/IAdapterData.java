package com.shuangge.english.view.ranklist.adapter;

public interface IAdapterData {

	public static final int TYPE_USER = 0;
	public static final int TYPE_CLASS = 1;
	
	public Long getIndex();
	
	public int getType();
	
	public Integer getNo();

	public String getName();

	public Integer getScore();

	public String getHeadUrl();

}
