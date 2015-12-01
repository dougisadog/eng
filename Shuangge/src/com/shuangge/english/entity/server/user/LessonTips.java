package com.shuangge.english.entity.server.user;

public class LessonTips {

	private Boolean type01 = false;
	private Boolean type02 = false;
	private Boolean type03 = false;
	private Boolean type05 = false;
	private Boolean type06 = false;
	private Boolean type07 = false;
	private Boolean type08 = false;
	private Boolean type10 = false;
	private Boolean type11 = false;
	private Boolean type13 = false;
	private Boolean type14 = false;
	
	private String core46 = "000000";
	private String keyTip;

	public Boolean getType01() {
		return type01;
	}

	public void setType01(Boolean type01) {
		this.type01 = type01;
	}

	public Boolean getType02() {
		return type02;
	}

	public void setType02(Boolean type02) {
		this.type02 = type02;
	}

	public Boolean getType03() {
		return type03;
	}

	public void setType03(Boolean type03) {
		this.type03 = type03;
	}

	public Boolean getType05() {
		return type05;
	}

	public void setType05(Boolean type05) {
		this.type05 = type05;
	}

	public Boolean getType06() {
		return type06;
	}

	public void setType06(Boolean type06) {
		this.type06 = type06;
	}

	public Boolean getType07() {
		return type07;
	}

	public void setType07(Boolean type07) {
		this.type07 = type07;
	}

	public Boolean getType08() {
		return type08;
	}

	public void setType08(Boolean type08) {
		this.type08 = type08;
	}

	public Boolean getType10() {
		return type10;
	}

	public void setType10(Boolean type10) {
		this.type10 = type10;
	}

	public Boolean getType11() {
		return type11;
	}

	public void setType11(Boolean type11) {
		this.type11 = type11;
	}

	public Boolean getType13() {
		return type13;
	}

	public void setType13(Boolean type13) {
		this.type13 = type13;
	}

	public Boolean getType14() {
		return type14;
	}

	public void setType14(Boolean type14) {
		this.type14 = type14;
	}

	public String getCore46() {
		return core46;
	}

	public void setCore46(String core46) {
		this.core46 = core46;
	}
	

	public Boolean getReadHome() {
		String[] core46s = core46.split("");  //  "" 为 第0位
		return core46s[1].equals("1");
	}

	public void setReadHome(Boolean readHome) {
		String[] core46s = core46.split("");
		core46s[1] = readHome ? "1" : "0";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < core46s.length; i++) {
			sb.append(core46s[i]);
		}
		this.core46 = sb.toString();
	}

	public Boolean getReadLLK() {
		String[] core46s = core46.split("");
		return core46s[2].equals("1");
	}

	public void setReadLLK(Boolean readLLK) {
		String[] core46s = core46.split("");
		core46s[2] = readLLK ? "1" : "0";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < core46s.length; i++) {
			sb.append(core46s[i]);
		}
		this.core46 = sb.toString();
	}

	public Boolean getRead() {
		String[] core46s = core46.split("");
		return core46s[3].equals("1");
	}

	public void setRead(Boolean read) {
		String[] core46s = core46.split("");
		core46s[3] = read ? "1" : "0";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < core46s.length; i++) {
			sb.append(core46s[i]);
		}
		this.core46 = sb.toString();
	}

	public Boolean getWordList() {
		String[] core46s = core46.split("");
		return core46s[4].equals("1");
	}

	public void setWordList(Boolean wordList) {
		String[] core46s = core46.split("");
		core46s[4] = wordList ? "1" : "0";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < core46s.length; i++) {
			sb.append(core46s[i]);
		}
		this.core46 = sb.toString();
	}

	public Boolean getReadQuestion() {
		String[] core46s = core46.split("");
		return core46s[5].equals("1");
	}

	public void setReadQuestion(Boolean readQuestion) {
		String[] core46s = core46.split("");
		core46s[5] = readQuestion ? "1" : "0";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < core46s.length; i++) {
			sb.append(core46s[i]);
		}
		this.core46 = sb.toString();
	}

	public Boolean getReadQuestionMenu() {
		String[] core46s = core46.split("");
		return core46s[6].equals("1");
	}

	public void setReadQuestionMenu(Boolean readQuestionMenu) {
		String[] core46s = core46.split("");
		core46s[6] = readQuestionMenu ? "1" : "0";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < core46s.length; i++) {
			sb.append(core46s[i]);
		}
		this.core46 = sb.toString();
	}

	public String getKeyTip() {
		return keyTip;
	}

	public void setKeyTip(String keyTip) {
		this.keyTip = keyTip;
	}

	

}
