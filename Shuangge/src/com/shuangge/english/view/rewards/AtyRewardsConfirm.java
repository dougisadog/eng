package com.shuangge.english.view.rewards;

import java.util.List;

import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.WheelView;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.read.RewardSettingsData;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.network.rewards.TaskReqRewardsDetail;
import com.shuangge.english.network.rewards.TaskReqRewardsPostDatas;
import com.shuangge.english.support.app.ShareContentWebPage;
import com.shuangge.english.support.app.ShareManager.ShareConstants;
import com.shuangge.english.support.app.ShareManager1;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.binding.AtyBindingWeChat;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.component.wheel.WeelAdapter;
import com.shuangge.english.view.rewards.component.DialogCashRewardsFragment;
import com.tencent.mm.sdk.constants.ConstantsAPI;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.weixin.media.CircleShareContent;
import com.tencent.mm.sdk.modelbase.BaseResp;

/**
 * 
 * @author Doug
 * 
 */
public class AtyRewardsConfirm extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_REWARDS_CONFIRM = 2000;

	private InfoData entity;

	private TextView leftScholarship; // 微信号 剩余奖学金

	private EditTextWidthTips wechatId;

	private String wechatNo;

	private ImageButton btnBack; // 返回按钮

	private CircleImageView imgHead; // 微信头像

	private TextView geRewardsBtn; // 提现按钮

	private static double rewardsCount = 0; // 选择领取的奖学金数目

	private DialogCashRewardsFragment rewardsDialog = null;

	private WheelView wv;

	private Intent i;
	
	private List<RewardSettingsData> rewardSettingsDatas;

//	private final UMSocialService mController = UMServiceFactory
//			.getUMSocialService(ShareConstants.DESCRIPTOR);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void requestRewardsSelector(int index) {
		String[] datas = null;
		if (GlobalRes.getInstance().getBeans().getLoginData().getRewardSettingsData() != null) {
			rewardSettingsDatas = GlobalRes.getInstance()
					.getBeans().getLoginData().getRewardSettingsData();
			datas = new String[rewardSettingsDatas.size()];
			for (int i = 0; i < rewardSettingsDatas.size(); i++) {
				RewardSettingsData rewardSettingsData = rewardSettingsDatas
						.get(i);
				if (rewardSettingsData == null)
					break;
				datas[i] = rewardSettingsDatas.get(i).getRewards() + "元";
			}
			wv = (WheelView) findViewById(R.id.selector);
			wv.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
			wv.setVisibleItems(rewardSettingsDatas.size());
			wv.setWheelBackground(R.drawable.wheel_bg_holo);
			wv.setWheelForeground(R.drawable.wheel_val_reward);
			if (datas.length != 0) {
				wv.setViewAdapter(new WeelAdapter(R.layout.item_wheel_reward, this,
						datas));
			}
			
			wv.setCurrentItem(index);
			// 点击滑动
			wv.addClickingListener(new OnWheelClickedListener() {
				@Override
				public void onItemClicked(WheelView wheel, int itemIndex) {
					if (wheel.getCurrentItem() != itemIndex) {
						wv.scroll(itemIndex - wheel.getCurrentItem(), 1000);
						return;
					}
				}
			});
		}
		

		

	}

	// 请求提现详细页
	private void requestRewardsFragment() {
		showLoading();
		new TaskReqRewardsDetail(0, new CallbackNoticeView<Void, String>() {

			@Override
			public void refreshView(int tag, String result) {
				hideLoading();
				if (TextUtils.isEmpty(result)) {
					// 错误提示待修改
					Toast.makeText(AtyRewardsConfirm.this,
							R.string.dialogRewardsErrTip, Toast.LENGTH_SHORT)
							.show();
					geRewardsBtn.setEnabled(true);
					return;
				}
				initRewardsDialog(result);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

		});
	}

	/**
	 * 提现btn 弹出dialog 分享
	 * 
	 * @param html
	 *            加载的html
	 */
	private void initRewardsDialog(String html) {

		// 获取提现的fragment
		rewardsDialog = new DialogCashRewardsFragment(
				new DialogCashRewardsFragment.CallBackDialogConfirm() {

					@Override
					public void onClose() {
						rewardsDialog.dismiss();
						geRewardsBtn.setEnabled(true);
						rewardsDialog = null;
					}

					@Override
					public void onSubmit() {
						if (null != rewardsDialog) {
							rewardsDialog.dismiss();
							rewardsDialog = null;
						}
						wxq();
					}

				}, html, rewardsCount);

		if (rewardsDialog.isVisible()) {
			return;
		}
		rewardsDialog.showDialog(this.getSupportFragmentManager());

	}

	/**
	 * 分享接口调用？
	 */
	public void wxq() {
		if (null == entity) {
			return;
		}
		Long userNo = entity.getUserNo();
		// 此处内容需要修改
//		UMImage urlImage = new UMImage(this, ConfigConstants.SHARE_IMG4);
//		CircleShareContent circleContent = new CircleShareContent();
//		circleContent.setShareContent(ConfigConstants.SHARE_CONTENT4);
//		circleContent.setTitle(ConfigConstants.SHARE_TITLE4);
//		circleContent.setShareMedia(urlImage);
//
//		circleContent.setTargetUrl(ConfigConstants.SHARE_URL5 + "/" + userNo + "-" + rewardsCount);
//		mController.setShareMedia(circleContent);
//		mController.getConfig().closeToast();
//		mController.postShare(this, SHARE_MEDIA.WEIXIN_CIRCLE,
//				new SnsPostListener() {
//
//					@Override
//					public void onStart() {
//					}
//
//					@Override
//					public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity1) {
//						geRewardsBtn.setEnabled(true);
//						mController.getConfig().cleanListeners();
//						if (eCode == 200) {
//							AtyRewardsConfirm.this.showLoading();
//							new TaskReqRewardsPostDatas(0, new CallbackNoticeView<Void, Integer>() {
//
//								@Override
//								public void onProgressUpdate(int tag, Void[] values) {
//								}
//
//								@Override
//								public void refreshView(int tag, Integer result) {
//									AtyRewardsConfirm.this.hideLoading();
//									if (null != rewardsDialog) {
//										rewardsDialog.dismissAllowingStateLoss();
//										rewardsDialog = null;
//									}
//									if (null == result || result == TaskReqRewardsPostDatas.STATUS_ERR) {
//										return;
//									}
//									if (result == TaskReqRewardsPostDatas.STATUS_SCUCESS) {
//										Toast.makeText(AtyRewardsConfirm.this, "提取￥" + rewardsCount + "元成功，"
//										+AtyRewardsConfirm.this.getString(R.string.reward_get_tip), Toast.LENGTH_SHORT).show();
//										//通知页面刷新
//										leftScholarship.setText("账户余额：     " + entity.getMoney() + "元");
//										return;
//									}
//								}
//
//							}, rewardsCount, wechatNo);
//							return;
//						}
//						AtyRewardsConfirm.this.hideLoading();
//						String eMsg = "";
//						if (eCode == -101) {
//							eMsg = "没有授权";
//						}
//						Toast.makeText(AtyRewardsConfirm.this, "分享失败[" + eCode + "] " + eMsg, Toast.LENGTH_SHORT).show();
//					}
//				});
		
		ShareContentWebPage wxContent = new ShareContentWebPage(ConfigConstants.SHARE_TITLE4, 
        		ConfigConstants.SHARE_CONTENT4, 
        		ConfigConstants.SHARE_URL5 + "/" + userNo + "-" + rewardsCount,
        		ConfigConstants.SHARE_IMG4);
        
        ShareManager1.getInstance(this).shareByWeixin(wxContent, 1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		BaseResp resp = GlobalRes.getInstance().getBeans().getWxResp();
		if(resp == null) {
			return;
		}
		if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
			GlobalRes.getInstance().getBeans().setWxResp(null);
			shareSuccess();
		}
	}
	
	private void shareSuccess() {
		AtyRewardsConfirm.this.showLoading();
		new TaskReqRewardsPostDatas(0, new CallbackNoticeView<Void, Integer>() {

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

			@Override
			public void refreshView(int tag, Integer result) {
				AtyRewardsConfirm.this.hideLoading();
				if (null != rewardsDialog) {
					rewardsDialog.dismissAllowingStateLoss();
					rewardsDialog = null;
				}
				if (null == result || result == TaskReqRewardsPostDatas.STATUS_ERR) {
					return;
				}
				if (result == TaskReqRewardsPostDatas.STATUS_SCUCESS) {
					Toast.makeText(AtyRewardsConfirm.this, "提取￥" + rewardsCount + "元成功，"
					+AtyRewardsConfirm.this.getString(R.string.reward_get_tip), Toast.LENGTH_SHORT).show();
					//通知页面刷新
					leftScholarship.setText("账户余额：     " + entity.getMoney() + "元");
					return;
				}
			}

		}, rewardsCount, wechatNo);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_get_rewards_confirm);
		i = getIntent();

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		geRewardsBtn = (TextView) findViewById(R.id.get_rewards_btn);
		geRewardsBtn.setOnClickListener(this);

		wechatId = (EditTextWidthTips) findViewById(R.id.wechat_id);
		leftScholarship = (TextView) findViewById(R.id.left_scholarship);
		imgHead = (CircleImageView) findViewById(R.id.imgHead);

		requestRewardsSelector(1);
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		entity = beans.getLoginData().getInfoData();
		// scholarship = 200D;
		leftScholarship.setText("账户余额：     " + entity.getMoney() + "元");
		initMyself(entity);
	}

	/**
	 * wechatId 绑定
	 * 
	 * @param entity
	 */
	private void initMyself(InfoData entity) {
		if (!TextUtils.isEmpty(entity.getWechatNo())) {
			wechatId.setText(entity.getWechatNo());
		}

		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(
					new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(),
							imgHead));
		} else {
			imgHead.clear();
		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.get_rewards_btn:
			rewardsCount = rewardSettingsDatas.get(wv.getCurrentItem()).getRewards();
			getRewardsSubmit();
			break;
		}
	}

	private void getRewardsSubmit() {
		if (rewardsCount > entity.getMoney() || rewardsCount <= 0) {
			Toast.makeText(this, R.string.reward_confirm_wrong_account_tip,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(wechatId.getText())) {
			Toast.makeText(this, R.string.reward_confirm_wrong_wechat_tip,
					Toast.LENGTH_SHORT).show();
			return;
		}
		// 跳转至微信绑定
		wechatNo = wechatId.getText().toString();

		if (TextUtils.isEmpty(entity.getWechatNo())) {
			Intent i = new Intent(this, AtyBindingWeChat.class);
			i.putExtra("wechatNo", wechatNo);
			startActivity(i);
			return;
		}
		if (!wechatNo.equals(entity.getWechatNo().toString())) {
			Intent i = new Intent(this, AtyBindingWeChat.class);
			i.putExtra("wechatNo", wechatNo);
			startActivity(i);
			return;
		}
		requestRewardsFragment();
		geRewardsBtn.setEnabled(false);
	}

	@Override
	protected void onDestroy() {
		if (rewardsDialog != null) {
			rewardsDialog.dismiss();
		}
		super.onDestroy();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			getRewardsSubmit();
		}
	}
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyRewardsConfirm.class);
		context.startActivityForResult(i, REQUEST_REWARDS_CONFIRM);
	}

}
