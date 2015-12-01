package com.shuangge.english.view.about;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.MainActivity;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.network.login.TaskCheckVersion;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.task.TaskCheckCatalog;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.MenuActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.contactus.AtyContactUs;
import com.shuangge.english.view.download.AtyDownload2;
import com.shuangge.english.view.group.AtyBrowseClassInfo;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyAboutList extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_ABOUT_LIST = 1070;
	
	private ImageButton btnBack;
	private RelativeLayout rlBtnFAQ, rlEvaluation, rlAboutUs,rlCheckVersion ,rlDown, rlContactUs, rlBtnApp;
	private TextView txtVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_about_list);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		rlBtnFAQ = (RelativeLayout) findViewById(R.id.rlBtnFAQ);
		rlBtnFAQ.setOnClickListener(this);
		rlEvaluation = (RelativeLayout) findViewById(R.id.rlEvaluation);
		rlEvaluation.setOnClickListener(this);
		rlAboutUs = (RelativeLayout) findViewById(R.id.rlAboutUs);
		rlAboutUs.setOnClickListener(this);
		rlCheckVersion = (RelativeLayout) findViewById(R.id.rlCheckVersion);
		rlCheckVersion.setOnClickListener(this);
		txtVersion = (TextView) findViewById(R.id.txtVersion);
		txtVersion.setText("爽哥英语 "+AppInfo.APP_VERSION_NAME);
		rlDown = (RelativeLayout) findViewById(R.id.rlDown);
		rlDown.setOnClickListener(this);
		rlContactUs = (RelativeLayout) findViewById(R.id.rlContactUs);
		rlContactUs.setOnClickListener(this);
		rlBtnApp = (RelativeLayout) findViewById(R.id.rlBtnApp);
		rlBtnApp.setOnClickListener(this);
//		GlobalRes.getInstance().getBeans().getLoginData().setInfoData(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.rlBtnFAQ:
			AtyAboutFaq.startAty(AtyAboutList.this);
			break;
		case R.id.rlEvaluation:
			Uri uri = Uri.parse("market://details?id="+getPackageName());		
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);		
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			isIntentAvailable(intent);
//			startActivity(intent);
			break;
		case R.id.rlAboutUs:
			AtyAbout.startAty(AtyAboutList.this);
			break;
		case R.id.rlCheckVersion:
			checkVersion();
			break;
		case R.id.rlDown:
			AtyDownload2.startAty(AtyAboutList.this);
			break;
		case R.id.rlContactUs:
			AtyContactUs.startAty(AtyAboutList.this);
			break;
		case R.id.rlBtnApp:
			AtyAppList.startAty(AtyAboutList.this);
			break;
		}
	}
	public void isIntentAvailable (Intent intent ) {
	    final PackageManager packageManager = this.getPackageManager () ;
	    List <ResolveInfo > list = packageManager.queryIntentActivities (intent,
	            PackageManager. GET_ACTIVITIES ) ;
	    if (list. size () > 0) {
	    	startActivity(intent);
	    }else {
	    	Toast.makeText(AtyAboutList.this, R.string.systemMsgTip3, Toast.LENGTH_SHORT).show();
	    }
	}
	private void checkVersion() {
		showLoading();
		new TaskCheckVersion(0, new CallbackNoticeView<Void, Integer>() {

			@Override
			public void refreshView(int tag, Integer state) {
				hideLoading();
				switch (state) {
				case TaskCheckVersion.NO_UPDATE:
					new AlertDialog.Builder(AtyAboutList.this).setTitle(R.string.versiontipTitle)
		    		.setCancelable(false).setMessage(getString(R.string.aboutContent8)).setPositiveButton(R.string.versionOK, new android.content.DialogInterface.OnClickListener() {
		    			
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
						
					}).show();
					break;
				case TaskCheckVersion.UPDATE:
					System.out.println(ConfigConstants.VERSION_TIP);
					new AlertDialog.Builder(AtyAboutList.this).setTitle(R.string.versiontipTitle)
		    		.setCancelable(false).setMessage(ConfigConstants.VERSION_TIP).setPositiveButton(R.string.versionOK, new android.content.DialogInterface.OnClickListener() {
		    			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateVersion();
						}
						
					}).setNegativeButton(R.string.versionCancel, new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
//							checkResPackages();
						}
						
					}).show();
					break;
				case TaskCheckVersion.FORCED_UPDATE:
					new AlertDialog.Builder(AtyAboutList.this).setTitle(R.string.versiontipTitle)
		    		.setCancelable(false).setMessage(ConfigConstants.FORCED_UPDATE_TIP).setPositiveButton(R.string.versionOK, new android.content.DialogInterface.OnClickListener() {
		    			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateVersion();
						}
						
					}).show();
					break;
				case TaskCheckVersion.NO_NETWORK:
					new AlertDialog.Builder(AtyAboutList.this).setTitle(R.string.versiontipTitle)
					.setCancelable(false).setMessage(R.string.versiontip3).setPositiveButton(R.string.versionOK, new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
						
					}).show();
					break;
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	public void updateVersion() {
		Intent intent = null;
//		try {
//			intent = new Intent(Intent.ACTION_VIEW);
//			intent.setData(Uri.parse("market://details?id=" + getPackageName()));
//			startActivity(intent); 
//		}
//		catch (Exception e) {
//			try {
//				Uri uri = Uri.parse(ConfigConstants.APP_URL);  
//				intent = new Intent(Intent.ACTION_VIEW, uri);  
//				startActivity(intent);
//			}
//			catch (Exception e1) {
//			}
//		}
		Uri uri = Uri.parse(ConfigConstants.APP_URL);  
		intent = new Intent(Intent.ACTION_VIEW, uri);  
		startActivity(intent);
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyAboutList.class);
		context.startActivityForResult(i, REQUEST_ABOUT_LIST);
	}

}
