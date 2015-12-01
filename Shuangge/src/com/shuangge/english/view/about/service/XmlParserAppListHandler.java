package com.shuangge.english.view.about.service;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.view.about.model.AppDataModel;

public class XmlParserAppListHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private List<AppDataModel> appDataList = new ArrayList<AppDataModel>();
	 	  
	public XmlParserAppListHandler() {
		
	}

	public List<AppDataModel> getDataList() {
		return appDataList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	AppDataModel appDataModel = new AppDataModel();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("appinfo")) {
			appDataModel = new AppDataModel();
			appDataModel.setName(attributes.getValue(0));
			appDataModel.setDetail(attributes.getValue(1));
			appDataModel.setImgUrl(ConfigConstants.RECOMMEND_APP_URL + attributes.getValue(2));
			appDataModel.setAppUrl(attributes.getValue(3));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("appinfo")) {
        	appDataList.add(appDataModel);
        }
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
