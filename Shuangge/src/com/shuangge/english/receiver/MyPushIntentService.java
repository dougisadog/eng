package com.shuangge.english.receiver;

import java.util.List;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.msg.ModuleMsgData;
import com.umeng.common.message.Log;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

/**
 * Developer defined push intent service. Remember to call
 * {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 * 
 * @author lucas
 *
 */
public class MyPushIntentService extends UmengBaseIntentService {

	public static final int MSG_SYSTEM = 1;
	public static final int MSG_CLASS = 2;
	public static final int MSG_ATTENTION = 3;
	public static final int MSG_POST = 4;
	public static final int MSG_SECRET = 5;
	public static final int MSG_Gift = 6;

	private static final String TAG = MyPushIntentService.class.getName();

	private NotificationManager manager;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
		try {
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			Log.d(TAG, "message=" + message);
			Log.d(TAG, "custom=" + msg.custom);
//			if ("notification".equals(msg.display_type)) {
				if (!isLauncherRunnig() && !"0".equals(msg.title)) {
					showNormal(msg.title, msg.text ,msg.ticker);
					return;
				}
//			}
			String data1 = msg.extra.get("noticeType");
			String data2 = msg.extra.get("timestamp");
			if (TextUtils.isEmpty(data1) || TextUtils.isEmpty(data2)) {
				return;
			}
			ModuleMsgData msgs = GlobalRes.getInstance().getBeans().getMsgDatas();
			long timestamp = Long.parseLong(data2);
			int noticeType = Integer.parseInt(data1);
			boolean refreshViewFlag = false;
			switch (noticeType) {
			case MSG_SYSTEM:
				if (null == msgs.getAttentionTimestamp()
						|| msgs.getAttentionTimestamp() < timestamp) {
					msgs.setSystemMsg(true);
					msgs.setSystemTimestamp(timestamp);
					refreshViewFlag = true;
				}
				break;
			case MSG_CLASS:
				if (null == msgs.getAttentionTimestamp()
						|| msgs.getAttentionTimestamp() < timestamp) {
					msgs.setClassMsg(true);
					msgs.setClassTimestamp(timestamp);
					refreshViewFlag = true;
				}
				break;
			case MSG_ATTENTION:
				if (null == msgs.getAttentionTimestamp()
						|| msgs.getAttentionTimestamp() < timestamp) {
					msgs.setAttentionMsg(true);
					msgs.setAttentionTimestamp(timestamp);
					refreshViewFlag = true;
				}
				break;
			case MSG_POST:
				if (null == msgs.getAttentionTimestamp()
						|| msgs.getAttentionTimestamp() < timestamp) {
					msgs.setPostMsg(true);
					msgs.setPostTimestamp(timestamp);
					refreshViewFlag = true;
				}
				break;
			case MSG_SECRET:
				if (null == msgs.getSecretTimestamp()
						|| msgs.getSecretTimestamp() < timestamp) {
					msgs.setSecretMsg(1);
					msgs.setSecretTimestamp(timestamp);
					refreshViewFlag = true;
				}
				break;
			case MSG_Gift:
				if (null == msgs.getGiftMsgTimestamp()
						|| msgs.getGiftMsgTimestamp() < timestamp) {
					msgs.setGiftMsg(1);
					msgs.setGiftMsgTimestamp(timestamp);
					refreshViewFlag = true;
				}
				break;
			}
			if (!refreshViewFlag) {
				return;
			}
			if (isLauncherRunnig() && isAppOnForeground()) {
				GlobalApp.getInstance().pushMsg();
				return;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private boolean isLauncherRunnig() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(300);
		boolean isAppRunning = false;
		String MY_PKG_NAME = "air.com.shuangge.phone.ShuangEnglish";
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
					|| info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
				isAppRunning = true;
				// Log.i(TAG,info.topActivity.getPackageName() +
				// " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
				break;
			}
		}
		return isAppRunning;
	}

	private void showNormal(String title, String content, String ticker) {
		try {
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					new Intent(this,Class.forName("com.shuangge.english.MainActivity")), PendingIntent.FLAG_CANCEL_CURRENT);
			Notification notification = new NotificationCompat.Builder(this)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
			.setSmallIcon(R.drawable.ic_launcher).setTicker(ticker).setContentTitle(title)
			.setContentText(content).setAutoCancel(true)
			.setDefaults(Notification.DEFAULT_ALL).build();

			notification.setLatestEventInfo(this, title,
					content, contentIntent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
			manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(0, notification);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * 程序是否在前台运行
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
                return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

}
