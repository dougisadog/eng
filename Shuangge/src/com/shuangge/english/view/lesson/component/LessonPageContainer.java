package com.shuangge.english.view.lesson.component;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.support.utils.DensityUtils;

public class LessonPageContainer extends LinearLayout implements OnClickListener, OnTouchListener {

	public static final int STATE_COMPLETE = 0;
	public static final int STATE_NORMAL = 1;
	public static final int STATE_RIGHT = 2;
	public static final int STATE_WRONG = 3;
	public static final int STATE_CURRENT = 10;
	
	private List<TextView> pages = new ArrayList<TextView>();
	private CallbackPage callback;
	private static int H = 0;
	private static int MARGIN = 5;
	
	private boolean touchable = false;
	
	private int onMouseOverPageNo = -1;
	private int pageNum = 0;
	private int currentPageNo = 0;
	private int completePageNo = 0;
	private int canClickedPageNo = 0;
	private SparseIntArray pageStates = new SparseIntArray();
	
	public LessonPageContainer(Context context) {
		super(context);
		setOrientation(HORIZONTAL);
		this.setOnTouchListener(this);
	}
	
	public LessonPageContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(HORIZONTAL);
		this.setOnTouchListener(this);
    }
    
	public static interface CallbackPage {
		
		public void onPageSelected(int pageNo);
		
	}
	
	/**
	 * 设置总页数
	 */
	public void setPageNum(int num) {
		this.pageNum = num;
		initAllPageInfo();
	}
	
	public int getPageNum() {
		return pageNum;
	}
	
	public void setPageInfo(int pageNo, int state) {
		pageStates.put(pageNo, state);
	}
	
	public void setPageInfoAndRefresh(int pageNo, int state) {
		setPageInfo(pageNo, state);
		refreshPageState(pageNo);
	}
	
	public void setCanClickedPageNo(int pageNo) {
		if (pageNo < canClickedPageNo) {
			return;
		}
		this.canClickedPageNo = pageNo;
	}
	
	public int getCanClickedPageNo() {
		return canClickedPageNo;
	}
	
	public void setCallback(CallbackPage callback) {
		this.callback = callback;
	}
	
	public void refreshAllPageInfo() {
		canClickedPageNo = 0;
		int pageNo = 0;
		int state = 0;
		for (int i = 0; i < pageStates.size(); ++i) {
			pageNo = pageStates.keyAt(i);
			state = pageStates.get(pageNo);
			if (state > STATE_COMPLETE) {
				pageStates.put(pageNo, STATE_COMPLETE);
			}
			refreshPageState(pageNo);
		}
	}
	
	private void initAllPageInfo() {
		if (H == 0) {
			H = DensityUtils.dip2px(getContext(), 10f); 
		}
		if (pages.size() > 0) {
			removeAllViews();
			pages.clear();
		}
		TextView txt = null;
		for (int i = 0; i < pageNum; i++) {
			txt = new TextView(getContext());
			txt.setTag(i);
			txt.setGravity(Gravity.CENTER);
			txt.setHeight(H);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);  // , 1是可选写的
			lp.setMargins(MARGIN, 0, MARGIN, 0); 
			txt.setLayoutParams(lp);
			refreshPageState(txt, i);
			txt.getBackground().setAlpha(0);
			addView(txt);
			pages.add(txt);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (null != callback)
			callback.onPageSelected((Integer) v.getTag());
	}
	
	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  page stage
	/**
	/***************************************************************************************************************************************/
	
	private void refreshPageState(int page) {
		if (page >= pages.size()) {
			return;
		}
		View v = pages.get(page);
		refreshPageState(v, page);
	}
	
	private void refreshPageState(View v, int page) {
		int state = pageStates.get(page, canClickedPageNo < page ? STATE_COMPLETE : STATE_NORMAL);
		switch (state) {
		case STATE_COMPLETE:
			v.setBackgroundResource(R.drawable.bg_btn_lesson_page);
			break;
		case STATE_NORMAL:
			v.setBackgroundResource(R.drawable.bg_btn_lesson_page2);
			break;
		case STATE_RIGHT:
			v.setBackgroundResource(R.drawable.bg_btn_lesson_page3);
			break;
		case STATE_WRONG:
			v.setBackgroundResource(R.drawable.bg_btn_lesson_page4);
			break;
		}
	}
	
	private void setPageCurrentState(int page) {
		TextView txt = pages.get(page);
		txt.setBackgroundResource(R.drawable.bg_btn_lesson_page_p);
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  setCompletePageNo
	/**
	/***************************************************************************************************************************************/
	
	public int getCompletePage() {
		return completePageNo;
	}
	
	public void setCompletePage(int pageNo) {
		if (pages.size() <= pageNo || pageNo < 0) {
			return;
		}
		completePageNo = pageNo;
		TextView txt = pages.get(completePageNo);
		txt.getBackground().setAlpha(255);
	}
	
	public void setCompleteAllPage() {
		for (int i = 0; i < pages.size(); i++) {
			TextView txt = pages.get(i);
			txt.getBackground().setAlpha(255);
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!touchable) {
			return true;
		}
		Integer pageNo = getPageNoByXY(event.getX(), event.getY());
		if (canClickedPageNo < pageNo || completePageNo < pageNo) {
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onMouseOverPageBtn(pageNo);
			break;
		case MotionEvent.ACTION_MOVE:
			onMouseOverPageBtn(pageNo);
			break;
		case MotionEvent.ACTION_UP:
			gotoPageNo(pageNo);
			break;
		}
		return true;
	}

	private Integer getPageNoByXY(float x, float y) {
		if (y < 0 || null == pages || pages.size() == 0) {
			return currentPageNo;
		}
		int width = getWidth() / pages.size();
		return (int) (x / width);
	}
	
	private void onMouseOverPageBtn(int pageNo) {
		if (onMouseOverPageNo !=- 1) {
			refreshPageState(onMouseOverPageNo);
		}
		onMouseOverPageNo = pageNo;
		setPageCurrentState(onMouseOverPageNo);
	}
	
	public void gotoPageNo(int pageNo) {
		if (canClickedPageNo < pageNo || completePageNo < pageNo) {
			return;
		}
		this.currentPageNo = pageNo;
		if (null != callback)
			callback.onPageSelected(pageNo);
		// 执行该page 在进度条中状态的
		onMouseOverPageBtn(currentPageNo);
	}
	
	public void setCurrentPageNo(int pageNo) {
		if (canClickedPageNo < pageNo || completePageNo < pageNo) {
			return;
		}
		this.currentPageNo = pageNo;
		onMouseOverPageBtn(currentPageNo);
	}
}
