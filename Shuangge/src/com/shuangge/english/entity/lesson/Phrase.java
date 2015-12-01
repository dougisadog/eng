package com.shuangge.english.entity.lesson;

import java.util.HashMap;
import java.util.Map;

import com.shuangge.english.support.utils.ZipUtils;

public class Phrase {

	public static Map<String, Phrase> PHRASE_MAP = new HashMap<String, Phrase>();

	public Phrase(String id, String content, String soundUrl, String imgUrl) {
		this.id = id;
		this.content = content;
		this.soundUrl = soundUrl;
		this.imgUrl = imgUrl;
	}

	private String id;
	private String content;
	private String soundUrl;
	private String imgUrl;
	private Blanks blanks;
	private WriteBlanks writeBlanks;
	
	private String localImgPath;
	private String localSoundPath; 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSoundUrl() {
		return soundUrl;
	}

	public void setSoundUrl(String soundUrl) {
		this.soundUrl = soundUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Blanks getBlanks() {
		return blanks;
	}

	public void setBlanks(Blanks blanks) {
		this.blanks = blanks;
	}

	public WriteBlanks getWriteBlanks() {
		return writeBlanks;
	}

	public void setWriteBlanks(WriteBlanks writeBlanks) {
		this.writeBlanks = writeBlanks;
	}

	public String getLocalImgPath() {
		return localImgPath;
	}

	public void setLocalImgPath(String localImgPath) {
		this.localImgPath = localImgPath;
	}

	public String getLocalSoundPath() {
		return localSoundPath;
	}

	public void setLocalSoundPath(String localSoundPath) {
		this.localSoundPath = localSoundPath;
	}

	public boolean isMp4() {
		return null != imgUrl && imgUrl.endsWith("mp4");
	}
	
	public static class Blanks {

		private int count;
		private String[] options1;
		private String[] options2;
		
		public Blanks(int count, String[] options1) {
			this.count = count;
			this.options1 = options1;
		}
		
		public Blanks(int count, String[] options1, String[] options2) {
			this.count = count;
			this.options1 = options1;
			this.options2 = options2;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String[] getOptions1() {
			return options1;
		}

		public void setOptions1(String[] options1) {
			this.options1 = options1;
		}

		public String[] getOptions2() {
			return options2;
		}

		public void setOptions2(String[] options2) {
			this.options2 = options2;
		}

	}

	public static class WriteBlanks {

		private int count;
		private String[] words;
		
		public WriteBlanks(int count, String[] words) {
			this.count = count;
			this.words = words;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String[] getWords() {
			return words;
		}

		public void setWords(String[] words) {
			this.words = words;
		}

	}

	public static void parse(String path) {
		long time = System.currentTimeMillis();
		PHRASE_MAP.clear();
		String str = ZipUtils.getTextFromZipFile(path, "phrase.txt");
		if (null == str) {
			return;
		}
		Phrase phrase = null;
		String[] arr = null;
		String[] arr1 = null;
		String[] strs = str.split("\r\n");
		for (int i = 1; i < strs.length; i++) {
			arr = strs[i].split("\t");
			phrase = new Phrase(arr[0], arr[1], "snd/" + arr[2], "img/" + arr[3]);
			PHRASE_MAP.put(phrase.getId(), phrase);
		}
		
		int count = 0;
		str = ZipUtils.getTextFromZipFile(path, "blanks.txt");
		strs = str.split("\r\n");
		for (int i = 1; i < strs.length; i++) {
			arr = strs[i].split("\t");
			arr1 = arr[2].split("\\|");
			count = Integer.valueOf(arr[1]);
			String[] options = new String[arr1.length];
			for (int j = 0; j < arr1.length; j++) {
				options[j] = arr1[j].trim();
			}
			phrase = PHRASE_MAP.get(arr[0]);
			if (null == phrase) {
				continue;
			}
			if (count > 1) {
				arr1 = arr[3].split("\\|");
				String[] options2 = new String[arr1.length];
				for (int j = 0; j < arr1.length; j++) {
					options2[j] = arr1[j].trim();
				}
				phrase.blanks = new Blanks(count, options, options2);
			}
			else {
				phrase.blanks = new Blanks(count, options);
			}
		}
		
		str = ZipUtils.getTextFromZipFile(path, "writeBlanks.txt");
		strs = str.split("\r\n");
		for (int i = 1; i < strs.length; i++) {
			arr = strs[i].split("\t");
			if (arr.length < 4) {
				continue;
			}
			count = Integer.valueOf(arr[2]);
			String[] words = new String[arr.length == 4 ? 1 : 2];
			words[0] = arr[3].trim();
			if (arr.length == 5) {
				words[1] = arr[4].trim();
			}
			phrase = PHRASE_MAP.get(arr[0]);
			if (null == phrase) {
				continue;
			}
			phrase.writeBlanks = new WriteBlanks(count, words);
		}
		System.out.println((System.currentTimeMillis() - time) + "ms");
	}
	
}
