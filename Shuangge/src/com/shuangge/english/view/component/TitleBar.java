package com.shuangge.english.view.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TitleBar extends FrameLayout {
	
	private TextView txtBarTitle, btnDo, txtTitlePoint;
	private Spinner spinnerTitle;
	private int titleColor, doColor;

	@SuppressLint("Recycle")
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_title_bar2, this);
        
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.jattrs, 0, 0);
        String titleStr = typedArray.getString(R.styleable.jattrs_txtTitle);
        titleColor = typedArray.getColor(R.styleable.jattrs_titleColor, 0);
        txtBarTitle = (TextView) findViewById(R.id.txtBarTitle);
        String doStr = typedArray.getString(R.styleable.jattrs_txtDo);
        doColor = typedArray.getColor(R.styleable.jattrs_doColor, 0);
        btnDo = (TextView) findViewById(R.id.btnDo);
        int spinnerTitleDataId = typedArray.getResourceId(R.styleable.jattrs_spinnerTitleDataId, 0);
        spinnerTitle = (Spinner) findViewById(R.id.spinnerTitle);
        txtTitlePoint = (TextView) findViewById(R.id.txtTitlePoint);
        setTitle(titleStr, titleColor);
        setEdit(doStr, doColor);
        setSpinnerTitleResId(spinnerTitleDataId);
	}
	
	public void setSpinnerTitleResId(int spinnerTitleDataId) {
		if (spinnerTitleDataId > 0) {
			setSpinnerTitleDatas(getContext().getResources().getStringArray(spinnerTitleDataId));
		}
		else {
			spinnerTitle.setVisibility(View.GONE);
			txtTitlePoint.setVisibility(View.GONE);
		}
	}
	
	public void setSpinnerTitleDatas(String[] spinnerTitleDatas) {
		if (spinnerTitleDatas.length > 0) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.title_spinner_title_bar, spinnerTitleDatas);
			adapter.setDropDownViewResource(R.layout.item_spinner_title_bar);
			spinnerTitle.setAdapter(adapter);
			spinnerTitle.setVisibility(View.VISIBLE);
			txtTitlePoint.setVisibility(View.VISIBLE);
		}
		else {
			spinnerTitle.setVisibility(View.GONE);
			txtTitlePoint.setVisibility(View.GONE);
		}
	}
	
	public void setTitle(String str) {
		setTitle(str, titleColor);
	}
	
	public void setTitle(String str, int color) {
		if (!TextUtils.isEmpty(str)) {
        	txtBarTitle.setText(str);
        	txtBarTitle.setTextColor(color);
        	txtBarTitle.setVisibility(View.VISIBLE);
        }
        else {
        	txtBarTitle.setVisibility(View.GONE);
        }
	}
	
	public void setEdit(String str) {
		setTitle(str, doColor);
	}
	
	public void setEdit(String str, int color) {
		if (!TextUtils.isEmpty(str)) {
			btnDo.setText(str);
			btnDo.setTextColor(color);
			btnDo.setVisibility(View.VISIBLE);
        }
        else {
        	btnDo.setVisibility(View.GONE);
        }
	}

	public Spinner getSpinnerTitle() {
		return spinnerTitle;
	}
	
}
