package com.shuangge.english.support.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

	public static boolean isABC(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z]");
		return pattern.matcher(str).matches();
	}
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^\\d+$");
		return pattern.matcher(str).matches();
	}
	
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^\\d+\\.\\d+$");
		return pattern.matcher(str).matches();
	}
	
	public static boolean isRightAccount(String str){
        Pattern p = Pattern.compile("^\\w*[a-zA-Z]+\\w*$");
        Matcher m = p.matcher(str);
        return m.find();
    }
	
	public static boolean isEmail(String str){
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(str);
        return m.find();
    }
	
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false; 
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}

	public static void main(String[] args) {
		System.out.println(isNumeric("012"));
		System.out.println(isDouble(".3"));
	}
}
