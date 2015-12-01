package com.shuangge.english.entity.server.read;

import java.util.ArrayList;
import java.util.List;

public class ReadQuestionData {

	private String question;
	private String options;
	private int helpIndex = 0;
	private int rightIndex = 0;
	private int selectedIndex = -1;

	public static int ORGIN_SELECTED_STATE = -1;

	private String simpleQuestion;
	private List<String> answers = new ArrayList<String>();

	public String getSimpleQuestion() {
		return simpleQuestion;
	}

	public void setSimpleQuestion(String simpleQuestion) {
		this.simpleQuestion = simpleQuestion;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}


	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public int getHelpIndex() {
		return helpIndex;
	}

	public void setHelpIndex(int helpIndex) {
		this.helpIndex = helpIndex;
	}

	public int getRightIndex() {
		return rightIndex;
	}

	public void setRightIndex(int rightIndex) {
		this.rightIndex = rightIndex;
	}
	
	public boolean isRight() {
		return rightIndex - 1 == selectedIndex;
	}

}