package com.shuangge.english.support.utils;

import com.shuangge.english.network.read.TaskReqReadIWordRes;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;

import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

public class SoundUtils {

	public static void loadIWordRes(final String path , String wordSoundUrl , final ProgressBar wordResProgress) {
		if (null != wordResProgress) {
			wordResProgress.setVisibility(View.VISIBLE);
		}
		final Long time = System.currentTimeMillis();
		new TaskReqReadIWordRes(0, new CallbackNoticeView<Integer, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (result) {
					if (null != wordResProgress) {
						wordResProgress.setVisibility(View.GONE);
					}
					//资源加载时间少于1s 则自动播放音频
					if (System.currentTimeMillis() - time < 1000)
					MediaPlayerMgr.getInstance().playMp(path);
				}
			}

			@Override
			public void onProgressUpdate(int tag, Integer[] values) {
				if (null != wordResProgress) {
					wordResProgress.setProgress(values[0]/values[1] * wordResProgress.getMax());
				}
			}
		}, wordSoundUrl , path);
	}
	
	public static String getFileNameByUrl(String url){
		if (TextUtils.isEmpty(url)) return url;
		String[] u = url.split("/");
		return u[u.length-1];
	}
	
}
