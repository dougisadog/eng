package com.shuangge.english.entity.server.lesson;

import java.util.HashMap;
import java.util.Map;

public class LessonData {

	private Map<String, Type1Data> type1s = new HashMap<String, Type1Data>();
	private Map<String, Type2Data> type2s = new HashMap<String, Type2Data>();
	private Map<String, Type3Data> type3s = new HashMap<String, Type3Data>();
	private Map<String, Type4Data> type4s = new HashMap<String, Type4Data>();
	private Map<String, Type5Data> type5s = new HashMap<String, Type5Data>();

	public Map<String, Type1Data> getType1s() {
		return type1s;
	}

	public void setType1s(Map<String, Type1Data> type1s) {
		this.type1s = type1s;
	}

	public Map<String, Type2Data> getType2s() {
		return type2s;
	}

	public void setType2s(Map<String, Type2Data> type2s) {
		this.type2s = type2s;
	}

	public Map<String, Type3Data> getType3s() {
		return type3s;
	}

	public void setType3s(Map<String, Type3Data> type3s) {
		this.type3s = type3s;
	}

	public Map<String, Type4Data> getType4s() {
		return type4s;
	}

	public void setType4s(Map<String, Type4Data> type4s) {
		this.type4s = type4s;
	}

	public Map<String, Type5Data> getType5s() {
		return type5s;
	}

	public void setType5s(Map<String, Type5Data> type5s) {
		this.type5s = type5s;
	}

}
