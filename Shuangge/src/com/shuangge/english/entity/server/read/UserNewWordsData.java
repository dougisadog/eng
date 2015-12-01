package com.shuangge.english.entity.server.read;

/**
 * @author vee
 * @time 2015年9月24日 下午4:35:42
 */
public class UserNewWordsData implements IWord {

	public UserNewWordsData() {
	}
	
	private Long userNewWordNo;
	private Long wordNo;
	private String word;
	private String soundUrl;
	private String imgUrl;
	private String translation;
	private String mnemonics;
	private int frequency = 0;
	
	private Integer weight;
	private Integer state;

	

	public Long getWordNo() {
		return wordNo;
	}

	public void setWordNo(Long wordNo) {
		this.wordNo = wordNo;
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


	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public Long getUserNewWordNo() {
		return userNewWordNo;
	}

	public void setUserNewWordNo(Long userNewWordNo) {
		this.userNewWordNo = userNewWordNo;
	}

	public String getSoundUrl() {
		return soundUrl;
	}

	public void setSoundUrl(String soundUrl) {
		this.soundUrl = soundUrl;
	}


	public String getMnemonics() {
		return mnemonics;
	}

	public void setMnemonics(String mnemonics) {
		this.mnemonics = mnemonics;
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
		return wordNo;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
