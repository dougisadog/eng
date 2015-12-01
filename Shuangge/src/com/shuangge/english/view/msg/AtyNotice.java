package com.shuangge.english.view.msg;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.support.utils.ClipboardUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.tencent.tauth.Tencent;
import android.graphics.Bitmap;
/**
 * 
 * @author Jeffrey
 *
 */
public class AtyNotice extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_NOTICE = 1072;

	private ImageView btnClose;
	private WebView noticeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_notice);
		btnClose = (ImageView) findViewById(R.id.btnClose);
		btnClose.setOnClickListener(this);
		noticeView = (WebView)findViewById(R.id.webView1);
		ConfigConstants.NOTICE_VERSION = 1; 
		webNotice();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnClose:
			this.finish();
			break;
		}
	}
	private void webNotice() { 
		String _url = getString(R.string.faqUrl);//ConfigConstants.FAQ_URL;
		 //得到webview设置  
        WebSettings webSettings = noticeView.getSettings();    
        //允许使用javascript  
        webSettings.setJavaScriptEnabled(true); 
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        noticeView.setWebViewClient(new WebViewClient ());
        try { 
        	noticeView.loadUrl(_url);
 	    } catch (Exception ex) { 
 	    
 	    	ex.printStackTrace(); 
 	    
 	    } 
		//加上下面这段代码可以使网页中的链接不以浏览器的方式打开  
        noticeView.setWebViewClient(new WebViewClient () {
			
			@Override 
            public void onLoadResource(WebView view, String url) { 
                super.onLoadResource(view, url); 
            } 
   
            @Override 
            public boolean shouldOverrideUrlLoading(WebView webview, String url) { 
            	 try { 
         	    	webview.loadUrl(url);
         	    } catch (Exception ex) { 
         	    
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
//                Toast.makeText(getApplicationContext(), "", 
//                        Toast.LENGTH_LONG).show(); 
            } 
		});  
       
    } 
	
	
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyNotice.class);
		context.startActivityForResult(i, REQUEST_NOTICE);
	}

}
