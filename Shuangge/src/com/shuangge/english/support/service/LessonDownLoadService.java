package com.shuangge.english.support.service;

import com.shuangge.english.entity.lesson.GlobalResTypes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LessonDownLoadService extends Service {

	public LessonDownLoadService() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
		String id = intent.getStringExtra("typeId");
		if (!GlobalResTypes.getInstance().startDownload(id)) {
			stopSelf();
		}
        System.out.println("当前线程 执行了====TestService0000====="+Thread.currentThread().getId()); 
        return super.onStartCommand(intent, flags, startId); 
    } 
    @Override
    public void onDestroy() {
        System.out.println("当前线程 执行了=====TestService=结束====="+Thread.currentThread().getId()); 
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    } 

}
