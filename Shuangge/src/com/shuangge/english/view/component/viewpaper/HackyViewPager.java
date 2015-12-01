package com.shuangge.english.view.component.viewpaper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {
	
	private ViewPaperIndicator mIndicator;
	private PagerAdapter mAdapter;
	private int initIndex = 0;

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public void setAdapter(PagerAdapter adapter) {
    	super.setAdapter(adapter);
    	this.mAdapter = adapter;
    }
    
    @Override
    public void setCurrentItem(int item) {
    	super.setCurrentItem(item);
    	initIndex = item;
    }
    
    public int getViewsCount() {
		return mAdapter.getCount();
	}
    
    private int getWidthPadding() {
		return getPaddingLeft() + getPaddingRight() + getHorizontalFadingEdgeLength() * 2;
	}

	public int getChildWidth() {
		return getWidth() - getWidthPadding();
	}
	
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
	protected void onScrollChanged(int h, int v, int oldh, int oldv) {
		super.onScrollChanged(h, v, oldh, oldv);
		if (mIndicator != null) {
			/*
			 * The actual horizontal scroll origin does typically not match the
			 * perceived one. Therefore, we need to calculate the perceived
			 * horizontal scroll origin here, since we use a view buffer.
			 */
//			int hPerceived = h + (mCurrentAdapterIndex - mCurrentBufferIndex) * getChildWidth();
//			mIndicator.onScrolled(hPerceived, v, oldh, oldv);
			System.out.println(getChildWidth());
			int hPerceived = h == 0 ? (h + getCurrentItem() * getChildWidth()) : (h + initIndex * getChildWidth());
			mIndicator.onScrolled(hPerceived, v, oldh, oldv);
//			mIndicator.onScrolled(h, v, oldh, oldv);
		}
	}
    
    private boolean hasMeasured;
    
	public void setViewPagerIndicator(ViewPaperIndicator viewPaperIndicator) {
		mIndicator = viewPaperIndicator;
		mIndicator.setViewPager(this);
		mIndicator.onSwitched(null, initIndex);
		hasMeasured = true;
		
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				if (!hasMeasured) {
					return;
				}
				onScrollChanged(0, 0, 0, 0);
				hasMeasured = false;
			}
		});
		
	}

}