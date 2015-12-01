package com.shuangge.english.support.app;

import android.app.Activity;
import android.content.Context;

import com.shuangge.english.GlobalApp;
import com.umeng.analytics.MobclickAgent;

public class AnalyticsManager {
	
	private static AnalyticsManager instance = null;

	public static AnalyticsManager getInstance() {
		if (null == instance) {
			instance = new AnalyticsManager();
		}
		return instance;
	}
    
    public void init(Context context) {
    	MobclickAgent.setDebugMode(true);
//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);
		
		MobclickAgent.updateOnlineConfig(context);
    }
    
    public void reportError(Throwable e) {
    	MobclickAgent.reportError(GlobalApp.getInstance(), e);
    }
    
    public void onPageStart(Activity activity) {
    	MobclickAgent.onPageStart(getClassName(activity));
        MobclickAgent.onResume(activity);
    }
    
    public void onPageEnd(Activity activity) {
    	MobclickAgent.onPageEnd(getClassName(activity));
    	MobclickAgent.onPause(activity);
    }
    
    private String getClassName(Activity activity) {
    	return activity.getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1);
    }
}
