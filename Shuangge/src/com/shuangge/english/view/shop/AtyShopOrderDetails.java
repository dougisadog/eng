package com.shuangge.english.view.shop;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.shop.OrderData;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.network.shop.TaskReqOrderDetail;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.ClipboardUtils;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.user.AtyBrowseUserInfo;

public class AtyShopOrderDetails extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_SHOP_CONFIRM_ORDER = 1083;
	
	private final static int UNPAID = 0;
	private final static int PAID = 1;
	private final static int SENT = 2;
	private final static int RECEUVED = 3;
	private final static int CANCELED = 99;
	private final static int TIMEOUT = -2;
	private final static int NOT_ENOUGH_STOCK = -1;  //库存不足
	private ImageView imgView;
	private TextView txtName, txtStatus1, txtAddress, txtStatus2, txtStatus3, txtStatus4, txtGoodsNum, 
	txtGoodsPrice, txtStatus5, txtStatus6, txtExpress, txtUserName, txtOrderNo, txtTime, txtExchangeNo;
	private FrameLayout btnBuy;
	private View line1;
	private LinearLayout ll1, ll3;
	private CircleImageView imgHead;
	private RelativeLayout rlView;
	private ImageButton btnBack;
	private List<String> addresses;
	
	private OrderData data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		setContentView(R.layout.aty_shop_order_details);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtName = (TextView) findViewById(R.id.txtName);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtExpress = (TextView) findViewById(R.id.txtExpress);
		txtStatus1 = (TextView) findViewById(R.id.txtStatus1);
		txtStatus2 = (TextView) findViewById(R.id.txtStatus2);
		txtStatus3 = (TextView) findViewById(R.id.txtStatus3);
		txtStatus4 = (TextView) findViewById(R.id.txtStatus4);
		txtStatus5 = (TextView) findViewById(R.id.txtStatus5);
		txtStatus6 = (TextView) findViewById(R.id.txtStatus6);
		txtGoodsNum = (TextView) findViewById(R.id.txtGoodsNum);
		txtGoodsPrice = (TextView) findViewById(R.id.txtGoodsPrice);
		txtUserName = (TextView) findViewById(R.id.txtUserName);
		txtOrderNo = (TextView) findViewById(R.id.txtOrderNo);
		txtTime = (TextView) findViewById(R.id.txtTime);
		txtExchangeNo = (TextView) findViewById(R.id.txtExchangeNo);
		btnBuy = (FrameLayout) findViewById(R.id.btnBuy);
		rlView = (RelativeLayout) findViewById(R.id.rlView);
		line1 = (View) findViewById(R.id.line1);
		imgView = (ImageView) findViewById(R.id.imgView);
		imgHead = (CircleImageView) findViewById(R.id.imgHead);
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll3.setOnClickListener(this);
		btnBuy.setOnClickListener(this);
		rlView.setOnClickListener(this);
		txtStatus1.setVisibility(View.GONE);
		btnBuy.setVisibility(View.GONE);
		txtStatus2.setVisibility(View.GONE);
		txtStatus3.setVisibility(View.GONE);
		txtStatus4.setVisibility(View.GONE);
		txtStatus5.setVisibility(View.GONE);
		txtStatus6.setVisibility(View.GONE);
		txtExpress.setVisibility(View.GONE);
		txtAddress.setVisibility(View.GONE);
		line1.setVisibility(View.GONE);
		ll1.setVisibility(View.GONE);
		ll1.setOnClickListener(this);
		ll3.setVisibility(View.GONE);
		txtTime.setVisibility(View.GONE);
	}
	
	protected void initRequestData() {
		showLoading();
		new TaskReqOrderDetail(0, new CallbackNoticeView<Void, OrderSimpleResult>() {

			@Override
			public void refreshView(int tag, OrderSimpleResult result) {
				hideLoading();
				if (null == result)
					return;
				data = result.getOrderData();
				AtyShopOrderDetails.this.refreshView();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, GlobalRes.getInstance().getBeans().getCurrentOrderId());
	}
	
	private void refreshView() {
//		goods = data.getShopCartDatas().get(0).getGoodsData();
//		address = data.getAddressData();
		if (data.getAddresses() != null) {
			addresses = data.getAddresses();
		}
		if (!TextUtils.isEmpty(data.getGoodsName())) {
			txtName.setText(data.getGoodsName().toString());
		}
		
		if (!TextUtils.isEmpty(data.getGoodsPic())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(data.getGoodsPic(), imgView));
		} else {
			imgView.setImageResource(R.drawable.head_male);
		}
		if (data.getPayType() == 0) {
			txtGoodsPrice.setText(String.valueOf(data.getCost())+ "元");
		} else {
			
			txtGoodsPrice.setText(String.valueOf(data.getCost())+ "金币");
		}
		txtGoodsNum.setText(String.valueOf(data.getAmount()));
		
		if (addresses != null && !data.getVirtualGoods()) {
//			txtAddress.setText(address.getRecipient() + address.getPhone() + "\n" + address.getDetailed());
			txtAddress.setText(addresses.get(3) + "  " + addresses.get(4) + "\n" + addresses.get(0) + addresses.get(1) + addresses.get(2));
			txtAddress.setVisibility(View.VISIBLE);
			line1.setVisibility(View.VISIBLE);
		}
		
		if (!TextUtils.isEmpty(data.getExpressNo()) && !TextUtils.isEmpty(data.getExpressor())) {
			txtExpress.setText(data.getExpressor().toString() + "\n" + "快递单号: " + data.getExpressNo().toString());
			txtExpress.setVisibility(View.VISIBLE);
			line1.setVisibility(View.VISIBLE);
		}
		
		if (!TextUtils.isEmpty(data.getOrderNo())) {
			txtOrderNo.setText(data.getOrderNo().toString());
		}
		
		if (!TextUtils.isEmpty(data.getGiverName())) {
			ll1.setVisibility(View.VISIBLE);
			txtUserName.setText(data.getGiverName());
			line1.setVisibility(View.VISIBLE);
		}
		
		if (!TextUtils.isEmpty(data.getGiverHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(data.getGiverHeadUrl(), imgHead));
		} else {
			imgHead.setImageResource(R.drawable.head_male);
		}
		
		if (data.getIsCode() == 1) {
			ll3.setVisibility(View.VISIBLE);
			txtTime.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(data.getExchangeCode())) {
				txtExchangeNo.setText(data.getExchangeCode() + "  (点击可复制)");
				ll3.setTag(data.getExchangeCode());
			}
			if (data.getOverTime() != null) {
				txtTime.setText(DateUtils.convert(data.getOverTime()) + " 截止兑换");
			}
		}
		
		switch(data.getState()) {
			case UNPAID:
				txtStatus1.setVisibility(View.VISIBLE);
				btnBuy.setVisibility(View.VISIBLE);
				break;
			case PAID:
				txtStatus4.setVisibility(View.VISIBLE);
				break;
			case SENT:
				txtStatus2.setVisibility(View.VISIBLE);
				break;
			case RECEUVED:
				txtStatus3.setVisibility(View.VISIBLE);
				break;
			case NOT_ENOUGH_STOCK:
				txtStatus6.setVisibility(View.VISIBLE);
				break;
			case TIMEOUT:
				txtStatus5.setVisibility(View.VISIBLE);
				break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.rlView:
				AtyShopItemDetail.startAty(AtyShopOrderDetails.this, data.getGoodsId(), data.getPayType());
				break;
			case R.id.ll1:
				AtyBrowseUserInfo.startAty(this, data.getGiverId());
				break;
			case R.id.btnBuy:
				AtyShopOrderPay.startAty(AtyShopOrderDetails.this, data);
				this.finish();
				break;
			case R.id.btnBack:
				this.finish();
				break;
			case R.id.ll3:
				if (null != v.getTag()) {
					ClipboardUtils.copy(v.getTag().toString());
					Toast.makeText(this, R.string.copyExchangeNo, Toast.LENGTH_SHORT).show();
				}
				break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyShopOrderDetails.class);
		context.startActivityForResult(i, REQUEST_SHOP_CONFIRM_ORDER);
	}

}
