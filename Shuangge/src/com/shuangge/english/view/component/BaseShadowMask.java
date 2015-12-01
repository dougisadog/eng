package com.shuangge.english.view.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ImageView圆形遮罩处理 background设置遮罩颜色 tag设置圆的边框色 padding设置圆形线宽
 * 
 * @author planet
 *
 */
public class BaseShadowMask extends Fragment {
	
	public static interface CallbackHomeMask {
		
		public void close();
		
	}
	
	private TextView ivMaskOk, title;
	private static CallbackHomeMask callback;
	private static ViewGroup vg;
	
	private String content , btnName;
	
	public BaseShadowMask() {
		super();
	}
	
	public BaseShadowMask(CallbackHomeMask callback, ViewGroup vg , String content, String btnName) {
		super();
		BaseShadowMask.callback = callback;
		BaseShadowMask.vg = vg;
		this.btnName = btnName;
		this.content = content;
	}
	
	public void show(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(vg.getId(), this);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
	}
	
	public void hide(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.remove(this);
		ft.commit();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.component_llk_mask, container, false);
        ivMaskOk = (TextView) v.findViewById(R.id.ivMaskOk);
        if (!TextUtils.isEmpty(btnName)) {
        	ivMaskOk.setText(btnName);
        }
        ivMaskOk.setOnClickListener(clickListener);
        
        title = (TextView) v.findViewById(R.id.title);
        if (!TextUtils.isEmpty(content)) {
        	title.setText(content);
        }
        v.setOnClickListener(clickListener);
		return v;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ivMaskOk) {
				callback.close();
			}
		}
		
	};
	
}
