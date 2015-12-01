package com.shuangge.english.entity.server.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Read {

	private String title;
	private String img;
	private List<ReadContentData> contents = new ArrayList<ReadContentData>();
	private Map<String, ReadWordData> words = new HashMap<String, ReadWordData>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<ReadContentData> getContents() {
		return contents;
	}

	public void setContents(List<ReadContentData> contents) {
		this.contents = contents;
	}

	public Map<String, ReadWordData> getWords() {
		return words;
	}

	public void setWords(Map<String, ReadWordData> words) {
		this.words = words;
	}

}
