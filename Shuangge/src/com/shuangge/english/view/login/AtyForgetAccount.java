package com.shuangge.english.view.login;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.network.login.TaskReqCheckTokenForForgetAccount;
import com.shuangge.english.network.login.TaskReqPhoneTokenForForgetAccount;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.CheckUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyForgetAccount extends AbstractAppActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {
	
	private ImageButton btnBack, btnClearNewPhone, btnClearToken;
	private Button btnGetToken, btnSubmit;
	private EditText inputPhone, inputToken;
	private TextView txtTokenTips, txtNewPhoneTips;
	
	private TimeCount timeCount;
	private String tokenStr;
	private boolean timeUp = true;
	
	private boolean requesting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_forget_account);
	
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		btnGetToken = (Button) findViewById(R.id.btnGetToken);
		btnGetToken.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		inputPhone = (EditText) findViewById(R.id.inputPhone);
		inputPhone.addTextChangedListener(this);
		inputPhone.setOnFocusChangeListener(this);
		inputToken = (EditText) findViewById(R.id.inputToken);
		inputToken.setOnFocusChangeListener(this);
		inputToken.addTextChangedListener(this);
		
		txtTokenTips = (TextView) findViewById(R.id.txtTokenTips);
		txtNewPhoneTips = (TextView) findViewById(R.id.txtNewPhoneTips);
		
		btnClearNewPhone = (ImageButton) findViewById(R.id.btnClearNewPhone);
		btnClearNewPhone.setOnClickListener(this);
		btnClearToken = (ImageButton) findViewById(R.id.btnClearToken);
		btnClearToken.setOnClickListener(this);
		
		timeCount = new TimeCount(60000, 1000);
		tokenStr = btnGetToken.getText().toString();
		
		checkStatus();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		timeCount.cancel();
		timeCount = null;
	}
	
	private void requestData() {
		if (requesting) {
			return;
		}
		requesting = true;
		GlobalReqDatas.getInstance().setRequestPhoto(inputPhone.getText().toString());
		ViewUtils.setEnable(btnGetToken, R.drawable.s_btn_green, false);
		new TaskReqPhoneTokenForForgetAccount(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				if (null != result && result) {
					timeCount.start();
					timeUp = false;
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnGetToken:
			if (btnGetToken.isEnabled()) {
				requestData();
			}
			break;
		case R.id.btnSubmit:
			if (requesting) {
				return;
			}
			requesting = true;
			showLoading();
			GlobalReqDatas.getInstance().setRequestPhotoToken(inputToken.getText().toString());
			new TaskReqCheckTokenForForgetAccount(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyForgetAccount.this, R.string.forgetAccountSuccessTip, Toast.LENGTH_SHORT).show();
					startActivityForResult(new Intent(AtyForgetAccount.this, AtyResetPwd.class), 0);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
			break;
		case R.id.btnClearNewPhone:
			inputPhone.setText("");
			break;
		case R.id.btnClearToken:
			inputToken.setText("");
			break;
		}
	}
	
	private void checkStatus() {
		btnClearNewPhone.setVisibility(TextUtils.isEmpty(inputPhone.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearToken.setVisibility(TextUtils.isEmpty(inputToken.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
//		txtNewPhoneTips.setVisibility(TextUtils.isEmpty(inputPhone.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
//		txtTokenTips.setVisibility(TextUtils.isEmpty(inputToken.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		ViewUtils.setEnableColor(btnGetToken, 0xff26a69a, timeUp && CheckUtils.isMobile(inputPhone.getText().toString()));
		ViewUtils.setEnableColor(btnSubmit, 0xff26a69a, inputToken.getText().toString().length() == 4);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		checkStatus();
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btnGetToken.setText(tokenStr + " (" + millisUntilFinished / 1000 + "s)");
		}

		@Override
		public void onFinish() {
			timeUp = true;
			btnGetToken.setText(tokenStr);
			ViewUtils.setEnable(btnGetToken, 0xff26a69a, timeUp && CheckUtils.isMobile(inputPhone.getText().toString()));
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CODE_QUIT) {
			this.finish();
		}
	}

}
