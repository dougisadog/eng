package com.shuangge.english.view.settings;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
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
import com.shuangge.english.network.account.TaskReqBindingAlipay;
import com.shuangge.english.network.settings.TaskReqChangePwd;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyChangePwd extends AbstractAppActivity implements
		OnClickListener, TextWatcher, OnFocusChangeListener {

	private ImageButton btnBack, btnClearOldPwd, btnClearNewPwd;
	private EditText inputNewPwd, inputOldPwd;
	private Button btnChangePwd;
	private boolean requesting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_changepassword);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		inputOldPwd = (EditText) findViewById(R.id.inputOldPwd);
		inputOldPwd.addTextChangedListener(this);
		inputOldPwd.setOnFocusChangeListener(this);
		inputNewPwd = (EditText) findViewById(R.id.inputNewPwd);
		inputNewPwd.setOnFocusChangeListener(this);
		inputNewPwd.addTextChangedListener(this);
		
		btnClearOldPwd = (ImageButton) findViewById(R.id.btnClearOldPwd);
		btnClearOldPwd.setOnClickListener(this);
		btnClearNewPwd = (ImageButton) findViewById(R.id.btnClearNewPwd);
		btnClearNewPwd.setOnClickListener(this);
		btnChangePwd = (Button) findViewById(R.id.btnChangePwd);
		btnChangePwd.setOnClickListener(this);
		
		checkStatus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnChangePwd:
			if (requesting) {
				return;
			}
			if (TextUtils.isEmpty(inputOldPwd.getText())||TextUtils.isEmpty(inputNewPwd.getText())) {
				Toast.makeText(AtyChangePwd.this, R.string.loginErrTip2, Toast.LENGTH_SHORT).show();
			}
			requesting = true;
			showLoading();
			new TaskReqChangePwd(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyChangePwd.this, R.string.changePasswordTip3, Toast.LENGTH_SHORT).show();
					AtyChangePwd.this.finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, inputOldPwd.getText().toString(), inputNewPwd.getText().toString());
			break;
		case R.id.btnClearOldPwd:
			inputOldPwd.setText("");
			break;
		case R.id.btnClearNewPwd:
			inputNewPwd.setText("");
			break;
		}
	}
	
	private void checkStatus() {
		btnClearOldPwd.setVisibility(TextUtils.isEmpty(inputOldPwd.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearNewPwd.setVisibility(TextUtils.isEmpty(inputNewPwd.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
//		txtNewAlipayTips.setVisibility(TextUtils.isEmpty(inputNewAlipay.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
//		txtPasswordTips.setVisibility(TextUtils.isEmpty(inputPassword.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
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

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
	}

}
