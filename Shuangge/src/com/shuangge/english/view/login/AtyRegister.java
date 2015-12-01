package com.shuangge.english.view.login;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.network.login.TaskReqRegisterCheckName;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.EditTextWidthTips;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyRegister extends AbstractAppActivity implements OnClickListener, OnFocusChangeListener, TextWatcher, OnCheckedChangeListener {

	public static final int REQUEST_RGISTER = 1051;
	
	private final static int RGISTER = 1;
	
	private View bgLoginName, bgName, bgPassword, bgInviteNo;
	private ImageButton btnBack, btnClearLoginName, btnClearPassword, btnClearName, btnInviteNo;
	private RelativeLayout btnRegister;
	private EditText txtLoginName, txtPassword, txtName;
	private EditTextWidthTips txtInviteNo;
	private TextView tip1Register, tip2Register, tip3Register, tip4Register;
	private CheckBox checkBoxHidePwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_register);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnRegister = (RelativeLayout) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		txtLoginName = (EditText) findViewById(R.id.txtLoginName);
		txtLoginName.addTextChangedListener(this);
		txtLoginName.setOnFocusChangeListener(this);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPassword.setOnFocusChangeListener(this);
		txtPassword.addTextChangedListener(this);
		txtName = (EditText) findViewById(R.id.txtName);
		txtName.setOnFocusChangeListener(this);
		txtName.addTextChangedListener(this);
		txtInviteNo = (EditTextWidthTips) findViewById(R.id.txtInviteNo);
		txtInviteNo.setOnFocusChangeListener(this);
		txtInviteNo.addTextChangedListener(this);
		
		bgLoginName = findViewById(R.id.bgLoginName);
		bgName = findViewById(R.id.bgName);
		bgPassword = findViewById(R.id.bgPassword);
		bgInviteNo = findViewById(R.id.bgInviteNo);
		
		tip1Register = (TextView) findViewById(R.id.tip1Register);
		tip2Register = (TextView) findViewById(R.id.tip2Register);
		tip3Register = (TextView) findViewById(R.id.tip3Register);
		tip4Register = (TextView) findViewById(R.id.tip4Register);
		
		btnClearLoginName = (ImageButton) findViewById(R.id.btnClearLoginName);
		btnClearLoginName.setOnClickListener(this);
		btnClearPassword = (ImageButton) findViewById(R.id.btnClearPassword);
		btnClearPassword.setOnClickListener(this);
		btnClearName = (ImageButton) findViewById(R.id.btnClearName);
		btnClearName.setOnClickListener(this);
		btnInviteNo = (ImageButton) findViewById(R.id.btnInviteNo);
		btnInviteNo.setOnClickListener(this);
		
		checkBoxHidePwd = (CheckBox) findViewById(R.id.checkBoxHidePwd);
		checkBoxHidePwd.setOnCheckedChangeListener(this);
		checkRegisterStatus();
		txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
	}

	private boolean validateRegister() {
		if (txtLoginName.getText().toString().length() < 4) {
			Toast.makeText(this, getResources().getString(R.string.loginErrTip1), Toast.LENGTH_SHORT).show();
			return false;
		}
		if (txtName.getText().toString().length() < 3) {
			Toast.makeText(this, getResources().getString(R.string.loginErrTip3), Toast.LENGTH_SHORT).show();
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
				this.finish();
				break;
			case R.id.btnForgetPwd:
				startActivityForResult(new Intent(this, AtyForgetAccount.class), 0);
				break;
			case R.id.btnLogin:
				startActivity(new Intent(this, AtyLogin.class));
				this.finish();
				break;
			case R.id.btnRegister:
				InputUitls.closeInputMethod(this, txtLoginName, txtPassword, txtName);
				if (!validateRegister()) 
					return;
				GlobalReqDatas.getInstance().setRequestLoginName(txtLoginName.getText().toString());
				GlobalReqDatas.getInstance().setRequestName(txtName.getText().toString());
				GlobalReqDatas.getInstance().setRequestPassword(txtPassword.getText().toString());
				GlobalReqDatas.getInstance().setOtherInvitationCode(txtInviteNo.getVal());
				
				
//				if (CheckUtils.isMobile(txtLoginName.getText().toString())) {
					
//					return;
//				}
				showLoading();
				new TaskReqRegisterCheckName(0, new CallbackNoticeView<Void, Boolean>() {

					@Override
					public void refreshView(int tag, Boolean result) {
						hideLoading();
						if (null == result || !result) {
							return;
						}
						AtyRegisterPhone.startAty(AtyRegisterPhone.TYPE_REGISTER, AtyRegister.this, 0);
					}

					@Override
					public void onProgressUpdate(int tag, Void[] values) {
						
					}

				});
				break;
//			case R.id.btnVistor:
//				InputUitls.closeInputMethod(this, txtLoginName, txtPassword, txtName);
//				if (requesting)
//					return;
//				requesting = true;
//				showLoading();
//				new TaskReqVister(VISITER, new CallbackNoticeView<Void, Boolean>() {
//
//					@Override
//					public void refreshView(int tag, Boolean result) {
//						requesting = false;
//						hideLoading();
//						if (null != result && result) {
//							startActivity(new Intent(AtyRegister.this, AtyHome.class));
//							AtyRegister.this.finish();
//						}
//					}
//
//					@Override
//					public void onProgressUpdate(int tag, Void[] values) {
//					}
//
//				});
//				break;
			case R.id.btnClearLoginName:
				txtLoginName.setText("");
				txtLoginName.requestFocus();
				break;
			case R.id.btnClearPassword:
				txtPassword.setText("");
				txtPassword.requestFocus();
				break;
			case R.id.btnClearName:
				txtName.setText("");
				txtName.requestFocus();
				break;
			case R.id.btnInviteNo:
				txtInviteNo.setVal("");
				txtInviteNo.setText("可不填");
				txtInviteNo.requestFocus();
				break;
		}
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
		case R.id.txtName:
			if (hasFocus) {
				bgName.setBackgroundResource(R.drawable.bg_login_input2);
			}
			else if (TextUtils.isEmpty(txtName.getText().toString())) {
				bgName.setBackgroundResource(R.drawable.bg_login_input);
			}
			break;
		case R.id.txtInviteNo:
			if (hasFocus) {
				txtInviteNo.setText("");
				bgInviteNo.setBackgroundResource(R.drawable.bg_login_input2);
			}
			else if (TextUtils.isEmpty(txtInviteNo.getVal())) {
				bgInviteNo.setBackgroundResource(R.drawable.bg_login_input);
			}
			break;
		default:
			break;
		}
	}
	
	private void checkRegisterStatus() {
		btnClearLoginName.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearPassword.setVisibility(TextUtils.isEmpty(txtPassword.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnClearName.setVisibility(TextUtils.isEmpty(txtName.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
		btnInviteNo.setVisibility(TextUtils.isEmpty(txtInviteNo.getVal()) ? View.INVISIBLE : View.VISIBLE);
		tip1Register.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		tip2Register.setVisibility(TextUtils.isEmpty(txtLoginName.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		tip3Register.setVisibility(TextUtils.isEmpty(txtName.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
		tip4Register.setVisibility(TextUtils.isEmpty(txtPassword.getText().toString()) ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		checkRegisterStatus();
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		txtPassword.setTransformationMethod(isChecked ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
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
		Intent i = new Intent(context, AtyRegister.class);
		context.startActivityForResult(i, REQUEST_RGISTER);
	}
	
}
