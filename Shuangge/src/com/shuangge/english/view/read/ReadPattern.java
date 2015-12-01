package com.shuangge.english.view.read;

/**
 * 核心正则匹配  
 * @author doug
 *
 */
public class ReadPattern {
	
	// 匹配单词
//	public static String PATTERN_WORD_PART = "[a-zA-Z]+[\\-/'’]?[a-zA-Z]+";
	public static String PATTERN_WORD_PART = "[^\\-/'’:0-9\"\u4e00-\u9fa5][a-zA-Z][a-zA-Z0-9\\-/'’]*";
	//匹配 [123-1] 类型id码
	public static String PATTERN_CODE_PART = "\\[(\\d)+(-\\d)?\\]";
	//匹配单词 和带翻译单词
//	public static String PATTERN_STRING = "[a-zA-Z]+[\\-/'’]?[a-zA-Z]+[/'(（]?[，\u4e00-\u9fa5（）；]*";
	public static String PATTERN_STRING = "[^\\-/'’:0-9\"\u4e00-\u9fa5][a-zA-Z][a-zA-Z0-9\\-/'’]*[/'(（]?[，…\u4e00-\u9fa5（）；]*";
	
	//匹配翻译
	public static String PATTERN_STRING_TRANSLATION = "[/'(（]?[，…\u4e00-\u9fa5（）；]+";

}
