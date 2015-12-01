package com.shuangge.english.support.http;

import java.io.File;

/**
 * 
 * @author Jeffrey
 *
 */
public class HttpReqHelper {

	/**
	 * Http请求回调
	 * @author Jeffrey
	 *
	 */
	public static interface HttpReqListener {
		
        public void completeHandler(String result);

        public void errorHandler(Exception e, String errMsg);
        
	}
	
	/**
	 * 下载回调
	 * @author Jeffrey
	 *
	 */
	public static interface DownloadBinaryFileListener {
		
		public void progressHandler(long progress, long max);
		
		public void completeHandler(byte[] bytes);
		
        public void errorHandler(Exception e, String errMsg);
		
	}
	
	/**
	 * 下载回调
	 * @author Jeffrey
	 *
	 */
	public static interface DownloadFileListener {
		
		public void startHandler(long max);
		
		public void stopHandler(long progress, long max);
		
		public void progressHandler(long progress, long max);
		
		public void completeHandler(File file);
		
        public void errorHandler(Exception e, String errMsg);
		
	}
	
	/**
	 * 上传回调
	 * @author Jeffrey
	 *
	 */
	public static interface UploadFileFileListener extends HttpReqListener {
		
		public void progressHandler(long progress, long max);
		
		public void waitServerResponse();
		
		public void completeHandler(String result);

        public void errorHandler(Exception e, String errMsg);
		
	}
	
}
