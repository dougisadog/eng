package com.shuangge.english.entity.server.read;


public class ReadWordData implements IWord {

	public ReadWordData() {
	}

	//private Long id;  //8月17号  由id 改成了 readNo
	private Long readNo;   
	private String word;
	private String translation;
	private String imgUrl;
	private String soundUrl;
	private String mnemonics;
	private int level = 0;
	private int type = 0;
	private int frequency = 0;
	
	private boolean core4 = false;
	private boolean core6 = false;


	public boolean isCore4() {
		return core4;
	}

	public void setCore4(boolean core4) {
		this.core4 = core4;
	}

	public boolean isCore6() {
		return core6;
	}

	public void setCore6(boolean core6) {
		this.core6 = core6;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getTranslation() {
		String translation = this.translation.replace("，，，","…").replace(",,,","…");
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Long getReadNo() {
		return readNo;
	}

	public void setReadNo(Long readNo) {
		this.readNo = readNo;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSoundUrl() {
		return soundUrl;
	}

	public void setSoundUrl(String soundUrl) {
		this.soundUrl = soundUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMnemonics() {
		return mnemonics;
	}

	public void setMnemonics(String mnemonics) {
		this.mnemonics = mnemonics;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public int compareTo(IWord another) {
		int result = another.getFrequency() - frequency;
		if (result > 0)
			return 1;
		if (result < 0)
			return -1;
		return 0;
	}

	@Override
	public Long getId() {
		return readNo;
	}

}
