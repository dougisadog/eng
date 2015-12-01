package com.shuangge.english.entity.server.shop;

import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.LessonData;

public class ObtainLessonResult extends RestResult {

	private LessonData lessonData;

	public LessonData getLessonData() {
		return lessonData;
	}

	public void setLessonData(LessonData lessonData) {
		this.lessonData = lessonData;
	}
	
}

