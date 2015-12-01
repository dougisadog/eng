package com.shuangge.english.view.binding;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyBindingInfos extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_BINDING_INFOS = 1043;
	
	private ImageButton btnBack;
	private RelativeLayout rlBindingPhone, rlBindingWeChat, rlBindingAlipay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_binding_infos);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		rlBindingPhone = (RelativeLayout) findViewById(R.id.rlBindingPhone);
		rlBindingPhone.setOnClickListener(this);
		rlBindingWeChat = (RelativeLayout) findViewById(R.id.rlBindingWeChat);
		rlBindingWeChat.setOnClickListener(this);
		rlBindingAlipay = (RelativeLayout) findViewById(R.id.rlBindingAlipay);
		rlBindingAlipay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.rlBindingPhone:
			startActivity(new Intent(AtyBindingInfos.this, AtyBindingPhone.class));
			break;
		case R.id.rlBindingWeChat:
			startActivity(new Intent(AtyBindingInfos.this, AtyBindingWeChat.class));
			break;
		case R.id.rlBindingAlipay:
			startActivity(new Intent(AtyBindingInfos.this, AtyBindingAlipay.class));
			break;
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyBindingInfos.class);
		context.startActivityForResult(i, REQUEST_BINDING_INFOS);
	}

}
