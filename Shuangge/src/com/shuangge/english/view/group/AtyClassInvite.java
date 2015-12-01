package com.shuangge.english.view.group;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.support.app.ShareContentWebPage;
import com.shuangge.english.support.app.ShareManager.ShareConstants;
import com.shuangge.english.support.app.ShareManager1;
import com.shuangge.english.support.utils.ClipboardUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
//import com.umeng.socialize.media.QQShareContent;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.weixin.media.CircleShareContent;
//import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.tencent.tauth.UiError;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassInvite extends AbstractAppActivity implements OnClickListener {

public static final int REQUEST_CLASS_INVITE = 1007;
	
//	private final UMSocialService mController = UMServiceFactory.getUMSocialService(ShareConstants.DESCRIPTOR);
	private Tencent mTencent;
	private ImageButton btnBack;
	private RelativeLayout rlInviteSGUser, rlInviteQQUser, rlInviteWXUser, rlInviteXCircle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_class_invite);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		rlInviteSGUser = (RelativeLayout) findViewById(R.id.rlInviteSGUser);
		rlInviteSGUser.setOnClickListener(this);
		
		rlInviteQQUser = (RelativeLayout) findViewById(R.id.rlInviteQQUser);
		rlInviteQQUser.setOnClickListener(this);
		rlInviteWXUser = (RelativeLayout) findViewById(R.id.rlInviteWXUser);
		rlInviteWXUser.setOnClickListener(this);
		rlInviteXCircle = (RelativeLayout) findViewById(R.id.rlInviteXCircle);
		rlInviteXCircle.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.rlInviteSGUser:
			startActivity(new Intent(AtyClassInvite.this, AtyClassInviteList.class));
			break;
		case R.id.txtWx:
			if (null != v.getTag()) {
				ClipboardUtils.copy(v.getTag().toString());
				Toast.makeText(this, R.string.copyWechat, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.rlInviteWXUser:
//			UMImage urlImage1 = new UMImage(this, ConfigConstants.SHARE_IMG);
//			WeiXinShareContent weixinContent = new WeiXinShareContent();
//	        weixinContent.setShareContent(ConfigConstants.SHARE_CONTENT);
//	        weixinContent.setTitle(ConfigConstants.SHARE_TITLE);
//	        weixinContent.setTargetUrl(ConfigConstants.SHARE_URL);
//	        weixinContent.setShareMedia(urlImage1);
//	        mController.setShareMedia(weixinContent);
//	        mController.getConfig().closeToast();
//	        showLoading(true);
//	        mController.postShare(this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {
//							@Override
//							public void onStart() {
//								hideLoading();
////							    Toast.makeText(AtyClassInvite.this, "开始分享.", Toast.LENGTH_SHORT).show();
//							}
//							
//							@Override
//							public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
//								mController.getConfig().cleanListeners();
//								hideLoading();
//								if (eCode == 200) {
////									Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
//									return;
//								}
//								String eMsg = "";
//								if (eCode == -101){
//									eMsg = "没有授权";
//								}
////								Toast.makeText(AtyClassInvite.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//							}
//					});
			ShareContentWebPage wxContent = new ShareContentWebPage(ConfigConstants.SHARE_TITLE, 
	        		ConfigConstants.SHARE_CONTENT, 
	        		ConfigConstants.SHARE_URL,
	        		ConfigConstants.SHARE_IMG);
	        
	        ShareManager1.getInstance(this).shareByWeixin(wxContent, 0);
			break;
		case R.id.rlInviteXCircle:
//			UMImage urlImage2 = new UMImage(this, ConfigConstants.SHARE_IMG);
//			CircleShareContent circleContent = new CircleShareContent();
//			circleContent.setShareContent(ConfigConstants.SHARE_CONTENT);
//			circleContent.setTitle(ConfigConstants.SHARE_TITLE);
//	        circleContent.setTargetUrl(ConfigConstants.SHARE_URL);
//	        circleContent.setShareMedia(urlImage2);
//	        mController.setShareMedia(circleContent);
//	        mController.getConfig().closeToast();
//	        showLoading(true);
//	        mController.postShare(this, SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {
//	        	
//				@Override
//				public void onStart() {
//					hideLoading();
////				    Toast.makeText(AtyClassInvite.this, "开始分享.", Toast.LENGTH_SHORT).show();
//				}
//				
//				@Override
//				public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
//					mController.getConfig().cleanListeners();
//					hideLoading();
//					if (eCode == 200) {
////						Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
//						return;
//					}
//					String eMsg = "";
//					if (eCode == -101){
//						eMsg = "没有授权";
//					}
////					Toast.makeText(AtyClassInvite.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//				}
//	        });
			ShareContentWebPage wxContent1 = new ShareContentWebPage(ConfigConstants.SHARE_TITLE, 
	        		ConfigConstants.SHARE_CONTENT, 
	        		ConfigConstants.SHARE_URL,
	        		ConfigConstants.SHARE_IMG);
	        
	        ShareManager1.getInstance(this).shareByWeixin(wxContent1, 1);
			break;
		case R.id.rlInviteQQUser:
//			UMImage urlImage3 = new UMImage(this, ConfigConstants.SHARE_IMG);
//			QQShareContent qqShareContent = new QQShareContent();
//			qqShareContent.setShareContent(ConfigConstants.SHARE_CONTENT);
//			qqShareContent.setTitle(ConfigConstants.SHARE_TITLE);
//			qqShareContent.setShareImage(urlImage3);
//			qqShareContent.setTargetUrl(ConfigConstants.SHARE_URL);
//			mController.setShareMedia(qqShareContent);
//			mController.getConfig().closeToast();
//			showLoading(true);
//			mController.postShare(this, SHARE_MEDIA.QQ, new SnsPostListener() {
//				@Override
//				public void onStart() {
//					hideLoading();
////				    Toast.makeText(AtyClassInvite.this, "开始分享.", Toast.LENGTH_SHORT).show();
//				}
//				
//				@Override
//				public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
//					mController.getConfig().cleanListeners();
//					hideLoading();
//					if (eCode == 200) {
////						Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
//						return;
//					}
//					String eMsg = "";
//					if (eCode == -101){
//						eMsg = "没有授权";
//					}
////					Toast.makeText(AtyClassInvite.this, "分享失败[" + eCode + "] " + eMsg,Toast.LENGTH_SHORT).show();
//				}
//	        });
			mTencent = Tencent.createInstance(ConfigConstants.QQAPP_ID,getApplicationContext());
			Bundle params = new Bundle();
		    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		    params.putString(QQShare.SHARE_TO_QQ_TITLE, ConfigConstants.SHARE_TITLE);
		    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, ConfigConstants.SHARE_CONTENT);
		    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, ConfigConstants.SHARE_URL);
		    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ConfigConstants.SHARE_IMG);
		    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "爽哥英语");
		    mTencent.shareToQQ(AtyClassInvite.this, params, new BaseUiListener());
			break;
		}
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
			Toast.makeText(AtyClassInvite.this, "分享成功.", Toast.LENGTH_SHORT).show();
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
//			shareSuccess();
		}
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != mTencent) 
			mTencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
	};

	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassInvite.class);
		context.startActivityForResult(i, REQUEST_CLASS_INVITE);
	}
	
}
