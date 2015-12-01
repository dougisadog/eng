package com.shuangge.english.view.about;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.view.AbstractAppActivity;
/**
 * 
 * @author Jeffrey
 *
 */
public class AtyAboutFaq extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_ABOUT_FAQ = 1071;

	private ImageButton btnBack;
	private WebView webView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_about_faq);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		webView1 = (WebView)findViewById(R.id.webView1);
		 
		webFaq();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
//		case R.id.txtQQ:
//			String appId = "1103428370";
//			String qqGroup = "101186255";
//			Tencent mTencent = Tencent.createInstance(appId, this);
//			mTencent.joinQQGroup(this, qqGroup);
//			break;
		}
	}
	private void webFaq() { 
		 //得到webview设置  
        WebSettings webSettings = webView1.getSettings();    
        //允许使用javascript  
        webSettings.setJavaScriptEnabled(false); 
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView1.setWebViewClient(new WebViewClient ());
        try { 
        	webView1.loadUrl(ConfigConstants.RES_FAQ_URL);
 	    } catch (Exception ex) { 
 	    
 	    	ex.printStackTrace(); 
 	    
 	    } 
		//加上下面这段代码可以使网页中的链接不以浏览器的方式打开  
		webView1.setWebViewClient(new WebViewClient () {
			
			@Override 
            public void onLoadResource(WebView view, String url) { 
                super.onLoadResource(view, url); 
            } 
   
            @Override 
            public boolean shouldOverrideUrlLoading(WebView webview, String url) { 
            	try { 
         	    	webview.loadUrl(url);
         	    } 
            	catch (Exception ex) { 
         	    	ex.printStackTrace(); 
         	    } 
                return true; 
            } 
   
            @Override 
            public void onPageStarted(WebView view, String url, Bitmap favicon) { 
                showLoading();   
            } 
   
            @Override 
            public void onPageFinished(WebView view, String url) { 
            	hideLoading();
            } 
   
            @Override 
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { 
                hideLoading();
            } 
		});  
       
    } 
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyAboutFaq.class);
		context.startActivityForResult(i, REQUEST_ABOUT_FAQ);
	}

}
