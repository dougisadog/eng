package com.shuangge.english.view.about;

import android.os.Handler;
import android.os.Message;

public class UpdateRunnable {
	
	private Handler updateHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

		}
	};

	Message message = updateHandler.obtainMessage();
	public void run() {
//	message.what = DOWNLOAD_COMPLETE;
//	try{
//	//增加权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
//	if(!updateDir.exists()){
//	updateDir.mkdirs();
//	}
//	if(!updateFile.exists()){
//	updateFile.createNewFile();
//	}
//	//下载函数，以QQ为例子
//	//增加权限<uses-permission android:name="android.permission.INTERNET">;
//	long downloadSize = downloadUpdateFile("http://softfile.3g.qq.com:8080/msoft/179/1105/10753/MobileQQ1.0(Android)_Build0198.apk",updateFile);
//	if(downloadSize>0){
//	//下载成功
//	updateHandler.sendMessage(message);
//	}
//	}catch(Exception ex){
//	ex.printStackTrace();
//	message.what = DOWNLOAD_FAIL;
//	//下载失败
//	updateHandler.sendMessage(message);
//	}
	}


}
