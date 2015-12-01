package com.shuangge.english.view.about;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.AssetManager;

import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.task.TaskAppList;
import com.shuangge.english.view.about.model.AppDataModel;


public class BaseAtyAppList extends Activity {
	
	
    
    /**
	 * 所有friend
	 */
	protected String[] mAppListDatas;
	/**
	 * key - friend value - msg
	 */
	protected Map<String, String[]> mAppListDatasMap = new HashMap<String, String[]>();
//	/**
//	 * key - msg values - reply
//	 */
//	protected Map<String, String[]> mReplyDatasMap = new HashMap<String, String[]>();
	
//	/**
//	 * key - 区 values - 邮编
//	 */
//	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

//	/**
//	 * 当前省的名称
//	 */
//	protected String mCurrentFriendName;
//	/**
//	 * 当前市的名称
//	 */
//	protected String mCurrentMsg;
//	/**
//	 * 当前区的名称
//	 */
//	protected String mCurrentStr ="";
//	
//	/**
//	 * 当前区的邮政编码
//	 */
//	protected String mCurrentScore ="";
	
	protected String defaultStr;
	
	protected List<AppDataModel> appList;
	
	/**
	 * 解析XML数据
	 */
	
    protected void initAppListDatas() {
    	appList = null;
    	AssetManager asset = getAssets();
    	
    	new TaskAppList(0, new CallbackNoticeView<Void, List<AppDataModel>>() {

			@Override
			public void refreshView(int tag, List<AppDataModel> result) {
				if (null != result) {
					appList = result;
					return;
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, asset);
    	
//    	HttpReqFactory.createDownloadFileReq(new HttpReqHelper.DownloadBinaryFileListener() {
//			
//			@Override
//			public void progressHandler(long progress, long max) {
//			}
//			
//			@Override
//			public void errorHandler(Exception e, String errMsg) {
//				DebugPrinter.e("TaskCheckCatalog:" + e);
//			}
//			
//			@Override
//			public void completeHandler(byte[] bytes) {
//				InputStream input = new ByteArrayInputStream(bytes);
//				try {
//					 // 创建一个解析xml的工厂对象
//					SAXParserFactory spf = SAXParserFactory.newInstance();
//					// 解析xml
//					SAXParser parser = spf.newSAXParser();
//					XmlParserAppListHandler handler = new XmlParserAppListHandler();
//					parser.parse(input, handler);
//					input.close();
//					// 获取解析出来的数据
//					appList = handler.getDataList();
//					mAppListDatas = new String[appList.size()];
//				}
//				catch (Exception e) {
//		        	e.printStackTrace();
//		        	DebugPrinter.e("TaskCheckCatalog:" + e);
//		        } 
//				finally {
//					Utility.closeSilently(input);
//				}
//			}
//		}, ConfigConstants.RECOMMEND_APP_URL + "applist_data.xml");

//        try {
//        	
////            InputStream input = asset.open(ConfigConstants.RECOMMEND_APP_URL + "applist_data.xml");
//            
//            String url = ConfigConstants.RECOMMEND_APP_URL + "applist_data.xml";   
//            URL myUrl = new URL(url);  
//            HttpURLConnection conn = (HttpURLConnection)myUrl.openConnection();  
//            conn.setDoInput(true);  
//            conn.connect();  
//            InputStream input = conn.getInputStream();
////            HttpGet httpGet = new HttpGet(url);  //远程http地址   
////            HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);  
////            InputStream input = httpResponse.getEntity().getContent();
//            
//            
//            // 创建一个解析xml的工厂对象
//			SAXParserFactory spf = SAXParserFactory.newInstance();
//			// 解析xml
//			SAXParser parser = spf.newSAXParser();
//			XmlParserAppListHandler handler = new XmlParserAppListHandler();
//			parser.parse(input, handler);
//			input.close();
//			// 获取解析出来的数据
//			appList = handler.getDataList();
//			mAppListDatas = new String[appList.size()];
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        } finally {
//        	
//        } 
	}

}
