package com.shuangge.english.view.group;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.network.group.TaskReqClassCreate;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmSaveFragment.CallBackDialogConfirmSave;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment;
import com.shuangge.english.view.component.wheel.DialogCitiesFragment.CallBackDialogCitiesWheel;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassCreate extends AbstractAppActivity implements OnClickListener, OnFocusChangeListener, TextWatcher {

	public static final int REQUEST_CLASS_CREATE = 1016;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private ImageView btnSave;
	
	private RelativeLayout rl1, rl2, rl3, rl4,rl5;
	private EditText et1, et2, et4, et5;
	private TextView txt3;
	private RadioGroup raidoGroup;
	
	private DialogConfirmSaveFragment dialogFragment;
	private DialogCitiesFragment dialogWheel;
	private boolean dataHasChanged = false;
	
	private Integer joinRule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_class_create);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
	
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
		
		txt3 = (TextView) findViewById(R.id.classEditTip3);
		et1 = (EditText) findViewById(R.id.classEditTip1);
		et1.setOnFocusChangeListener(this);
		et2 = (EditText) findViewById(R.id.classEditTip2);
		et2.setOnFocusChangeListener(this);
		et4 = (EditText) findViewById(R.id.classEditTip4);
		et4.setOnFocusChangeListener(this);
		et4.addTextChangedListener(this);
		et5 = (EditText) findViewById(R.id.classEditTip5);
		et5.setOnFocusChangeListener(this);
		
		raidoGroup = (RadioGroup) findViewById(R.id.radioGroup);
		RadioButton rb = (RadioButton) findViewById(raidoGroup.getCheckedRadioButtonId());
		joinRule = Integer.valueOf(rb.getTag().toString());
		
		raidoGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) findViewById(radioButtonId);
				joinRule = Integer.valueOf(rb.getTag().toString());
			}
		});
		
		String weChatNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getWechatNo();
		if (!TextUtils.isEmpty(weChatNo)) {
			et5.setText(weChatNo);
		}
	}
	
	private void requestData() {
		if (TextUtils.isEmpty(et1.getText().toString())) {
			Toast.makeText(this, getResources().getString(R.string.classCreateErrTip1), Toast.LENGTH_SHORT).show();
			return;
		}
		if (requesting) {
			return;
		}
		
		showLoading();
		new TaskReqClassCreate(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				requesting = false;
				if (null == result || !result) {
					return;
				}
				dataHasChanged = false;
				Toast.makeText(AtyClassCreate.this,	R.string.classCreateSuccessTip, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(AtyClassCreate.this, AtyClassHome.class));
				AtyClassCreate.this.setResult(AtyClassCreate.CODE_QUIT);
				AtyClassCreate.this.finish();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, et1.getText().toString(), et2.getText().toString(), txt3.getText().toString(), et4.getText().toString(), et5.getText().toString(), joinRule);
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				gotoBack();
				break;
			case R.id.btnSave:
				requestData();
				break;
			case R.id.rl3:
				dataHasChanged = true;
				dialogWheel = new DialogCitiesFragment(callback, getString(R.string.userInfoEditDialogTip9), txt3.getText().toString());
				dialogWheel.showDialog(getSupportFragmentManager());
				break;
			case R.id.rl1:
				dataHasChanged = true;
				et1.requestFocus();
				break;
			case R.id.rl2:
				dataHasChanged = true;
				et2.requestFocus();
				break;
			case R.id.rl4:
				dataHasChanged = true;
				et4.requestFocus();
				break;
			case R.id.rl5:
				dataHasChanged = true;
				et5.requestFocus();
				break;
		}
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		et4.setGravity(et4.getLineCount() > 1 ? Gravity.LEFT : Gravity.CENTER);
	}
	
	private CallBackDialogCitiesWheel callback = new CallBackDialogCitiesWheel() {
		
		@Override
		public void submit(String data) {
			dataHasChanged = true;
			txt3.setText(data);
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
    public boolean onBack() {
    	return gotoBack();
    }
	
	private boolean gotoBack() {
		if (!dataHasChanged || null != dialogFragment) {
			finish();
			return false;
		}
		dialogFragment = new DialogConfirmSaveFragment(new CallBackDialogConfirmSave() {
			
			@Override
			public void save() {
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
		return true;
	}
	
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassCreate.class);
		context.startActivityForResult(i, REQUEST_CLASS_CREATE);
	}

	
}
