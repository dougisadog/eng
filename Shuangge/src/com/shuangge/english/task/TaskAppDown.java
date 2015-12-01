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

public class TaskAppDown extends BaseTask<String, Void, Boolean> {
	
	
	private Boolean result = null;
	
	public TaskAppDown(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(String... params) {
    	
		
		
		return result;
	}
	
}
