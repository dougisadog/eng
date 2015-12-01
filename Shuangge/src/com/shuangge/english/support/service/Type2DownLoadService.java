package com.shuangge.english.support.service;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.GlobalResTypes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Type2DownLoadService extends Service {

	public Type2DownLoadService() {
		super();
	}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		EntityResType2 t2 = null;
		for (String key : GlobalResTypes.ALL_TYPE2S_MAP.keySet()) {
			t2 = GlobalResTypes.ALL_TYPE2S_MAP.get(key);
			if (!t2.isFinished()) {
				GlobalResTypes.getInstance().startDownload(t2);
			}
		}
        return super.onStartCommand(intent, flags, startId); 
    } 
    @Override
    public void onDestroy() {
        System.out.println("当前线程 执行了=====Type2DownLoadService=结束====="+Thread.currentThread().getId()); 
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    } 

}
