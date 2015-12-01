package com.shuangge.english.view.group;

import java.util.ArrayList;

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

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.group.TaskReqClassApply;
import com.shuangge.english.network.user.TaskReqOtherDataInfo;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.dialog.DialogWithContentFragment;
import com.shuangge.english.view.component.dialog.DialogWithContentFragment.CallBackDialogWithContent;
import com.shuangge.english.view.component.photograph.AtyPhotoBrowser;
import com.shuangge.english.view.user.AtyBrowseUserInfo;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassInviteDetails extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_CLASS_INVITE_DETAILS = 1029;
	
	public static final String PARAM_INDEX = "param index";
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private TextView txtBtn;
	private CircleImageView imgHead;
	private TextView txtName, txtSignature, txtLocation, txtClassName, txtOccupation, txtIncome, txtInterest,txtWx;
	private LinearLayout llBg;
	private ImageView imgAddress;
	
	private ArrayList<String> urls;
	
	private DialogWithContentFragment classApplyDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_class_invite_details);
		
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
		
		requestData();
	}
	
	private void requestData() {
		GlobalRes.getInstance().getBeans().setCurrentUserNo(getIntent().getLongExtra(PARAM_INDEX, -1));
		new TaskReqOtherDataInfo(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
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
				if (!TextUtils.isEmpty(data.getLocation())) {
					imgAddress.setVisibility(View.VISIBLE);
				}
				txtLocation.setText(!TextUtils.isEmpty(data.getLocation()) ? data.getLocation() : "");
				txtClassName.setText(data.getClassNames().size() > 0 ? data.getClassNames().get(0) : "");
				txtOccupation.setText(!TextUtils.isEmpty(data.getOccupation()) ? data.getOccupation() : "");
				txtIncome.setText(!TextUtils.isEmpty(data.getIncome()) ? data.getIncome() : "");
				txtInterest.setText(!TextUtils.isEmpty(data.getInterest()) ? data.getInterest() : "");
				txtWx.setText(!TextUtils.isEmpty(data.getWechatNo()) ? data.getWechatNo() : "未绑定");
				txtWx.setText(!TextUtils.isEmpty(data.getWechatNo()) ? data.getWechatNo() + " (点击可复制)" : "未绑定");
				txtWx.setOnClickListener(AtyClassInviteDetails.this);
				if (!TextUtils.isEmpty(data.getWechatNo()))
					txtWx.setTag(data.getWechatNo());
				if (!TextUtils.isEmpty(data.getHeadUrl())) {
					GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(data.getHeadUrl(), imgHead));
				}
				
				if (null != data.getPhotoUrls() && data.getPhotoUrls().size() > 0) {
					urls = (ArrayList<String>) data.getPhotoUrls();
				}
				
				txtBtn.setVisibility(data.getClassNos().size() == 0 ? View.VISIBLE : View.GONE);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
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
			case R.id.txtBtn:
				if (null != classApplyDialog && classApplyDialog.isVisible()) {
					return;
				}
				classApplyDialog = new DialogWithContentFragment(new CallBackDialogWithContent() {
					
					@Override
					public void notice(String msg, final String wechatNo) {
						if (requesting) {
							return;
						}
						requesting = true;
						classApplyDialog.dismiss();
						classApplyDialog = null;
						showLoading();
						new TaskReqClassApply(0, new CallbackNoticeView<Void, Boolean>() {

							@Override
							public void refreshView(int tag, Boolean result) {
								requesting = false;
								hideLoading();
								if (null != result && result) {
									Toast.makeText(AtyClassInviteDetails.this, R.string.applySuccessTip, Toast.LENGTH_SHORT).show();
									CacheBeans beans = GlobalRes.getInstance().getBeans();
									if (!TextUtils.isEmpty(wechatNo) && TextUtils.isEmpty(beans.getMyGroupDatas().getClassInfos().get(0).getWechatNo())
											&& beans.getMyGroupDatas().getMyClassAuth() > ClassData.NORMAL) {
										beans.getMyGroupDatas().getClassInfos().get(0).setWechatNo(wechatNo);
									}
								}
							}

							@Override
							public void onProgressUpdate(int tag, Void[] values) {
							}
						
						}, msg, wechatNo);
					}
				}, getString(R.string.classApplyTitle), getString(R.string.classApplyTip));
				classApplyDialog.showDialog(getSupportFragmentManager());
				break;
			default:
				break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	/************************************************************************************************/
	
	public static void startAty(Activity context, Long userNo) {
		Intent i = new Intent(context, AtyClassInviteDetails.class);
		i.putExtra(AtyClassInviteDetails.PARAM_INDEX, userNo);
		context.startActivityForResult(i, REQUEST_CLASS_INVITE_DETAILS);
	}
	
}
