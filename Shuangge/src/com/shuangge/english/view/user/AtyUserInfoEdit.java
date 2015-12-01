package com.shuangge.english.view.user;

import java.util.Date;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.user.UpdateInfoData;
import com.shuangge.english.network.user.TaskReqUpdateDataInfo;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.binding.AtyBindingAccount;
import com.shuangge.english.view.component.AtyInterest;
import com.shuangge.english.view.component.ComponentMultiSwitch;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment.CallBackDialogConfirmSave;
import com.shuangge.english.view.component.dialog.DialogDatePickerFragment;
import com.shuangge.english.view.component.dialog.DialogDatePickerFragment.CallBackDialogDatePicker;
import com.shuangge.english.view.component.drag.DragGridView.OnDragListener;
import com.shuangge.english.view.component.photograph.MyScrollView;
import com.shuangge.english.view.component.photograph.PhotosEditContainer;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment.CallBackDialogCitiesWheel;
import com.shuangge.english.view.component.wheel.DialogWheelFragment;
import com.shuangge.english.view.component.wheel.DialogWheelFragment.CallBackDialogWheel;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyUserInfoEdit extends AbstractAppActivity implements OnClickListener, OnFocusChangeListener, OnDragListener {
	
	public static final int REQUEST_USER_INFO_EDIT = 1025;
	
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
	private ImageView btnSave;
	private PhotosEditContainer photosEditContainer;
	
	private EditTextWidthTips et1, et5;
	private TextView txt2, txt4, txt6, txt7, txt8, txt9, txt10;
	private ComponentMultiSwitch txt3;
	private MyScrollView sv;
	
	private Integer sex = 0;
	private Date birthday;
	private Integer age;
	
	private DialogConfirmSaveFragment dialogFragment;
	private DialogWheelFragment dialogWheel;
	private DialogCitiesFragment dialogWheel2;
	private boolean dataHasChanged = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		photosEditContainer.recycle();
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_userinfo_edit);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		sv = (MyScrollView) findViewById(R.id.sv);
		photosEditContainer = (PhotosEditContainer) findViewById(R.id.photosEditContainer);
		photosEditContainer.setDragListener(this);
		
		et1 = (EditTextWidthTips) findViewById(R.id.userInfoEditTip1);
		et1.setOnFocusChangeListener(this);
		et5 = (EditTextWidthTips) findViewById(R.id.userInfoEditTip5);
		et5.setOnFocusChangeListener(this);
		txt2 = (TextView) findViewById(R.id.userInfoEditTip2);
		txt2.setOnClickListener(this);
		txt3 = (ComponentMultiSwitch) findViewById(R.id.userInfoEditTip3);
		txt4 = (TextView) findViewById(R.id.userInfoEditTip4);
		txt4.setOnClickListener(this);
		txt6 = (TextView) findViewById(R.id.userInfoEditTip6);
		txt6.setOnClickListener(this);
		txt7 = (TextView) findViewById(R.id.userInfoEditTip7);
		txt7.setOnClickListener(this);
		txt8 = (TextView) findViewById(R.id.userInfoEditTip8);
		txt8.setOnClickListener(this);
		txt9 = (TextView) findViewById(R.id.userInfoEditTip9);
		txt9.setOnClickListener(this);
		txt10 = (TextView) findViewById(R.id.userInfoEditTip10);
		txt10.setOnClickListener(this);
		InfoData data = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
		et1.setText(data.getName());
		txt2.setText(data.getLoginName());
		if (null == data.getSex()) {
			data.setSex(0);
		}
		sex = data.getSex();
		if (null == sex || sex > 2) {
			sex = 0;
		}
		birthday = data.getBirthday();
		txt3.setVal(sex);
		txt4.setText(null != data.getAge() ? data.getAge().toString() : getString(R.string.secrecy));
		et5.setText(!TextUtils.isEmpty(data.getSignature()) ? data.getSignature() : getString(R.string.isNull));
		txt6.setText(!TextUtils.isEmpty(data.getEmotion()) ? data.getEmotion() : getString(R.string.secrecy));
		txt7.setText(!TextUtils.isEmpty(data.getIncome()) ? data.getIncome() : getString(R.string.secrecy));
		txt8.setText(!TextUtils.isEmpty(data.getOccupation()) ? data.getOccupation() : getString(R.string.secrecy));
		txt9.setText(!TextUtils.isEmpty(data.getLocation()) ? data.getLocation() : getString(R.string.secrecy));
		txt10.setText(!TextUtils.isEmpty(data.getInterest()) ? data.getInterest() : getString(R.string.secrecy));
		
		photosEditContainer.setUrls(GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getPhotoUrls(),
				GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getPhotoNos(),
				GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getPhotoSortNos());
	}
	
	private void requestData(final boolean close) {
		if (requesting) {
			return;
		}
		UpdateInfoData data = GlobalReqDatas.getInstance().getUpdateInfoData();
		data.setName(et1.getText().toString());
		sex = txt3.getVal();
		data.setSex(sex);
		data.setAge(age);
		data.setBirthday(birthday);
		data.setSignature(et5.getText().toString());
		data.setEmotion(txt6.getText().toString());
		data.setIncome(txt7.getText().toString());
		data.setOccupation(txt8.getText().toString());
		data.setLocation(txt9.getText().toString());
		data.setInterest(txt10.getText().toString());
		
		data.clear();
		data.setPhotoDatas(photosEditContainer.getDatas());
		
		showLoading();
		new TaskReqUpdateDataInfo(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				requesting = false;
				if (null == result || !result) {
					return;
				}
				dataHasChanged = false;
				Toast.makeText(AtyUserInfoEdit.this, R.string.saveSuccessTipCn, Toast.LENGTH_SHORT).show();
				if (close) {
					finish();
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				gotoBack();
				return;
			case R.id.btnSave:
				requestData(true);
				return;
			
		}
		switch(v.getId()) {
			case R.id.userInfoEditTip1:
//				if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().isVisitor()) {
//					Toast.makeText(AtyUserInfoEdit.this, R.string.userInfoEditVistorErr, Toast.LENGTH_SHORT).show();
//					startActivityForResult(new Intent(AtyUserInfoEdit.this, AtyBindingAccount.class), MODULE_1);
//					return;
//				}
				dataHasChanged = true;
				break;
			case R.id.userInfoEditTip2:
				dataHasChanged = true;
				break;
			case R.id.userInfoEditTip3:
//				dialogWheel = new DialogWheelFragment(MODULE_3, callback, getString(R.string.userInfoEditDialogTip3), getResources().getStringArray(R.array.sex), txt3.getText().toString());
//				dialogWheel.showDialog(getSupportFragmentManager());
				dataHasChanged = true;
				break;
			case R.id.userInfoEditTip4:
			    DialogDatePickerFragment datePicker = new DialogDatePickerFragment(0, birthday, callback3);  
			    datePicker.show(getSupportFragmentManager(), "datePicker");  
				break;
			case R.id.userInfoEditTip5:
				dataHasChanged = true;
				break;
			case R.id.userInfoEditTip6:
				dialogWheel = new DialogWheelFragment(MODULE_6, callback, getString(R.string.userInfoEditDialogTip6), getResources().getStringArray(R.array.emotion), txt6.getText().toString());
				dialogWheel.showDialog(getSupportFragmentManager());
				break;
			case R.id.userInfoEditTip7:
				dialogWheel = new DialogWheelFragment(MODULE_7, callback, getString(R.string.userInfoEditDialogTip7), getResources().getStringArray(R.array.income), txt7.getText().toString());
				dialogWheel.showDialog(getSupportFragmentManager());
				break;
			case R.id.userInfoEditTip8:
				dialogWheel = new DialogWheelFragment(MODULE_8, callback, getString(R.string.userInfoEditDialogTip8), getResources().getStringArray(R.array.occupation), txt8.getText().toString());
				dialogWheel.showDialog(getSupportFragmentManager());
				break;
			case R.id.userInfoEditTip9:
				dialogWheel2 = new DialogCitiesFragment(callback2, getString(R.string.userInfoEditDialogTip9), txt9.getText().toString());
				dialogWheel2.showDialog(getSupportFragmentManager());
				break;
			case R.id.userInfoEditTip10:
				AtyInterest.startAty(this, txt10.getText().toString());
				break;
		}
	}
	
	private CallBackDialogWheel callback = new CallBackDialogWheel() {
		
		@Override
		public void submit(int tag, String data) {
			dataHasChanged = true;
			switch (tag) {
			case MODULE_6:
				txt6.setText(data);
				break;
			case MODULE_7:
				txt7.setText(data);
				break;
			case MODULE_8:
				txt8.setText(data);
				break;
			}
			cancel();
		}
		
		@Override
		public void cancel() {
			if (null != dialogWheel) {
				dialogWheel.dismissAllowingStateLoss();
			}
			dialogWheel = null;
		}
		
	};
	
	private CallBackDialogCitiesWheel callback2 = new CallBackDialogCitiesWheel() {
		
		@Override
		public void submit(String data) {
			txt9.setText(data);
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
	
	private CallBackDialogDatePicker callback3 = new CallBackDialogDatePicker() {

		@Override
		public void submit(int tag, Integer data, Date date) {
			if (null == data || null == date) {
				return;
			}
			txt4.setText(data + "");
			birthday = date;
			age = data; 
		}
		
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MODULE_1 && resultCode == AtyBindingAccount.CODE_BINDING_ACCOUNT) {
			InfoData data1 = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
			et1.setText(data1.getName());
			txt2.setText(data1.getLoginName());
			et1.setTouchable(true);
		}
		if (null == data) {
			return;
		}
		switch (requestCode) {
		case AtyInterest.REQUEST_INTEREST:
			dataHasChanged = true;
			txt10.setText(data.getStringExtra(AtyInterest.CALLBACK_DATAS));
			break;
		case PhotosEditContainer.MODULE_PHOTO:
			photosEditContainer.onActivityResult(resultCode, data);
		}
		dataHasChanged = true;
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (v instanceof EditText) {
				((EditText) v).setSelection(((EditText) v).getText().length());
			}
			switch (v.getId()) {
			case R.id.userInfoEditTip5:
				if (et5.getText().toString().equals(getString(R.string.isNull))) {
					et5.setText("");
				}
				break;
			}
			return;
		}
		switch (v.getId()) {
		case R.id.userInfoEditTip5:
			if (TextUtils.isEmpty(et5.getText())) {
				et5.setText(getString(R.string.isNull));
			}
			break;
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
				requestData(true);
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
		Intent i = new Intent(context, AtyUserInfoEdit.class);
		context.startActivityForResult(i, REQUEST_USER_INFO_EDIT);
	}
	
}
