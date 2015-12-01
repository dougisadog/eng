package com.shuangge.english.view.shop;

import java.util.Date;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.network.shop.TaskReqAddressNew;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment.CallBackDialogConfirmSave;
import com.shuangge.english.view.component.drag.DragGridView.OnDragListener;
import com.shuangge.english.view.component.photograph.MyScrollView;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment.CallBackDialogCitiesWheel;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyShippingAddressEdit extends AbstractAppActivity implements OnClickListener, OnFocusChangeListener, OnDragListener {
	
	public static final int REQUEST_SHIPPING_ADDRESS_EDIT = 1078;
	
	public static final int MODULE_1 = 1;
	public static final int MODULE_2 = 2;
	public static final int MODULE_4 = 4;
	public static final int MODULE_5 = 5;
	public static final int MODULE_6 = 6;
	public static final int MODULE_7 = 7;
	public static final int MODULE_8 = 8;
	public static final int MODULE_9 = 9;

	private boolean requesting = false;
	
	private ImageButton btnBack;
	private FrameLayout btnSave;
	
	private EditText etPhone, etAddress, etPostCode, etName;
	private RelativeLayout rl1, rl2, rl3, rl4,rl5;
	private TextView txtCity;
	private MyScrollView sv;
	
	private Integer sex = 0;
	private Date birthday;
	private Integer age;
	
	private DialogConfirmSaveFragment dialogFragment;
	private DialogCitiesFragment dialogWheel2;
	private boolean dataHasChanged = false;
	private Integer addressId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_shipping_address);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnSave = (FrameLayout) findViewById(R.id.flSave);
		btnSave.setOnClickListener(this);
		
		sv = (MyScrollView) findViewById(R.id.sv);
		
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl1.setOnClickListener(this);
		rl2 = (RelativeLayout) findViewById(R.id.rl2);
		rl2.setOnClickListener(this);
		rl3 = (RelativeLayout) findViewById(R.id.rl3);
		rl3.setOnClickListener(this);
		rl4 = (RelativeLayout) findViewById(R.id.rl4);
		rl4.setOnClickListener(this);
		rl5 = (RelativeLayout) findViewById(R.id.rl5);
		rl5.setOnClickListener(this); 
		txtCity = (TextView) findViewById(R.id.txtCity);
		
		etPhone = (EditText) findViewById(R.id.etPhone);
		etPhone.setOnFocusChangeListener(this);
		etAddress = (EditText) findViewById(R.id.etAddress);
		etAddress.setOnFocusChangeListener(this);
		etPostCode = (EditText) findViewById(R.id.etPostCode);
		etPostCode.setOnFocusChangeListener(this);
		etName = (EditText) findViewById(R.id.etName);
		etName.setOnFocusChangeListener(this);
		if (GlobalRes.getInstance().getBeans().getCurrentAddress() != null) {
			AddressData data = GlobalRes.getInstance().getBeans().getCurrentAddress();
			txtCity.setText(data.getLocation());
			etAddress.setText(data.getDetailed());
			etPostCode.setText(data.getZipCode());
			etName.setText(data.getRecipient());
			etPhone.setText(data.getPhone());
			addressId = data.getAddressId();
		}
	}
	
	private void requestData() {
		if (txtCity.getText().toString().length() == 0 || etAddress.getText().toString().length() == 0 || etPostCode.getText().toString().length() == 0
				|| etName.getText().toString().length() == 0 || etPhone.getText().toString().length() == 0) {
			Toast.makeText(AtyShippingAddressEdit.this, R.string.saveFailTip, Toast.LENGTH_SHORT).show();
			return;
		}
		showLoading();
		new TaskReqAddressNew(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				Toast.makeText(AtyShippingAddressEdit.this, R.string.saveSuccessTipCn, Toast.LENGTH_SHORT).show();
//				AtyShopAddressList.startAty(AtyShippingAddressEdit.this);
				finish();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, addressId, txtCity.getText().toString(), etAddress.getText().toString(), etPostCode.getText().toString(), etName.getText().toString(), etPhone.getText().toString());
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				GlobalRes.getInstance().getBeans().setCurrentAddress(null);
				gotoBack();
				return;
			case R.id.flSave:
				requestData();
				return;
			
		}
		switch(v.getId()) {
			case R.id.rl1:
				if (null == dialogWheel2 || !dialogWheel2.isVisible()) {
					dialogWheel2 = new DialogCitiesFragment(callback2, getString(R.string.userInfoEditDialogTip9), txtCity.getText().toString());
					dialogWheel2.showDialog(getSupportFragmentManager());
				}
				break;
			case R.id.rl2:
				dataHasChanged = true;
				etPhone.requestFocus();
				break;
			case R.id.rl3:
				dataHasChanged = true;
				etAddress.requestFocus();
				break;
			case R.id.rl4:
				dataHasChanged = true;
				etPostCode.requestFocus();
				break;
			case R.id.rl5:
				dataHasChanged = true;
				etName.requestFocus();
				break;
			
		}
	}
	
	
	private CallBackDialogCitiesWheel callback2 = new CallBackDialogCitiesWheel() {
		
		@Override
		public void submit(String data) {
			txtCity.setText(data);
			cancel();
		}
		
		@Override
		public void cancel() {
			if (null != dialogWheel2) {
				dialogWheel2.dismissAllowingStateLoss();
			}
			dialogWheel2 = null;
		}
		
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		dataHasChanged = true;
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (v instanceof EditText) {
				((EditText) v).setSelection(((EditText) v).getText().length());
			}
			return;
		}
	}

	@Override
	public void onDrag() {
		sv.setCanScroll(false);
	}


	@Override
	public void onStopDrag() {
		sv.setCanScroll(true);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	gotoBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	private void gotoBack() {
		if (!dataHasChanged || null != dialogFragment) {
			finish();
			return;
		}
		dialogFragment = new DialogConfirmSaveFragment(new CallBackDialogConfirmSave() {
			
			@Override
			public void save() {
				dataHasChanged = false;  
				dialogFragment.dismissAllowingStateLoss();
				dialogFragment = null;
				requestData();
			}
			
			@Override
			public void noSave() {
				dataHasChanged = false;  
				dialogFragment.dismissAllowingStateLoss();
				dialogFragment = null;
				finish();
			}
			
			@Override
			public void cancel() {
				dialogFragment.dismissAllowingStateLoss();
				dialogFragment = null;
			}
			
		});
		dialogFragment.showDialog(getSupportFragmentManager());
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyShippingAddressEdit.class);
		context.startActivityForResult(i, REQUEST_SHIPPING_ADDRESS_EDIT);
	}
	
	public static void startAty(Context context) {
		Intent i = new Intent(context, AtyShippingAddressEdit.class);
		context.startActivity(i);
	}
}
