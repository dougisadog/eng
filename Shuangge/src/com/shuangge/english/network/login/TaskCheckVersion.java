package com.shuangge.english.network.login;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Xml;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqHelper.HttpReqListener;
import com.shuangge.english.support.service.BaseTask;

public class TaskCheckVersion extends BaseTask<Void, Void, Integer>{
	
	public static final int NO_NETWORK = 10;
	public static final int NO_UPDATE = 0;
	public static final int UPDATE = 1;
	public static final int FORCED_UPDATE = 2;
	
	public TaskCheckVersion(int tag, CallbackNoticeView<Void, Integer> callback, Void... params) {
		super(tag, callback, params);
	}

	private String jsonData;
	
	@Override
	protected Integer doInBackground(Void... params) {
		return checkVersion();
	}
	
	public int checkVersion() {
		HttpReqFactory.createGetReq(new HttpReqListener() {
			@Override
			public void errorHandler(Exception e, String errMsg) {
			}
			@Override
			public void completeHandler(String result) {
				jsonData = result;
			}
		}, ConfigConstants.CONFIG_URL);
		if (TextUtils.isEmpty(jsonData)) {
			return NO_NETWORK;
		}
		XmlPullParser parser = Xml.newPullParser();
		try {
//			parser.setInput(new StringReader(jsonData));
			ByteArrayInputStream is = new ByteArrayInputStream(jsonData.getBytes());
			parser.setInput(is, "UTF-8");
			String[] versions = parseVersionXml(parser);
			String localVersion = AppInfo.APP_VERSION_NAME;
			if (versions[1].compareTo(ConfigConstants.FORCED_UPDATE_NUM) > 0) {
				return FORCED_UPDATE;
			}
			if (versions[0].compareTo(localVersion) > 0) {
				return UPDATE;
			}
			return NO_UPDATE;
		} 
		catch (Exception e) {
			DebugPrinter.e("checkVersion "+ e.getMessage());
		}
		return NO_UPDATE;
	}

	private String[] parseVersionXml(XmlPullParser parser) {
		
		String[] versions = new String[2];
		
		try {
			// 开始解析事件
			int eventType = parser.getEventType();
			// 处理事件，不碰到文档结束就一直处理
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// 因为定义了一堆静态常量，所以这里可以用switch
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						// 给当前标签起个名字
						String tagName = parser.getName();
						DebugPrinter.i(tagName);
						String tagText = parser.getText();
						if (tagName.equals("config")) {
	
						} 
						else if (tagName.equals("versionNum")) {
							eventType = parser.next();
							tagText = parser.getText();
							versions[0] = tagText;
							eventType = XmlPullParser.END_DOCUMENT;
						}
						else if (tagName.equals("versionTip")) {
							eventType = parser.next();
							ConfigConstants.VERSION_TIP = parser.getText();
							eventType = XmlPullParser.END_DOCUMENT;
						}
						else if (tagName.equals("forcedUpdateNum")) {
							eventType = parser.next();
							tagText = parser.getText();
							versions[1] = tagText;
							eventType = XmlPullParser.END_DOCUMENT;
						}
						else if (tagName.equals("forcedUpdateTip")) {
							eventType = parser.next();
							ConfigConstants.FORCED_UPDATE_TIP = parser.getText();
							eventType = XmlPullParser.END_DOCUMENT;
						}
						else if (tagName.equals("apkUrl")) {
							eventType = parser.next();
							ConfigConstants.APP_URL = parser.getText();
						}
						else if (tagName.equals("shareImg")) {
							eventType = parser.next();
							ConfigConstants.SHARE_IMG = parser.getText();
						}
						else if (tagName.equals("shareTitle")) {
							eventType = parser.next();
							ConfigConstants.SHARE_TITLE = parser.getText();
						}
						else if (tagName.equals("shareUrl")) {
							eventType = parser.next();
							ConfigConstants.SHARE_URL = parser.getText();
						}
						else if (tagName.equals("shareContent")) {
							eventType = parser.next();
							ConfigConstants.SHARE_CONTENT = parser.getText();
						}
						else if (tagName.equals("shareImg2")) {
							eventType = parser.next();
							ConfigConstants.SHARE_IMG2 = parser.getText();
						}
						else if (tagName.equals("shareTitle2")) {
							eventType = parser.next();
							ConfigConstants.SHARE_TITLE2 = parser.getText();
						}
						else if (tagName.equals("shareUrl2")) {
							eventType = parser.next();
							ConfigConstants.SHARE_URL2 = parser.getText();
						}
						else if (tagName.equals("shareContent2")) {
							eventType = parser.next();
							ConfigConstants.SHARE_CONTENT2 = parser.getText();
						}
						else if (tagName.equals("shareBtn")) {
							eventType = parser.next();
							ConfigConstants.SHARE_BTN = parser.getText();
						}
						else if (tagName.equals("giftContent")) {
							eventType = parser.next();
							ConfigConstants.GIFT_CONTENT = parser.getText();
						}
						else if (tagName.equals("giftVersion")) {
							eventType = parser.next();
							try {
								ConfigConstants.GIFT_VERSION = Integer.valueOf(parser.getText());
							}
							catch (Exception e) {
								ConfigConstants.GIFT_VERSION = 1;
							}
						}
						else if (tagName.equals("qqGroupKey")) {
							eventType = parser.next();
							ConfigConstants.QQGROUP_KEY = parser.getText();
						}
						else if (tagName.equals("qqGroupUrl")) {
							eventType = parser.next();
							ConfigConstants.QQGROUP_URL = parser.getText();
						}
						else if (tagName.equals("qqGroupNumber")) {
							eventType = parser.next();
							ConfigConstants.QQGROUP_NUMBER = parser.getText();
						}
						else if (tagName.equals("faqUrl")) {
							eventType = parser.next();
							ConfigConstants.RES_FAQ_URL = parser.getText();
						}
						else if (tagName.equals("systemNoticeUrl")) {
							eventType = parser.next();
							ConfigConstants.RES_NOTICE_URL = parser.getText();
						}
						else if (tagName.equals("noticeVersion")) {
							eventType = parser.next();
							ConfigConstants.NOTICE_VERSION = Integer.valueOf(parser.getText());
						}
						else if (tagName.equals("rewardsVersion")) {
							eventType = parser.next();
							ConfigConstants.REWARDS_VERSION = Integer.valueOf(parser.getText());
						}
						else if (tagName.equals("shareImg3")) {
							eventType = parser.next();
							ConfigConstants.SHARE_IMG3 = parser.getText();
						}
						else if (tagName.equals("shareTitle3")) {
							eventType = parser.next();
							ConfigConstants.SHARE_TITLE3 = parser.getText();
						}
						else if (tagName.equals("shareContent3")) {
							eventType = parser.next();
							ConfigConstants.SHARE_CONTENT3 = parser.getText();
						}
						else if (tagName.equals("shareUrl3")) {
							eventType = parser.next();
							ConfigConstants.SHARE_URL3 = parser.getText();
						}
						else if (tagName.equals("shareImg4")) {
							eventType = parser.next();
							ConfigConstants.SHARE_IMG4 = parser.getText();
						}
						else if (tagName.equals("shareTitle4")) {
							eventType = parser.next();
							ConfigConstants.SHARE_TITLE4 = parser.getText();
						}
						else if (tagName.equals("shareContent4")) {
							eventType = parser.next();
							ConfigConstants.SHARE_CONTENT4 = parser.getText();
						}
						else if (tagName.equals("shareUrl4")) {
							eventType = parser.next();
							ConfigConstants.SHARE_URL4 = parser.getText();
						}
						else if (tagName.equals("shareUrl5")) {
							eventType = parser.next();
							ConfigConstants.SHARE_URL5 = parser.getText();
						}
						else if (tagName.equals("shareUrl6")) {
							eventType = parser.next();
							ConfigConstants.SHARE_URL6 = parser.getText();
						}
						else if (tagName.equals("shareTitle6")) {
							eventType = parser.next();
							ConfigConstants.SHARE_TITLE6 = parser.getText();
						}
						else if (tagName.equals("shareContent6")) {
							eventType = parser.next();
							ConfigConstants.SHARE_CONTENT6 = parser.getText();
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					case XmlPullParser.END_DOCUMENT:
						break;
				}
				eventType = parser.next();
			}
		} 
		catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return versions;
	}
	
	
}
