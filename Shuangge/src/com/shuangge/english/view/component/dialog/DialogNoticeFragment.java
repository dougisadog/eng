package com.shuangge.english.view.component.dialog;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.shuangge.english.config.ConfigConstants;

public class DialogNoticeFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onClose(int position);
		
	}
	
	private CallBackDialogConfirm callback;
	private ImageView btnClose;
	private int position;
	private WebView noticeView;
	
	public DialogNoticeFragment() {
		super();
	}
	
	public DialogNoticeFragment(CallBackDialogConfirm callback) {
		super();
		this.callback = callback;
		setCancelable(true);
		
    	int style = DialogFragment1.STYLE_NO_TITLE; 
    	setStyle(style, R.style.DialogAllScreenTheme);
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		if (isAdded()) {
			return;
		}
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_notice, null);// 得到加载view
		btnClose = (ImageView) v.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(this);
		noticeView = (WebView) v.findViewById(R.id.webView1);
		ConfigConstants.NOTICE_VERSION = 1; 
		webNotice();
		return v;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		int gravity = Gravity.CENTER;
		if (getTheme() == R.style.DialogTopToBottomTheme) {
			gravity = Gravity.TOP;
		}
		else if (getTheme() == R.style.DialogBottomToTopTheme)  {
			gravity = Gravity.BOTTOM;
		}
		dialog.getWindow().setGravity(Gravity.LEFT | gravity);
		return dialog;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.btnClose:
			callback.onClose(position);
			break;
		}
	}
	
	private void webNotice() { 
		 //得到webview设置  
        WebSettings webSettings = noticeView.getSettings();    
        //允许使用javascript  
        webSettings.setJavaScriptEnabled(false); 
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        noticeView.setWebViewClient(new WebViewClient ());
        try { 
        	noticeView.loadUrl(ConfigConstants.RES_NOTICE_URL);
 	    }
        catch (Exception ex) { 
 	    	ex.printStackTrace(); 
 	    } 
//		//加上下面这段代码可以使网页中的链接不以浏览器的方式打开  
//        noticeView.setWebViewClient(new WebViewClient () {
//			
//			@Override 
//            public void onLoadResource(WebView view, String url) { 
//                super.onLoadResource(view, url); 
//            } 
//   
//            @Override 
//            public boolean shouldOverrideUrlLoading(WebView webview, String url) { 
//            	try { 
//         	    	webview.loadUrl(url);
//         	    } 
//            	catch (Exception ex) { 
//         	    	ex.printStackTrace(); 
//         	    } 
//                return true; 
//            } 
//   
//            @Override 
//            public void onPageStarted(WebView view, String url, Bitmap favicon) { 
//            } 
//   
//            @Override 
//            public void onPageFinished(WebView view, String url) { 
//            } 
//   
//            @Override 
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { 
////                Toast.makeText(getApplicationContext(), "", 
////                        Toast.LENGTH_LONG).show(); 
//            } 
//		});  
       
    } 
}
