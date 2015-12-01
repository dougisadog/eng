package com.shuangge.english.view.login;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.network.login.TaskReqLogin;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.home.AtyHome;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyMetenLogin extends AbstractAppActivity implements OnClickListener, OnFocusChangeListener, TextWatcher {

	public static final int REQUEST_LOGIN = 1092;
	
	private final static int VISITER = 0;
	private final static int LOGIN = 0;
	
	private View bgLoginName, bgPassword;
	private ImageButton btnBack, btnClearLoginName, btnClearPassword;
	private Button btnForgetPwd;
	private RelativeLayout btnLogin, btnRegister;
	private EditText txtLoginName, txtPassword;
	private TextView tip1Login, tip2Login, tip3Login;
	private ImageView imgWX, imgQQ, imgMeten;
	
	private boolean requesting = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_meten_login);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnLogin = (RelativeLayout) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		txtLoginName = (EditText) findViewById(R.id.txtLoginName);
		txtLoginName.addTextChangedListener(this);
		txtLoginName.setOnFocusChangeListener(this);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPassword.setOnFocusChangeListener(this);
		txtPassword.addTextChangedListener(this);
		
		bgLoginName = findViewById(R.id.bgLoginName);
		bgPassword = findViewById(R.id.bgPassword);
		
		tip1Login = (TextView) findViewById(R.id.tip1Login);
		tip2Login = (TextView) findViewById(R.id.tip2Login);
		tip3Login = (TextView) findViewById(R.id.tip3Login);
		
		btnClearLoginName = (ImageButton) findViewById(R.id.btnClearLoginName);
		btnClearLoginName.setOnClickListener(this);
		btnClearPassword = (ImageButton) findViewById(R.id.btnClearPassword);
		btnClearPassword.setOnClickListener(this);
		if (TextUtils.isEmpty(GlobalReqDatas.getInstance().getRequestLoginName())) {
			return;
		}
		txtLoginName.setText(GlobalReqDatas.getInstance().getRequestLoginName());
		checkLoginStatus();
	}

	private boolean validateLogin() {
		if (txtLoginName.getText().toString().length() < 4) {
			Toast.makeText(this, getResources().getString(R.string.loginErrTip1), Toast.LENGTH_SHORT).show();
			return false;
		}
		if (txtPassword.getText().toString().length() < 6) {
			Toast.makeText(this, getResources().getString(R.string.loginErrTip2), Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnBack:
				startActivity(new Intent(this, AtyGuide.class));
				this.finish();
				break;
			case R.id.btnLogin:
				InputUitls.closeInputMethod(this, txtLoginName, txtPassword);
				if (!validateLogin()) 
					return;
				if (requesting)
					return;
				requesting = true;
				showLoading();
				GlobalReqDatas.getInstance().setRequestLoginName(txtLoginName.getText().toString());
				GlobalReqDatas.getInstance().setRequestPassword(txtPassword.getText().toString());
				new TaskReqLogin(LOGIN, new CallbackNoticeView<Void, Boolean>() {

					@Override
					public void refreshView(int tag, Boolean result) {
						requesting = false;
						hideLoading();
						if (null != result && result) {
							startActivity(new Intent(AtyMetenLogin.this, AtyHome.class));
							researchAty();
							
						}
					}

					@Override
					public void onProgressUpdate(int tag, Void[] values) {
					}
				});
				break;
				
			case R.id.btnClearLoginName:
				txtLoginName.setText("");
				txtLoginName.requestFocus();
				break;
			case R.id.btnClearPassword:
				txtPassword.setText("");
				txtPassword.requestFocus();
				break;
		}
		
	}
	
	private void researchAty() {
		List<Activity> atyList = GlobalApp.getInstance().getStackActivities();
		for (int i = 0; i < atyList.size(); i++) {
			if (atyList.get(i).getLocalClassName().toString().equals("com.shuangge.english.view.login.AtyLogin")) {
				atyList.get(i).finish();
			}
		}
		AtyMetenLogin.this.finish();
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.txtLoginName:
			if (hasFocus) {
				bgLoginName.setBackgroundResource(R.drawable.bg_login_input2);
			}
			else if (TextUtils.isEmpty(txtLoginName.getText().toString())) {
				bgLoginName.setBackgroundResource(R.drawable.bg_login_input);
			}
			break;
		case R.id.txtPassword:
			if (hasFocus) {
				bgPassword.setBackgroundResource(R.drawable.bg_login_input2);
			}
			else if (TextUtils.isEmpty(txtPassword.getText().toString())) {
				bgPassword.setBackgroundResource(R.drawable.bg_login_input);
			}
			break;
		default:
			break;
		}
	}
	
	private void checkLoginStatus() {
		btnClearLoginName.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearPassword.setVisibility(TextUtils.isEmpty(txtPassword.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		tip1Login.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		tip2Login.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		tip3Login.setVisibility(TextUtils.isEmpty(txtPassword.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		checkLoginStatus();
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CODE_QUIT) {
			this.finish();
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyMetenLogin.class);
		context.startActivityForResult(i, REQUEST_LOGIN);
	}
	
}
