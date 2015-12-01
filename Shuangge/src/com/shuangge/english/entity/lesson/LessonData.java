package com.shuangge.english.entity.lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程配置
 * @author Jeffrey
 *
 */
public class LessonData {

	private LessonFragment fragment;
	
	private Phrase answer;
	private String type;
	private List<Phrase> phrases = new ArrayList<Phrase>();
	
	public static final int RESULT_WRONG = 0;
	public static final int RESULT_RIGHT = 1;
	private Integer result;

	public Phrase getAnswer() {
		return answer;
	}

	public void setAnswer(Phrase answer) {
		this.answer = answer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Phrase> getPhrases() {
		return phrases;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public LessonFragment getFragment() {
		return fragment;
	}

	public void setFragment(LessonFragment fragment) {
		this.fragment = fragment;
	}
	
}
