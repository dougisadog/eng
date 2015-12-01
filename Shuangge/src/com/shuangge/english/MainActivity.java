package com.shuangge.english;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

//import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
//import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.network.login.TaskCheckVersion;
import com.shuangge.english.network.login.TaskReqLogin;
import com.shuangge.english.network.login.TaskReqOAuth;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.app.AnalyticsManager;
import com.shuangge.english.support.app.PushManager;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.setting.SettingUtility;
import com.shuangge.english.task.TaskCheckCatalog;
import com.shuangge.english.task.TaskInitLocalUserData;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.home.AtyHome;
import com.shuangge.english.view.login.AtyGuide;
import com.shuangge.english.view.login.AtyLogin;

public class MainActivity extends AbstractAppActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initConfig();
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		PushManager.getInstance().init();
		PushManager.getInstance().register();
		AnalyticsManager.getInstance().init(this);
//		ShareManager.getInstance().init(this);
		
		pauseMusic();
		
//		parseChannel();
		checkVersion();
	}
	
	private void initConfig() {
		ConfigConstants.SERVER_URL = getResources().getString(R.string.serverUrl);
		ConfigConstants.RES_URL = getResources().getString(R.string.resUrl);
		ConfigConstants.RES_URL2 = getResources().getString(R.string.resUrl2);
		ConfigConstants.IMG_URL = getResources().getString(R.string.imgUrl);
		ConfigConstants.SND_URL = getResources().getString(R.string.sndUrl);
		ConfigConstants.CONFIG_URL = getResources().getString(R.string.configUrl);
		ConfigConstants.FORCED_UPDATE_NUM = getResources().getString(R.string.forced_update_version);
		ConfigConstants.RES_FAQ_URL = getString(R.string.faqUrl);
		ConfigConstants.RES_NOTICE_URL = getString(R.string.noticeUrl);
		ConfigConstants.RES_REWARDS_HELP_URL = getString(R.string.rewardsHelpUrl);
		ConfigConstants.RES_REWARDS_ClASS_MANAGER_TIP_URL = getString(R.string.rewardsClassManagerTip);
		ConfigConstants.RES_REWARDS_CLASS_MEMBER_TIP_URL = getString(R.string.rewardsClassMemberTip);
		ConfigConstants.RES_WITHDRAWALS_TIP_URL = getString(R.string.withdrawalsTip);
		ConfigConstants.RES_READ_URL = getString(R.string.resReadUrl);
		ConfigConstants.SECRET_MSG_MONTH = Integer.valueOf(getString(R.integer.secretMsg_month));
		ConfigConstants.RECOMMEND_APP_URL = getResources().getString(R.string.recommendAppUrl);
	}
	
//	private void parseChannel() {
//		try {
//			ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//			String channel = appInfo.metaData.getString("UMENG_CHANNEL", "");
//			if ("BAIDU".equals(channel.toUpperCase())) {
//				baiduUpdate();
//				return;
//			}
//		} catch (Exception e) {}
//		checkVersion();
//	}
	
//	private void baiduUpdate() {
//		BDAutoUpdateSDK.uiUpdateAction(this, new MyUICheckUpdateCallback());
//	}
//	
//	private class MyUICheckUpdateCallback implements UICheckUpdateCallback {
//
//		@Override
//		public void onCheckComplete() {
//			new TaskCheckVersion(0, new CallbackNoticeView<Void, Integer>() {
//
//				@Override
//				public void refreshView(int tag, Integer state) {
//					checkResPackages();
//				}
//
//				@Override
//				public void onProgressUpdate(int tag, Void[] values) {}
//			});
//		}
//
//	}
	
	
	private void checkVersion() {
		new TaskCheckVersion(0, new CallbackNoticeView<Void, Integer>() {

			@Override
			public void refreshView(int tag, Integer state) {
				switch (state) {
				case TaskCheckVersion.NO_UPDATE:
					checkResPackages();
					break;
				case TaskCheckVersion.UPDATE:
					new AlertDialog.Builder(MainActivity.this).setTitle(R.string.versiontipTitle)
		    		.setCancelable(false).setMessage("1.全新上线了旅游英语课程\n2.完善了好友间发私信的功能\n3.完善了发音模块功能\n4.增加了班级成员之间互相邀请的功能\n5.增加了个人账户模块，方便自动领取奖学金\n6..完善了发音模块功能\n7.优化了写作模块功能\n8.修复了部分机型卡顿的bug\n").setPositiveButton(R.string.versionOK, new OnClickListener() {
		    			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateVersion();
						}
						
					}).setNegativeButton(R.string.versionCancel, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							checkResPackages();
						}
						
					}).show();
					break;
				case TaskCheckVersion.FORCED_UPDATE:
					new AlertDialog.Builder(MainActivity.this).setTitle(R.string.versiontipTitle)
		    		.setCancelable(false).setMessage(ConfigConstants.FORCED_UPDATE_TIP).setPositiveButton(R.string.versionOK, new OnClickListener() {
		    			
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateVersion();
						}
						
					}).show();
					break;
				case TaskCheckVersion.NO_NETWORK:
					new AlertDialog.Builder(MainActivity.this).setTitle(R.string.versiontipTitle)
					.setCancelable(false).setMessage(R.string.versiontip3).setPositiveButton(R.string.versionOK, new OnClickListener() {
						
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
		try {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + getPackageName()));
			startActivity(intent); 
		}
		catch (Exception e) {
			try {
				Uri uri = Uri.parse(ConfigConstants.APP_URL);  
				intent = new Intent(Intent.ACTION_VIEW, uri);  
				startActivity(intent);
			}
			catch (Exception e1) {
			}
		}
	}
	
	private void checkResPackages() {
		new TaskCheckCatalog(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (null != result && result) {
					initLocalUserData();
					return;
				}
				new AlertDialog.Builder(MainActivity.this).setTitle(R.string.versionResTitle)
				.setCancelable(false).setMessage(R.string.versionResErr).setPositiveButton(R.string.versionResOk, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.this.finish();
					}
					
				}).show();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, getAssets());
	}
	
	private void initLocalUserData() {
		new TaskInitLocalUserData(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (result) {
					//普通用户登陆
					if (GlobalReqDatas.getInstance().getRequestUIDType() == EntityUser.NORMAL_USER) {
						new TaskReqLogin(0, new CallbackNoticeView<Void, Boolean>() {
	
							@Override
							public void refreshView(int tag, Boolean result) {
								if (null == result || !result) {
									goToNextView();
									return;
								}
								startActivity(new Intent(MainActivity.this, AtyHome.class));
								MainActivity.this.finish();
							}
	
							@Override
							public void onProgressUpdate(int tag, Void[] values) {
							}
							
						});
						return;
					}
					if (TextUtils.isEmpty(GlobalReqDatas.getInstance().getRequestUID())) {
						return;
					}
					new TaskReqOAuth(0, new CallbackNoticeView<Void, Boolean>() {

						@Override
						public void refreshView(int tag, Boolean result) {
							if (null == result || !result) {
								goToNextView();
								return;
							}
							startActivity(new Intent(MainActivity.this, AtyHome.class));
							MainActivity.this.finish();
						}

						@Override
						public void onProgressUpdate(int tag, Void[] values) {
							
						}
						
					});
					return;
				}
				goToNextView();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}

	private void goToNextView() {
		Intent intent = null;
		if (SettingUtility.firstStart()) {
			intent = new Intent(this, AtyGuide.class);
		}
		else {
			intent = new Intent(this, AtyLogin.class);
		}
		startActivity(intent);
		this.finish();
	}
	
	@Override
	public boolean onBack() {
		return true;
	}
	
}
