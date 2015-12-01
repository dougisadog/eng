package com.shuangge.english.view.user;

import java.util.ArrayList;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
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
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.attention.TaskReqAttention;
import com.shuangge.english.network.attention.TaskReqAttentionCancel;
import com.shuangge.english.network.user.TaskReqOtherDataInfo;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.ClipboardUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.photograph.AtyPhotoBrowser;
import com.shuangge.english.view.secretmsg.AtySecretDetails;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyBrowseUserInfo extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_BROWSE_USER_INFO = 1026;
	
	public static final String PARAM_INDEX = "param index";
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private TextView txtBtn,moreBtn;
	private CircleImageView imgHead;
	private TextView txtName, txtSignature, txtLocation, txtClassName, txtOccupation, txtIncome, txtInterest, txtWx, txtWeekRanlist, txtAllRanlist, txtWeekScore, txtAllScore;;
	private LinearLayout llBg;
	private ImageView imgAddress;
	private DialogUserSetting dialogUserSetting;
	
	private ArrayList<String> urls;
	
	private boolean isAttentioned = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_userinfo);
		
		llBg = (LinearLayout) findViewById(R.id.llBg);
		llBg.setVisibility(View.GONE);
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtBtn = (TextView) findViewById(R.id.txtBtn);
		txtBtn.setOnClickListener(this);
		txtBtn.setVisibility(View.GONE);
		
		imgHead = (CircleImageView) findViewById(R.id.imgHead);
		imgHead.setOnClickListener(this);
		imgAddress = (ImageView) findViewById(R.id.imgAddress);
		imgAddress.setVisibility(View.GONE);
		txtName = (TextView) findViewById(R.id.txtName);
		txtSignature = (TextView) findViewById(R.id.userInfoTip1);
		txtLocation = (TextView) findViewById(R.id.userInfoTip2);
		txtClassName = (TextView) findViewById(R.id.userInfoTip3);
		txtOccupation = (TextView) findViewById(R.id.userInfoTip4);
		txtIncome = (TextView) findViewById(R.id.userInfoTip5);
		txtInterest = (TextView) findViewById(R.id.userInfoTip6);
		txtWx = (TextView) findViewById(R.id.txtWx);
		
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
		moreBtn = (TextView) findViewById(R.id.moreBtn);
		moreBtn.setOnClickListener(this);
//		chatBtn.setVisibility(View.GONE);
		
		if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() != ClassData.MANAGER) {
			findViewById(R.id.llWx).setVisibility(View.GONE);
		}
		
		requestData();
	}
	
	private void requestData() {
		showLoading();
		GlobalRes.getInstance().getBeans().setCurrentUserNo(getIntent().getLongExtra(PARAM_INDEX, -1));
		new TaskReqOtherDataInfo(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				OtherInfoData data = GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData();
				if (null == data) {
					return;
				}
				llBg.setVisibility(View.VISIBLE);
				AlphaAnimation animation = new AlphaAnimation(0, 1);
				animation.setDuration(500);
				llBg.startAnimation(animation);
				txtName.setText(data.getName());
				txtSignature.setText(!TextUtils.isEmpty(data.getSignature()) ? data.getSignature() : "");
				txtSignature.setVisibility(!TextUtils.isEmpty(data.getSignature()) ? View.VISIBLE : View.GONE);
				if (!TextUtils.isEmpty(data.getLocation())) {
					imgAddress.setVisibility(View.VISIBLE);
				}
				txtLocation.setText(!TextUtils.isEmpty(data.getLocation()) ? data.getLocation() : "");
				txtClassName.setText(data.getClassNames().size() > 0 ? data.getClassNames().get(0) : "");
				txtOccupation.setText(!TextUtils.isEmpty(data.getOccupation()) ? data.getOccupation() : "");
				txtIncome.setText(!TextUtils.isEmpty(data.getIncome()) ? data.getIncome() : "");
				txtInterest.setText(!TextUtils.isEmpty(data.getInterest()) ? data.getInterest() : "");
				txtWx.setText(!TextUtils.isEmpty(data.getWechatNo()) ? data.getWechatNo() + " (点击可复制)" : "未绑定");
				txtWx.setOnClickListener(AtyBrowseUserInfo.this);
				if (!TextUtils.isEmpty(data.getWechatNo()))
					txtWx.setTag(data.getWechatNo());
				if (!TextUtils.isEmpty(data.getHeadUrl())) {
					GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(data.getHeadUrl(), imgHead));
				}
				
				if (null != data.getPhotoUrls() && data.getPhotoUrls().size() > 0) {
					urls = (ArrayList<String>) data.getPhotoUrls();
				}
				
				if (null != data.getWeekNo() && data.getWeekNo() > 0)
					txtWeekRanlist.setText(data.getWeekNo().toString());
				if (null != data.getNo() && data.getNo() > 0)
					txtAllRanlist.setText(data.getNo().toString());
				txtWeekScore.setText(data.getWeekScore().toString());
				txtAllScore.setText(data.getScore().toString());
				
				isAttentioned = data.getAttention();
				refreshAttentionedBtn();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void refreshAttentionedBtn() {
		txtBtn.setText(isAttentioned ? R.string.attetionCancel : R.string.attetion);
		txtBtn.setVisibility(View.VISIBLE);
		txtBtn.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				this.finish();
				break;
			case R.id.imgHead:
				if (null != urls) {
					AtyPhotoBrowser.startAty(this, 0, urls);
				}
				break;
			case R.id.txtWx:
				if (null != v.getTag()) {
					ClipboardUtils.copy(v.getTag().toString());
					Toast.makeText(this, R.string.copyWechat, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.moreBtn:
//				OtherInfoData data = GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData();
//				GlobalRes.getInstance().getBeans().setCurrentFriendNo(data.getUserNo());
//				GlobalRes.getInstance().getBeans().setCurrentFriendName(data.getName());
//				AtySecretDetails.startAty(AtyBrowseUserInfo.this);
//				DialogUserSetting.startAty(AtyBrowseUserInfo.this);
				dialogUserSetting = new DialogUserSetting();
				dialogUserSetting.showDialog(getSupportFragmentManager());
				break;
			case R.id.txtBtn:
				if (requesting) {
					return;
				}
				requesting = true;
				showLoading();
				if (!isAttentioned) {
					new TaskReqAttention(0, new CallbackNoticeView<Void, Boolean>() {

						@Override
						public void refreshView(int tag, Boolean result) {
							requesting = false;
							hideLoading();
							if (null == result || !result) {
								return;
							}
							Toast.makeText(AtyBrowseUserInfo.this, R.string.attetionSuccessTip, Toast.LENGTH_SHORT).show();
							isAttentioned = true;
							refreshAttentionedBtn();
						}

						@Override
						public void onProgressUpdate(int tag, Void[] values) {
						}
						
					});
					return;
				}
				new TaskReqAttentionCancel(0, new CallbackNoticeView<Void, Boolean>() {

					@Override
					public void refreshView(int tag, Boolean result) {
						requesting = false;
						hideLoading();
						if (null == result || !result) {
							return;
						}
						Toast.makeText(AtyBrowseUserInfo.this, R.string.attetionCancelSuccessTip, Toast.LENGTH_SHORT).show();
						isAttentioned = false;
						refreshAttentionedBtn();
					}

					@Override
					public void onProgressUpdate(int tag, Void[] values) {
					}
					
				});
				return;
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Context context, Long userNo) {
		Intent i = new Intent(context, AtyBrowseUserInfo.class);
		i.putExtra(PARAM_INDEX, userNo);
		context.startActivity(i);
	}

	
}
