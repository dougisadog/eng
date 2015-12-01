package com.shuangge.english.support.utils;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 
 * @author Jeffrey
 *
 */
public class ViewUtils {

	public static ViewGroup.LayoutParams setViewGroupWH(View v, int w, int h) {
		ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(w, h);
		v.setLayoutParams(p);
		return (ViewGroup.LayoutParams) v.getLayoutParams();
	}
	
	public static AbsListView.LayoutParams setAbsListViewWH(View v, int w, int h) {
		AbsListView.LayoutParams p = new AbsListView.LayoutParams(w, h);
		v.setLayoutParams(p);
		return (AbsListView.LayoutParams) v.getLayoutParams();
	}
	
	public static FrameLayout.LayoutParams setFrameMargins(View v, int w, int h, int l, int t, int r, int b) {
		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(w, h);
		p.setMargins(l, t, r, b);
		v.setLayoutParams(p);
		return (FrameLayout.LayoutParams) v.getLayoutParams();
	}
	
	public static LinearLayout.LayoutParams setLinearMargins(View v, int w, int h, int l, int t, int r, int b) {
//		if (null == v.getLayoutParams()) {
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w, h);
			p.setMargins(l, t, r, b);
			v.setLayoutParams(p);
			return (LinearLayout.LayoutParams) v.getLayoutParams();
//		}
//		else if (v.getLayoutParams() instanceof LinearLayout.LayoutParams) {
//			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
//			p.setMargins(l, t, r, b);
//			v.requestLayout();
//			return (LinearLayout.LayoutParams) v.getLayoutParams();
//		}
//		return null;
	}
	
	public static RelativeLayout.LayoutParams setRelativeMargins(View v, int w, int h, int l, int t, int r, int b) {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, h);
		p.setMargins(l, t, r, b);
		v.setLayoutParams(p);
		return (RelativeLayout.LayoutParams) v.getLayoutParams();
	}
	
	public static RelativeLayout.LayoutParams setRelativeMargins(View v, int w, int h) {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w, h);
		v.setLayoutParams(p);
		return (RelativeLayout.LayoutParams) v.getLayoutParams();
	}

	public static int[] getViewSize(View v) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		v.measure(width, height);
		System.out.println("w:" + v.getMeasuredWidth() + " h:" + v.getMeasuredHeight());
		return new int[] {v.getMeasuredWidth(), v.getMeasuredHeight()};
	}
	
	public static Bitmap convertViewToBitmap(View v){
		v.setDrawingCacheEnabled(true);
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight()); 
		v.buildDrawingCache(true);
		Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);
		return b;
	}
	
	public static Bitmap loadBitmapFromView(View v) {
		System.out.println(v.getWidth());
		System.out.println(v.getHeight());
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);                
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.draw(c);
		return b;
	}
	
	public static void setFocus(View v, boolean status) {
		v.setEnabled(status);
		v.setFocusable(status);
		v.setFocusableInTouchMode(status);
		v.requestFocus();
	}
	
	public static void setEnable(View v, boolean enable) {
		v.setEnabled(enable);
	}
	
	public static void setEnable(View v, int drawable, boolean enable) {
		v.setEnabled(enable);
		if (drawable > 0) {
			v.setBackgroundResource(v.isEnabled() ? drawable : R.drawable.s_btn_gray);
		}
	}
	
	public static void setEnableColor(View v, int color, boolean enable) {
		v.setEnabled(enable);
		v.setBackgroundColor(v.isEnabled() ? color : Color.GRAY);
	}
	
}
