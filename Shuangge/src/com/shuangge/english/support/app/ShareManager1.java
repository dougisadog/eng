package com.shuangge.english.support.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.support.utils.pay.Util;
import com.shuangge.english.view.login.AtyLogin;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class ShareManager1 {
	
	/**
     * 文字
     */
    public static final int WEIXIN_SHARE_WAY_TEXT = 1;
    /**
     * 图片
     */
    public static final int WEIXIN_SHARE_WAY_PIC = 2;
    /**
     * 链接
     */
	public static final int WEIXIN_SHARE_WAY_WEBPAGE = 3;
	
	/**
     * 朋友圈
     */
    public static final int WEIXIN_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline;
	
    private IWXAPI wxApi;
    private Context context;
    
    private ShareManager1(Context context){
        this.context = context;
        
        initWxShare(context);
        
    }
	
    private static ShareManager1 instance = null;

	public static ShareManager1 getInstance(Context context) {
		if (null == instance) {
			instance = new ShareManager1(context);
		}
		return instance;
	}
	
	private void initWxShare(Context context) {
		wxApi = WXAPIFactory.createWXAPI(context, ConfigConstants.WX_APP_ID, true);
		if( !wxApi.isWXAppInstalled()){
		     Toast.makeText(context, "请先安装微信应用", Toast.LENGTH_SHORT).show();
		     return;
		}
        wxApi.registerApp(ConfigConstants.WX_APP_ID);
	}
	
	/**
     * 通过微信分享
     * @param shareWay 分享的方式（文本1、图片2、链接3）
     * @param shareType 分享的类型（朋友圈1，会话0）
     */
    public void shareByWeixin(final ShareContentWebPage shareContent, final int shareType){
        switch (shareContent.getShareWay()) {
        case WEIXIN_SHARE_WAY_TEXT:
            break;
        case WEIXIN_SHARE_WAY_PIC:
            break;
        case WEIXIN_SHARE_WAY_WEBPAGE:
//            shareWebPage(shareType, shareContent);
            new Thread(){
            	public void run(){
            		shareWebPage(shareType, shareContent);
            	}
            }.start();
            break;
        }
    }
    
    /*
     * 分享链接
     */
    private void shareWebPage(final int shareType, final ShareContentWebPage shareContent) {
    	//http://res.happyge.com:8081/client/content1_1/share_result.png,"http://res.happyge.com:8081/recommend-app/icon/liwushuo.png"
        GlobalRes.getInstance().loadBitmap(shareContent.getPicUrl(), new GlobalRes.DownloadBitampListener() {
			
			@Override
			public void progressHandler(long progress, long max) {
				
			}
			
			@Override
			public void errorHandler(Exception e) {
			}
			
			@Override
			public void completeHandler(Bitmap bitmap) {
				
				WXWebpageObject webpage = new WXWebpageObject();
		        webpage.webpageUrl = shareContent.getURL();
		        WXMediaMessage msg = new WXMediaMessage(webpage);
		        msg.title = shareContent.getTitle();
		        msg.description = shareContent.getContent();
		        if(bitmap == null){
		            Toast.makeText(context, "图片不能为空", Toast.LENGTH_SHORT).show();
		        }else{
//		            msg.thumbData = Util.bmpToByteArray(thumb, true);
		        	msg.setThumbImage(bitmap);
		        }
		         
		        SendMessageToWX.Req req = new SendMessageToWX.Req();
		        req.transaction = buildTransaction("webpage");
		        req.message = msg;
		        req.scene = shareType;
		        wxApi.sendReq(req);
			}
		});
        
    }
    
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
	
//	private abstract class ShareContent{
//        protected abstract int getShareWay();
//        protected abstract String getContent();
//        protected abstract String getTitle();
//        protected abstract String getURL();
//        protected abstract String getPicUrl();
//        protected abstract void setContent(String content);
//        protected abstract void setTitle(String title);
//        protected abstract void setURL(String url);
//        protected abstract void setPicUrl(String url);
//    }
//	
//	/**
//     * 设置分享链接的内容
//     * @author Administrator
//     *
//     */
//    public class ShareContentWebpage extends ShareContent{
//        private String title;
//        private String content;
//        private String url;
//        private String picUrl;
//        public ShareContentWebpage() {
//        	
//        }
//        public ShareContentWebpage(String title, String content, 
//                String url, String picUrl){
//            this.title = title;
//            this.content = content;
//            this.url = url;
//            this.picUrl = picUrl;
//        }
// 
//        @Override
//        protected String getContent() {
//            return content;
//        }
// 
//        @Override
//        protected String getTitle() {
//            return title;
//        }
// 
//        @Override
//        protected String getURL() {
//            return url;
//        }
// 
//        @Override
//        protected String getPicUrl() {
//            return picUrl;
//        }
// 
//        @Override
//        protected int getShareWay() {
//            return WEIXIN_SHARE_WAY_WEBPAGE;
//        }
//		@Override
//		protected void setContent(String content) {
//			this.content = content;
//		}
//		@Override
//		protected void setTitle(String title) {
//			this.title = title;
//		}
//		@Override
//		protected void setURL(String url) {
//			this.url = url;
//		}
//		@Override
//		protected void setPicUrl(String url) {
//			this.picUrl = picUrl;
//		}
//         
//    }
	
}
