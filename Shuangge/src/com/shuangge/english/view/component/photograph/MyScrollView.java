package com.shuangge.english.view.component.photograph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	private boolean canScroll = true;
	
	public MyScrollView(Context context) {
		super(context);
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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
