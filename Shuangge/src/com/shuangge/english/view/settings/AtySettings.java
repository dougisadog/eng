package com.shuangge.english.view.settings;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.network.settings.TaskReqSettings;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.ComponentMultiSwitch;
import com.shuangge.english.view.component.ComponentMultiSwitch.onSelectedListener;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtySettings extends AbstractAppActivity implements OnClickListener, onSelectedListener {

	public static final int REQUEST_SETTINGS = 1046;
	
	private ImageButton btnBack;
	private ImageView btnSave;
	private RelativeLayout btnChangePwd;
	private ComponentMultiSwitch ms1, ms2, ms3, ms6;
	private int speechAccuracy, secretMsgAcceptStatus;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_settings);
		speechAccuracy = GlobalRes.getInstance().getBeans().getSpeechAccuracy();
		secretMsgAcceptStatus = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getSecretMsgAcceptStatus();
		
		int index = 0;
		if (speechAccuracy <= 20) {
		}
		else if (speechAccuracy <= 50) {
			index = 1;
		}
		else {
			index = 2;
		}
		
		ms1 = (ComponentMultiSwitch) findViewById(R.id.ms1);
		ms1.setOnSelectedListener(this);
		ms1.setVal(index);
		
		ms2 = (ComponentMultiSwitch) findViewById(R.id.ms2);
		ms2.setOnSelectedListener(this);
		ms2.setVal(GlobalRes.getInstance().getBeans().isWifiAutoDownLoad() ? 0 : 1);
		
		ms3 = (ComponentMultiSwitch) findViewById(R.id.ms3);
		ms3.setOnSelectedListener(this);
		ms3.setVal(GlobalRes.getInstance().getBeans().isCellularTipsDownLoad() ? 0 : 1);
		
		ms6 = (ComponentMultiSwitch) findViewById(R.id.ms6);
		ms6.setOnSelectedListener(this);
		ms6.setVal(secretMsgAcceptStatus);
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		btnChangePwd = (RelativeLayout) findViewById(R.id.rlChangePwd);
		btnChangePwd.setOnClickListener(this);
		
		boolean isMetenUser = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserType().indexOf("meten") != -1;
		
		boolean isVisitter = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().isVisitor();
		
		if(isMetenUser || isVisitter) {
			btnChangePwd.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnSave:
			showLoading();
			GlobalRes.getInstance().getBeans().setSpeechAccuracy(speechAccuracy);
			GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().setSecretMsgAcceptStatus(secretMsgAcceptStatus);
			new TaskReqSettings(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtySettings.this, getString(R.string.saveSuccessTipCn), Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, speechAccuracy, null, secretMsgAcceptStatus);
			break;
		case R.id.rlChangePwd:
			startActivity(new Intent(AtySettings.this, AtyChangePwd.class));
			break;
		}
	}

	@Override
	public void onSelected(View v, int index) {
		switch (v.getId()) {
		case R.id.ms1:
			switch (index) {
			case 0:
				speechAccuracy = 20;
				break;
			case 1:
				speechAccuracy = 50;
				break;
			case 2:
				speechAccuracy = 70;
				break;
			}
			break;
		case R.id.ms2:
			switch (index) {
			case 0:
				GlobalRes.getInstance().getBeans().setWifiAutoDownLoad(true);
				break;
			case 1:
				GlobalRes.getInstance().getBeans().setWifiAutoDownLoad(false);
				break;
			}
			break;
		case R.id.ms3:
			switch (index) {
			case 0:
				GlobalRes.getInstance().getBeans().setCellularTipsDownLoad(true);
				break;
			case 1:
				GlobalRes.getInstance().getBeans().setCellularTipsDownLoad(false);
				break;
			}
			break;
		case R.id.ms6:
			secretMsgAcceptStatus = index;
			break;
		}
	}

	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtySettings.class);
		context.startActivityForResult(i, REQUEST_SETTINGS);
	}
	
}
