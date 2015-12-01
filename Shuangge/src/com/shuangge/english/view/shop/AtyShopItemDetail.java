package com.shuangge.english.view.shop;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.network.shop.TaskReqCreateOrder;
import com.shuangge.english.network.shop.TaskReqGoodsDetail;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;

public class AtyShopItemDetail extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_SHOP_ITEM_DETAIL = 1079;
	private static final String PARAM_GOOD_ID = "1";
	private static final String PARAM_PAY_TYPE = "2";
	private static int TYPE_VIRTUAL = 1;
	private static int TYPE_REAL = 0;
	
	private ImageView imgView, imgType;
	private TextView txtName, txtSalesTime, txtIntroduce, txtPoints, txtMoney, txtCredits, txtBuy;
	private LinearLayout llOutOfStock, llAlready, llIsHave, llBuyDetail, llOff;
	private FrameLayout flBuy, flMoney, flCredit, flGive;
	private WebView webView1;
	private ImageButton btnBack;
	private GoodsData _data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_shop_item_detail);
//		imgView = (ImageView) findViewById(R.id.imgView);
//		imgType = (ImageView) findViewById(R.id.imgType);
//		txtName = (TextView) findViewById(R.id.txtName);
//		txtSalesTime = (TextView) findViewById(R.id.txtSalesTime);
//		txtIntroduce = (TextView) findViewById(R.id.txtIntroduce);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		webView1 = (WebView) findViewById(R.id.webView1);
		txtPoints = (TextView) findViewById(R.id.txtPoints);
		txtMoney = (TextView) findViewById(R.id.txtMoney);
		txtCredits = (TextView) findViewById(R.id.txtCredit);
		txtBuy = (TextView) findViewById(R.id.txtBuy);
		llBuyDetail = (LinearLayout) findViewById(R.id.llBuyDetail);
		llOutOfStock = (LinearLayout) findViewById(R.id.llOutOfStock);
		llAlready = (LinearLayout) findViewById(R.id.llAlready);
		llIsHave = (LinearLayout) findViewById(R.id.llIsHave);
		llOff = (LinearLayout) findViewById(R.id.llOff);
		flMoney = (FrameLayout) findViewById(R.id.flMoney);
		flCredit = (FrameLayout) findViewById(R.id.flCredit);
		flBuy = (FrameLayout) findViewById(R.id.flBuy);
		flGive = (FrameLayout) findViewById(R.id.flGive);
		flBuy.setOnClickListener(this);
		flGive.setVisibility(View.GONE);
		flGive.setOnClickListener(this);
		flBuy.setVisibility(View.GONE);
		flMoney.setVisibility(View.GONE);
		llBuyDetail.setVisibility(View.GONE);
		flCredit.setVisibility(View.GONE);
		llIsHave.setVisibility(View.GONE);
		llOff.setVisibility(View.GONE);
		webView1.setVisibility(View.GONE);
		refreshData();
	}
	
	protected void refreshData() {
		showLoading();
		new TaskReqGoodsDetail(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					Toast.makeText(AtyShopItemDetail.this, "访问服务器失败,请返回重新访问", Toast.LENGTH_SHORT).show();
					return;
				}
				GoodsData goods = GlobalRes.getInstance().getBeans().getGoodsResult().getGoodsData();
				itemInfo(goods);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, getIntent().getIntExtra(PARAM_GOOD_ID, -1));
	}
	
	private void itemInfo(final GoodsData data) { 
		if (data == null) {
			return;
		}
		_data = data;
//		if (GlobalRes.getInstance().getBeans().getPayType() == 0) {
//			txtMoney.setText(String.valueOf(data.getPrice()));
//			flMoney.setVisibility(View.VISIBLE);
//			flBuy.setVisibility(View.VISIBLE);
//			
//		} else {
//			txtCredits.setText(String.valueOf(data.getScorePrice()));
//			flCredit.setVisibility(View.VISIBLE);
//			flBuy.setVisibility(View.VISIBLE);
//		}
//		if (data.getGift() == 0) {
//			if (data.getLimitCount() == data.getTakenCount()||data.getLimitCount() < data.getTakenCount()) {
//				llBuyDetail.setVisibility(View.VISIBLE);
//				llOutOfStock.setVisibility(View.GONE);
//				llIsHave.setVisibility(View.VISIBLE);
//				flGive.setVisibility(View.GONE);
//				flBuy.setVisibility(View.GONE);
//			} else {
//				llBuyDetail.setVisibility(View.VISIBLE);
//				flGive.setVisibility(View.GONE);
//				flBuy.setVisibility(View.VISIBLE);
//				llOutOfStock.setVisibility(View.GONE);
//				llIsHave.setVisibility(View.GONE);
//			}
//		} else {
//			flGive.setVisibility(View.VISIBLE);
//			flBuy.setVisibility(View.VISIBLE);
//			llBuyDetail.setVisibility(View.VISIBLE);
//			llOutOfStock.setVisibility(View.GONE);
//			llIsHave.setVisibility(View.GONE);
//		}
//		
//		if (data.getStock() == 0) {
//			llBuyDetail.setVisibility(View.GONE);
//			llOutOfStock.setVisibility(View.VISIBLE);
//			llIsHave.setVisibility(View.GONE);
//		}
//		
//		if (data.getState() == data.OFF) {
//			llBuyDetail.setVisibility(View.GONE);
//			llOutOfStock.setVisibility(View.GONE);
//			llIsHave.setVisibility(View.GONE);
//			llOff.setVisibility(View.VISIBLE);
//		}
		
		
		
//		String url = ConfigConstants.SERVER_URL.substring(0, ConfigConstants.SERVER_URL.length()-4) + "goods/details/" + data.getGoodsID() + "-" + GlobalRes.getInstance().getBeans().getPayType();
		String url = ConfigConstants.SERVER_URL + "/goods/details/" + getIntent().getIntExtra(PARAM_GOOD_ID, -1) + "-" + getIntent().getIntExtra(PARAM_PAY_TYPE, -1);
	 //得到webview设置  
       WebSettings webSettings = webView1.getSettings();    
       //允许使用javascript  
       webSettings.setJavaScriptEnabled(true); 
       webSettings.setAppCacheEnabled(true);
       webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
       webSettings.setAppCachePath(url);
       webView1.getSettings().setBlockNetworkImage(true); 
       webView1.setWebViewClient(new WebViewClient ());
       try { 
       		webView1.loadUrl(url);
	    } catch (Exception ex) { 
	    
	    	ex.printStackTrace(); 
	    
	    } 
       
//		       webSettings.setBuiltInZoomControls(true);
//		       webSettings.setSupportZoom(true);
//		       webSettings.setUseWideViewPort(true);
//		       webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
       
//		       webView1.setWebViewClient(new WebViewClient ());
//		       String customHtml = data.getDetails();
//		       webView1.getSettings().setDefaultTextEncodingName("UTF-8");
//		       webView1.loadDataWithBaseURL(null, customHtml, "text/html", "utf-8", null);
       
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
        	   webView1.getSettings().setBlockNetworkImage(false);
        	   webView1.setVisibility(View.VISIBLE);
        	   AlphaAnimation alphaAnimation1 = new AlphaAnimation(0, 1);
        	   alphaAnimation1.setFillAfter(true);
        	   alphaAnimation1.setDuration(2000);
        	   webView1.startAnimation(alphaAnimation1);
        	   
        	   alphaAnimation1.setAnimationListener(new AnimationListener() { 
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
			
					@Override
					public void onAnimationEnd(Animation animation) {
//			        	   
		        	   flBuy.setVisibility(View.VISIBLE);
		        	   if (GlobalRes.getInstance().getBeans().getPayType() == 0) {
			       			txtMoney.setText(String.valueOf(data.getPrice()));
			       			flMoney.setVisibility(View.VISIBLE);
			       			txtBuy.setText("购买");
//				       			flBuy.setVisibility(View.VISIBLE);
		       			
			       		} else {
			       			txtCredits.setText(String.valueOf(data.getScorePrice()));
			       			flCredit.setVisibility(View.VISIBLE);
			       			txtBuy.setText("兑换");
//				       			flBuy.setVisibility(View.VISIBLE);
			       		}
			       		if (data.getGift() == 0) {
			       			if (data.getLimitCount() == data.getTakenCount()||data.getLimitCount() < data.getTakenCount()) {
//				       				llBuyDetail.setVisibility(View.VISIBLE);
			       				llOutOfStock.setVisibility(View.GONE);
			       				llIsHave.setVisibility(View.VISIBLE);
			       				flGive.setVisibility(View.GONE);
			       				flBuy.setVisibility(View.GONE);
			       			} else {
//				       				llBuyDetail.setVisibility(View.VISIBLE);
			       				flGive.setVisibility(View.GONE);
//				       				flBuy.setVisibility(View.VISIBLE);
			       				llOutOfStock.setVisibility(View.GONE);
			       				llIsHave.setVisibility(View.GONE);
			       			}
			       		} else {
			       			flGive.setVisibility(View.VISIBLE);
//				       			flBuy.setVisibility(View.VISIBLE);
//				       			llBuyDetail.setVisibility(View.VISIBLE);
			       			llOutOfStock.setVisibility(View.GONE);
			       			llIsHave.setVisibility(View.GONE);
			       		}
			       		llBuyDetail.setVisibility(View.VISIBLE);
			       		if (data.getStock() == 0) {
			       			llBuyDetail.setVisibility(View.GONE);
			       			llOutOfStock.setVisibility(View.VISIBLE);
			       			llIsHave.setVisibility(View.GONE);
			       		}
			       		
			       		if (data.getState() == data.OFF) {
			       			llBuyDetail.setVisibility(View.GONE);
			       			llOutOfStock.setVisibility(View.GONE);
			       			llIsHave.setVisibility(View.GONE);
			       			llOff.setVisibility(View.VISIBLE);
			       		}
					}
			
					@Override
					public void onAnimationRepeat(Animation animation) {
					}  
				});

           } 
  
           @Override 
           public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { 
               hideLoading();
           } 
		});  
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		refreshData();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView1.stopLoading();
		webView1.removeAllViews();
		webView1.destroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				this.finish();
				break;
			
			case R.id.flBuy:
				GlobalRes.getInstance().getBeans().setGive(false);
				if (_data.getIsCode() == 1) {
					showLoading();
					new TaskReqCreateOrder(0, new CallbackNoticeView<Void, OrderSimpleResult>() {

						@Override
						public void refreshView(int tag, OrderSimpleResult result) {
							hideLoading();
							if (null == result) 
								return;
							AtyShopOrderPay.startAty(AtyShopItemDetail.this, result.getOrderData());
							finish();
						}

						@Override
						public void onProgressUpdate(int tag, Void[] values) {
							
						}
					}, 1, _data.getGoodsId(), null, TYPE_VIRTUAL);
					
					return;
				} else if (_data.getFunc() > 0 && _data.getFunc() < 100) {
					showLoading();
					new TaskReqCreateOrder(0, new CallbackNoticeView<Void, OrderSimpleResult>() {

						@Override
						public void refreshView(int tag, OrderSimpleResult result) {
							hideLoading();
							if (null == result) 
								return;
							AtyShopOrderPay.startAty(AtyShopItemDetail.this, result.getOrderData());
							finish();
						}

						@Override
						public void onProgressUpdate(int tag, Void[] values) {
							
						}
					}, 1, _data.getGoodsId(), null, TYPE_REAL);
					return;
				}
				
				AtyShopConfirmOrder.startAty(AtyShopItemDetail.this);
				break;
				
			case R.id.flGive:
				GlobalRes.getInstance().getBeans().setGive(true);
				AtyShopConfirmOrder.startAty(AtyShopItemDetail.this);
				break;
		}
		
	}
	
	public static void startAty(Activity context, Integer goodId, int payType) {
		Intent i = new Intent(context, AtyShopItemDetail.class);
		i.putExtra(PARAM_GOOD_ID, goodId);
		i.putExtra(PARAM_PAY_TYPE, payType);
		context.startActivityForResult(i, REQUEST_SHOP_ITEM_DETAIL);
	}

}
