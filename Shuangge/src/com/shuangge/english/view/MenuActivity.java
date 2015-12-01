package com.shuangge.english.view;

import java.lang.reflect.Field;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.network.group.TaskReqMyClassDatas;
import com.shuangge.english.network.login.TaskReqLogout;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.app.AnalyticsManager;
import com.shuangge.english.support.app.ScreenObserver.ScreenStateListener;
import com.shuangge.english.support.error.ExceptionHandler;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.setting.SettingHelper;
import com.shuangge.english.view.about.AtyAboutList;
import com.shuangge.english.view.binding.AtyBindingAccount;
import com.shuangge.english.view.binding.AtyBindingInfos;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.dialog.DialogLoadingFragment;
import com.shuangge.english.view.component.dialog.DialogLoadingFragment.ICallbackDialog;
import com.shuangge.english.view.contactus.AtyContactUs;
//import com.shuangge.english.view.date.AtyDate1;
import com.shuangge.english.view.download.AtyDownload2;
import com.shuangge.english.view.group.AtyClassCreate;
import com.shuangge.english.view.group.AtyClassHome;
import com.shuangge.english.view.group.AtyClassRecommend;
import com.shuangge.english.view.home.AtyHome;
import com.shuangge.english.view.login.AtyLogin;
import com.shuangge.english.view.menu.AtyAccountCenter;
import com.shuangge.english.view.menu.AtyInvite;
import com.shuangge.english.view.msg.AtySystemNotice;
import com.shuangge.english.view.ranklist.AtyRanklist;
import com.shuangge.english.view.secretmsg.AtySecretMsgList;
import com.shuangge.english.view.settings.AtySettings;
import com.shuangge.english.view.shop.AtyShopList;
import com.tencent.tauth.Tencent;

public class MenuActivity extends SlidingFragmentActivity implements ScreenStateListener, ICallbackDialog, IPushMsgNotice {

	public final static int CODE_QUIT = 1;
	public static boolean isActive = true;
	private boolean isRunning = false;
	
	private DialogLoadingFragment loadingDialog;
	private ImageView imgRedPoint2, imgRedPoint9, imgRedPoint13;
	private Tencent mTencent;
    @Override
    protected void onResume() {
        super.onResume();
        GlobalApp.getInstance().setCurrentRunningActivity(this);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //从后台唤醒，进入前台
        if (!isActive) {
        	isActive = true;
//        	System.out.println("从后台唤醒，进入前台");
//        	PushManager.getInstance().start();
        }
        //友盟统计
        AnalyticsManager.getInstance().onPageStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pauseMusic();
        //友盟统计
        AnalyticsManager.getInstance().onPageEnd(this);
        if (GlobalApp.getInstance().getCurrentRunningActivity().equals(this)) {
            GlobalApp.getInstance().setCurrentRunningActivity(null);
        }
    }
    
	@Override
	protected void onStop() {
		super.onStop();
		//进入后台
		if (!isAppOnForeground()) {
			isActive = false;
//			System.out.println("进入后台");
//			PushManager.getInstance().stop();
        }
	}
	
//	@Override
//	public boolean onCreatePanelMenu(int featureId, Menu menu) {
//		return false;
//	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forceShowActionBarOverflowMenu();
//        initNFC();
        GlobalApp.getInstance().setActivity(this);
        GlobalApp.getInstance().addStackActivity(this);
        isRunning = true;
        
        //Menu
        setBehindContentView(R.layout.menu_frame);
        findViewById(R.id.llMenu1).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu2).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu3).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu4).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu5).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu6).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu7).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu8).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu9).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu10).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu11).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu13).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu14).setOnClickListener(onMenuClickListener);
        findViewById(R.id.llMenu15).setOnClickListener(onMenuClickListener);
        imgRedPoint2 = (ImageView) findViewById(R.id.imgRedPoint2);
        imgRedPoint9 = (ImageView) findViewById(R.id.imgRedPoint9);
        imgRedPoint13 = (ImageView) findViewById(R.id.imgRedPoint13);
        findViewById(R.id.llMenu6).setVisibility(View.GONE);
        findViewById(R.id.llMenu8).setVisibility(View.GONE);
//        findViewById(R.id.llMenu14).setVisibility(View.GONE);
        refreshRedPoint();
//		if (savedInstanceState == null) {
//			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
//			mFrag = new SampleListFragment();                                                                                                                                                                                                                                                                                                                                                                              
//			t.replace(R.id.menu_frame, mFrag);
//			t.commit();
//		} 
//		else {
//			mFrag = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
//		}

		SlidingMenu sm = getSlidingMenu();
//		sm.setShadowWidthRes(R.dimen.shadow_width);
//		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindWidthRes(R.dimen.slidingmenu_width);
//		sm.setFadeDegree(0.5f);
		sm.setFadeEnabled(false);
		sm.setBgFadeEnabled(true);
		sm.setBgFadeDegree(0.8f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		if (ExceptionHandler.checkServerDataErr()) {
        	GlobalApp.getInstance().restart();
        	return;
        }
        
        initData();
        initRequestData();
    }
    
    public void setTouchEnabled(boolean enabled) {
		getSlidingMenu().setSlidingEnabled(enabled);
    }
    
    private OnClickListener onMenuClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.llMenu1:
				if (MenuActivity.this instanceof AtyHome) {
					toggle();
				}
				break;
			case R.id.llMenu2:
//				AtyUserInfo.startAty(MenuActivity.this);
				AtyAccountCenter.startAty(MenuActivity.this);
				break;
			case R.id.llMenu3:
				intoClass();
				break;
			case R.id.llMenu4:
				AtyRanklist.startAty(MenuActivity.this);
				break;
			case R.id.llMenu5:
				AtySystemNotice.startAty(MenuActivity.this);
				break;
			case R.id.llMenu6:
				AtyDownload2.startAty(MenuActivity.this);
//				AtyDownload.startAty(MenuActivity.this);
				break;
			case R.id.llMenu7:
				if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().isVisitor()) {
					Toast.makeText(MenuActivity.this, R.string.bindingAccountErrTip, Toast.LENGTH_SHORT).show();
					AtyBindingAccount.startAty(MenuActivity.this);
					return;
				}
				AtyBindingInfos.startAty(MenuActivity.this);
				break;
			case R.id.llMenu8:
				AtyContactUs.startAty(MenuActivity.this);
				break;
			case R.id.llMenu9:
//				SettingHelper.setEditor(MenuActivity.this, "aboutUsMenu", true);
//				refreshRedPoint();
				AtyAboutList.startAty(MenuActivity.this);
				break;
			case R.id.llMenu10:
				AtySettings.startAty(MenuActivity.this);
				break;
			case R.id.llMenu11:
				showQuitDialog();
				break;
			case R.id.llMenu13:
//				imgRedPoint13.setVisibility(View.GONE);
				AtySecretMsgList.startAty(MenuActivity.this);
				break;
			case R.id.llMenu14:
				SettingHelper.setEditor(MenuActivity.this, "aboutUsMenu", true);
				refreshRedPoint();
				AtyShopList.startAty(MenuActivity.this);
				break;
			case R.id.llMenu15:
//				AtyDate1.startAty(MenuActivity.this);
				AtyInvite.startAty(MenuActivity.this);
				break;
			}
		}
		
	};
	
	private void refreshRedPoint() {
		imgRedPoint9.setVisibility(SettingHelper.getSharedPreferences(this, "aboutUsMenu", false) ? View.GONE : View.VISIBLE);
	}
	
	private DialogConfirmFragment quitDialog;
	
	private void showQuitDialog() {
		if (null == quitDialog) {
			quitDialog = new DialogConfirmFragment(new CallBackDialogConfirm() {
				
				@Override
				public void onSubmit(int position) {
					hideDialog();
					new TaskReqLogout(0, new CallbackNoticeView<Void, Boolean>() {

						@Override
						public void refreshView(int tag, Boolean result) {
							switch (GlobalReqDatas.getInstance().getRequestUIDType()) {
							case EntityUser.QQ:
//								ShareManager.getInstance().logoutQQ(MenuActivity.this, callbackLogin);
								mTencent = Tencent.createInstance(ConfigConstants.QQAPP_ID,getApplicationContext());
								mTencent.logout(MenuActivity.this);
								ouitToLogin();
								break;
							case EntityUser.WX:
//								ShareManager.getInstance().logoutWX(MenuActivity.this, callbackLogin);
								ouitToLogin();
								break;
							default:
								AtyLogin.startAty(MenuActivity.this);
								finish();
								break;
							}
						}

						@Override
						public void onProgressUpdate(int tag, Void[] values) {
						}
						
					});
				}
				
				@Override
				public void onCancel() {
					hideDialog();
				}

				@Override
				public void onKeyBack() {
					onCancel();
				}
				
			}, getString(R.string.menuTip1En),  getString(R.string.menuTip1Cn), 0, R.style.DialogBottomToTopTheme);
		}
		if (quitDialog.isVisible()) {
			return;
		}
		quitDialog.showDialog(getSupportFragmentManager());
	}

	private void hideDialog() {
		if (null == quitDialog) {
			return;
		}
		quitDialog.dismiss();
		quitDialog = null;
	}
	private void ouitToLogin() {
		AtyLogin.startAty(MenuActivity.this);
		finish();
	}
//	private CallBackLogin callbackLogin = new CallBackLogin() {
//		
//		@Override
//		public void onError() {
//			AtyLogin.startAty(MenuActivity.this);
//			finish();
//		}
//		
//		@Override
//		public void onComplete() {
//			AtyLogin.startAty(MenuActivity.this);
//			finish();
//		}
//		
//	};

	protected boolean flagSecret = false;
	
	public void notice() {
		boolean flag = GlobalRes.getInstance().getBeans().getMsgDatas().isSystemMsg();
		if (null != imgRedPoint2)
			if (flag || GlobalRes.getInstance().getBeans().getMsgDatas().getGiftMsg() > 0) {
				imgRedPoint2.setVisibility(View.VISIBLE);
			} else {
				imgRedPoint2.setVisibility(View.GONE);
			}
//			imgRedPoint2.setVisibility(flag ? View.VISIBLE : View.GONE);
		boolean flagSecret1 = GlobalRes.getInstance().getBeans().getMsgDatas().getSecretMsg() > 0;
		if (null != imgRedPoint13) {
			List<SecretMsgDetailData> msgs = GlobalRes.getInstance().getBeans().getSecretListDatas();
			boolean isVisible = false;
			if (null != msgs) {
				for (int i = 0; i < msgs.size(); i++) {
					if (msgs.get(i).getStatus() == SecretMsgDetailData.STATUS_UNREAD) {
						isVisible = true;
						break;
					}
				}
			}
			flagSecret = flagSecret1 || isVisible;
			imgRedPoint13.setVisibility(flagSecret ? View.VISIBLE : View.GONE);
		}
//		icon3.setVisibility(flag ? View.GONE : View.VISIBLE);
//		icon3WitdhMsg.setVisibility(flag ? View.VISIBLE : View.GONE);
	}
	
	protected void intoClass() {
		if(GlobalRes.getInstance().getBeans().getLoginData().getInfoData() == null) {
			Toast.makeText(this, R.string.bindingAccountErrTip1, Toast.LENGTH_LONG).show();
			return;
		}
		if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().isVisitor()) {
			Toast.makeText(this, R.string.bindingAccountErrTip, Toast.LENGTH_LONG).show();
			AtyBindingAccount.startAty(this);
			return;
		}
		requestClassData(false);
	}
	
	protected void requestClassData(final boolean init) {
		showLoading();
		//获取自己班级信息
		new TaskReqMyClassDatas(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				
				initClassData();
				
				if (init) {
					return;
				}
				
				if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().size() > 0) {
					AtyClassHome.startAty(MenuActivity.this);
					return;
				}
				//美联学员 不只能创建班级 不可以加入别人班级
				if (GlobalRes.getInstance().getBeans().isMetenUser()) {
					AtyClassCreate.startAty(MenuActivity.this);
					return;
				}
				AtyClassRecommend.startAty(MenuActivity.this);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

		});
	}
	
	protected void initClassData() {
	}
    
//    @Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			toggle();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getSupportMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

    private void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {

        }
    }
    
/*
    @SuppressLint("NewApi")
	private void initNFC() {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (null == mNfcAdapter) {
            return;
        }
        mNfcAdapter.setNdefPushMessageCallback(new NfcAdapter.CreateNdefMessageCallback() {
            @Override
            public NdefMessage createNdefMessage(NfcEvent event) {
                String text = (GlobalApp.getInstance().getCurrentAccountName());
                NdefMessage msg = new NdefMessage(
                        new NdefRecord[]{createMimeRecord(
                                "application/org.qii.weiciyuan.beam", text.getBytes()),
                                NdefRecord.createApplicationRecord(getPackageName())
                        });
                return msg;
            }
        }, this);
    }

    @SuppressLint("NewApi")
	private NdefRecord createMimeRecord(String mimeType, byte[] payload) {
        byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
        return mimeRecord;
    }

*/
    protected void dealWithException(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void finish() {
    	View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    	super.finish();
    }
    
    @Override
    protected void onDestroy() {
    	GlobalApp.getInstance().removeStackActivity(this);
    	hideLoading();
    	super.onDestroy();
    }
    
    public void showLoading() {
    	showLoading(false);
    }
    
    public void showLoading(boolean cancelable) {
    	if (null == loadingDialog) {
	    	loadingDialog = new DialogLoadingFragment(cancelable, this);
    	}
    	if (loadingDialog.isVisible() || loadingDialog.isAdded()) {
    		return;
    	}
    	showLoadingDialog();
	}
    
    public void showLoading(String loadingInfo) {
    	showLoading(loadingInfo, false);
    }
    
    public void showLoading(String loadingInfo, boolean cancelable) {
    	if (null == loadingDialog) {
    		loadingDialog = new DialogLoadingFragment(loadingInfo, cancelable, this);
    	}
    	if (loadingDialog.isVisible() || loadingDialog.isAdded()) {
    		return;
    	}
    	showLoadingDialog();
    }
    
    private void showLoadingDialog() {
    	loadingDialog.showDialog(getSupportFragmentManager());
		if (isRunning)
			loadingDialog.onResumeFragments();
    }
    
    public void hideLoading() {
    	if (null == loadingDialog) {
    		return;
    	}
    	loadingDialog.hideDialog();
    	loadingDialog = null;
	}
    
    
    @Override
    protected void onPostResume() {
    	super.onPostResume();
    	isRunning = true;
    	if (null != loadingDialog) {
    		loadingDialog.onResumeFragments();
    	}
    }
    
    public boolean isLoading() {
    	return null != loadingDialog && loadingDialog.isAdded() && loadingDialog.isVisible();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != loadingDialog && loadingDialog.isAdded() && loadingDialog.isCancelable()) {
				hideLoading();
				return true;
			}
			if (onBack()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    
    public boolean onBack() {
		return false;
    }
    
    /*************************************************************************************************************************************************/
    
    /**
     * 程序是否在前台运行
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
                return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
            }
        }
        return false;
    }

	@Override
	public void onScreenOn() {
	}

	@Override
	public void onScreenOff() {
	}

	@Override
	public void onUserPresent() {
	}
    
	protected void pauseMusic() {  
//	    Intent freshIntent = new Intent();  
//	    freshIntent.setAction("com.android.music.musicservicecommand.pause");  
//	    freshIntent.putExtra("command", "pause");  
//	    sendBroadcast(freshIntent);  
//	    freshIntent = new Intent();  
//	    freshIntent.setAction("com.android.music.musicservicecommand");  
//	    freshIntent.putExtra("command", "pause");  
//	    sendBroadcast(freshIntent);  
	}
	
	/***********************************************************************************************************************************************/
	/**
	/** 数据
	/**
	/***********************************************************************************************************************************************/
	
	protected void initData() {
		
	}
	
	protected void initRequestData() {
		
	}
	
	protected CacheBeans getBeans() {
		return GlobalRes.getInstance().getBeans();
	}
	
}
