package com.shuangge.english.support.app;

import android.os.Build;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.receiver.MyPushIntentService;
import com.shuangge.english.support.http.HttpReqFactory;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;


/**
 * 推送管理器
 * 
 * @author Jeffrey
 *
 */
public class PushManager {

	private static PushManager instance = null;
	
	private PushAgent mPushAgent;
	
	public static PushManager getInstance() {
		if (null == instance) {
			instance = new PushManager();
		}
		return instance;
	}
    
    public void init() {
    	mPushAgent = PushAgent.getInstance(GlobalApp.getInstance());
    	mPushAgent.setDebugMode(true);
    	mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
    	mPushAgent.onAppStart();
    }
    
    public void register() {
    	if (isRegistered()) {
    		return;
    	}
    	if (isRunning()) {
    		mPushAgent.disable(mUnregisterCallback2);
    		return;
    	}
    	mPushAgent.enable(mRegisterCallback);
    }
    
    public void unregister() {
    	if (isRunning()) {
    		GlobalRes.getInstance().getBeans().setDeviceToken(null);
			mPushAgent.disable(mUnregisterCallback);
		}
    }
    
    public void login() {
    	if (!isLoggedIn()) {
    		register();
    		return;
    	}
    	RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.PUSH_REGISTER,
    			new HttpReqFactory.ReqParam("deviceToken", GlobalRes.getInstance().getBeans().getDeviceToken()));
    }
    
    public void logout() {
    	if (null == GlobalRes.getInstance().getBeans().getDeviceToken()) {
    		RestResult result = HttpReqFactory.getServerResultByToken(RestResult.class, ConfigConstants.PUSH_UNREGISTER);
    		return;
    	}
    }
    
    public boolean isRunning() {
    	return null != mPushAgent && mPushAgent.isEnabled() && mPushAgent.isRegistered();
    }
    
    public boolean isLoggedIn() {
    	return null != GlobalRes.getInstance().getBeans().getLoginData();
    }
    
    public boolean isRegistered() {
    	return null != GlobalRes.getInstance().getBeans().getDeviceToken();
    }
    
    public IUmengUnregisterCallback mUnregisterCallback2 = new IUmengUnregisterCallback() {
    	
    	@Override
    	public void onUnregistered(String registrationId) {
    		mPushAgent.enable(mRegisterCallback);
    	}
    };
    
    public IUmengUnregisterCallback mUnregisterCallback = new IUmengUnregisterCallback() {
		
		@Override
		public void onUnregistered(String registrationId) {
		}
	};
	
	public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
		
		@Override
		public void onRegistered(String registrationId) {
			if (Build.VERSION.SDK_INT < 11) 
				return;
			GlobalRes.getInstance().getBeans().setDeviceToken(mPushAgent.getRegistrationId());
			if (isLoggedIn()) {
				login();
			}
		}
	};

}
