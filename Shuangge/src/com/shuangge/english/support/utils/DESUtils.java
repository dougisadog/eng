package com.shuangge.english.support.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import android.text.TextUtils;
import android.util.Base64;

import com.shuangge.english.config.ConfigConstants.RewardsCode;
import com.shuangge.english.support.http.HttpReqFactory;

public class DESUtils {

	private static String charSet = "utf-8";

	private static String key = "07882f106f9f93929447b3895ab058fd";

	private static final int TYPE_STRING = 0;
	private static final int TYPE_INT = 1;
	private static final int TYPE_DOUBLE = 2;
	private static final int TYPE_BOOLEAN = 3;
	private static final int TYPE_DATE = 4;
	private static final int TYPE_STRING_ARR = 10;
	private static final int TYPE_INT_ARR = 11;
	private static final int TYPE_DOUBLE_ARR = 12;
	private static final int TYPE_BOOLEAN_ARR = 13;
	private static final int TYPE_DATE_ARR = 14;
	private static final int TYPE_ENUM_REWARDS_CODE = 21;
	
	public enum ParamType {
		
		base, rewardsCode;
		
	}

	private static String encryptStr(HttpReqFactory.ReqParam[] params) {
		final String PARAMS_SYMBOL = "#@@#";
		final String LINk_SYMBOL = "#==#";
		String str = "";
		ArrayList<?> list = null;
		ParamType paramType = ParamType.base;
		for (HttpReqFactory.ReqParam param : params) {
			boolean isDate = false;
			if (null == param.getValue()) {
				continue;
			}
			Class<?> clazz = param.getValue().getClass();
			if (clazz.equals(String.class)) {
				if (TextUtils.isEmpty(param.getValue().toString())) {
					continue;
				}
				str += param.getKey() + LINk_SYMBOL + TYPE_STRING + LINk_SYMBOL + param.getValue();
			}
			else if (clazz.equals(Integer.class) || clazz.equals(Long.class)) {
				str += param.getKey() + LINk_SYMBOL + TYPE_INT + LINk_SYMBOL + param.getValue();
			}
			else if (clazz.equals(Double.class)) {
				str += param.getKey() + LINk_SYMBOL + TYPE_DOUBLE + LINk_SYMBOL + param.getValue();
			} 
			else if (clazz.equals(Boolean.class)) {
				str += param.getKey() + LINk_SYMBOL + TYPE_BOOLEAN + LINk_SYMBOL + param.getValue();
			}
			else if (clazz.equals(Date.class)) {
				str += param.getKey() + LINk_SYMBOL + TYPE_DATE + LINk_SYMBOL + ((Date) param.getValue()).getTime();
			}
			else if (clazz.equals(RewardsCode.class)) {
				str += param.getKey() + LINk_SYMBOL + TYPE_ENUM_REWARDS_CODE + LINk_SYMBOL + ((RewardsCode) param.getValue()).name();
			}
			else if (clazz.equals(ArrayList.class)) {
				list = (ArrayList<?>) param.getValue();
				if (list.size() == 0) {
					continue;
				}
				if (null == list.get(0)) {
					clazz = String.class;
				}
				else {
					clazz = list.get(0).getClass();
				}
				if (clazz.equals(String.class)) {
					if (TextUtils.isEmpty(param.getValue().toString())) {
						continue;
					}
					str += param.getKey() + LINk_SYMBOL + TYPE_STRING_ARR + LINk_SYMBOL;
				}
				else if (clazz.equals(Integer.class) || clazz.equals(Long.class)) {
					str += param.getKey() + LINk_SYMBOL + TYPE_INT_ARR + LINk_SYMBOL;
				}
				else if (clazz.equals(Double.class)) {
					str += param.getKey() + LINk_SYMBOL + TYPE_DOUBLE_ARR + LINk_SYMBOL;
				} 
				else if (clazz.equals(Boolean.class)) {
					str += param.getKey() + LINk_SYMBOL + TYPE_BOOLEAN_ARR + LINk_SYMBOL;
				}
				else if (clazz.equals(Date.class)) {
					str += param.getKey() + LINk_SYMBOL + TYPE_DATE_ARR + LINk_SYMBOL;
					isDate = true;
				}
				else if (clazz.equals(RewardsCode.class)) {
					str += param.getKey() + LINk_SYMBOL + TYPE_ENUM_REWARDS_CODE + LINk_SYMBOL;
					paramType = ParamType.rewardsCode;
				}
				for (int i = 0; i < list.size(); i++) {
					if (i != 0) {
						str += ",";
					}
					if (isDate) {
						str += ((Date) list.get(i)).getTime();
						continue;
					}
					switch (paramType) {
					case rewardsCode:
						str += ((RewardsCode) list.get(i)).name();
						continue;
					default:
						str += list.get(i);
						continue;
					}
				}
			}
			str += PARAMS_SYMBOL;
		}
		return str;
	}
	
	public static HttpReqFactory.ReqParam encryptFromParams(HttpReqFactory.ReqParam[] params) {
		String paramStr = null;
		paramStr = encryptStr(params);
		paramStr = encrypt(paramStr);
		return new HttpReqFactory.ReqParam("params", paramStr);
	}

	public static String encrypt(String str) {
//		if (true) {
//			return str;
//		}
		String string = null;
		byte[] b = null;
		try {
			string = Base64.encodeToString(str.getBytes(charSet),
					Base64.DEFAULT);
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(charSet));
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			b = cipher.doFinal(string.getBytes());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		if (null == b)
			return "";
		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	public static String decrypt(String str) {
//		if (true) {
//			return str;
//		}
		byte[] bytesrc = null;
		DESedeKeySpec dks = null;
		String val = "";
		try {
			bytesrc = Base64.decode(str.getBytes(charSet), Base64.DEFAULT);
			dks = new DESedeKeySpec(key.getBytes(charSet));
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			byte[] retByte = cipher.doFinal(bytesrc);
			String string = new String(retByte);
			byte[] b = Base64.decode(string.getBytes(charSet), Base64.DEFAULT);
			val = new String(b, charSet);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return val;
	}

}
