package com.shuangge.english.support.app;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.notice.FetchNewMsgService;

/**
 * 所有atitivy生命周期管理
 * @author Jeffrey
 * 
 */
@SuppressLint("NewApi")
public class AppActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

    private static final int SCHEDULE_DELAY_MILLS = 3000;

    private static final int FETCH_PERIOD_SECONDS = 30;

    private int visibleActivityCount = 0;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    private ExceptionWithLogScheduledExecutor exceptionWithLogScheduledExecutor;

    private ScheduledFuture<?> scheduledFuture;

    /**
     * 统一管理activity
     * 
     */
    public AppActivityLifecycleCallbacks() {
    	exceptionWithLogScheduledExecutor = new ExceptionWithLogScheduledExecutor(1);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        DebugPrinter.i("Activity is created, class name : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        DebugPrinter.i("Activity is started, class name : " + activity.getClass().getSimpleName());
        if (visibleActivityCount == 0) {
//            startFetchUnread();
        }
        visibleActivityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        DebugPrinter.i("Activity is stopped, class name : " + activity.getClass().getSimpleName());
        visibleActivityCount--;
        if (visibleActivityCount == 0) {
//            stopFetchUnread();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        DebugPrinter.i("Activity saved instance state, class name : " + activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        DebugPrinter.i("Activity is destroyed, class name : " + activity.getClass().getSimpleName());
    }

    private void startFetchUnread() {
        uiHandler.postDelayed(scheduleFetchUnreadRunnable, SCHEDULE_DELAY_MILLS);
        uiHandler.removeCallbacks(unScheduleFetchUnreadRunnable);
    }

    private void stopFetchUnread() {
        uiHandler.postDelayed(unScheduleFetchUnreadRunnable, SCHEDULE_DELAY_MILLS);
        uiHandler.removeCallbacks(scheduleFetchUnreadRunnable);
    }

    private Runnable scheduleFetchUnreadRunnable = new Runnable() {
        @Override
        public void run() {
            DebugPrinter.i("Schedule fetch unread message");
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            scheduledFuture = exceptionWithLogScheduledExecutor.scheduleAtFixedRate(fetchRunnable, 0, FETCH_PERIOD_SECONDS, TimeUnit.SECONDS);
        }
    };

    private Runnable unScheduleFetchUnreadRunnable = new Runnable() {
        @Override
        public void run() {
            DebugPrinter.i("Stop schedule fetch unread message");
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    };

    private Runnable fetchRunnable = new Runnable() {
        @Override
        public void run() {
            DebugPrinter.i("Start fetch unread message service");
            GlobalApp.getInstance().startService(FetchNewMsgService.newIntentFromOpenApp());
        }
    };
}


