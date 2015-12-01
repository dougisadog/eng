package com.shuangge.english.view.about;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

public class UpdateService extends Service{
	
	//标题
	private int titleId = 0;

	//文件存储
	private File updateDir = null;
	private File updateFile = null;

	//通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	//通知栏跳转Intent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
	@SuppressLint("NewApi") 
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	//获取传值
	titleId = intent.getIntExtra("titleId",0);
	//创建文件
	if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){
	updateDir = new File(Environment.getExternalStorageDirectory(), "");
	updateFile = new File(updateDir.getPath(),getResources().getString(titleId)+".apk");
	}
	this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	this.updateNotification = new Notification();
	//设置下载过程中，点击通知栏，回到主界面
	updateIntent = new Intent(this, AtyTestPage.class);
	updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
	
	byte b[] = null;  
    try {  
        String _data = getImageData("");//我这里的测试图片传入的是base64内容格式的.  
        if (_data != null) {  
            b = Base64.decode(_data, 0);  
        }  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
    final Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
	
	//设置通知栏显示内容
	updateNotification.tickerText = "开始下载";
	updateNotification = new Notification.Builder(this)
	  .setContentTitle("New mail from ")
	  .setContentText("appName" + "下载…")
	  .setSmallIcon(R.drawable.icon_logo)
	  .setLargeIcon(bitmap)
	  .build();
	
	//发出通知
	updateNotificationManager.notify(0,updateNotification);
	
	//开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
//	new Thread(new UpdateRunnable()).start();//这个是下载的重点，是下载的过程
	return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	 private String getImageData(String url) throws ClientProtocolException, IOException {  
	        Log.d("getImageData", "URL:" + url);  
	        org.apache.http.client.HttpClient client = new DefaultHttpClient();  
	        HttpGet httpget = new HttpGet(url);  
	        HttpResponse httpResponse = client.execute(httpget);  
	        int status = httpResponse.getStatusLine().getStatusCode();  
	        if (status == HttpStatus.SC_OK) {  
	            Log.d("getImageData", "status:" + status);  
	            String strResult = EntityUtils.toString(httpResponse.getEntity());  
	            return strResult;  
	        }  
	        return null;  
	    }


}
