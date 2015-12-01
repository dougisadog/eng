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
import com.shuangge.english.network.account.TaskReqBindingWeChat;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyBindingWeChat extends AbstractAppActivity implements
		OnClickListener, OnFocusChangeListener, TextWatcher {

	private ImageButton btnBack, btnClearNewWechat, btnClearPassword;
	private EditText inputNewWechat, inputPassword;
	private TextView txtWechat, txtNewWechatTips, txtPasswordTips, txtSubmit;
	
	private boolean requesting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_binding_wechat);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		txtWechat = (TextView) findViewById(R.id.txtWechat);
		
		txtSubmit = (TextView) findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		inputNewWechat = (EditText) findViewById(R.id.inputNewWechat);
		inputNewWechat.addTextChangedListener(this);
		inputNewWechat.setOnFocusChangeListener(this);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		inputPassword.setOnFocusChangeListener(this);
		inputPassword.addTextChangedListener(this);
		
		txtNewWechatTips = (TextView) findViewById(R.id.txtNewWechatTips);
		txtPasswordTips = (TextView) findViewById(R.id.txtPasswordTips);
		
		btnClearNewWechat = (ImageButton) findViewById(R.id.btnClearNewWechat);
		btnClearNewWechat.setOnClickListener(this);
		btnClearPassword = (ImageButton) findViewById(R.id.btnClearPassword);
		btnClearPassword.setOnClickListener(this);
		
		String wechatNo = getIntent().getStringExtra("wechatNo");
		if (!TextUtils.isEmpty(wechatNo)){
			inputNewWechat.setText(wechatNo);	
		}
		
		String weChatNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getWechatNo();
		if (!TextUtils.isEmpty(weChatNo)) {
			txtWechat.setText(txtWechat.getText() + "\n" + weChatNo);
		}
		txtWechat.setVisibility(TextUtils.isEmpty(weChatNo) ? View.GONE : View.VISIBLE);
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
			new TaskReqBindingWeChat(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyBindingWeChat.this, R.string.bindingWechatSuccessTip, Toast.LENGTH_SHORT).show();
					AtyBindingWeChat.this.setResult(1);
					AtyBindingWeChat.this.finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, inputNewWechat.getText().toString(), inputPassword.getText().toString());
			break;
		case R.id.btnClearNewWechat:
			inputNewWechat.setText("");
			break;
		case R.id.btnClearPassword:
			inputPassword.setText("");
			break;
		}
	}
	
	private void checkStatus() {
		btnClearNewWechat.setVisibility(TextUtils.isEmpty(inputNewWechat.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearPassword.setVisibility(TextUtils.isEmpty(inputPassword.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		txtNewWechatTips.setVisibility(TextUtils.isEmpty(inputNewWechat.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		txtPasswordTips.setVisibility(TextUtils.isEmpty(inputPassword.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		ViewUtils.setEnable(txtSubmit, R.drawable.s_btn_green, inputNewWechat.toString().length() > 1 && inputPassword.length() > 3);
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
