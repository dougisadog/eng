package com.shuangge.english.entity.server.read;

public class ReadListData {

	private Long readNo;
	private String title;
	private String imgUrl;
	private String description;

	private int wordNum = 0;
	
	private int coreWordCount = 0; //核心单词数量

	
	/*public ReadListData(String title, String imgUrl, int wordNum) {
		this.title = title;
		this.imgUrl = imgUrl;
		this.wordNum = wordNum;
	}*/
	
	public ReadListData(String title, String imgUrl, String description, int wordNum) {
		super();
		this.title = title;
		this.imgUrl = imgUrl;
		this.description = description;
		this.wordNum = wordNum;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getWordNum() {
		return wordNum;
	}

	public void setWordNum(int wordNum) {
		this.wordNum = wordNum;
	}

	public Long getReadNo() {
		return readNo;
	}

	public void setReadNo(Long readNo) {
		this.readNo = readNo;
	}

	public int getCoreWordCount() {
		return coreWordCount;
	}

	public void setCoreWordCount(int coreWordCount) {
		this.coreWordCount = coreWordCount;
	}

}
