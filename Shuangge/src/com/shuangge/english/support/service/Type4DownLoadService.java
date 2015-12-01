package com.shuangge.english.support.service;

import java.util.List;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.IResType;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackResDownload;
import com.shuangge.english.view.download.AtyDownload2;
import com.shuangge.english.view.download.adapter.AdapterDownload.ViewHolder;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

public class Type4DownLoadService extends Service {

	public Type4DownLoadService() {
		super();
	}

	private MyReciver receiver;
	
	@Override
	public void onCreate() {
		
		// 采用代码方式注册广播接收者
		receiver = new MyReciver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.shuangge.english.support.service.Type4DownLoadService.MyReciver");
		registerReceiver(receiver, filter);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null != intent) {
		boolean state = intent.getBooleanExtra("state", true);
		String id = intent.getStringExtra("typeId");

		List<String> ids = intent.getStringArrayListExtra("typeIds");
//		if (null != id) {
//			 startDownLoadType4Res(state, id);
//		}

			if (state) {
				// startDownLoadType4ResV2(id);
				startDownLoadType4ResV3(ids);
			} else {
				// stopDownLoadType4ResV2(id);
				stopDownLoadType4ResV3(ids);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		System.out.println("当前线程 执行了=====TestService=结束====="
				+ Thread.currentThread().getId());
		unregisterReceiver(receiver);
		receiver = null;
		GlobalResTypes.getInstance().stopAllQueueDownload();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void startDownLoadType4ResV2(String id) {
		IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(id);
		GlobalResTypes.getInstance().startDownload(entity);
	}

	private void stopDownLoadType4ResV2(String id) {
		GlobalResTypes.getInstance().stopDownload(id);
	}

	private static void startDownLoadType4ResV3(List<String> ids) {
		for (int i = 0; i < ids.size(); i++) {
			IResType entity = GlobalResTypes.ALL_TYPES_MAP.get(ids.get(i));
			GlobalResTypes.getInstance().startDownload(entity);
//			refreshNotification();
//			GlobalResTypes.getInstance().setCallBackType4(callbackResDownload);
//			mNotificationManager.notify(Integer.parseInt(ids.get(i)), mNotification);
		}

	}

	private static void stopDownLoadType4ResV3(List<String> ids) {
		for (int i = 0; i < ids.size(); i++) {
			GlobalResTypes.getInstance().stopDownload(ids.get(i));
		}
	}


	public static class MyReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			boolean state = arg1.getBooleanExtra("state", false);
			List<String> ids = arg1.getStringArrayListExtra("typeIds");
			String id = arg1.getStringExtra("typeId");
			if (null == arg0) {
				return;
			}
			// startDownLoadType4Res(state , id);

			if (state) {
				// startDownLoadType4ResV2(id);
				startDownLoadType4ResV3(ids);
			} else {
				// stopDownLoadType4ResV2(id);
				stopDownLoadType4ResV3(ids);
			}

			String str = "接收到" + arg0.getClass().getSimpleName() + (state ? "下载" : "停止下载");
			for (int i = 0; i < ids.size(); i++) {
				str = str + " " + GlobalResTypes.ALL_TYPES_MAP.get(ids.get(i)).getName();
			}
			str = str + "的广播";
			System.out.println(str);
		}

	}

}
