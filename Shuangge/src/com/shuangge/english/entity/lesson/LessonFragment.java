package com.shuangge.english.entity.lesson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.utils.Utility;

/**
 * 课程片段
 * @author Jeffrey
 *
 */
public class LessonFragment {

	public static List<List<LessonFragment>> LESSON_PAGES = new ArrayList<List<LessonFragment>>();
	
	private List<LessonData> datas = new ArrayList<LessonData>();

	public List<LessonData> getDatas() {
		return datas;
	}
	
	public static void resetDatas() {
		for (List<LessonFragment> list : LESSON_PAGES) {
			for (LessonFragment lessonFragment : list) {
				for (LessonData lessonData : lessonFragment.datas) {
					lessonData.setResult(null);
				}
			}
		}
	}
	
	public static void parseTxt(File file, String type5Dir) {
		long time = System.currentTimeMillis();
		LESSON_PAGES.clear();
		BufferedReader br = null;
		StringBuilder builder = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line = null;
			while (null != (line = br.readLine())) {
				builder.append(line).append("\n");
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		finally {
			Utility.closeSilently(br);
		}
		parse(builder.toString(), type5Dir);
		System.out.println((System.currentTimeMillis() - time) + "ms");
	}
	
	private static void parse(String str, String type5Dir) {
		if (null == str) {
			return;
		}
		boolean err = false;
		List<LessonFragment> fragments = null;
		LessonFragment fragment = null;
		LessonData lessonData = null; 
		Phrase phrase = null;
		String[] arr = null;
		String[] strs = str.split("\n");
		int count = 0;
		int j = 0;
		int n = 0;
		int m = 0;
		int lessonInFragmentCount = 0;
		List<Integer> lessonInFragmentCounts = new ArrayList<Integer>();
		String phraseId = null;
		
		//0506题型特殊处理
		boolean is0506Type = false; 
		List<Phrase> phrasesFor0506 = new ArrayList<Phrase>(); 
		List<LessonData> lessonDatasFor0506 = new ArrayList<LessonData>(); 
		for (int i = 1; i < strs.length; i++) {
			lessonInFragmentCounts.clear();
			arr = strs[i].split("\t");
			if (arr.length <= 2) {
				continue;
			}
			fragments = new ArrayList<LessonFragment>();
			LESSON_PAGES.add(fragments);
			DebugPrinter.i("in parse page size:" + LessonFragment.LESSON_PAGES.size());
			count = Integer.valueOf(arr[1]);
			if (Integer.valueOf(arr[2]) == 0) {
				for(j = 0; j < count; ++j) {
					lessonInFragmentCounts.add(0);
				}
			}
			else {
				for(j = 0; j < arr[2].length(); ++j) {
					lessonInFragmentCounts.add(Integer.parseInt(arr[2].charAt(j) + ""));
				}
			}
			n = 0;
			fragment = new LessonFragment();
			j = 4;
			
			//0506题型特殊处理
			is0506Type = false;
			phrasesFor0506.clear();
			lessonDatasFor0506.clear();
			for (m = 0; m < count; ++m) {
				lessonData = new LessonData();
				phraseId = arr[j];
				phrase = Phrase.PHRASE_MAP.get(phraseId);
				if (null == phrase) {
					err = true;
				} else {
					phrase.setLocalImgPath(CacheDisk.getLessonPath(type5Dir + phrase.getImgUrl()));
					phrase.setLocalSoundPath(CacheDisk.getLessonPath(type5Dir + phrase.getSoundUrl()));
					lessonData.setAnswer(phrase);
				}
				lessonData.setType(arr[j + 1]);
				//0506题型特殊处理
				if (is0506Type || (is0506Type = (lessonData.getType().indexOf("05") != -1 || lessonData.getType().indexOf("06") != -1))) {
					lessonDatasFor0506.add(lessonData);
				}
				for (String s : arr[j + 2].split("\\|")) {
					phraseId = s;
					phrase = Phrase.PHRASE_MAP.get(phraseId);
					if (null == phrase) {
						err = true;
					} else {
						phrase.setLocalImgPath(CacheDisk.getLessonPath(type5Dir + phrase.getImgUrl()));
						phrase.setLocalSoundPath(CacheDisk.getLessonPath(type5Dir + phrase.getSoundUrl()));
						//0506题型特殊处理
						if (is0506Type) {
							phrasesFor0506.add(phrase);
						}
						else {
							lessonData.getPhrases().add(phrase);
						}
					}
				}
				fragment.getDatas().add(lessonData);
				lessonData.setFragment(fragment);
				lessonInFragmentCount = lessonInFragmentCounts.get(n);
				if (lessonInFragmentCount > 1) {
					lessonInFragmentCounts.set(n, --lessonInFragmentCount) ;
				}
				else {
					fragments.add(fragment);
					++n;
					fragment = new LessonFragment();
				}
				j += 3;
			}
			//0506题型特殊处理
			if (is0506Type) {
				for (LessonData lessonData0506 : lessonDatasFor0506) {
					for (Phrase phrase0506 : phrasesFor0506) {
						lessonData0506.getPhrases().add(phrase0506);
					}
				}
			}
		}
		if (err) {
			DebugPrinter.e("LessonFragment parse err!");
			LESSON_PAGES.clear();
		}
	}

}
