package com.shuangge.english.view.menu;

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

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.network.lesson.TaskReqShare;
import com.shuangge.english.network.rewards.TaskReqRewardsCode;
import com.shuangge.english.support.app.ShareContentWebPage;
import com.shuangge.english.support.app.ShareManager1;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.component.dialog.DialogAlertFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.dialog.DialogConfirmWithInviteNoFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmWithInviteNoFragment.CallBackDialogConfirmWithInviteNo;
import com.shuangge.english.view.component.dialog.DialogShareFragment;
import com.shuangge.english.view.component.dialog.DialogShareFragment.CallBackDialogShare;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
//import com.umeng.socialize.media.QQShareContent;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.weixin.media.CircleShareContent;
//import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyInvite extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_INVITE = 1091;

	private ImageButton btnBack;
	
	private Long shareId;
	private TextView txtInviteNo, txtInvite, txtInviteMsg;
	
	private DialogAlertFragment shareSuccessDialog;
	
	private DialogConfirmWithInviteNoFragment inviteNoDialog;
	private Tencent mTencent;
	private IWXAPI wxApi;
//	private final UMSocialService mController = UMServiceFactory.getUMSocialService(ShareConstants.DESCRIPTOR);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_invite);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtInviteNo = (TextView) findViewById(R.id.txtInviteNo);
		txtInvite = (TextView) findViewById(R.id.txtInvite);
		txtInviteMsg = (TextView) findViewById(R.id.txtInviteMsg);
		findViewById(R.id.txtSubmit).setOnClickListener(this);
		txtInvite.setOnClickListener(this);
		refreshData();
	}
	protected void refreshData() {
		if (!TextUtils.isEmpty(getBeans().getLoginData().getInfoData().getInvitationCode())) {
			txtInviteNo.setText(getBeans().getLoginData().getInfoData().getInvitationCode());
		}
		if (!TextUtils.isEmpty(getBeans().getLoginData().getInfoData().getOtherInvitationCode())) {
			if (getBeans().getLoginData().getInfoData().getOtherInvitationCode().equals(0)) {
				txtInvite.setVisibility(View.GONE);
				txtInviteMsg.setVisibility(View.GONE);
			}
			txtInvite.setVisibility(View.GONE);
			txtInviteMsg.setVisibility(View.VISIBLE);
			txtInviteMsg.setText("已填写邀请码：" + getBeans().getLoginData().getInfoData().getOtherInvitationCode());
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.txtSubmit:
			shareId = getBeans().getLoginData().getInfoData().getUserNo();
			if (null != shareId) {
				showShareDialog();
				return;
			}
			break;
		case R.id.txtInvite:
			showInviteNoDialog();
			break;
		}
	}
	
	private void showInviteNoDialog() {
		if (null == inviteNoDialog) {
			inviteNoDialog = new DialogConfirmWithInviteNoFragment(new CallBackDialogConfirmWithInviteNo() {
				
				@Override
				public void submit(int position, final String inviteNo) {
					new TaskReqRewardsCode(0, new CallbackNoticeView<Void, Boolean>() {

						@Override
						public void refreshView(int tag, Boolean result) {
//							hideLoading();
							if (null == result || !result) {
								return;
							}
							txtInvite.setVisibility(View.GONE);
							txtInviteMsg.setVisibility(View.VISIBLE);
							txtInviteMsg.setText("已填写邀请码：" + inviteNo);
							getBeans().getLoginData().getInfoData().setOtherInvitationCode(inviteNo);
							Toast.makeText(AtyInvite.this, "填写邀请码成功", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onProgressUpdate(int tag, Void[] values) {
						}
					}, inviteNo);
					
					inviteNoDialog.dismiss();
					inviteNoDialog = null;
					
				}
				
				@Override
				public void cancel() {
					inviteNoDialog.dismiss();
					inviteNoDialog = null;
				}
				
			}, 0);
		}
		if (inviteNoDialog.isVisible()) {
			return;
		}
		inviteNoDialog.showDialog(getSupportFragmentManager());
		
	}
	
	private DialogShareFragment shareDialog;
	
	private void showShareDialog() {
		if (null == shareDialog) {
			shareDialog = new DialogShareFragment(new CallBackDialogShare() {
				
				@Override
				public void onWxq() {
					wxq();
				}
				
				@Override
				public void onWx() {
					wx();
				}
				
				@Override
				public void onQQ() {
					qq();
					
				}
				
				@Override
				public void onKeyBack() {
				}
				
			}, 0, R.style.DialogBottomToTopTheme);
		}
		if (shareDialog.isVisible()) {
			return;
		}
		shareDialog.showDialog(getSupportFragmentManager());
	}
	
	public void wxq(){
//		UMImage urlImage2 = new UMImage(this, ConfigConstants.SHARE_IMG2);
//		CircleShareContent circleContent = new CircleShareContent();
//		circleContent.setShareContent(ConfigConstants.SHARE_CONTENT6);
//		circleContent.setTitle(ConfigConstants.SHARE_TITLE6);
//        circleContent.setTargetUrl(ConfigConstants.SHARE_URL6 + "/" + shareId);
//        circleContent.setShareMedia(urlImage2);
//        mController.setShareMedia(circleContent);
//        mController.getConfig().closeToast();
//        showLoading(true);
//        mController.postShare(this, SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {
//        	
//			@Override
//			public void onStart() {
//				hideLoading();
////			    Toast.makeText(AtyClassInvite.this, "开始分享.", Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
//				mController.getConfig().cleanListeners();
//				hideLoading();
//				if (eCode == 200) {
////					Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
//					shareSuccess();
//					return;
//				}
//				String eMsg = "";
//				if (eCode == -101){
//					eMsg = "没有授权";
//				}
////				Toast.makeText(AtyClassInvite.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//			}
//        });
		ShareContentWebPage wxContent = new ShareContentWebPage(ConfigConstants.SHARE_TITLE6, 
        		ConfigConstants.SHARE_CONTENT6, 
        		ConfigConstants.SHARE_URL6 + "/" + shareId,
        		ConfigConstants.SHARE_IMG2);
        
        ShareManager1.getInstance(this).shareByWeixin(wxContent, 1);
		
	}
	public void wx(){
//		UMImage urlImage1 = new UMImage(this, ConfigConstants.SHARE_IMG2);
//		WeiXinShareContent weixinContent = new WeiXinShareContent();
//        weixinContent.setShareContent(ConfigConstants.SHARE_CONTENT6);
//        weixinContent.setTitle(ConfigConstants.SHARE_TITLE6);
//        weixinContent.setTargetUrl(ConfigConstants.SHARE_URL6 + "/" + shareId);
//        weixinContent.setShareMedia(urlImage1);
//        mController.setShareMedia(weixinContent);
//        mController.getConfig().closeToast();
//        showLoading(true);
//        mController.postShare(this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {
//			@Override
//			public void onStart() {
//				hideLoading();
////						    Toast.makeText(AtyClassInvite.this, "开始分享.", Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
//				mController.getConfig().cleanListeners();
//				hideLoading();
//				if (eCode == 200) {
////								Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
//					shareSuccess();
//					return;
//				}
//				String eMsg = "";
//				if (eCode == -101){
//					eMsg = "没有授权";
//				}
////							Toast.makeText(AtyClassInvite.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//			}
//	});
		ShareContentWebPage wxContent = new ShareContentWebPage(ConfigConstants.SHARE_TITLE6, 
        		ConfigConstants.SHARE_CONTENT6, 
        		ConfigConstants.SHARE_URL6 + "/" + shareId,
        		ConfigConstants.SHARE_IMG2);
        
        ShareManager1.getInstance(this).shareByWeixin(wxContent, 0);
	}
	
	public void qq(){
//		UMImage urlImage3 = new UMImage(this, ConfigConstants.SHARE_IMG2);
//		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setShareContent(ConfigConstants.SHARE_CONTENT6);
//		qqShareContent.setTitle(ConfigConstants.SHARE_TITLE6);
//		qqShareContent.setShareImage(urlImage3);
//		qqShareContent.setTargetUrl(ConfigConstants.SHARE_URL6 + "/" + shareId);
//		mController.setShareMedia(qqShareContent);
//		mController.getConfig().closeToast();
//		showLoading(true);
//		mController.postShare(this, SHARE_MEDIA.QQ, new SnsPostListener() {
//			@Override
//			public void onStart() {
//				hideLoading();
////			    Toast.makeText(AtyClassInvite.this, "开始分享.", Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
//				mController.getConfig().cleanListeners();
//				hideLoading();
//				if (eCode == 200) {
////					Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
//					shareSuccess();
//					return;
//				}
//				String eMsg = "";
//				if (eCode == -101){
//					eMsg = "没有授权";
//				}
////				Toast.makeText(AtyClassInvite.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//			}
//        });
		mTencent = Tencent.createInstance(ConfigConstants.QQAPP_ID,getApplicationContext());
		Bundle params = new Bundle();
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE, ConfigConstants.SHARE_TITLE6);
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, ConfigConstants.SHARE_CONTENT6);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ConfigConstants.SHARE_URL6 + "/" + shareId);
	    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ConfigConstants.SHARE_IMG2);
	    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "爽哥英语");
	    mTencent.shareToQQ(AtyInvite.this, params, new BaseUiListener());
	}
	
	private class BaseUiListener implements IUiListener {

		@Override
		public void onCancel() {
			
		}

		@Override
		public void onError(UiError arg0) {
			
		}

		@Override
		public void onComplete(Object arg0) {
			shareSuccess();
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		BaseResp resp = getBeans().getWxResp();
		if(resp == null) {
			return;
		}
		if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
			getBeans().setWxResp(null);
			shareSuccess();
		}
	}
	
	private void shareSuccess() {
		new TaskReqShare(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
//				hideLoading();
				if (null == result || !result) {
					return;
				}
				int shareScore = getBeans().getShareResult().getShareScore();
				showShareSuccessDialog(shareScore);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
		}, shareId);
	}
	
	private void showShareSuccessDialog(int shareScore) {
		String str = "恭喜您分享成功，获得" + shareScore + "个积分";
		shareSuccessDialog = new DialogAlertFragment(callback2, str, "", 0);
		shareSuccessDialog.showDialog(getSupportFragmentManager());
	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
//			showLoading();
			shareSuccessDialog.dismiss();
			shareSuccessDialog = null;
		}
		
		@Override
		public void onKeyBack() {
			shareSuccessDialog.dismiss();
			shareSuccessDialog = null;
		}
		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refreshData();
		if (mTencent != null) {
			mTencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyInvite.class);
		context.startActivityForResult(i, REQUEST_INVITE);
	}
	
}
