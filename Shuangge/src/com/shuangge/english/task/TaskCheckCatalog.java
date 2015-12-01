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

public class TaskCheckCatalog extends BaseTask<AssetManager, Void, Boolean> {
	
	private static Map<String, EntityResType2> LOCAL_TYPE2S_MAP = new HashMap<String, EntityResType2>();
	private static Map<String, EntityResType4> LOCAL_TYPE4S_MAP = new HashMap<String, EntityResType4>();
	
	private boolean result = true;
	
	public TaskCheckCatalog(int tag, CallbackNoticeView<Void, Boolean> callback, AssetManager... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(AssetManager... params) {
    	
    	GlobalResTypes.ALL_TYPE1S_MAP.clear();
    	DaoResType2 dao2 = new DaoResType2();
		List<EntityResType2> type2s = dao2.getAll();
		for (EntityResType2 type2 : type2s) {
			LOCAL_TYPE2S_MAP.put(type2.getId(), type2);
		}
		
		HttpReqFactory.createDownloadFileReq(new HttpReqHelper.DownloadBinaryFileListener() {
			
			@Override
			public void progressHandler(long progress, long max) {
			}
			
			@Override
			public void errorHandler(Exception e, String errMsg) {
				DebugPrinter.e("TaskCheckCatalog:" + e);
	        	result = false;
			}
			
			@Override
			public void completeHandler(byte[] bytes) {
				InputStream input = new ByteArrayInputStream(bytes);
				try {
					SAXParserFactory spf = SAXParserFactory.newInstance();
		            SAXParser parser = spf.newSAXParser();
		            XmlParserHandler handler = new XmlParserHandler();
					parser.parse(input, handler);
				}
				catch (Exception e) {
		        	e.printStackTrace();
		        	DebugPrinter.e("TaskCheckCatalog:" + e);
		        	result = false;
		        } 
				finally {
					Utility.closeSilently(input);
				}
			}
		}, ConfigConstants.RES_URL + "catalog.xml");
		
		DaoResType4 dao4 = new DaoResType4();
		List<EntityResType4> type4s = dao4.getAll();
		for (EntityResType4 type4 : type4s) {
			LOCAL_TYPE4S_MAP.put(type4.getId(), type4);
		}
		
		for (EntityResType4 type4 : type4s) {
			parseType4Data(type4);
		}
		if (GlobalResTypes.ALL_TYPE2S_MAP.size() == 0) {
			DebugPrinter.e("数据错误 TaskCheckCatalog ALL_TYPE2S_MAP size = 0");
			result = false;
		}
		return result;
	}

	private EntityResType2 parseType2Data(EntityResType2 serverEntity) {
		File file = serverEntity.getLocalFile();
		EntityResType2 localEntity = LOCAL_TYPE2S_MAP.get(serverEntity.getId());
		if (null == localEntity) {
			localEntity = serverEntity;
		}
		boolean needUpdate = false;
		localEntity.setParentId(serverEntity.getParentId());
		localEntity.setStatus(EntityResType4.STATUS_NORMAL);
		if (file.exists()) {
			long fileLength = file.length();
			if (localEntity.getVersion() < serverEntity.getVersion()) {
				file.delete();
				localEntity.setProgress(0);
				localEntity.setMax(1);
				needUpdate = true;
			}
			else {
				localEntity.setProgress(fileLength);
				if (fileLength == localEntity.getMax()) {
					localEntity.setStatus(EntityResType4.STATUS_FINISH);
				}
			}
		}
		localEntity.setVersion(serverEntity.getVersion());
		
		if (needUpdate) {
			DaoResType2 dao2 = new DaoResType2();
			dao2.update(localEntity.getId(), localEntity);
		}
		
		//更新内存
		GlobalResTypes.ALL_TYPES_MAP.put(localEntity.getId(), localEntity);
		if (TextUtils.isEmpty(localEntity.getParentId())) {
			return localEntity;
		}
		EntityResType2 type2 = (EntityResType2) localEntity;
		EntityResType1 parentEntity = GlobalResTypes.ALL_TYPE1S_MAP.get(localEntity.getParentId());
		if (null != parentEntity) {
			parentEntity.getType2s().add(type2);
			GlobalResTypes.ALL_TYPE2S_MAP.put(type2.getId(), type2);
		}
		return localEntity;
//		System.out.println(localEntity.getId() + "-----" + localEntity.getParentId() + "-----" + localEntity.getProgress() + "-----" + localEntity.getMax());
	}
	
	public static IResType parseType4Data(EntityResType4 serverEntity) {
		File file = serverEntity.getLocalFile();
		EntityResType4 localEntity = LOCAL_TYPE4S_MAP.get(serverEntity.getId());
		if (null == localEntity) {
			localEntity = serverEntity;
		}
		localEntity.setParentId(serverEntity.getParentId());
		localEntity.setStatus(EntityResType4.STATUS_NORMAL);
		if (localEntity.getVersion() < serverEntity.getVersion()) {
			//删除课程包
			if (file.exists()) {
				file.delete();
			}
			//删除临时文件
			FileUtils.deleteDirectory(localEntity.getLocalTempDir());
			localEntity.setProgress(0);
			localEntity.setMax(1);
			localEntity.setVersion(serverEntity.getVersion());
			localEntity.getType5s().clear();
			DaoResType4 dao4 = new DaoResType4();
			
			//更新数据
			dao4.update(localEntity.getId(), localEntity);
		}
		else if (file.exists()) {
			long fileLength = file.length();
			localEntity.setProgress(fileLength);
			if (fileLength == localEntity.getMax()) {
				localEntity.setStatus(EntityResType4.STATUS_FINISH);
			}
		}
		//更新内存
		GlobalResTypes.ALL_TYPES_MAP.put(localEntity.getId(), localEntity);
		if (TextUtils.isEmpty(localEntity.getParentId())) {
			return localEntity;
		}
		EntityResType4 type4 = (EntityResType4) localEntity;
		IResType parentEntity = GlobalResTypes.ALL_TYPES_MAP.get(localEntity.getParentId());
		if (null != parentEntity) {
			((EntityResType2) parentEntity).getType4s().add(type4);
			GlobalResTypes.ALL_TYPE4S_MAP.put(type4.getId(), type4);
		}
		return localEntity;
//		System.out.println(localEntity.getId() + "-----" + localEntity.getParentId() + "-----" + localEntity.getProgress() + "-----" + localEntity.getMax());
	}
	
	public class XmlParserHandler extends DefaultHandler {

		private EntityResType1 type1;
		 	  
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (qName.equals("type1")) {
				type1 = new EntityResType1(attributes.getValue(0), attributes.getValue(1), Integer.parseInt(attributes.getValue(2)) == 1);
				GlobalResTypes.ALL_TYPE1S_MAP.put(type1.getId(), type1);
			} 
			else if (qName.equals("type2")) {
				String id = attributes.getValue(0);
				double version = Double.parseDouble(attributes.getValue(2));
				String url = ConfigConstants.RES_URL + type1.getId() + "/" + id + "/" + id + EntityResType2.FORMAT;
				EntityResType2 type2 = new EntityResType2(id, type1.getId(), attributes.getValue(1), url, version);
				parseType2Data(type2);
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
		}

	}
	
}
