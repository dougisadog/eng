package com.shuangge.english.support.app;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.shuangge.english.support.debug.DebugPrinter;


/**
 * 带日志 定时任务线程池
 * @author Jeffrey
 *
 * ScheduledThreadPoolExecutor will stop when exception occur, and it wont notify us, it stop silently
 * here we catch exception, write to log, then stop
 */
public class ExceptionWithLogScheduledExecutor extends ScheduledThreadPoolExecutor {

    public ExceptionWithLogScheduledExecutor(int corePoolSize) {
        super(corePoolSize);
    }

	@Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return super.scheduleAtFixedRate(wrapRunnable(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, delay, unit);
    }

    private Runnable wrapRunnable(Runnable command) {
        return new LogOnExceptionRunnable(command);
    }

    private class LogOnExceptionRunnable implements Runnable {

        private Runnable runnable;

        public LogOnExceptionRunnable(Runnable runnable) {
            super();
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } 
            catch (Exception e) {
                DebugPrinter.e("error in executing: " + runnable + ". It will no longer be run!");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


}