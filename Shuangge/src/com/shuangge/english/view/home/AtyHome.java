package com.shuangge.english.view.home;

import java.util.List;
import java.util.Map;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.meetme.android.horizontallistview.HorizontalListView.OnHLVTouchListener;
import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackResDownload;
import com.shuangge.english.entity.server.lesson.Type2Data;
import com.shuangge.english.entity.server.msg.ModuleMsgData;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.user.RewardsData;
import com.shuangge.english.network.account.TaskReqSetLevel;
import com.shuangge.english.network.lesson.TaskReqLessonProgress;
import com.shuangge.english.network.read.TaskReqReadOverView;
import com.shuangge.english.network.settings.TaskReqSettings;
import com.shuangge.english.network.shop.TaskReqGoodsLesson;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.service.Type4DownLoadService;
import com.shuangge.english.support.setting.SettingHelper;
import com.shuangge.english.task.TaskGetType4sByType2Id;
import com.shuangge.english.view.MenuActivity;
import com.shuangge.english.view.binding.AtyBindingAccount;
import com.shuangge.english.view.group.AtyClassRecommend;
import com.shuangge.english.view.home.adapter.AdapterHome;
import com.shuangge.english.view.home.component.DialogNoticeFragment;
import com.shuangge.english.view.home.component.DialogRewardsHelpFragment;
import com.shuangge.english.view.home.component.HomeMask;
import com.shuangge.english.view.lesson.AtyType4s;
import com.shuangge.english.view.menu.AtyAccountCenter;
import com.shuangge.english.view.msg.AtySystemNotice;
import com.shuangge.english.view.read.AtyReadFourth;
import com.shuangge.english.view.read.AtyVideoTest;
import com.shuangge.english.view.shop.AtyShopItemDetail;

/**
 * 主页
 * @author Jeffrey
 *
 */
public class AtyHome extends MenuActivity implements OnClickListener, OnCheckedChangeListener, IPushMsgNotice, OnItemClickListener {

	public static final String GIFT_VERSION = "giftVersion";
	public static final String NOTICE_VERSION = "noticeVersion";
	public static final int MODULE_MENU = 0;
	public static final int MODULE_LESSON = 3;
	public static final int CODE_NORMAL = 0;

	private ImageButton btnMenu, btnMenuWithMsg;
	private CheckBox checkboxVoice;
	private LinearLayout llCreateClass, llLastLesson;
	private RelativeLayout rlContainer;
	private TextView txtLastLesson;
	private ImageView imgLastLesson, imgRecommend;
	private ImageView imgGift, imgRewards, imgRewards2, imgGoodsGift;
	private TextView txtGiftPaopao;
	private FrameLayout flPaopao;
	private View giftMask;
	private HorizontalListView hlv;
	private AdapterHome adapter;
	
	private HomeMask mask2;
	private DialogNoticeFragment dialogNotice;
	private DialogRewardsHelpFragment dialogRewardsHelp;
	private View markView;
	private Boolean enabledAll = false;
	private Boolean isHave = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void initClassData() {
		super.initClassData();
		refreshIcons();
	}
	
	private void refreshIcons() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (null == beans || beans.getLoginData() == null) {
			return;
		}
		
		//班级奖励tip
		imgRewards2.setVisibility((null != beans.getLoginData().getRewardsData() 
				&& beans.getLoginData().getRewardsData().getAuth() == RewardsData.HAVE_AUTH) ? View.VISIBLE : View.GONE);
		
		imgGift.setVisibility(View.GONE);
		imgRewards.setVisibility(View.VISIBLE);
//		Toast.makeText(AtyHome.this, "size"+beans.getMyGroupDatas().getClassInfos().size(), Toast.LENGTH_SHORT).show();
		if ( null == beans.getMyGroupDatas()
				|| beans.getMyGroupDatas().getClassInfos().size() == 0) {
			imgGift.setVisibility(View.VISIBLE);
			int giftVersion = SettingHelper.getSharedPreferences(this, GIFT_VERSION, 1);
			if (giftVersion	< ConfigConstants.GIFT_VERSION) {
				imgGift.setImageResource(R.drawable.icon_gift_p);
			}
			else {
				imgGift.setImageResource(R.drawable.icon_gift);
			}
			txtGiftPaopao.setText(ConfigConstants.GIFT_CONTENT);
			imgGift.setOnClickListener(this);
			giftMask.setOnClickListener(this);
			imgRewards.setVisibility(View.GONE);
			return;
		}
		if (ConfigConstants.REWARDS_VERSION > 0) {
			imgRewards.setVisibility(View.VISIBLE);
			imgRewards.setOnClickListener(this);
			return;	
		}
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_home);
		rlContainer = (RelativeLayout) findViewById(R.id.rlContainer);
		llLastLesson = (LinearLayout) findViewById(R.id.llLastLesson);
		llLastLesson.setOnClickListener(this);
		llLastLesson.setVisibility(View.INVISIBLE);
		imgLastLesson = (ImageView) findViewById(R.id.imgLastLesson);
		txtLastLesson = (TextView) findViewById(R.id.txtLastLesson);
		txtLastLesson.setText("");
		
		btnMenu = (ImageButton) findViewById(R.id.btnMenu);
		btnMenu.setOnClickListener(this);
		btnMenuWithMsg = (ImageButton) findViewById(R.id.btnMenuWithMsg);
		btnMenuWithMsg.setOnClickListener(this);
		checkboxVoice = (CheckBox) findViewById(R.id.checkboxVoice);
		checkboxVoice.setOnCheckedChangeListener(this);

		llCreateClass = (LinearLayout) findViewById(R.id.llCreateClass);
		llCreateClass.setOnClickListener(this);
		
		giftMask = findViewById(R.id.llGiftMask);
		giftMask.setVisibility(View.GONE);
		imgGift = (ImageView) findViewById(R.id.imgGift);
		imgGift.setVisibility(View.GONE);
		txtGiftPaopao = (TextView) findViewById(R.id.txtGiftPaopao);
		flPaopao = (FrameLayout) findViewById(R.id.flPaopao);
		flPaopao.setVisibility(View.GONE);
		
		imgRewards = (ImageView) findViewById(R.id.imgRewards);
		imgRewards.setVisibility(View.GONE);
		imgRewards2 = (ImageView) findViewById(R.id.imgRewards2);
		imgRewards2.setVisibility(View.GONE);
		imgGoodsGift = (ImageView) findViewById(R.id.imgGoodsGift);
		imgGoodsGift.setVisibility(View.GONE);
		imgGoodsGift.setOnClickListener(this);
		markView = (View) findViewById(R.id.markView);
		markView.setVisibility(View.GONE);
		markView.setOnClickListener(this);
		hlv = (HorizontalListView) findViewById(R.id.hlv);
		hlv.setOnItemClickListener(this);
		hlv.setOnHLVTouchListener(new OnHLVTouchListener() {
			
			@Override
			public void onTouch(MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (hlv.getmCurrentX() > 0) {
						setTouchEnabled(false);
					}
					return;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					setTouchEnabled(true);
					return;
				}
			}
			
		});
		notice();
		refreshLastLessonInfo();
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		checkboxVoice.setChecked(!beans.isSpeechEnabled());
		if (beans.getUnlockDatas() != null) {
			
			adapter = new AdapterHome(this, beans.getUnlockDatas().getType2s());
			hlv.setAdapter(adapter);
			
		}
		if (beans.getLoginData() == null) {
			return;
		}
		if (beans.getLoginData().getFirst()) {
			AtySetLevel.show(this);
			return;
		}
		
//		if (true) {
//			new TaskReqReadList(0, new CallbackNoticeView<Void, Boolean>() {
//
//				@Override
//				public void refreshView(int tag, Boolean result) {
//					if (null == result || !result) {
//						return;
//					}
//					AtyReadList.startAty(AtyHome.this);
//				}
//
//				@Override
//				public void onProgressUpdate(int tag, Void[] values) {
//				}
//				
//			});
//			return;
//		}
		
		//通知面板
		int noticeVersion = SettingHelper.getSharedPreferences(this, NOTICE_VERSION, 0);
		if (noticeVersion < ConfigConstants.NOTICE_VERSION && ConfigConstants.NOTICE_VERSION > 0) {
			dialogNotice = new DialogNoticeFragment(new DialogNoticeFragment.CallBackDialogConfirm() {
				
				@Override
				public void onClose() {
					dialogNotice.dismiss();
					dialogNotice = null;
				}
			});
			if (dialogNotice.isVisible()) {
				return;
			}
			dialogNotice.showDialog(getSupportFragmentManager());
			SettingHelper.setEditor(this, NOTICE_VERSION, ConfigConstants.NOTICE_VERSION);
		}
		
//		int level = beans.getLoginData().getInfoData().getLevel();
//		ImageView img = null; 
//		switch (level) {
//		case 1:
//			img = imgM0Recommend;
//			break;
//		case 2:
//		case 3:
//			img = imgM1Recommend;
//			break;
//		case 4:
//			img = imgM2Recommend;
//			break;
//		}
//		img.setVisibility(View.VISIBLE);
//		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//		alphaAnimation.setFillAfter(true);
//		alphaAnimation.setDuration(1500);
//		img.startAnimation(alphaAnimation);
		
		requestClassData(true);
	}
	
	private void compareLessonInfo() {
		final CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (beans.getObtainLesson() == null) {
//			Toast.makeText(AtyHome.this,"obtainLesson为空", Toast.LENGTH_SHORT).show();
			return;
		}
		final Map<String, Type2Data> unlockDatasType2 = beans.getUnlockDatas().getType2s();
		Map<String, Type2Data> obtainLessonType2 = beans.getObtainLesson().getType2s();
		for (final String key : unlockDatasType2.keySet()) {
			if (!unlockDatasType2.get(key).getEnabled()) {
				continue;
			}
//			Toast.makeText(AtyHome.this,unlockDatasType2.get(key).getHasAuth().toString() +","+obtainLessonType2.get(key).getName()+","+ obtainLessonType2.get(key).getHasAuth().toString(), Toast.LENGTH_SHORT).show();
			if (!unlockDatasType2.get(key).getHasAuth() && obtainLessonType2.get(key).getHasAuth()) {
				markView.setVisibility(View.VISIBLE);
				showContent(false);
				enabledAll = true;
				Type2Data type2Data = unlockDatasType2.get(key);
//				Toast.makeText(AtyHome.this,obtainLessonType2.get(key).getName() + obtainLessonType2.get(key).getHasAuth().toString(), Toast.LENGTH_SHORT).show();
				final int position = adapter.getItemPos(type2Data.getClientId());
				
				int distance = (int) (position * AppInfo.getScreenWidth() / 3.5 - hlv.getmCurrentX());
				int time = distance > (AppInfo.getScreenWidth() >> 1) ? 4000 : 2000;
				hlv.scrollTo(distance, time);
				showAnim(position, type2Data);
				return;
			}
		}
		enabledAll = false;
		markView.setVisibility(View.GONE);
		beans.setObtainLesson(null);
		adapter.setDatas(obtainLessonType2);
		adapter.notifyDataSetChanged();
//		beans.setUnlockDatas12(beans.getObtainLesson());
//		adapter.setDatas(beans.getUnlockDatas().getType2s());
//		adapter.notifyDataSetChanged();
//		beans.setObtainLesson(null);
	}
	
	@SuppressLint("NewApi")
	private void showAnim(final int position, final Type2Data type2Data) {
		new Handler().postDelayed(new Runnable() {
			
			
			public void run() {
				
				final AdapterHome.ViewHolder viewHolder = adapter.getViewHolderByPos(position);
				if (null == viewHolder.imgLevel || null == viewHolder.imgAnim || null == viewHolder.txtName) {
					new Handler().postDelayed(new Runnable() {
						public void run() {
							showAnim(position, type2Data);
						}
					}, 500);
					markView.setVisibility(View.GONE);
					return;
				}
		    	AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
				alphaAnimation1.setFillAfter(true);
				alphaAnimation1.setDuration(2000);
				viewHolder.imgLevel.startAnimation(alphaAnimation1);

				type2Data.setHasAuth(true);
				viewHolder.imgAnim.setVisibility(View.VISIBLE);
				AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
				alphaAnimation2.setFillAfter(true);
				alphaAnimation2.setDuration(2000);
				viewHolder.imgAnim.startAnimation(alphaAnimation2);
				
				alphaAnimation2.setAnimationListener(new AnimationListener() { 
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
			
					@Override
					public void onAnimationEnd(Animation animation) {
						compareLessonInfo();
					}
			
					@Override
					public void onAnimationRepeat(Animation animation) {
					}  
				});
				
				if (Build.VERSION.SDK_INT >= 11) {
					ObjectAnimator txtColorAnimator = ObjectAnimator.ofObject(viewHolder.txtName,
							"textColor",
							new ArgbEvaluator(),
							0xffe9e9e9,
							0xff5cd4e3);
					txtColorAnimator.setDuration(2000);
					txtColorAnimator.start();
				} else {
					viewHolder.txtName.setTextColor(0xff5cd4e3);
				}
			}
		}, 1000);
	}
	
	private void refreshLastLessonInfo() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		try {
			if (null == beans.getLastType1Id() 
					|| (beans.isMetenUser() && beans.getLastType1Id().equals("101"))
					|| (!beans.isMetenUser() && beans.getLastType1Id().equals("102"))) {
				return;
			}
			if (!TextUtils.isEmpty(beans.getLastType4Name()) && !TextUtils.isEmpty(beans.getLastType5Name())) {
//				txtLastLesson.setText(type2.getName() + " > " + type4.getName() + " > " + type5.getName());
				txtLastLesson.setText(beans.getLastType4Name() + " > " + beans.getLastType5Name());
				llLastLesson.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(beans.getLastType4Id())) {
				String resId = beans.getLastType4Id().replaceFirst(GlobalRes.getInstance().getBeans().getDefaultLessonId(), "101");
				int id = getResources().getIdentifier("icon_" + resId + "_p", "drawable", "air.com.shuangge.phone.ShuangEnglish");
				if (id == 0) {
					id = R.drawable.icon_core_p;
				}
				imgLastLesson.setImageDrawable(getResources().getDrawable(id));
			}
		} catch (Exception e) {
		}
	}
	
	public void notice() {
		super.notice();
		ModuleMsgData msg = GlobalRes.getInstance().getBeans().getMsgDatas();
//		btnMenu.setVisibility(msg.isSystemMsg() ? View.GONE : View.VISIBLE);
//		btnMenuWithMsg.setVisibility(msg.isSystemMsg() ? View.VISIBLE : View.GONE);
		if (flagSecret|| msg.isSystemMsg()||msg.getGiftMsg() > 0) {
			btnMenuWithMsg.setVisibility(View.VISIBLE);
			btnMenu.setVisibility(View.GONE);
		} else {
			btnMenuWithMsg.setVisibility(View.GONE);
			btnMenu.setVisibility(View.VISIBLE);
		}
		if (msg.getGiftMsg() > 0) {
			imgGoodsGift.setVisibility(View.VISIBLE);
		} else {
			imgGoodsGift.setVisibility(View.GONE);
		}
		llCreateClass.getChildAt(0).setBackgroundResource(msg.isClassMsg() ? R.drawable.bg_create_class_p : R.drawable.bg_create_class);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMenu: case R.id.btnMenuWithMsg:
			getSlidingMenu().toggle(true);
			break;
		case R.id.llCreateClass:
			intoClass();
			break;
		case R.id.imgGift:
			int giftVersion = SettingHelper.getSharedPreferences(this, GIFT_VERSION, 1);
			if (giftVersion < ConfigConstants.GIFT_VERSION) {
				imgGift.setImageResource(R.drawable.icon_gift);
				SettingHelper.setEditor(this, GIFT_VERSION, ConfigConstants.GIFT_VERSION);
			}
			giftMask.setVisibility(View.VISIBLE);
			flPaopao.setVisibility(View.VISIBLE);
			break;
		case R.id.imgRewards:
			imgRewards.setEnabled(false);
			dialogRewardsHelp = new DialogRewardsHelpFragment(new DialogRewardsHelpFragment.CallBackDialogConfirm() {
				
				@Override
				public void onClose() {
					dialogRewardsHelp.dismiss();
					imgRewards.setEnabled(true);
					dialogRewardsHelp = null;
				}
				
			});
			if (dialogRewardsHelp.isVisible()) {
				return;
			}
			dialogRewardsHelp.showDialog(getSupportFragmentManager());
			break;
		case R.id.llGiftMask:
			giftMask.setVisibility(View.GONE);
			flPaopao.setVisibility(View.GONE);
			break;
		case R.id.imgGoodsGift:
			AtySystemNotice.startAty(AtyHome.this);
			break;
		}
		switch (v.getId()) {
		case R.id.llLastLesson:
			String type1Id = getBeans().getLastType1Id();
			if (null == type1Id 
				|| (getBeans().isMetenUser() && type1Id.equals("101"))
				|| (!getBeans().isMetenUser() && type1Id.equals("102"))) {
				return;
			}
			String type2Id = getBeans().getLastType2Id();
			if (!TextUtils.isEmpty(type1Id) 
					&& !TextUtils.isEmpty(type2Id)
					&& !TextUtils.isEmpty(getBeans().getLastType4Id()) 
					&& !TextUtils.isEmpty(getBeans().getLastType5Id())) {
				selectType12(type1Id, type2Id, true);
			}
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Type2Data data = adapter.getItem(position);
		if (!"20002".equals(data.getClientId())) {
			AtyVideoTest.startAty(this);
			return;			
		}
		if ("20002".equals(data.getClientId())) {
			showLoading();
			new TaskReqReadOverView(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					AtyReadFourth.startAty(AtyHome.this);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
			return;
		}
		isHave = false;
		List<String> clientIds = GlobalRes.getInstance().getBeans().getLoginData().getClientIds(); 
		if (null == data) {
			return;
		}
		if (!data.getEnabled()) {
			Toast.makeText(this, R.string.levelTip1, Toast.LENGTH_SHORT).show();
			return;
		}
		if (clientIds != null) {
			for (int i = 0; i < clientIds.size(); i++) {
				if (data.getClientId().equals(clientIds.get(i))) {
					if (data.getHasAuth()) {
						break;
					} else {
						Toast.makeText(this, R.string.levelTip3, Toast.LENGTH_SHORT).show();
						isHave = true;
						return;
					}
				}
			}
		}
		
		if (!data.getHasAuth() && !isHave) {
			Toast.makeText(this, R.string.levelTip2, Toast.LENGTH_SHORT).show();
			showLoading();
			//添加跳转到详情
			new TaskReqGoodsLesson(0, new CallbackNoticeView<Void, GoodsData>() {

				@Override
				public void refreshView(int tag, GoodsData data) {
					hideLoading();
					if (null == data)
						return;
					AtyShopItemDetail.startAty(AtyHome.this, data.getGoodsId(), data.getPayType());
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, data.getClientId().substring(0, 3));
			return;
		}
	
		selectType12(data.getClientId().substring(0, 3), data.getClientId(), false);
	}

	private void selectType12(String type1Id, String type2Id, final boolean goNext) {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		beans.setCurrentType1Id(type1Id);
		beans.setCurrentType2Id(type2Id);
		
		if (GlobalResTypes.getInstance().isDownloaded(type2Id)) {
			showLoading();
			requestType12s(goNext);
		} 
		else {
			showLoading(getString(R.string.loaddingTip2));
			GlobalResTypes.getInstance().setCallBackType2(
					new CallbackResDownload() {

						public void waitingHanler(String url) {
						}

						public void stopHandler(String url, long progress, long max) {
						}

						public void startHandler(String url, long max) {
						}

						public void progressHandler(String url, long progress,long max) {
						}

						public void finishedHandler(String url) {
							requestType12s(goNext);
						}

						public void errorHandler(String url) {
							hideLoading();
						}
					});
			GlobalResTypes.getInstance().startDownload(type2Id);
		}
	}
	
	private void startAtyType4s(final boolean goOn) {
		new TaskGetType4sByType2Id(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					//err
					return;
				}
				Intent i = new Intent(AtyHome.this, AtyType4s.class);
				i.putExtra("goOn", goOn);
				startActivityForResult(i, MODULE_LESSON);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
		});
	}
	
	private void requestType12s(final boolean goNext) {
		new TaskReqLessonProgress(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (null == result || !result) {
					hideLoading();
					//err
					return;
				}
				CacheBeans beans = GlobalRes.getInstance().getBeans();
				if (null != beans.getCurrentType5Id()) {
					if (null == beans.getUnlockDatas().getType5s().get(beans.getCurrentType5Id())) {
						Toast.makeText(AtyHome.this, R.string.lockTip1, Toast.LENGTH_SHORT).show();
						return;
					}
				}
				startAtyType4s(goNext);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		notice();
		compareLessonInfo();
		refreshIcons();
		switch (requestCode) {
		case AtyAccountCenter.REQUEST_ACCOUNT_CENTER:
			if (resultCode == CODE_QUIT) {
//				startActivity(new Intent(AtyHome.this, AtyLogin.class));
				this.finish();
			}
			break;
		case AtyClassRecommend.REQUEST_CLASS_RECOMMEND: 
//			requestData();
			break;
		case MODULE_LESSON:
			if (null != imgRecommend) {
				imgRecommend.clearAnimation();
				imgRecommend.setVisibility(View.GONE);
			}
			refreshLastLessonInfo();
			break;
		case AtyBindingAccount.REQUEST_BINDING_ACCOUNT:
			if (resultCode == AtyBindingAccount.CODE_BINDING_ACCOUNT) {
				AtyClassRecommend.startAty(AtyHome.this);
			}
			break;
		case AtySetLevel.REQUEST_CODE:
			showLoading();
			final int level = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getLevel();
			new TaskReqSetLevel(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					mask2 = new HomeMask(new HomeMask.CallbackHomeMask() {
						
						@Override
						public void close() {
							if (null != mask2) {
								mask2.hide(getSupportFragmentManager());
								mask2 = null;
							}
							switch (level) {
							case 1:
								adapter.toRecommendByPos(0);
								break;
							case 2:
							case 3:
								adapter.toRecommendByPos(1);
								break;
							case 4:
								adapter.toRecommendByPos(2);
								break;
							}
						}
						
					}, rlContainer, btnMenu, checkboxVoice);
					mask2.show(getSupportFragmentManager());
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, level);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isChecked = !isChecked;
		GlobalRes.getInstance().getBeans();
		GlobalRes.getInstance().getBeans().setSpeechEnabled(isChecked);
		new TaskReqSettings(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, null, isChecked, null);
	}

	/*********************************************************************************************************************************/

	private static boolean isExit = false;

	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (enabledAll) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if (getSlidingMenu().isMenuShowing()) {
				getSlidingMenu().showContent();
				return true;
			}
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (isLoading()) {
			return;
		}
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), R.string.backExit,
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} 
		else {
			finish();
			stopService(new Intent(this, Type4DownLoadService.class));
			List<Activity> atyList = GlobalApp.getInstance().getStackActivities();
			if (atyList != null) {
				for (int i = 0; i < atyList.size(); i++) {
					atyList.get(i).finish();
				}
			}
			System.exit(0);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	

}
