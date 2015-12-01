package com.shuangge.english.view.shop;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import air.com.shuangge.phone.ShuangEnglish.R;
import air.com.shuangge.phone.ShuangEnglish.wxapi.WXPayEntryActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.entity.server.shop.CashPayData;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.OrderData;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.entity.server.shop.WXOrderResult;
import com.shuangge.english.entity.server.shop.ZfbPayResult;
import com.shuangge.english.network.shop.TaskReqOrderBuyCash;
import com.shuangge.english.network.shop.TaskReqOrderBuyScore;
import com.shuangge.english.network.shop.TaskReqOrderBuyZfb;
import com.shuangge.english.network.shop.TaskReqPayDataBuyWx;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.pay.MD5;
import com.shuangge.english.support.utils.pay.SignUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.component.dialog.DialogAlertFragment.CallBackDialogConfirm;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AtyShopOrderPay extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_SHOP_ORDER_PAY = 1085;
	
	private static int WX_PAY = 0;
	private static int ZFB_PAY = 1;
	public static final Integer SUCCESS_STATUS = 0;
	public static final Integer UNCHECKED_STATUS = 1;
	public static final Integer FAIL_STATUS = 2;
	
	private ImageView imgView;
	private TextView txtGoodsName, txtGoodsPrice, txtAddress, txtPayValue, txtOrderNo, txtTip, txtCashPay;
	private FrameLayout flZfbPay, flWxPay, flScorePay;
	private LinearLayout llCashPay, flCashPay;
	private ImageButton btnBack;
	private AddressData address;
	private GoodsData goods;
	private List<String> addresses;
	private double price;
	private int payType;
	private static OrderData data;
	private DialogAlertFragment successDialog;
	private Integer code;
	StringBuffer sb;
	
	public static boolean paying = false; //防止 点击多次点击
	//微信支付变量
	PayReq req ;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(AtyShopOrderPay.this, null);
	
	
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	
	private int amount;

	//支付宝支付 Handler,里面有支付的返回码
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				ZfbPayResult zfbPayResult = new ZfbPayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = zfbPayResult.getResult();
				String resultStatus = zfbPayResult.getResultStatus();
				code = Integer.valueOf(resultStatus);

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					requestPayData(code);
					Toast.makeText(AtyShopOrderPay.this, "支付成功", Toast.LENGTH_SHORT).show();
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				} else if (TextUtils.equals(resultStatus, "8000")) {
					requestPayData(code);
					Toast.makeText(AtyShopOrderPay.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					Toast.makeText(AtyShopOrderPay.this, "支付失败", Toast.LENGTH_SHORT).show();
				}
				paying = false;
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(AtyShopOrderPay.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_shop_order_pay);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtGoodsName = (TextView) findViewById(R.id.txtGoodsName);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtGoodsPrice = (TextView) findViewById(R.id.txtGoodsPrice);
		txtPayValue = (TextView) findViewById(R.id.txtPayValue);
		txtOrderNo = (TextView) findViewById(R.id.txtOrderNo);
		txtTip = (TextView) findViewById(R.id.txtTip);
		txtCashPay = (TextView) findViewById(R.id.txtCashPay);
		flZfbPay = (FrameLayout) findViewById(R.id.flZfbPay);
		flWxPay = (FrameLayout) findViewById(R.id.flWxPay);
		flScorePay = (FrameLayout) findViewById(R.id.flScorePay);
		flCashPay = (LinearLayout) findViewById(R.id.flCashPay);
		llCashPay = (LinearLayout) findViewById(R.id.llCashPay);
		
		//微信支付 实例化变量
		sb = new StringBuffer();
		msgApi.registerApp(ConfigConstants.WX_APP_ID);
		
		flZfbPay.setOnClickListener(this);
		flWxPay.setOnClickListener(this);
		flScorePay.setOnClickListener(this);
		flCashPay.setOnClickListener(this);
		flCashPay.setVisibility(View.GONE);
		/*//以下是微信支付返回的errCode, 写到这个地方可能不对
		String errCode = getIntent().getStringExtra("errCode");*/
		paying = false;
		if (data != null) {
			if (!TextUtils.isEmpty(data.getGoodsName())) {
				txtGoodsName.setText(data.getGoodsName().toString());
			}
			if (data.getPayType() == 1) {
//				txtGoodsPrice.setText(String.valueOf(data.getShopCartDatas().get(0).getPrice()*data.getShopCartDatas().get(0).getAmount())+ "积分");
//				txtPayValue.setText(String.valueOf(data.getShopCartDatas().get(0).getPrice()*data.getShopCartDatas().get(0).getAmount()) + "积分");
				txtGoodsPrice.setText(String.valueOf(data.getCost())+ "金币");
				txtPayValue.setText(String.valueOf(data.getCost()) + "金币");
				flScorePay.setVisibility(View.VISIBLE);
				llCashPay.setVisibility(View.GONE);
				txtTip.setVisibility(View.GONE);
			} else {
//				txtGoodsPrice.setText(String.valueOf(data.getShopCartDatas().get(0).getPrice()*data.getShopCartDatas().get(0).getAmount())+ "元");
//				txtPayValue.setText(String.valueOf(data.getShopCartDatas().get(0).getPrice()*data.getShopCartDatas().get(0).getAmount()) + "元");
				txtGoodsPrice.setText(String.valueOf(data.getCost())+ "元");
				txtPayValue.setText(String.valueOf(data.getCost()) + "元");
				llCashPay.setVisibility(View.VISIBLE);
				flScorePay.setVisibility(View.GONE);
			}
			//cashPayType  ： 0代表不可用账户余额付款， 1代表可以
			if (data.isSupportedDeposit()) {
				flCashPay.setVisibility(View.VISIBLE);
				txtCashPay.setText("奖学金余额：￥ " + getBeans().getLoginData().getInfoData().getMoney().toString());
				if (data.getCost() < getBeans().getLoginData().getInfoData().getMoney()) {
					flCashPay.setBackgroundResource(R.drawable.icon_yellow);
					flCashPay.setClickable(true);
				} else {
					flCashPay.setBackgroundResource(R.drawable.bg_gray);
					flCashPay.setClickable(false);
				}
			}
			amount = data.getAmount();
			txtOrderNo.setText(data.getOrderNo().toString());
			if (data.getAddresses() != null && !data.getVirtualGoods()) {
				txtAddress.setText(data.getAddresses().get(3)+ "  " + data.getAddresses().get(4) + "\n" + data.getAddresses().get(0) + data.getAddresses().get(1) + data.getAddresses().get(2));
			}
			
		}
	}
	
	private void requestPayData(int code) {
		showLoading();
		List<String> orderNos = new ArrayList<String>();
		orderNos.add(txtOrderNo.getText().toString());
		List<Integer> codes = new ArrayList<Integer>();
		codes.add(code);
		new TaskReqOrderBuyZfb(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null != result && result) {
					List<CashPayData> cashPayDatas = GlobalRes.getInstance().getBeans().getPayResult().getCashPayDatas();
					if (cashPayDatas != null && cashPayDatas.size() > 0 && cashPayDatas.get(0).getCode() == SUCCESS_STATUS) {
//						if (cashPayDatas.get(0).getFunc() != 0) {
//							
//						}
						requestLessonData(cashPayDatas.get(0).getFunc());
//						GlobalRes.getInstance().getBeans().setCurrentOrderId(txtOrderNo.getText().toString());
//						AtyShopOrderDetails.startAty(AtyShopOrderPay.this);
//						finish();
						return;
					}
				}
				successDialog = new DialogAlertFragment(callback2, "服务器连接失败，请重试", "", 0);
				successDialog.showDialog(getSupportFragmentManager());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, orderNos, codes);
	}
	
	private void requestLessonData(int func) {
//		showLoading();
//		new TaskReqObtainLesson(0, new CallbackNoticeView<Void, Boolean>() {
//
//			@Override
//			public void refreshView(int tag, Boolean result) {
//				hideLoading();
//				if (null == result || !result) {
//					return;
//				}
//			}
//
//			@Override
//			public void onProgressUpdate(int tag, Void[] values) {
//				
//			}
//		});
		if (func == 100) {
			int num = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getKeyNum() + amount;
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setKeyNum(num);
			GlobalRes.getInstance().getBeans().setCurrentOrderId(txtOrderNo.getText().toString());
			AtyShopOrderDetails.startAty(AtyShopOrderPay.this);
			finish();
		}
		if (func == 0 || func == 10000) {
			GlobalRes.getInstance().getBeans().setCurrentOrderId(txtOrderNo.getText().toString());
			AtyShopOrderDetails.startAty(AtyShopOrderPay.this);
			finish();
		}
		
		GoodsFunc goodsFunc = new GoodsFunc(func, new GoodsFunc.CallBackFunc() {
			
			@Override
			public void onCallBack() {
				// TODO Auto-generated method stub
				GlobalRes.getInstance().getBeans().setCurrentOrderId(txtOrderNo.getText().toString());
				AtyShopOrderDetails.startAty(AtyShopOrderPay.this);
				finish();
			}
		
		});
		
	}
	
	private void requestDataByScore() {
		showLoading();
		new TaskReqOrderBuyScore(0, new CallbackNoticeView<Void, OrderSimpleResult>() {

			@Override
			public void refreshView(int tag, OrderSimpleResult result) {
				hideLoading();
				if (null == result)
					return;
				Toast.makeText(AtyShopOrderPay.this, R.string.shopOrderTip6, Toast.LENGTH_SHORT).show();
				GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setMoney2(result.getMoney());
				requestLessonData(data.getFunc());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, txtOrderNo.getText().toString());
	}
	
	private void requestDataByCash() {
		showLoading();
		new TaskReqOrderBuyCash(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
//					successDialog = new DialogAlertFragment(callback2, "服务器验证失败，点击确定重新验证", "", 0);
//					successDialog.showDialog(getSupportFragmentManager());
					return;
				}
				Toast.makeText(AtyShopOrderPay.this, R.string.shopOrderTip6, Toast.LENGTH_SHORT).show();
				
				
//				if (GlobalRes.getInstance().getBeans().getPayOrderResult().getOrderData().getFunc() == 0) {
//					GlobalRes.getInstance().getBeans().setCurrentOrderId(txtOrderNo.getText().toString());
//					AtyShopOrderDetails.startAty(AtyShopOrderPay.this);
//					finish();
//				} else {
//				}
				GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setMoney(GlobalRes.getInstance().getBeans().getCashOrderResult().getRewards());
				requestLessonData(GlobalRes.getInstance().getBeans().getCashOrderResult().getDto().getFunc());
				
				
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, txtOrderNo.getText().toString());
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.flWxPay:
				if(paying)
					return;
				
				WXPayEntryActivity.setWXPayData(data.getOrderNo(), data.getAmount());
				
				paying = true;
				payType = WX_PAY;
				//组装数据,并向微信服务器发送请求
				assembleDataAndSendDataToWXServer();
//				requestData();				
				break;
			case R.id.flZfbPay:
				if(paying)
					return;
				paying = true;
				payType = ZFB_PAY;
				
				assembleDataAndSendDataToZFBServer();
//				requestData();  
				break;
			case R.id.flScorePay:
				requestDataByScore();
				break;
			case R.id.flCashPay:
				requestDataByCash();
				break;
			case R.id.btnBack:
				this.finish();
		}
		
	}
	/**
	 * 组装数据,并向 支付宝 发送数据
	 */
	private void assembleDataAndSendDataToZFBServer() {
		// 订单
		String orderInfo = "";
		String goodsName = data.getGoodsName();
		String cost = String.valueOf(data.getCost());
		if( !(TextUtils.isEmpty(goodsName) && TextUtils.isEmpty(cost))) {
			orderInfo = getOrderInfo(goodsName, goodsName, cost, txtOrderNo.getText().toString());	
		}
		//String orderInfo = getOrderInfo("大象", "哈哈哈哈", "0.01");  出现Ali59的错误,是因为传递数据出现了问题,从上面获取到正常的数据 就可以了.
		//Log.i(TAG,"getOutTradeNo :"+ getOutTradeNo().toString());
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(AtyShopOrderPay.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static void startAty(Activity context, OrderData data) {
		AtyShopOrderPay.data = data;
		Intent i = new Intent(context, AtyShopOrderPay.class);
		context.startActivityForResult(i, REQUEST_SHOP_ORDER_PAY);
	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
//			showLoading();
			if (code != null) {
				requestPayData(code);
			}
			successDialog.dismiss();
			successDialog = null;
		}
		
		@Override
		public void onKeyBack() {
			successDialog.dismiss();
			successDialog = null;
		}
		
	};
	
	//↓↓↓↓↓↓↓↓↓ 此部分是   微信支付   功能方法   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*************************
	
	/**
	 * doInBackground: 发请求 -> 公司服务器 , refreshView: 发请求 -> 微信服务器   
	 */
	private void assembleDataAndSendDataToWXServer() {
		showLoading();
		
		new TaskReqPayDataBuyWx(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;	
				}
				//发送请求给微信服务器
				SendReqToWXServer();	
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {	
			}
			
		},  data.getOrderNo());  //订单号
		
	}
	
	
	/**
	 * 发请求  -> 微信服务器
	 */
	private void SendReqToWXServer() {
		req = new PayReq();
		
		WXOrderResult payDataWx = GlobalRes.getInstance().getBeans().getOrderResult();
		req.appId = payDataWx.getWxAppId();
		req.partnerId = payDataWx.getWxPartnerId();
		req.prepayId = payDataWx.getPrepay_id();
		req.packageValue = "Sign=WXPay";
		req.nonceStr = payDataWx.getNonce_str();
		req.timeStamp = payDataWx.getTimeStamp();
		
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");
		
		if (null != req){
			msgApi.registerApp(ConfigConstants.WX_APP_ID);
			msgApi.sendReq(req);
//			finish();
//			researchAty();
		}

		// show.setText(sb.toString());

		//Log.e("orion", signParams.toString());
	}
	
	/**
	 * 生成微信支付数据的签名方法 ,是  genPayReq() 方法里面的子方法
	 * @param params
	 * @return
	 */
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(ConfigConstants.WX_PAY_KEY);
//		sb.append(ConfigConstants.WX_API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion", appSign);
		return appSign;
		
	}
	
	// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 以上是   微信 支付   功能方法  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑**********************
	
	//--------------------------------------------------------------------------------------------------------------------------------
	
	// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 此部分是   支付宝 支付   功能方法   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓**********************
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price, String orderNo) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + ConfigConstants.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + ConfigConstants.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";	
		
		// 服务器异步通知页面路径    经过了截取路径的操作
		String url = ConfigConstants.SERVER_URL;
		orderInfo += "&notify_url=" + "\"" + url +ConfigConstants.ALI_NOTIFY_URL
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 *//*
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}*/

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, ConfigConstants.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	//↑↑↑↑↑↑↑↑↑↑  支付宝 支付 功能方法 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑**********

}
