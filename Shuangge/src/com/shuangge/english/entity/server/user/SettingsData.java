package com.shuangge.english.entity.server.user;

public class SettingsData {

	private Boolean speechEnabled = false;
	private Integer speechAccuracy = 50;
	private Integer secretMsgAcceptStatus = 0;
	
	private LessonTips lessonTips;

	public Boolean getSpeechEnabled() {
		return speechEnabled;
	}

	public void setSpeechEnabled(Boolean speechEnabled) {
		this.speechEnabled = speechEnabled;
	}

	public Integer getSpeechAccuracy() {
		return speechAccuracy;
	}

	public void setSpeechAccuracy(Integer speechAccuracy) {
		this.speechAccuracy = speechAccuracy;
	}

	public LessonTips getLessonTips() {
		return lessonTips;
	}

	public void setLessonTips(LessonTips lessonTips) {
		this.lessonTips = lessonTips;
	}

	public Integer getSecretMsgAcceptStatus() {
		return secretMsgAcceptStatus;
	}

	public void setSecretMsgAcceptStatus(Integer secretMsgAcceptStatus) {
		this.secretMsgAcceptStatus = secretMsgAcceptStatus;
	}

}
