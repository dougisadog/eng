package com.shuangge.english.view.component;

import com.shuangge.english.support.sdcard.SDResUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ShadowGuider {
	
	public ShadowGuider(Activity activity) {
		this.activity = activity;
	}

	private Activity activity;
	
	boolean isGuided = false;

	public boolean isGuided() {
		return isGuided;
	}

	public void setGuided(boolean isGuided) {
		this.isGuided = isGuided;
	}
	
	@SuppressLint("NewApi")
	public void addGuideImage(int layoutid, int resid) {
        View view = activity.getWindow().getDecorView().findViewById(layoutid);//查找通过setContentView上的根布局
        if(view==null)return;
        if(isGuided){
            //引导过了
            return;
        }
        ViewParent viewParent = view.getParent();
        if(viewParent instanceof FrameLayout){
            final FrameLayout frameLayout = (FrameLayout)viewParent;
                final ImageView guideImage = new ImageView(activity);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                guideImage.setLayoutParams(params);
//                guideImage.setImageAlpha(66);
                guideImage.setImageResource(resid);
                if (null != SDResUtils.getCurrentPhoto(activity)) {
                	guideImage.setImageBitmap(SDResUtils.getCurrentPhoto(activity));
                }
                guideImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frameLayout.removeView(guideImage);
                        //设置引导了
                        isGuided = true;
                    }
                });
                frameLayout.addView(guideImage);//添加引导图片
                
        }
    }
	
}
