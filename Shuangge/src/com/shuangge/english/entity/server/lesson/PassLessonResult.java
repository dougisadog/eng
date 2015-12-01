package com.shuangge.english.entity.server.lesson;

public class PassLessonResult extends UnlockResult {

	private Type5Data type5;

	private UserScoreData userScoreData;

	private SummaryData summaryData;
	
	public UserScoreData getUserScoreData() {
		return userScoreData;
	}

	public void setUserScoreData(UserScoreData userScoreData) {
		this.userScoreData = userScoreData;
	}

	public SummaryData getSummaryData() {
		return summaryData;
	}

	public void setSummaryData(SummaryData summaryData) {
		this.summaryData = summaryData;
	}

	public Type5Data getType5() {
		return type5;
	}

	public void setType5(Type5Data type5) {
		this.type5 = type5;
	}

}
