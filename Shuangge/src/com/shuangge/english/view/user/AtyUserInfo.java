package com.shuangge.english.view.user;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.entity.server.user.MyRanklistResult;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.photograph.AtyPhotoBrowser;
import com.shuangge.english.view.rewards.AtyRewardsConfirm;

/**
 * 
 * @author Jeffrey
 * 
 */
public class AtyUserInfo extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_USER_INFO = 1024;

	private ImageButton btnBack;
	private ImageView btnEdit;
	private CircleImageView imgHead;
	private TextView txtName, txtSignature, txtLocation, txtClassName,
			txtOccupation, txtIncome, txtInterest, txtWx, txtScholarship,
			moreBtn, txtScholarshipBtn, txtWeekRanlist, txtAllRanlist,
			txtWeekScore, txtAllScore, txtCoins;
	private LinearLayout llMoney;

	private ImageView imgAddress;

	private ArrayList<String> urls;

	private double scholarship;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_userinfo);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnEdit = (ImageView) findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(this);
		btnEdit.setVisibility(View.VISIBLE);
		moreBtn = (TextView) findViewById(R.id.moreBtn);
		moreBtn.setVisibility(View.GONE);
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
		txtCoins = (TextView) findViewById(R.id.userInfoTip8);
		txtWx = (TextView) findViewById(R.id.txtWx);

		txtScholarship = (TextView) findViewById(R.id.userInfoTip7);
		txtWeekRanlist = (TextView) findViewById(R.id.txtWeekRanlist);
		txtAllRanlist = (TextView) findViewById(R.id.txtAllRanlist);
		txtWeekScore = (TextView) findViewById(R.id.txtWeekScore);
		txtAllScore = (TextView) findViewById(R.id.txtAllScore);
		
		findViewById(R.id.rlMoney).setOnClickListener(this);
		findViewById(R.id.rlMoney).setVisibility(View.VISIBLE);
		findViewById(R.id.llMoney).setVisibility(View.VISIBLE);
		findViewById(R.id.vLine).setVisibility(View.VISIBLE);
		refreshData();
	}
	
	protected void refreshData() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		InfoData data = beans.getLoginData().getInfoData();
		List<ClassData> classDatas = null;
		if (null != beans.getMyGroupDatas()) {
			classDatas = beans.getMyGroupDatas().getClassInfos();
		}
		txtName.setText(data.getName());
		txtSignature.setText(!TextUtils.isEmpty(data.getSignature()) ? data
				.getSignature() : "");
		if (!TextUtils.isEmpty(data.getLocation())) {
			imgAddress.setVisibility(View.VISIBLE);
		}
		txtLocation.setText(!TextUtils.isEmpty(data.getLocation()) ? data
				.getLocation() : "");
		if (null != classDatas)
			txtClassName.setText(classDatas.size() > 0 ? classDatas.get(0)
					.getName() : "");
		txtOccupation.setText(!TextUtils.isEmpty(data.getOccupation()) ? data
				.getOccupation() : "");
		txtIncome.setText(!TextUtils.isEmpty(data.getIncome()) ? data
				.getIncome() : "");
		txtInterest.setText(!TextUtils.isEmpty(data.getInterest()) ? data
				.getInterest() : "");
		txtWx.setText(!TextUtils.isEmpty(data.getWechatNo()) ? data
				.getWechatNo() : "未绑定");
		
		txtCoins.setText(!TextUtils.isEmpty(data.getMoney2().toString()) ? data
				.getMoney2().toString() : "0");
		
		MyRanklistResult rankData = beans.getMyRanklistData();
		
		if (null != rankData) {
			if (null != rankData.getWeekNo() && rankData.getWeekNo() > 0)
				txtWeekRanlist.setText(rankData.getWeekNo().toString());
			if (null != rankData.getNo() && rankData.getNo() > 0)
				txtAllRanlist.setText(rankData.getNo().toString());
			txtWeekScore.setText(rankData.getWeekScore().toString());
			txtAllScore.setText(rankData.getScore().toString());
			txtCoins.setText(!TextUtils.isEmpty(rankData.getMoney().toString()) ? rankData
					.getMoney().toString() : "0");
			data.setMoney2(rankData.getMoney());
		}
		else {
			findViewById(R.id.ll0).setVisibility(View.GONE);
		}

		refreshScholarship();
		if (!TextUtils.isEmpty(data.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(
					new DisplayBitmapParam(data.getHeadUrl(), imgHead));
		}

		if (null != data.getPhotoUrls() && data.getPhotoUrls().size() > 0) {
			urls = (ArrayList<String>) data.getPhotoUrls();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnEdit:
			AtyUserInfoEdit.startAty(this);
			break;
		case R.id.imgHead:
			if (null != urls) {
				AtyPhotoBrowser.startAty(this, 0, urls);
			}
			break;
		case R.id.rlMoney:
			if (null != urls) {
				AtyRewardsConfirm.startAty(AtyUserInfo.this);
			}
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case AtyUserInfoEdit.REQUEST_USER_INFO_EDIT:
			refreshData();
			break;
		case AtyRewardsConfirm.REQUEST_REWARDS_CONFIRM:
			refreshScholarship();
			break;
		}
	}

	private void refreshScholarship() {
		// 获取个人 奖学金
		scholarship = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getMoney();
		txtScholarship.setText("￥" + scholarship);
//		findViewById(R.id.txtScholarshipBtn).setVisibility(scholarship >= 100 ? View.VISIBLE: View.GONE);

	}

	/************************************************************************************************/

	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyUserInfo.class);
		context.startActivityForResult(i, REQUEST_USER_INFO);
	}

	public static void startAty(Context context) {
		Intent i = new Intent(context, AtyUserInfo.class);
		context.startActivity(i);
	}

}
