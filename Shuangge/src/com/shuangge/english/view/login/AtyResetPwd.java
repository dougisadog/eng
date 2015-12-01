package com.shuangge.english.view.login;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.network.login.TaskReqSetNewPassword;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyResetPwd extends AbstractAppActivity implements OnClickListener, TextWatcher, OnFocusChangeListener {
	
	private boolean requesting = false;

	private ImageButton btnBack, btnClearNewPwd;
	private EditText inputNewPwd;
	private TextView txtNewPwdTips, txtSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_forget_reset_pwd);
	
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		txtSubmit = (TextView) findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		txtSubmit.setEnabled(false);
		inputNewPwd = (EditText) findViewById(R.id.inputNewPwd);
		inputNewPwd.setOnFocusChangeListener(this);
		inputNewPwd.addTextChangedListener(this);
		
		txtNewPwdTips = (TextView) findViewById(R.id.txtNewPwdTips);
		
		btnClearNewPwd = (ImageButton) findViewById(R.id.btnClearNewPwd);
		btnClearNewPwd.setOnClickListener(this);
		
		checkStatus();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.txtSubmit:
			if (requesting)
				return;
			requesting = true;
			showLoading();
			GlobalReqDatas.getInstance().setRequestPassword(inputNewPwd.getText().toString());
			new TaskReqSetNewPassword(0, new CallbackNoticeView<Void, Boolean>() {
				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyResetPwd.this, R.string.forgetResetPwdSuccessTip, Toast.LENGTH_SHORT).show();
					setResult(CODE_QUIT);
					AtyResetPwd.this.finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}

			});
			break;
		case R.id.btnClearNewPwd:
			inputNewPwd.setText("");
			break;
		}
	}
	
	private void checkStatus() {
		btnClearNewPwd.setVisibility(TextUtils.isEmpty(inputNewPwd.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		txtNewPwdTips.setVisibility(TextUtils.isEmpty(inputNewPwd.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		ViewUtils.setEnable(txtSubmit, R.drawable.s_btn_green, inputNewPwd.getText().toString().length() >= 6);
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

}
