package com.shuangge.english.view.lesson;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.lesson.SummaryData;
import com.shuangge.english.network.lesson.TaskReqShare;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgDeleteAll;
import com.shuangge.english.network.share.TaskReqShareResult;
import com.shuangge.english.support.app.ShareContentWebPage;
import com.shuangge.english.support.app.ShareManager.ShareConstants;
import com.shuangge.english.support.app.ShareManager1;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.RatingBarView;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.component.dialog.DialogAlertFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogShareFragment;
import com.shuangge.english.view.component.dialog.DialogShareFragment.CallBackDialogShare;
import com.shuangge.english.view.secretmsg.AtySecretMsgList;
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
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 课程列表
 * @author Jeffrey
 *
 */
public class AtySuccessResult extends AbstractAppActivity implements OnClickListener {

	private TextView txtShare,txtBack, txtAgain, txtNext, txtAllScore, txtRightRate, txtBaseScore, txtExtraScore, txtFisrtFinishedScore, txtFisrtPerfectScore;
	private RatingBarView rbStar;
	
	private SummaryData summaryData;
	
	private Long shareId;
	
	private DialogAlertFragment shareSuccessDialog;
	
	private Tencent mTencent;
	
//	private final UMSocialService mController = UMServiceFactory.getUMSocialService(ShareConstants.DESCRIPTOR);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		summaryData = null;
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_lesson_success_result);
		txtBack = (TextView) findViewById(R.id.txtBack);
		txtBack.setOnClickListener(this);
		
		txtAgain = (TextView) findViewById(R.id.txtAgain);
		txtAgain.setOnClickListener(this);
		txtNext = (TextView) findViewById(R.id.txtNext);
		txtNext.setOnClickListener(this);
		
		rbStar = (RatingBarView) findViewById(R.id.rbStar);
		rbStar.setStar(0);
		
		txtAllScore = (TextView) findViewById(R.id.txtAllScore);
		txtRightRate = (TextView) findViewById(R.id.txtRightRate);
		txtBaseScore = (TextView) findViewById(R.id.txtBaseScore);
		txtExtraScore = (TextView) findViewById(R.id.txtExtraScore);
		txtFisrtFinishedScore = (TextView) findViewById(R.id.txtFisrtFinishedScore);
		txtFisrtPerfectScore = (TextView) findViewById(R.id.txtFisrtPerfectScore);
		txtShare = (TextView) findViewById(R.id.txtShare);
		txtShare.setOnClickListener(this); 
		summaryData = GlobalRes.getInstance().getBeans().getPassLessonDatas().getSummaryData();
		if (summaryData == null) {
			return;
		}
		txtAllScore.setText(summaryData.getBaseScore() + summaryData.getExtraScore() + summaryData.getFisrtFinishedScore() + summaryData.getFisrtPerfectScore() + "");
		txtRightRate.setText(summaryData.getRightRate() + "%");
		txtBaseScore.setText(summaryData.getBaseScore() + "");
		txtExtraScore.setText(summaryData.getExtraScore() + "");
		txtFisrtFinishedScore.setText(summaryData.getFisrtFinishedScore() + "");
		txtFisrtPerfectScore.setText(summaryData.getFisrtPerfectScore() + "");
//		txtShare.setText(ConfigConstants.SHARE_BTN);
		
		if (null != summaryData.getRightRate()) {
			if (summaryData.getRightRate() < 60) {
				rbStar.setStar(1);
			}
			else if (summaryData.getRightRate() < 100) {
				rbStar.setStar(2);
			}
			else {
				rbStar.setStar(3);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtBack:
			setResult(0);
			finish();
			break;
		case R.id.txtAgain:
			setResult(1);
			finish();
			break;
		case R.id.txtNext:
			if (TextUtils.isEmpty(summaryData.getNextClientId())) {
				setResult(0);
			}
			else {
				GlobalRes.getInstance().getBeans().setCurrentType5Id(summaryData.getNextClientId());
				GlobalRes.getInstance().getBeans().setLastType5Name(summaryData.getNextName());
				setResult(2);
			}
			finish();
			break;
		case R.id.txtShare:
			if (null != shareId) {
				showShareDialog();
				return;
			}
			showLoading();
			new TaskReqShareResult(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					shareId = GlobalRes.getInstance().getBeans().getShareResult().getShareResultNo(); 
					showShareDialog();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
			});
			
			break;
		}
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
//		circleContent.setShareContent(ConfigConstants.SHARE_CONTENT2);
//		circleContent.setTitle(ConfigConstants.SHARE_TITLE2);
//        circleContent.setTargetUrl(ConfigConstants.SHARE_URL2 + "/" + shareId);
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
		ShareContentWebPage wxContent = new ShareContentWebPage(ConfigConstants.SHARE_TITLE2, 
        		ConfigConstants.SHARE_CONTENT2, 
        		ConfigConstants.SHARE_URL2 + "/" + shareId,
        		ConfigConstants.SHARE_IMG2);
        
        ShareManager1.getInstance(this).shareByWeixin(wxContent, 1);
	}
	public void wx(){
//		UMImage urlImage1 = new UMImage(this, ConfigConstants.SHARE_IMG2);
//		WeiXinShareContent weixinContent = new WeiXinShareContent();
//        weixinContent.setShareContent(ConfigConstants.SHARE_CONTENT2);
//        weixinContent.setTitle(ConfigConstants.SHARE_TITLE2);
//        weixinContent.setTargetUrl(ConfigConstants.SHARE_URL2 + "/" + shareId);
//        weixinContent.setShareMedia(urlImage1);
//        mController.setShareMedia(weixinContent);
//        mController.getConfig().closeToast();
//        showLoading(true);
//        mController.postShare(this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {
//						@Override
//						public void onStart() {
//							hideLoading();
////						    Toast.makeText(AtyClassInvite.this, "开始分享.", Toast.LENGTH_SHORT).show();
//						}
//						
//						@Override
//						public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
//							mController.getConfig().cleanListeners();
//							hideLoading();
//							if (eCode == 200) {
////								Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
//								shareSuccess();
//								return;
//							}
//							String eMsg = "";
//							if (eCode == -101){
//								eMsg = "没有授权";
//							}
////							Toast.makeText(AtyClassInvite.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//						}
//				});
		ShareContentWebPage wxContent = new ShareContentWebPage(ConfigConstants.SHARE_TITLE2, 
        		ConfigConstants.SHARE_CONTENT2, 
        		ConfigConstants.SHARE_URL2 + "/" + shareId,
        		ConfigConstants.SHARE_IMG2);
        
        ShareManager1.getInstance(this).shareByWeixin(wxContent, 0);
	}
	
	public void qq(){
//		UMImage urlImage3 = new UMImage(this, ConfigConstants.SHARE_IMG2);
//		QQShareContent qqShareContent = new QQShareContent();
//		qqShareContent.setShareContent(ConfigConstants.SHARE_CONTENT2);
//		qqShareContent.setTitle(ConfigConstants.SHARE_TITLE2);
//		qqShareContent.setShareImage(urlImage3);
//		qqShareContent.setTargetUrl(ConfigConstants.SHARE_URL2 + "/" + shareId);
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
	    params.putString(QQShare.SHARE_TO_QQ_TITLE, ConfigConstants.SHARE_TITLE2);
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, ConfigConstants.SHARE_CONTENT2);
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ConfigConstants.SHARE_URL2 + "/" + shareId);
	    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ConfigConstants.SHARE_IMG2);
	    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "爽哥英语");
	    mTencent.shareToQQ(AtySuccessResult.this, params, new BaseUiListener());
		
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
	
	private void shareSuccess() {
		showLoading();
		new TaskReqShare(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result || !isActive) {
					return;
				}
				int shareScore = GlobalRes.getInstance().getBeans().getShareResult().getShareScore();
				showShareSuccessDialog(shareScore);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
		}, shareId);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != mTencent) 
			mTencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
	};
	
	private void showShareSuccessDialog(int shareScore) {
		String str = "恭喜您分享成功，获得" + shareScore + "个积分";
		shareSuccessDialog = new DialogAlertFragment(callback2, str, "", 0);
		shareSuccessDialog.showDialog(getSupportFragmentManager());
	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
//			showLoading();
			if(shareSuccessDialog != null) {
				shareSuccessDialog.dismiss();
				shareSuccessDialog = null;
			}
		}
		
		@Override
		public void onKeyBack() {
			if(shareSuccessDialog != null) {
				shareSuccessDialog.dismiss();
				shareSuccessDialog = null;
			}
		}
		
	};

}
