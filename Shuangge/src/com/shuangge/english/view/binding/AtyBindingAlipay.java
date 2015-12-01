package com.shuangge.english.view.binding;

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

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.network.account.TaskReqBindingAlipay;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyBindingAlipay extends AbstractAppActivity implements
		OnClickListener, TextWatcher, OnFocusChangeListener {

	private ImageButton btnBack, btnClearNewAlipay, btnClearPassword;
	private EditText inputNewAlipay, inputPassword;
	private TextView txtAlipay, txtNewAlipayTips, txtPasswordTips, txtSubmit;
	
	private boolean requesting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_binding_alipay);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		txtAlipay = (TextView) findViewById(R.id.txtAlipay);
		
		txtSubmit = (TextView) findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		inputNewAlipay = (EditText) findViewById(R.id.inputNewAlipay);
		inputNewAlipay.addTextChangedListener(this);
		inputNewAlipay.setOnFocusChangeListener(this);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		inputPassword.setOnFocusChangeListener(this);
		inputPassword.addTextChangedListener(this);
		
		txtNewAlipayTips = (TextView) findViewById(R.id.txtNewAlipayTips);
		txtPasswordTips = (TextView) findViewById(R.id.txtPasswordTips);
		
		btnClearNewAlipay = (ImageButton) findViewById(R.id.btnClearNewAlipay);
		btnClearNewAlipay.setOnClickListener(this);
		btnClearPassword = (ImageButton) findViewById(R.id.btnClearPassword);
		btnClearPassword.setOnClickListener(this);
		
		String alipayNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getAlipay();
		if (!TextUtils.isEmpty(alipayNo)) {
			txtAlipay.setText(txtAlipay.getText() + "\n" + alipayNo);
		}
		txtAlipay.setVisibility(TextUtils.isEmpty(alipayNo) ? View.GONE : View.VISIBLE);
		checkStatus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.txtSubmit:
			if (requesting) {
				return;
			}
			requesting = true;
			showLoading();
			new TaskReqBindingAlipay(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyBindingAlipay.this, R.string.bindingAlipaySuccessTip, Toast.LENGTH_SHORT).show();
					AtyBindingAlipay.this.finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, inputNewAlipay.getText().toString(), inputPassword.getText().toString());
			break;
		case R.id.btnClearNewAlipay:
			inputNewAlipay.setText("");
			break;
		case R.id.btnClearPassword:
			inputPassword.setText("");
			break;
		}
	}
	
	private void checkStatus() {
		btnClearNewAlipay.setVisibility(TextUtils.isEmpty(inputNewAlipay.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearPassword.setVisibility(TextUtils.isEmpty(inputPassword.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
//		txtNewAlipayTips.setVisibility(TextUtils.isEmpty(inputNewAlipay.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
//		txtPasswordTips.setVisibility(TextUtils.isEmpty(inputPassword.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		ViewUtils.setEnableColor(txtSubmit, 0xff26a69a, inputNewAlipay.toString().length() > 1 && inputPassword.length() > 3);
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
