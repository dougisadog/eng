package com.shuangge.english.view.binding;

import air.com.shuangge.phone.ShuangEnglish.R;
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

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.network.account.TaskReqBindingPhone;
import com.shuangge.english.network.account.TaskReqPhoneToken;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.CheckUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyBindingPhone extends AbstractAppActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {

	private ImageButton btnBack, btnClearNewPhone, btnClearToken;
	private Button btnGetToken, btnBindingPhone;
	private EditText inputNewPhone, inputToken;
	private TextView txtPhone, txtTokenTips, txtNewPhoneTips;
	
	private TimeCount timeCount;
	private String tokenStr;
	private boolean timeUp = true;
	
	private boolean requesting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		timeCount.cancel();
		timeCount = null;
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_binding_phone);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		txtPhone = (TextView) findViewById(R.id.txtPhone);
		
		btnGetToken = (Button) findViewById(R.id.btnGetToken);
		btnGetToken.setOnClickListener(this);
		btnBindingPhone = (Button) findViewById(R.id.btnBindingPhone);
		btnBindingPhone.setOnClickListener(this);
		inputNewPhone = (EditText) findViewById(R.id.inputNewPhone);
		inputNewPhone.addTextChangedListener(this);
		inputNewPhone.setOnFocusChangeListener(this);
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
		
		String phone = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getPhone();
		if (!TextUtils.isEmpty(phone)) {
			txtPhone.setText(txtPhone.getText() + "\n" + phone);
		}
		txtPhone.setVisibility(TextUtils.isEmpty(phone) ? View.GONE : View.VISIBLE);
		checkStatus();
	}
	
	private void requestData() {
		if (requesting) {
			return;
		}
		requesting = true;
		ViewUtils.setEnable(btnGetToken, 0xff26a69a, false);
		new TaskReqPhoneToken(0, new CallbackNoticeView<Void, Boolean>() {

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
			
		}, inputNewPhone.getText().toString());
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
		case R.id.btnBindingPhone:
			if (requesting) {
				return;
			}
			requesting = true;
			showLoading();
			new TaskReqBindingPhone(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyBindingPhone.this, R.string.bindingPhoneSuccessTip, Toast.LENGTH_SHORT).show();
					AtyBindingPhone.this.finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, inputNewPhone.getText().toString(), inputToken.getText().toString());
			break;
		case R.id.btnClearNewPhone:
			inputNewPhone.setText("");
			break;
		case R.id.btnClearToken:
			inputToken.setText("");
			break;
		}
	}
	
	private void checkStatus() {
		btnClearNewPhone.setVisibility(TextUtils.isEmpty(inputNewPhone.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearToken.setVisibility(TextUtils.isEmpty(inputToken.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
//		txtNewPhoneTips.setVisibility(TextUtils.isEmpty(inputNewPhone.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
//		txtTokenTips.setVisibility(TextUtils.isEmpty(inputToken.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		ViewUtils.setEnableColor(btnGetToken, 0xff26a69a, timeUp && CheckUtils.isMobile(inputNewPhone.getText().toString()));
		ViewUtils.setEnableColor(btnBindingPhone, 0xff26a69a, inputToken.getText().toString().length() == 4);
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
			ViewUtils.setEnable(btnGetToken, 0xff26a69a, timeUp && CheckUtils.isMobile(inputNewPhone.getText().toString()));
		}
		
	}

}
