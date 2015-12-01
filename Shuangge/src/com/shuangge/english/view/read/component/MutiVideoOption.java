package com.shuangge.english.view.read.component;

import java.io.File;

import com.shuangge.english.cache.CacheDisk;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundUtils;
import com.shuangge.english.view.read.fragment.Answer;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MutiVideoOption extends LinearLayout {
	
	private boolean state = true; // ImageView/TextView visiable 

	/**
	 * 构建答案模板
	 * 
	 * @param context
	 * @param answer
	 *            答案内容
	 * @param position
	 *            答案所处位置
	 */ 
	public MutiVideoOption(Context context, final Answer answer) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout fl = (LinearLayout) inflater.inflate(R.layout.item_muti_video, this);
		ImageView toggleBtn = (ImageView) ((ViewGroup) fl.getChildAt(0)).getChildAt(0);
		final ImageView ov = (ImageView) ((ViewGroup) fl.getChildAt(0)).getChildAt(1);
		ov.setBackgroundColor(Color.YELLOW);
		ov.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = SoundUtils.getFileNameByUrl(answer.getSoundURL());
				String path = CacheDisk.CACHE_SOUND_DIR + "/" + name;
				if (!new File(path).exists()) {
					SoundUtils.loadIWordRes(path, answer.getSoundURL(), null);
				}
				else {
					MediaPlayerMgr.getInstance().playMp(path);
				}
				if (!TextUtils.isEmpty(answer.getSoundURL()))
				MediaPlayerMgr.getInstance().playMp(answer.getSoundURL());
				
			}
		});
		final TextView tv = (TextView) ((ViewGroup) fl.getChildAt(0)).getChildAt(2);
		tv.setText(answer.getName());

		toggleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (state) {
					ov.setVisibility(View.GONE);
					tv.setVisibility(View.VISIBLE);
				}
				else {
					tv.setVisibility(View.GONE);
					ov.setVisibility(View.VISIBLE);
				}
				state = !state;
			}
		});
	}

}
