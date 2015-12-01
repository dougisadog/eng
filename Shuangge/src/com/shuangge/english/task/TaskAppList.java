package com.shuangge.english.task;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoResType2;
import com.shuangge.english.dao.DaoResType4;
import com.shuangge.english.entity.lesson.EntityResType1;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.IResType;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqHelper;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.support.utils.Utility;
import com.shuangge.english.view.about.model.AppDataModel;
import com.shuangge.english.view.about.service.XmlParserAppListHandler;

public class TaskAppList extends BaseTask<AssetManager, Void, List<AppDataModel>> {
	
	
	private List<AppDataModel> result = null;
	
	public TaskAppList(int tag, CallbackNoticeView<Void, List<AppDataModel>> callback, AssetManager... params) {
		super(tag, callback, params);
	}

	@Override
	protected List<AppDataModel> doInBackground(AssetManager... params) {
    	
		HttpReqFactory.createDownloadFileReq(new HttpReqHelper.DownloadBinaryFileListener() {
			
			@Override
			public void progressHandler(long progress, long max) {
			}
			
			@Override
			public void errorHandler(Exception e, String errMsg) {
				DebugPrinter.e("TaskCheckCatalog:" + e);
	        	result = null;
			}
			
			@Override
			public void completeHandler(byte[] bytes) {
				InputStream input = new ByteArrayInputStream(bytes);
				try {
					SAXParserFactory spf = SAXParserFactory.newInstance();
		            SAXParser parser = spf.newSAXParser();
		            XmlParserAppListHandler handler = new XmlParserAppListHandler();
					parser.parse(input, handler);
					result = handler.getDataList();
				}
				catch (Exception e) {
		        	e.printStackTrace();
		        	DebugPrinter.e("TaskCheckCatalog:" + e);
		        	result = null;
		        } 
				finally {
					Utility.closeSilently(input);
					
				}
			}
		}, ConfigConstants.RECOMMEND_APP_URL + "applist_data.xml");
		
		return result;
	}
	
}
