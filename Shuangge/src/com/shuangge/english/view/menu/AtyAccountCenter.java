package com.shuangge.english.view.menu;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.entity.server.user.MyRanklistResult;
import com.shuangge.english.network.account.TaskReqMyRanklist;
import com.shuangge.english.network.login.TaskReqLogout;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.binding.AtyBindingAccount;
import com.shuangge.english.view.binding.AtyBindingInfos;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.login.AtyLogin;
import com.shuangge.english.view.msg.AtySystemNotice;
import com.shuangge.english.view.ranklist.AtyRanklist;
import com.shuangge.english.view.rewards.AtyRewardsConfirm;
import com.shuangge.english.view.settings.AtySettings;
import com.shuangge.english.view.user.AtyUserInfo;
import com.tencent.tauth.Tencent;

public class AtyAccountCenter extends AbstractAppActivity implements
		OnClickListener, IPushMsgNotice {

	public static final int REQUEST_ACCOUNT_CENTER = 1060;

	private boolean requesting = false;

	private ImageButton btnBack;
	private TextView txtName, txtProfile, txtWeekRanlist, txtAllRanlist, txtCredits,
			txtWeekScore, txtAllScore, txtKey;
	private ImageView imgHead, imgPoint1;

	private TextView txtScholarship;

	private LinearLayout txtScholarshipBtn;

	private LinearLayout ll0;
	
	private Tencent mTencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_account_center);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtProfile = (TextView) findViewById(R.id.txtProfile);
		txtName = (TextView) findViewById(R.id.txtName);
		imgHead = (ImageView) findViewById(R.id.imgHead);
		txtProfile.setOnClickListener(this);
		txtName.setOnClickListener(this);
		imgHead.setOnClickListener(this);
		imgPoint1 = (ImageView) findViewById(R.id.imgPoint1);

		txtScholarship = (TextView) findViewById(R.id.txtScholarship);
		txtCredits = (TextView) findViewById(R.id.txtCredits);
		txtKey = (TextView) findViewById(R.id.txtKey);

		txtScholarshipBtn = (LinearLayout) findViewById(R.id.txtScholarshipBtn);

		txtScholarshipBtn.setOnClickListener(this);

		txtWeekRanlist = (TextView) findViewById(R.id.txtWeekRanlist);
		txtAllRanlist = (TextView) findViewById(R.id.txtAllRanlist);
		txtWeekScore = (TextView) findViewById(R.id.txtWeekScore);
		txtAllScore = (TextView) findViewById(R.id.txtAllScore);
		txtWeekRanlist.setOnClickListener(this);
		txtAllRanlist.setOnClickListener(this);
		txtWeekScore.setOnClickListener(this);
		txtAllScore.setOnClickListener(this);
		findViewById(R.id.img1).setOnClickListener(this);
		findViewById(R.id.txt1).setOnClickListener(this);
		findViewById(R.id.img2).setOnClickListener(this);
		findViewById(R.id.txt2).setOnClickListener(this);
		findViewById(R.id.img3).setOnClickListener(this);
		findViewById(R.id.txt3).setOnClickListener(this);
		findViewById(R.id.img4).setOnClickListener(this);
		findViewById(R.id.txt4).setOnClickListener(this);

		findViewById(R.id.ll1).setOnClickListener(this);
		findViewById(R.id.ll2).setOnClickListener(this);
		findViewById(R.id.ll3).setOnClickListener(this);
		findViewById(R.id.ll4).setOnClickListener(this);

		ll0 = (LinearLayout) findViewById(R.id.ll0);
		ll0.setVisibility(View.INVISIBLE);
		refreshData();
	}
	
	protected void refreshData() {
		InfoData data = getBeans().getLoginData()
				.getInfoData();
		txtName.setText(data.getName());
		txtCredits.setText(!TextUtils.isEmpty(data.getMoney2().toString()) ? data
				.getMoney2().toString() : "0");
		txtKey.setText(!TextUtils.isEmpty(data.getKeyNum().toString()) ? data
				.getKeyNum().toString() : "0");
		if (!TextUtils.isEmpty(data.getHeadUrl()))
			GlobalRes.getInstance().displayBitmap(
					new DisplayBitmapParam(data.getHeadUrl(), imgHead));
		refreshScholarship();
		notice();
	}

	protected void initRequestData() {
		super.initRequestData();
		showLoading();
		new TaskReqMyRanklist(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				MyRanklistResult data = getBeans()
						.getMyRanklistData();
				if (data.getWeekNo() > 0)
					txtWeekRanlist.setText(data.getWeekNo().toString());
				if (data.getNo() > 0)
					txtAllRanlist.setText(data.getNo().toString());
				txtWeekScore.setText(data.getWeekScore().toString());
				txtAllScore.setText(data.getScore().toString());
				
				getBeans().getLoginData().getInfoData().setMoney2(data.getMoney());
				
				txtCredits.setText(!TextUtils.isEmpty(data.getMoney().toString()) ? data
						.getMoney().toString() : "0");
				txtKey.setText(!TextUtils.isEmpty(data.getKeyNum().toString()) ? data
						.getKeyNum().toString() : "0");
				
				refreshScholarship();

				ll0.setVisibility(View.VISIBLE);
				AlphaAnimation animation = new AlphaAnimation(0f, 1f);
				animation.setDuration(500);
				ll0.startAnimation(animation);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.ll1:
			AtySystemNotice.startAty(AtyAccountCenter.this);
			break;
		case R.id.ll2:
			AtySettings.startAty(AtyAccountCenter.this);
			break;
		case R.id.ll3:
			if (getBeans().getLoginData().getInfoData()
					.isVisitor()) {
				Toast.makeText(AtyAccountCenter.this,
						R.string.bindingAccountErrTip, Toast.LENGTH_SHORT)
						.show();
				AtyBindingAccount.startAty(AtyAccountCenter.this);
				return;
			}
			AtyBindingInfos.startAty(AtyAccountCenter.this);
			break;
		case R.id.ll4:
			showQuitDialog();
			break;
		case R.id.txtProfile:
		case R.id.imgHead:
		case R.id.txtName:
			AtyUserInfo.startAty(AtyAccountCenter.this);
			break;
		case R.id.txt1:
		case R.id.img1:
		case R.id.txtWeekRanlist:
		case R.id.txt2:
		case R.id.img2:
		case R.id.txtWeekScore:
			AtyRanklist.startAty(this, 1);
			break;
		case R.id.txt4:
		case R.id.img4:
		case R.id.txtAllScore:
		case R.id.txt3:
		case R.id.img3:
		case R.id.txtAllRanlist:
			AtyRanklist.startAty(this, 4);
			break;

		case R.id.txtScholarshipBtn:
			AtyRewardsConfirm.startAty(this);
		}
	}

	public void notice() {
		boolean flag = getBeans().getMsgDatas()
				.isSystemMsg();
//		imgPoint1.setImageResource(flag ? R.drawable.icon_ac5_p
//				: R.drawable.icon_ac5);
		if (flag || getBeans().getMsgDatas().getGiftMsg() > 0) {
			imgPoint1.setImageResource(R.drawable.icon_ac5_p);
		} else {
			imgPoint1.setImageResource(R.drawable.icon_ac5);
		}
	}

	private DialogConfirmFragment quitDialog;

	private void showQuitDialog() {
		if (null == quitDialog) {
			quitDialog = new DialogConfirmFragment(new CallBackDialogConfirm() {

				@Override
				public void onSubmit(int position) {
					hideDialog();
					new TaskReqLogout(0,
							new CallbackNoticeView<Void, Boolean>() {

								@Override
								public void refreshView(int tag, Boolean result) {
									switch (GlobalReqDatas.getInstance()
											.getRequestUIDType()) {
									case EntityUser.QQ:
										mTencent = Tencent.createInstance(ConfigConstants.QQAPP_ID,getApplicationContext());
										mTencent.logout(AtyAccountCenter.this);
//										ShareManager.getInstance().logoutQQ(
//												AtyAccountCenter.this,
//												callbackLogin);
										ouitToLogin();
										break;
									case EntityUser.WX:
										ouitToLogin();
//										ShareManager.getInstance().logoutWX(
//												AtyAccountCenter.this,
//												callbackLogin);
										break;
									default:
										setResult(AtyAccountCenter.CODE_QUIT);
										getBeans().resetReadCache();
										startActivity(new Intent(
												AtyAccountCenter.this,
												AtyLogin.class));
										finish();
										break;
									}
								}

								@Override
								public void onProgressUpdate(int tag,
										Void[] values) {
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

			}, getString(R.string.menuTip1En), getString(R.string.menuTip1Cn),
					0, R.style.DialogBottomToTopTheme);
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
		AtyLogin.startAty(AtyAccountCenter.this);
		setResult(AtyAccountCenter.CODE_QUIT);
		startActivity(new Intent(AtyAccountCenter.this, AtyLogin.class));
		finish();
	}

//	private CallBackLogin callbackLogin = new CallBackLogin() {
//
//		@Override
//		public void onError() {
//			AtyLogin.startAty(AtyAccountCenter.this);
//			setResult(AtyAccountCenter.CODE_QUIT);
//			startActivity(new Intent(AtyAccountCenter.this, AtyLogin.class));
//			finish();
//		}
//
//		@Override
//		public void onComplete() {
//			AtyLogin.startAty(AtyAccountCenter.this);
//			setResult(AtyAccountCenter.CODE_QUIT);
//			startActivity(new Intent(AtyAccountCenter.this, AtyLogin.class));
//			finish();
//		}
//
//	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		notice();
		switch (requestCode) {
		case AtyUserInfo.REQUEST_USER_INFO:
			refreshData();
			break;
		case AtyRewardsConfirm.REQUEST_REWARDS_CONFIRM:
			refreshScholarship();
			break;
		}
	}

	private void refreshScholarship() {
		// 个人奖学金
		double scholarship = getBeans().getLoginData().getInfoData().getMoney();
		txtScholarship.setText("￥" + scholarship);
//		txtScholarshipBtn.setVisibility(scholarship >= 100 ? View.VISIBLE
//				: View.GONE);
	}

	/************************************************************************************************/

	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyAccountCenter.class);
		context.startActivityForResult(i, REQUEST_ACCOUNT_CENTER);
	}

}
