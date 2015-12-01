package com.shuangge.english.entity.server.lesson;

import com.shuangge.english.entity.server.RestResult;

public class LessonKeyResult extends RestResult{
	
	private Integer keyNum;
	private LessonData lessonData;

	public Integer getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(Integer keyNum) {
		this.keyNum = keyNum;
	}

	public LessonData getLessonData() {
		return lessonData;
	}

	public void setLessonData(LessonData lessonData) {
		this.lessonData = lessonData;
	}

}
