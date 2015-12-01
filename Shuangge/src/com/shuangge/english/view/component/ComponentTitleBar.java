package com.shuangge.english.view.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ComponentTitleBar extends FrameLayout {
	
	private RelativeLayout rlBg;
	private TextView txtBarTitleEn, txtBarTitleCn, txtBtn, txtFriend;
	private String titleStrEn, titleStrCn;
	private ImageView btnBack, btnSettings, btnSettings2, btnMsgs, btnMsgs2, btnEdit, btnSave, btnDelete, btnQuit;
	private int theme = 0;

	public ComponentTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_title_bar, this);
        
        rlBg = (RelativeLayout) findViewById(R.id.rlBg);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        txtBtn = (TextView) findViewById(R.id.txtBtn);
        txtBarTitleEn = (TextView) findViewById(R.id.txtBarTitleEn);
        txtBarTitleCn = (TextView) findViewById(R.id.txtBarTitleCn);
        btnSettings = (ImageView) findViewById(R.id.btnSettings);
        btnSettings2 = (ImageView) findViewById(R.id.btnSettings2);
        btnMsgs = (ImageView) findViewById(R.id.btnMsgs);
        btnMsgs2 = (ImageView) findViewById(R.id.btnMsgs2);
        btnEdit = (ImageView) findViewById(R.id.btnEdit);
        btnSave = (ImageView) findViewById(R.id.btnSave);
        btnDelete = (ImageView) findViewById(R.id.btnDelete);
        btnQuit = (ImageView) findViewById(R.id.btnQuit);
        
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar, 0, 0);
        titleStrEn = typedArray.getString(R.styleable.TitleBar_titleStrEn);
        titleStrCn = typedArray.getString(R.styleable.TitleBar_titleStrCn);
        boolean showSettings = typedArray.getBoolean(R.styleable.TitleBar_settings, false);
        boolean showEdit = typedArray.getBoolean(R.styleable.TitleBar_edit, false);
        boolean showSave = typedArray.getBoolean(R.styleable.TitleBar_save, false);
        boolean showDelete = typedArray.getBoolean(R.styleable.TitleBar_delete, false);
        boolean showQuit = typedArray.getBoolean(R.styleable.TitleBar_quit, false);
        boolean showMsgs = typedArray.getBoolean(R.styleable.TitleBar_msgs, false);
        boolean showBtnTxt = typedArray.getBoolean(R.styleable.TitleBar_btnText, false);
        theme = typedArray.getInt(R.styleable.TitleBar_titleTheme, 0);
        if (theme == 0) {
        	txtBarTitleEn.setTextColor(getContext().getResources().getColor(R.color.titlebar_title1));
        	txtBarTitleCn.setTextColor(getContext().getResources().getColor(R.color.titlebar_title1));
        	rlBg.setBackgroundResource(R.color.titlebar_bg1);
        	btnBack.setImageResource(R.drawable.s_btn_back1);
        	btnSave.setImageResource(R.drawable.icon_save1);
        }
        else if (theme ==1 ) {
        	txtBarTitleEn.setTextColor(getContext().getResources().getColor(R.color.titlebar_title2));
        	txtBarTitleCn.setTextColor(getContext().getResources().getColor(R.color.titlebar_title2));
        	rlBg.setBackgroundResource(R.color.titlebar_bg2);
        	btnBack.setImageResource(R.drawable.s_btn_back2);
        	btnSave.setImageResource(R.drawable.icon_save2);
        }else {
        	txtBarTitleEn.setTextColor(getContext().getResources().getColor(R.color.titlebar_title2));
        	txtBarTitleCn.setTextColor(getContext().getResources().getColor(R.color.titlebar_title2));
        	rlBg.setBackgroundResource(R.color.titlebar_bg3);
        	btnBack.setImageResource(R.drawable.s_btn_back2);
        	btnSave.setImageResource(R.drawable.icon_save2);
		}
        
        setTitle(titleStrEn, titleStrCn);
        if (showSettings) {
        	btnSettings.setVisibility(View.VISIBLE);
        }
        else if (showMsgs) {
        	btnMsgs.setVisibility(View.VISIBLE);
        }
        else if (showEdit) {
        	btnEdit.setVisibility(View.VISIBLE);
        }
        else if (showSave) {
        	btnSave.setVisibility(View.VISIBLE);
        }
        else if (showDelete) {
        	btnDelete.setVisibility(View.VISIBLE);
        }
        else if (showQuit) {
        	btnQuit.setVisibility(View.VISIBLE);
        }
        else if (showBtnTxt) {
        	txtBtn.setVisibility(View.VISIBLE);
        }
        typedArray.recycle();
	}
	
	public void setTitle(String strEn, String strCn) {
		if (!TextUtils.isEmpty(strEn)) {
			txtBarTitleEn.setText(strEn);
			txtBarTitleEn.setVisibility(View.VISIBLE);
        }
        else {
        	txtBarTitleEn.setVisibility(View.GONE);
        }
		if (!TextUtils.isEmpty(strCn)) {
			txtBarTitleCn.setText(strCn);
			txtBarTitleCn.setVisibility(View.VISIBLE);
		}
		else {
			txtBarTitleCn.setVisibility(View.GONE);
		}
	}
	
	public void showSettingMsgs() {
		btnSettings.setVisibility(View.GONE);
		btnSettings2.setVisibility(View.VISIBLE);
	}
	
	public void clearSettingMsgs() {
		btnSettings.setVisibility(View.VISIBLE);
		btnSettings2.setVisibility(View.GONE);
	}
	
	public void showMsgs() {
		btnMsgs.setVisibility(View.GONE);
		btnMsgs2.setVisibility(View.VISIBLE);
	}
	
	public void clearMsgs() {
		btnMsgs.setVisibility(View.VISIBLE);
		btnMsgs2.setVisibility(View.GONE);
	}
	
}
