
package com.shuangge.english.support.error;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.text.TextUtils;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.MainActivity;
import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.login.LoginResult;
import com.shuangge.english.support.app.AnalyticsManager;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.view.login.AtyForgetAccount;
import com.shuangge.english.view.login.AtyGuide;
import com.shuangge.english.view.login.AtyLogin;
import com.shuangge.english.view.login.AtyMetenLogin;
import com.shuangge.english.view.login.AtyRegister;
import com.shuangge.english.view.login.AtyRegisterPhone;
import com.shuangge.english.view.login.AtyResetPwd;

/**
 * 自定义异常处理器
 * @author Jeffrey
 *
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler previousHandler;

    public ExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        this.previousHandler = handler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        final Date now = new Date();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        try {
            String logDir = CacheDisk.getLogDir();
            if (TextUtils.isEmpty(logDir)) {
                return;
            }
            String filename = UUID.randomUUID().toString();
            String path = logDir + File.separator + filename + ".stacktrace";
            BufferedWriter write = new BufferedWriter(new FileWriter(path));
            write.write("Package: " + AppInfo.APP_PACKAGE + "\n");
            write.write("Version: " + AppInfo.APP_VERSION + "\n");
            write.write("Android: " + AppInfo.ANDROID_VERSION + "\n");
            write.write("Manufacturer: " + AppInfo.PHONE_MANUFACTURER + "\n");
            write.write("Model: " + AppInfo.PHONE_MODEL + "\n");
            write.write("Date: " + now + "\n");
            write.write("\n");
            write.write(result.toString());
            write.flush();
            write.close();
        } 
        catch (Exception another) {

        } 
        finally {
        	AnalyticsManager.getInstance().reportError(exception);
        }
//        if (checkServerDataErr()) {
//        	GlobalApp.getInstance().restart();
//        	return;
//        }
        previousHandler.uncaughtException(thread, exception);
    }
    
    public static boolean checkServerDataErr() {
    	Activity aty = GlobalApp.getInstance().getActivity();
    	//处理内存被清空的问题 排除登陆 注册 启动页
        if (aty instanceof MainActivity 
        		|| aty instanceof AtyLogin 
        		|| aty instanceof AtyRegister 
        		|| aty instanceof AtyRegisterPhone 
        		|| aty instanceof AtyMetenLogin 
        		|| aty instanceof AtyForgetAccount 
        		|| aty instanceof AtyResetPwd 
        		|| aty instanceof AtyGuide) {
        	return false;
        }
        LoginResult data = GlobalRes.getInstance().getBeans().getLoginData();
        if (null == data || null == data.getInfoData()) {
        	return true;
        }
        return false;
    }
    
}