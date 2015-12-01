package com.shuangge.english.view.lesson.component;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.shuangge.english.support.app.AppInfo;

public class LessonViewPager extends ViewPager {
	
	private float mLastMotionX;
	private int canMoveItemNo = 0;
	
	private boolean moving = false;
	private boolean touched = false;
	private FixedSpeedScroller scroller;

	public LessonViewPager(Context context) {
		super(context);
		setViewPagerScrollSpeed();
	}
	
	public LessonViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setViewPagerScrollSpeed();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			onInterceptTouchEvent(ev);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float x = ev.getRawX();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			touched = true;
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!touched) {
				break;
			}
			int w = AppInfo.getScreenWidth() / 6;
			final int deltaX = (int) (mLastMotionX - x);
			//right
			if (deltaX > w) {
				int nextItem = getCurrentItem() + 1;
				if (canMoveItemNo >= nextItem && nextItem < getAdapter().getCount()) {
					setCurrentItem(nextItem);
					touched = false;
				}
			}
			//left
			else if (deltaX < -w) {
				int prevItem = getCurrentItem() - 1;
				if (prevItem >= 0) {
					setCurrentItem(prevItem);
					touched = false;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return false;
	}
	
	public int getCanMoveItemNo() {
		return canMoveItemNo;
	}

	public void setCanMoveItemNo(int canMoveItemNo) {
		this.canMoveItemNo = canMoveItemNo;
	}

	private void setViewPagerScrollSpeed() {  
		try {  
		    Field mScroller = null;  
		    mScroller = ViewPager.class.getDeclaredField("mScroller");  
		    mScroller.setAccessible(true);   
		    scroller = new FixedSpeedScroller(this.getContext());  
		    mScroller.set(this, scroller);  
		}
		catch(NoSuchFieldException e){  
		}
		catch (IllegalArgumentException e){  
		}
		catch (IllegalAccessException e){  
		} 
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
	}
	
	private class FixedSpeedScroller extends Scroller {  
	    private int mDuration = 2000;  
	  
	    public FixedSpeedScroller(Context context) {  
	        super(context);  
	    }  
	  
	    public FixedSpeedScroller(Context context, Interpolator interpolator) {  
	        super(context, interpolator);  
	    }  
	  
	    @Override  
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
//	    	if (touched) {
//	    		super.startScroll(startX, startY, dx, dy, duration);
//	    		return;
//	    	}
	        super.startScroll(startX, startY, dx, dy, mDuration);  
	    }  
	    
	    @Override  
	    public void startScroll(int startX, int startY, int dx, int dy) {  
	        super.startScroll(startX, startY, dx, dy, mDuration);  
	    }  
	}

	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
		moving = true;
	}
	
	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
}
