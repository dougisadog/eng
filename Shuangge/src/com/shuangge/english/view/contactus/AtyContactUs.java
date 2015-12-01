package com.shuangge.english.view.contactus;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.network.other.TaskReqContactUs;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.about.AtyAboutList;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.tencent.tauth.Tencent;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyContactUs extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_CONTACT_US = 1044;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private ImageView btnSave;
	private EditTextWidthTips txtFeedback, txtContactInfo;
	private FrameLayout btnJoinQQ;
	private TextView qqNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_contact_us);
		txtFeedback = (EditTextWidthTips) findViewById(R.id.txtFeedback);
		txtContactInfo = (EditTextWidthTips) findViewById(R.id.txtContactInfo);
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		btnJoinQQ = (FrameLayout) findViewById(R.id.flJoin);
		btnJoinQQ.setOnClickListener(this);
		
		qqNumber = (TextView) findViewById(R.id.qqNumber);
		qqNumber.setText("QQ群 :" + ConfigConstants.QQGROUP_NUMBER);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnSave:
//			if (TextUtils.isEmpty(txtContactInfo.getVal())) {
//				Toast.makeText(AtyContactUs.this, R.string.contactUsErrTip2, Toast.LENGTH_SHORT).show();
//				return;
//			}
			if (TextUtils.isEmpty(txtFeedback.getVal())) {
				Toast.makeText(AtyContactUs.this, R.string.contactUsErrTip1, Toast.LENGTH_SHORT).show();
				return;
			}
			if (requesting) {
				return;
			}
			requesting = true;
			new TaskReqContactUs(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyContactUs.this, R.string.contactUsSuccessTip, Toast.LENGTH_SHORT).show();
					AtyContactUs.this.finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, txtContactInfo.getVal(), txtFeedback.getVal());
			break;
		case R.id.flJoin:
//			String qqGroup = getString(R.string.qqGroupKey);
			joinQQGroup(ConfigConstants.QQGROUP_KEY);
			break;
		}
	}
	
	public boolean joinQQGroup(String key) {
	    Intent intent = new Intent();
	    intent.setData(Uri.parse(ConfigConstants.QQGROUP_URL + key));
	   // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	    try {
	        startActivity(intent);
	        return true;
	    } catch (Exception e) {
	        // 未安装手Q或安装的版本不支持
	    	Toast.makeText(AtyContactUs.this, R.string.systemMsgTip4, Toast.LENGTH_SHORT).show();
	        return false;
	    }
	}
	
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyContactUs.class);
		context.startActivityForResult(i, REQUEST_CONTACT_US);
	}
	
}
