package com.shuangge.english.entity.server.lesson;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.server.RestResult;


/**
 * 
 * @author Jeffrey
 *
 */
public class LessonDetailsResult extends RestResult {

	private String answers;
	private Integer frequency;
	private List<Type6Data> type6s = new ArrayList<Type6Data>(); 

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public List<Type6Data> getType6s() {
		return type6s;
	}

	public void setType6s(List<Type6Data> type6s) {
		this.type6s = type6s;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

}