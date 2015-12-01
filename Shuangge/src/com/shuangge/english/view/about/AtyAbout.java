package com.shuangge.english.view.about;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.shuangge.english.support.utils.ClipboardUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.tencent.tauth.Tencent;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyAbout extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_ABOUT_US = 1045;

	private ImageButton btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_about);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		findViewById(R.id.txt1).setOnClickListener(this);
//		findViewById(R.id.txtQQ).setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.txt1:
			ClipboardUtils.copy(getString(R.string.aboutWX));
			Toast.makeText(this, R.string.aboutClipboardTip, Toast.LENGTH_SHORT).show();
			break;
//		case R.id.txtQQ:
//			String appId = "1103428370";
//			String qqGroup = "101186255";
//			Tencent mTencent = Tencent.createInstance(appId, this);
//			mTencent.joinQQGroup(this, qqGroup);
//			break;
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyAbout.class);
		context.startActivityForResult(i, REQUEST_ABOUT_US);
	}

}
