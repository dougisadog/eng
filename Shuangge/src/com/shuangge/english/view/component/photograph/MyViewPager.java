package com.shuangge.english.view.component.photograph;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private boolean canScroll = true;
	
	public MyViewPager(Context context) {
		super(context);
	}
	
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (canScroll) {
			return super.onInterceptTouchEvent(ev);
		}
		return false;
	}

	public boolean isCanScroll() {
		return canScroll;
	}

	public void setCanScroll(boolean canScroll) {
		this.canScroll = canScroll;
	}

	
}
