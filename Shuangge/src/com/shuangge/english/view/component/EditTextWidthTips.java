package com.shuangge.english.view.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class EditTextWidthTips extends EditText {

	private int defaultColor;
	private String tips;
	private int tipsColor;
	private boolean touchable = true;
	
	public EditTextWidthTips(Context context) {
		super(context);
	}

	public EditTextWidthTips(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray styleAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.tipsEditText, 0, 0);
		defaultColor = getTextColors().getDefaultColor();
		tips = styleAttrs.getString(R.styleable.tipsEditText_tips);
		tipsColor = styleAttrs.getColor(R.styleable.tipsEditText_tipsColor, defaultColor);
		if (!TextUtils.isEmpty(tips) && tipsColor != -1) {
			setText(tips);
			setTextColor(tipsColor);
		}
		setOnFocusChangeListener(listener);
	}
	
	public void setTips(String tips, int tipsColor) {
		this.tips = tips;
		this.tipsColor = tipsColor;
		setText(tips);
		setTextColor(tipsColor);
	}
	
	public void setTips(String tips) {
		this.tips = tips;
		setText(tips);
		setTextColor(tipsColor);
	}
	
	private OnFocusChangeListener listener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus && getText().toString().equals(tips)) {
				setText("");
				setTextColor(defaultColor);
				return;
			}
			if (!hasFocus && TextUtils.isEmpty(getText().toString())) {
				setText(tips);
				setTextColor(tipsColor);
				return;
			}
			((EditText) v).setSelection(((EditText) v).getText().length());
		}
		
	};
	
	public void setVal(String str) {
		if (!TextUtils.isEmpty(str)) {
			setText(str);
			setTextColor(defaultColor);
			return;
		}
		setText(tips);
		setTextColor(tipsColor);
	}
	
	public String getVal() {
		if (getText().toString().equals(tips)) {
			return "";
		}
		return getText().toString();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!touchable) {
			return false;
		}
		return super.onTouchEvent(event);
	}

	public boolean isTouchable() {
		return touchable;
	}

	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}
	
}
