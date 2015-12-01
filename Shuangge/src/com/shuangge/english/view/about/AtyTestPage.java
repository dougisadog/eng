package com.shuangge.english.view.about;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.shuangge.english.view.AbstractAppActivity;
/**
 * 
 * @author Jeffrey
 *
 */
public class AtyTestPage extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_ABOUT_FAQ = 1071;

	private Button testBtn;
	
	public Notification downloadNotification;
	public NotificationManager downloadNM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.aty_test);
//		testBtn = (Button) findViewById(R.id.testBtn);
		testBtn.setOnClickListener(this);
		 
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.testBtn:
//			showNotifi();
//			break;
//		case R.id.txtQQ:
//			String appId = "1103428370";
//			String qqGroup = "101186255";
//			Tencent mTencent = Tencent.createInstance(appId, this);
//			mTencent.joinQQGroup(this, qqGroup);
//			break;
		}
	}
	
	private void showNotifi() {
//		downloadNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        downloadNotification = new Notification.Builder(this)
//        .setContentTitle("New mail from ")
//        .setContentText("appName" + "下载…")
//        .setSmallIcon(R.drawable.icon_logo)
//        .build();
////
//        downloadNotification.contentView = new RemoteViews(getPackageName(),R.layout.notification);
//        //设置进度条的最大进度和初始进度
//        downloadNotification.contentView.setProgressBar(R.id.pb, 100,0, false);
//        //显示下载的包名
//        downloadNotification.contentView.setTextViewText(R.id.down_tv, "appname");
//        //显示下载的进度
//        downloadNotification.contentView.setTextViewText(R.id.down_rate, "0%");
//        Intent notificationIntent = new Intent(AtyTestPage.this,AtyTestPage.class);
//        PendingIntent contentIntent =PendingIntent.getActivity(AtyTestPage.this,0,notificationIntent,0);
//        downloadNotification.contentIntent = contentIntent;
//        //显示通知
//        downloadNM.notify(10, downloadNotification);
		
		Intent updateIntent =new Intent(AtyTestPage.this, UpdateService.class);
		updateIntent.putExtra("titleId",R.string.app_name);
		startService(updateIntent);


	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyTestPage.class);
		context.startActivityForResult(i, REQUEST_ABOUT_FAQ);
	}

}
