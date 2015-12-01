package com.shuangge.english.view.login;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shuangge.english.network.account.TaskReqBindingAccount;
import com.shuangge.english.network.login.TaskReqRegister;
import com.shuangge.english.network.login.TaskReqRegisterPhoneToken;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.home.AtyHome;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyRegisterPhone extends AbstractAppActivity implements
		OnClickListener, TextWatcher, OnFocusChangeListener {

	public static final int TYPE_REGISTER = 1;
	public static final int TYPE_BINDING_ACCOUNT = 2;

	private boolean requesting = false;

	private ImageButton btnBack, btnClearToken;
	private EditText inputToken;
	private TextView txtTokenTips, txtGetToken, txtSubmit;

	private TimeCount timeCount;
	private String tokenStr;

	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_register_phone);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		txtGetToken = (TextView) findViewById(R.id.txtGetToken);
		txtGetToken.setOnClickListener(this);
		txtSubmit = (TextView) findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		txtSubmit.setEnabled(false);
		inputToken = (EditText) findViewById(R.id.inputToken);
		inputToken.setOnFocusChangeListener(this);
		inputToken.addTextChangedListener(this);

		txtTokenTips = (TextView) findViewById(R.id.txtTokenTips);

		btnClearToken = (ImageButton) findViewById(R.id.btnClearToken);
		btnClearToken.setOnClickListener(this);

		timeCount = new TimeCount(60000, 1000);
		tokenStr = txtGetToken.getText().toString();

		type = getIntent().getIntExtra("type", TYPE_REGISTER);

		requestData();
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
		ViewUtils.setEnable(txtGetToken, false);
		txtGetToken.setTextColor(getResources().getColor(R.color.gainsboro));
		new TaskReqRegisterPhoneToken(0,
				new CallbackNoticeView<Void, Boolean>() {

					@Override
					public void refreshView(int tag, Boolean result) {
						requesting = false;
						if (null != result && result) {
							timeCount.start();
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
		case R.id.txtGetToken:
			if (txtGetToken.isEnabled()) {
				requestData();
			}
			break;
		case R.id.txtSubmit:
			if (requesting)
				return;
			requesting = true;
			showLoading();
			GlobalReqDatas.getInstance().setRequestPhotoToken(
					inputToken.getText().toString());
			switch (type) {
			case TYPE_BINDING_ACCOUNT:
				new TaskReqBindingAccount(0,
						new CallbackNoticeView<Void, Boolean>() {

							@Override
							public void refreshView(int tag, Boolean result) {
								requesting = false;
								hideLoading();
								if (null != result && result) {
									setResult(CODE_QUIT);
									finish();
								}
							}

							@Override
							public void onProgressUpdate(int tag, Void[] values) {
							}

						});
				break;
			default:
				new TaskReqRegister(0, new CallbackNoticeView<Void, Boolean>() {
					@Override
					public void refreshView(int tag, Boolean result) {
						requesting = false;
						hideLoading();
						if (null != result && result) {
							startActivity(new Intent(AtyRegisterPhone.this, AtyHome.class));
							setResult(CODE_QUIT);
							finish();
						}
					}

					@Override
					public void onProgressUpdate(int tag, Void[] values) {
					}

				});
				break;
			}
			break;
		case R.id.btnClearToken:
			inputToken.setText("");
			break;
		}
	}

	private void checkStatus() {
		btnClearToken.setVisibility(TextUtils.isEmpty(inputToken.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
//		txtTokenTips.setVisibility(TextUtils.isEmpty(inputToken.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		ViewUtils.setEnableColor(txtSubmit, 0xff26a69a, inputToken.getText().toString().length() == 4);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
			txtGetToken.setText(tokenStr + " (" + millisUntilFinished / 1000 + "s)");
		}

		@Override
		public void onFinish() {
			txtGetToken.setText(tokenStr);
			ViewUtils.setEnable(txtGetToken, true);
			txtGetToken.setTextColor(0xff26a69a);
		}

	}
	
	/************************************************************************************************/
	
	public static void startAty(int type, Activity context, int requestCode) {
		Intent i = new Intent(context, AtyRegisterPhone.class);
		i.putExtra("type", type);
		context.startActivityForResult(i, requestCode);
	}

}
