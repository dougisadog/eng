package com.shuangge.english.support.app;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.debug.DebugPrinter;
//import com.umeng.socialize.bean.HandlerRequestCode;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.bean.SocializeEntity;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
//import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
//import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
//import com.umeng.socialize.exception.SocializeException;
//import com.umeng.socialize.sso.QZoneSsoHandler;
//import com.umeng.socialize.sso.UMQQSsoHandler;
//import com.umeng.socialize.weixin.controller.UMWXHandler;

public class ShareManager {
	
	public class ShareConstants {
		
		public static final String DESCRIPTOR = "air.com.shuangge.phone.ShuangEnglish.share";//"com.umeng.share";
		
	}
	
	private static ShareManager instance = null;

	public static ShareManager getInstance() {
		if (null == instance) {
			instance = new ShareManager();
		}
		return instance;
	}
    
//    public void init(Activity activity) {
//    	//微信 朋友圈
//    	addWXPlatform(activity);
//    	addQQPlatform(activity);
//    }
//    
//    public void addWXPlatform(Context context) {
//        String appId = "wxab78122b71fe5483";
//        String appSecret = "eb28dd2c943e8b5953c29c55363a3ece";
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
//        wxHandler.addToSocialSDK();
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//    }
//    
//    public void addQQPlatform(Activity activity) {
//    	String appId = "1103428370";
//        String appKey = "I8bHbNY1EfcyTdA5";
//    	UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, appId, appKey);
//    	qqSsoHandler.addToSocialSDK();
//    	
//    	QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, appId, appKey);
//    	qZoneSsoHandler.addToSocialSDK();
//    }
//    
//    /*****************************************************************************************************************/
//    /**
//    /**  login
//    /**
//    /*****************************************************************************************************************/
//    
//    public static interface CallBackLogin {
//		
//		public void onComplete();
//		
//		public void onError();
//		
//	}
//    
//    public void loginWX(final Context context, final CallBackLogin callback) {
//		login(SHARE_MEDIA.WEIXIN, context, callback);
//	}
//	
//	public void logoutWX(final Context context, final CallBackLogin callback) {
//		logout(SHARE_MEDIA.WEIXIN, context, callback);
//	}
//	
//	public void loginQQ(final Context context, final CallBackLogin callback) {
//		login(SHARE_MEDIA.QQ, context, callback);
//	}
//	
//	public void logoutQQ(final Context context, final CallBackLogin callback) {
//		logout(SHARE_MEDIA.QQ, context, callback);
//	}
//	
//	/*****************************************************************************************************************/
//	
//	private void login(final SHARE_MEDIA sm, final Context context, final CallBackLogin callback) {
//		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
////		if (OauthHelper.isAuthenticatedAndTokenNotExpired(context, sm)) {
////			getPlatformInfo(mController, sm, context, callback);
////			return;
////		}
//		try {
//			if (sm == SHARE_MEDIA.WEIXIN && !mController.getConfig().getSsoHandler(HandlerRequestCode.WX_REQUEST_CODE).isClientInstalled()) {
//				callback.onError();
//				return;
//			}
//		}
//		catch (Exception e) {
//			callback.onError();
//		}
//		mController.doOauthVerify(context, sm, new UMAuthListener() {
//			
//					@Override
//					public void onStart(SHARE_MEDIA platform) {
////						Toast.makeText(context, "授权开始", Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onError(SocializeException e, SHARE_MEDIA platform) {
////						Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT).show();
//						callback.onError();
//					}
//
//					@Override
//					public void onComplete(Bundle value, SHARE_MEDIA platform) {
////						Toast.makeText(context, "授权完成", Toast.LENGTH_SHORT).show();
//						// 获取相关授权信息
//						String uid = value.getString("uid");
//						getPlatformInfo(mController, sm, context, callback, uid);
//					}
//
//					@Override
//					public void onCancel(SHARE_MEDIA platform) {
//						callback.onError();
////						Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
//					}
//				});
//
//	}
//	
//	private void logout(final SHARE_MEDIA sm, final Context context, final CallBackLogin callback) {
//		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
//		mController.deleteOauth(context, sm, new SocializeClientListener() {
//			
//					@Override
//					public void onStart() {
//					}
//	
//					@Override
//					public void onComplete(int status, SocializeEntity entity) {
//						if (status == 200) {
////							Toast.makeText(context, "删除成功.", Toast.LENGTH_SHORT).show();
//							callback.onComplete();
//						} 
//						else {
////							Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
//							callback.onError();
//						}
//					}
//					
//				});
//	}
//	
//	private void getPlatformInfo(UMSocialService mController, final SHARE_MEDIA sm, final Context context, final CallBackLogin callback, final String uid) {
//		mController.getPlatformInfo(context, sm, new UMDataListener() {
//			
//			@Override
//			public void onStart() {
////				Toast.makeText(context, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onComplete(int status, Map<String, Object> info) {
//				if (status == 200 && info != null) {
////					StringBuilder sb = new StringBuilder("uid = " + uid);
////					Set<String> keys = info.keySet();
////					for (String key : keys) {
////						sb.append(key + "=" + info.get(key).toString() + "\r\n");
////					}
//					Object sexObj = null;
//					GlobalReqDatas.getInstance().setRequestUID(uid);
//					if (sm == SHARE_MEDIA.QQ) {
//						GlobalReqDatas.getInstance().setRequestName(info.get("screen_name").toString());
//						GlobalReqDatas.getInstance().setRequestImgHeadUrl(info.get("profile_image_url").toString());
//						sexObj = info.get("sex");
//						if (null != sexObj) {
//							try {
//								GlobalReqDatas.getInstance().setRequestSex(Integer.parseInt(sexObj.toString()));
//							}
//							catch (Exception e) {
//							}
//						}
//						GlobalReqDatas.getInstance().setRequestUIDType(EntityUser.QQ);
//					}
//					else {
//						GlobalReqDatas.getInstance().setRequestName(info.get("nickname").toString());
//						GlobalReqDatas.getInstance().setRequestImgHeadUrl(info.get("headimgurl").toString());
//						sexObj = info.get("gender");
//						Integer sex = null;
//						if ("男".equals(sexObj)) {
//							sex = 1;
//						}
//						else if ("女".equals(sexObj)) {
//							sex = 2;
//						}
//						GlobalReqDatas.getInstance().setRequestSex(sex);
//						GlobalReqDatas.getInstance().setRequestUIDType(EntityUser.WX);
//					}
//					//wx nickname headimgurl sex 1男
//					//qq screen_name profile_image_url gender 男
//					
////					Toast.makeText(context, sb, Toast.LENGTH_LONG).show();
////					DebugPrinter.d(sb.toString());
//					callback.onComplete();
//				} 
//				else {
//					callback.onError();
////					Toast.makeText(context, "发生错误：" + status, Toast.LENGTH_LONG).show();
//					DebugPrinter.e("发生错误：" + status);
//				}
//			}
//		});
//	}
}
