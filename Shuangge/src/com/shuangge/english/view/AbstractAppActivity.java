package com.shuangge.english.view;

import java.lang.reflect.Field;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.support.app.AnalyticsManager;
import com.shuangge.english.support.app.ScreenObserver.ScreenStateListener;
import com.shuangge.english.support.error.ExceptionHandler;
import com.shuangge.english.view.component.dialog.DialogLoadingFragment;
import com.shuangge.english.view.component.dialog.DialogLoadingFragment.ICallbackDialog;

/**
 * 
 * @author Jeffrey
 *
 */
public class AbstractAppActivity extends FragmentActivity implements ScreenStateListener, ICallbackDialog {

	public final static int CODE_QUIT = 1;
	public static boolean isActive = true;
	private boolean isRunning = false;
	
	private DialogLoadingFragment loadingDialog;
	
    @Override
    protected void onResume() {
        super.onResume();
        GlobalApp.getInstance().setCurrentRunningActivity(this);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //从后台唤醒，进入前台
        if (!isActive) {
        	isActive = true;
//        	System.out.println("从后台唤醒，进入前台");
//        	PushManager.getInstance().start();
        }
        //友盟统计
        AnalyticsManager.getInstance().onPageStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pauseMusic();
        //友盟统计
        AnalyticsManager.getInstance().onPageEnd(this);
        if (GlobalApp.getInstance().getCurrentRunningActivity().equals(this)) {
            GlobalApp.getInstance().setCurrentRunningActivity(null);
        }
    }
    
	@Override
	protected void onStop() {
		super.onStop();
		//进入后台
		if (!isAppOnForeground()) {
			isActive = false;
//			System.out.println("进入后台");
//			PushManager.getInstance().stop();
        }
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forceShowActionBarOverflowMenu();
//        initNFC();
        GlobalApp.getInstance().setActivity(this);
        GlobalApp.getInstance().addStackActivity(this);
        isRunning = true;

        if (ExceptionHandler.checkServerDataErr()) {
        	GlobalApp.getInstance().restart();
        	return;
        }
        
        initData();
        initRequestData();
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
    	super.onActivityResult(arg0, arg1, arg2);
    	//checkServerDataErr();
    }
    
    private void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {}
    }
    
/*
    @SuppressLint("NewApi")
	private void initNFC() {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (null == mNfcAdapter) {
            return;
        }
        mNfcAdapter.setNdefPushMessageCallback(new NfcAdapter.CreateNdefMessageCallback() {
            @Override
            public NdefMessage createNdefMessage(NfcEvent event) {
                String text = (GlobalApp.getInstance().getCurrentAccountName());
                NdefMessage msg = new NdefMessage(
                        new NdefRecord[]{createMimeRecord(
                                "application/org.qii.weiciyuan.beam", text.getBytes()),
                                NdefRecord.createApplicationRecord(getPackageName())
                        });
                return msg;
            }
        }, this);
    }

    @SuppressLint("NewApi")
	private NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }

*/
    protected void dealWithException(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void finish() {
    	View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    	super.finish();
    }
    
    @Override
    protected void onDestroy() {
    	GlobalApp.getInstance().removeStackActivity(this);
    	hideLoading();
    	super.onDestroy();
    }
    
    public void showLoading() {
    	showLoading(false);
    }
    
    public void showLoading(boolean cancelable) {
    	if (null == loadingDialog) {
	    	loadingDialog = new DialogLoadingFragment(cancelable, this);
	    	
    	}
    	if (loadingDialog.isVisible() || loadingDialog.isAdded()) {
    		return;
    	}
    	showLoadingDialog();
	}
    
    public void showLoading(String loadingInfo) {
    	showLoading(loadingInfo, false);
    }
    
    public void showLoading(String loadingInfo, boolean cancelable) {
    	if (null == loadingDialog) {
    		loadingDialog = new DialogLoadingFragment(loadingInfo, cancelable, this);
    	}
    	if (loadingDialog.isVisible() || loadingDialog.isAdded()) {
    		return;
    	}
    	showLoadingDialog();
    }
    
    private void showLoadingDialog() {
    	loadingDialog.showDialog(getSupportFragmentManager());
		if (isRunning)
			loadingDialog.onResumeFragments();
    }
    
    public void hideLoading() {
    	if (null == loadingDialog) {
    		return;
    	}
    	loadingDialog.hideDialog();
    	loadingDialog = null;
	}
    
    
    @Override
    protected void onPostResume() {
    	super.onPostResume();
    	isRunning = true;
    	if (null != loadingDialog) {
    		loadingDialog.onResumeFragments();
    	}
    }
    
    public boolean isLoading() {
    	return null != loadingDialog && loadingDialog.isAdded() && loadingDialog.isVisible();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != loadingDialog && loadingDialog.isAdded() && loadingDialog.isCancelable()) {
				hideLoading();
				return true;
			}
			if (onBack()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    
    public boolean onBack() {
		return false;
    }
    
    /*************************************************************************************************************************************************/
    
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
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    return true;
        }
        return false;
    }

	@Override
	public void onScreenOn() {
	}

	@Override
	public void onScreenOff() {
	}

	@Override
	public void onUserPresent() {
	}
    
	protected void pauseMusic() {  
//	    Intent freshIntent = new Intent();  
//	    freshIntent.setAction("com.android.music.musicservicecommand.pause");  
//	    freshIntent.putExtra("command", "pause");  
//	    sendBroadcast(freshIntent);  
//	    freshIntent = new Intent();  
//	    freshIntent.setAction("com.android.music.musicservicecommand");  
//	    freshIntent.putExtra("command", "pause");  
//	    sendBroadcast(freshIntent);  
	}
	
	
	/***********************************************************************************************************************************************/
	/**
	/** 数据
	/**
	/***********************************************************************************************************************************************/
	
	protected void initData() {
		
	}
	
	protected void initRequestData() {
		
	}
	
	protected CacheBeans getBeans() {
		return GlobalRes.getInstance().getBeans();
	}
	
}
