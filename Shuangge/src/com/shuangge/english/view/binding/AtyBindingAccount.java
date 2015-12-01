package com.shuangge.english.view.binding;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.utils.CheckUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.login.AtyRegisterPhone;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyBindingAccount extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_BINDING_ACCOUNT = 1018;
	
	public static final int CODE_BINDING_ACCOUNT = 20;

	private ImageButton btnBack;
	private ImageView btnSave;
	private EditTextWidthTips et1, et2, et3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_binding_account);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		et1 = (EditTextWidthTips) findViewById(R.id.classInfoEditTip1);
		et2 = (EditTextWidthTips) findViewById(R.id.classInfoEditTip2);
		et3 = (EditTextWidthTips) findViewById(R.id.classInfoEditTip3);
	}

	private boolean validateRegister() {
		if (TextUtils.isEmpty(et1.getVal()) || et1.getVal().length() < 4) {
			Toast.makeText(this, getResources().getString(R.string.loginErrTip1), Toast.LENGTH_SHORT).show();
			return false;
		}
//		if (TextUtils.isEmpty(et1.getVal()) || et2.getVal().length() < 3) {
//			Toast.makeText(this, getResources().getString(R.string.loginErrTip3), Toast.LENGTH_SHORT).show();
//			return false;
//		}
		if (TextUtils.isEmpty(et1.getVal()) || et3.getVal().length() < 6) {
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
		case R.id.btnSave:
			if (!validateRegister()) {
				return;
			}
			if (CheckUtils.isMobile(et1.getVal().toString())) {
				GlobalReqDatas.getInstance().setRequestLoginName(et1.getVal());
				GlobalReqDatas.getInstance().setRequestPassword(et3.getVal());
				AtyRegisterPhone.startAty(AtyRegisterPhone.TYPE_BINDING_ACCOUNT, AtyBindingAccount.this, 0);
				return;
			}
			Toast.makeText(AtyBindingAccount.this, R.string.bindingPhoneErrTip1, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CODE_QUIT) {
			Toast.makeText(AtyBindingAccount.this, R.string.bindingAccountSuccessTip, Toast.LENGTH_SHORT).show();
			setResult(AtyBindingAccount.CODE_BINDING_ACCOUNT);
			finish();
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyBindingAccount.class);
		context.startActivityForResult(i, REQUEST_BINDING_ACCOUNT);
	}

}
