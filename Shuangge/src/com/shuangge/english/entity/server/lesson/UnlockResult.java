package com.shuangge.english.entity.server.lesson;

import com.shuangge.english.entity.server.RestResult;

public class UnlockResult extends RestResult {

	private LessonData lessonData;

	public LessonData getLessonData() {
		return lessonData;
	}

	public void setLessonData(LessonData lessonData) {
		this.lessonData = lessonData;
	}
	
}
