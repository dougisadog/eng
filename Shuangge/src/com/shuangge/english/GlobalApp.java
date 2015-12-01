package com.shuangge.english;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.app.AppActivityLifecycleCallbacks;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.app.ScreenObserver;
import com.shuangge.english.support.error.ErrLogManager;
import com.shuangge.english.support.utils.Utility;
import com.shuangge.english.view.login.AtyLogin;

/**
 * Application
 * 用于 数据传递，数据共享 等,数据缓存等操作。
 * 
 * @author Jeffrey
 *
 */
public class GlobalApp extends Application {
	
	private static final int SHOW_CUSTOM_ERR_MSG = 0;
	private static final int SHOW_CONNECT_SERVER_ERR_MSG = 10;
	private static final int SHOW_SERVER_ERR_MSG = 11;
	private static final int PUSH_MSG = 20;
	private static final int RESET_TO_LOGIN = 100;

    private static GlobalApp instance = null;
    
    /**
     * app性格信息
     */
    private AppInfo appInfo = null;
    private Activity activity = null;
    private Activity currentRunningActivity = null;
    private List<Activity> stackActivities = new ArrayList<Activity>();
    private ScreenObserver observer = null;

    @SuppressLint("HandlerLeak")
	private Handler uiHandler = new Handler() {
    	
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case SHOW_CONNECT_SERVER_ERR_MSG:
				Toast.makeText(GlobalApp.getInstance().getActivity(), R.string.serverErrMsg, Toast.LENGTH_SHORT).show();
				break;
			case SHOW_SERVER_ERR_MSG:
				String serverMsg = (String) msg.obj;
				Toast.makeText(GlobalApp.getInstance().getActivity(), serverMsg, Toast.LENGTH_SHORT).show();
				break;
			case PUSH_MSG:
				if (GlobalApp.getInstance().getCurrentRunningActivity() instanceof IPushMsgNotice) {
					((IPushMsgNotice) GlobalApp.getInstance().getCurrentRunningActivity()).notice();
				}
				break;
			case SHOW_CUSTOM_ERR_MSG:
				String errMsg = (String) msg.obj;
				Toast.makeText(GlobalApp.getInstance().getActivity(), errMsg, Toast.LENGTH_SHORT).show();
				break;
			case RESET_TO_LOGIN:
				AtyLogin.startAty(GlobalApp.getInstance().getActivity());
				clearStackActivities();
				break;
			}
    	};
    	
    };

    @SuppressLint("NewApi")
	@Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initAppInfo();
        GlobalRes.getInstance().initCache();
        ErrLogManager.registerHandler();
        //验证包是否是正式包
        if (Utility.isCertificateFingerprintCorrect(this)) {
        	//接入统计SDK 类似友盟
//            Crashlytics.start(this);
        }
        if (Build.VERSION.SDK_INT >= 14) {
        	registerActivityLifecycleCallbacks(new AppActivityLifecycleCallbacks());
        }
        
        //推送消息SDK 初始化
        initPushInfo();
        
        // 监听屏幕
        observer = new ScreenObserver();
    }
    
    private void initAppInfo() {
    	appInfo = new AppInfo();
        appInfo.getAppInfo(this);
    }
    
    private void initPushInfo() {
    }
    
    /**********************************************************************************************************************************************************/
    
    public void showCustomErrMsg(String errMsg) {
    	Message msg = new Message();
    	msg.what = SHOW_CUSTOM_ERR_MSG;
    	msg.obj = errMsg;
    	uiHandler.sendMessage(msg);
    }
    
    public void showConnectServerErrMsg() {
    	Message msg = new Message();
        msg.what = SHOW_CONNECT_SERVER_ERR_MSG;
        uiHandler.sendMessage(msg);
	}
    
    public void showServerErrMsg(RestResult serverInfo) {
        showErrMsg(serverInfo.getMsg());
    }
    
    public void showErrMsg(String errorMsg) {
    	Message msg = new Message();
    	msg.what = SHOW_SERVER_ERR_MSG;
    	msg.obj = errorMsg;
    	uiHandler.sendMessage(msg);
    }
    
    public void pushMsg() {
    	Message msg = new Message();
    	msg.what = PUSH_MSG;
    	uiHandler.sendMessage(msg);
    }
    
    public void resetToLogin() {
    	Message msg = new Message();
    	msg.what = RESET_TO_LOGIN;
    	uiHandler.sendMessage(msg);
    }
    
    public void restart() {
    	AtyLogin.startAty(GlobalApp.getInstance().getActivity());
		clearStackActivities();
    	
//    	Toast.makeText(this, R.string.dataErrMsg, Toast.LENGTH_SHORT).show();
//    	Intent intent = new Intent("air.com.shuangge.phone.ShuangEnglish");  
//        // 参数1：包名，参数2：程序入口的activity  
//    	PendingIntent i = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);  
//    	AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);  
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1, i); // 1秒钟后重启应用  
        
//        System.exit(1);
    	
//    	Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());  
//    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//    	startActivity(i);
    	
//    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    /**********************************************************************************************************************************************************/
    
    public static GlobalApp getInstance() {
        return instance;
    }
    
	public AppInfo getAppInfo() {
		return appInfo;
	}

	public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getCurrentRunningActivity() {
        return currentRunningActivity;
    }

    public void setCurrentRunningActivity(Activity currentRunningActivity) {
        this.currentRunningActivity = currentRunningActivity;
    }
	
	public Handler getUiHandler() {
		return uiHandler;
	}

	public DisplayMetrics getDisplayMetrics() {
    	return appInfo.getDisplayMetrics(getActivity());
    }

	public ScreenObserver getObserver() {
		return observer;
	}

	public List<Activity> getStackActivities() {
		return stackActivities;
	}
	
	public void addStackActivity(Activity activity) {
		stackActivities.add(activity);
	}
	
	public void removeStackActivity(Activity activity) {
		if (stackActivities.contains(activity))
			stackActivities.remove(activity);
	}
	
	public void clearStackActivities() {
		for (Activity activity : stackActivities) {
			if (activity.isFinishing())
				continue;
			activity.finish();
		}
		stackActivities.clear();
	}
}
