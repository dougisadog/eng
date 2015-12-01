package com.shuangge.english.entity.server.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.annotation.SuppressLint;

import com.shuangge.english.entity.server.RestResult;

@SuppressLint("UseSparseArrays")
public class ReadResult extends RestResult {

	private String title;
	private String img;
	private List<ReadContentData> contents = new ArrayList<ReadContentData>();
	private List<ReadQuestionData> questions = new ArrayList<ReadQuestionData>();
	private Map<Long, ReadWordData> wordMap = new HashMap<Long, ReadWordData>();
	private Map<Long, UserWordData> userWordMap = new HashMap<Long, UserWordData>();
	private Set<Long> userWordIds = new TreeSet<Long>();//进度单词
	
	private Set<IWord> coreWords = new HashSet<IWord>();  //缓存 核心单词
	
	private Integer coreWordNums;

	private Long readNo = 0L;
	
	public static int PROGRESS_L1 = 1;
	public static int PROGRESS_L2 = 2;
	public static int PROGRESS_L3 = 3;
	public static int PROGRESS_L4 = 4;
	private Integer progress = 1;
	
	//true答案待重置
	private boolean reset = false;

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

	public Map<Long, ReadWordData> getWordMap() {
		return wordMap;
	}

	public Map<Long, UserWordData> getUserWordMap() {
		return userWordMap;
	}

	public List<ReadQuestionData> getQuestions() {
		return questions;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public Long getReadNo() {
		return readNo;
	}

	public void setReadNo(Long readNo) {
		this.readNo = readNo;
	}


	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Set<Long> getUserWordIds() {
		return userWordIds;
	}

	public void setUserWordIds(Set<Long> userWordIds) {
		this.userWordIds = userWordIds;
	}

	public Set<IWord> getCoreWords() {
		return coreWords;
	}

	public void setCoreWords(Set<IWord> coreWords) {
		this.coreWords = coreWords;
	}

	public Integer getCoreWordNums() {
		return coreWordNums;
	}

	public void setCoreWordNums(Integer coreWordNums) {
		this.coreWordNums = coreWordNums;
	}

}
