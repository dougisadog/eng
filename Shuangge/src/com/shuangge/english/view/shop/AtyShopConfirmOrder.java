package com.shuangge.english.view.shop;

import java.math.BigDecimal;

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
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.OrderSimpleResult;
import com.shuangge.english.network.shop.TaskReqAddressList;
import com.shuangge.english.network.shop.TaskReqCreateOrder;
import com.shuangge.english.network.shop.TaskReqGiveGift;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;

public class AtyShopConfirmOrder extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_SHOP_CONFIRM_ORDER = 1086;
	
	private static int TYPE_REAL = 0;
	private static int TYPE_VIRTUAL = 1;
	private static int TYPE_BOTH = -1;
	
	private static int PAYTYPE_CREDITS = 1;
	private static int PAYTYPE_CASH = 0;
	private static int PAYTYPE_BOTH = -1;
	
	private ImageView imgView, imgSub, imgAdd;
	private TextView txtName, txtNum, txtAddress, txtCredit, txtMoney, txtGiveUserName, txtPostType;
	private FrameLayout flBuy, flCredit, flMoney, flGive;
	private LinearLayout llGiveUserName, llChooseUser, llIsHave;
	private RelativeLayout rlAddress;
	private int itemCount = 1;
	private int maxCount;
	private AddressData address;
	private GoodsData goods;
	private double price;
	private ImageButton btnBack;
	private int isConnect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_shop_confirm_order);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtName = (TextView) findViewById(R.id.txtName);
		txtNum = (TextView) findViewById(R.id.txtNum);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		txtCredit = (TextView) findViewById(R.id.txtCredit);
		txtMoney = (TextView) findViewById(R.id.txtMoney);
		txtGiveUserName = (TextView) findViewById(R.id.txtGiveUserName);
		txtPostType = (TextView) findViewById(R.id.txtPostType);
		llGiveUserName = (LinearLayout) findViewById(R.id.llGiveUserName);
		llChooseUser = (LinearLayout) findViewById(R.id.llChooseUser);
		llIsHave = (LinearLayout) findViewById(R.id.llIsHave);
		flBuy = (FrameLayout) findViewById(R.id.flBuy);
		flMoney = (FrameLayout) findViewById(R.id.flMoney);
		flCredit = (FrameLayout) findViewById(R.id.flCredit);
		flGive = (FrameLayout) findViewById(R.id.flGive);
		imgView = (ImageView) findViewById(R.id.imgView);
		imgSub = (ImageView) findViewById(R.id.imgSub);
		imgAdd = (ImageView) findViewById(R.id.imgAdd);
		rlAddress = (RelativeLayout) findViewById(R.id.rlAddress);
		flBuy.setOnClickListener(this);
		flGive.setOnClickListener(this);
		llChooseUser.setOnClickListener(this);
		imgSub.setOnClickListener(this);
		imgAdd.setOnClickListener(this);
		rlAddress.setOnClickListener(this);
		flCredit.setVisibility(View.GONE);
		flMoney.setVisibility(View.GONE);
		flBuy.setVisibility(View.GONE);
		flGive.setVisibility(View.GONE);
		txtAddress.setVisibility(View.GONE);
		rlAddress.setVisibility(View.GONE);
		llGiveUserName.setVisibility(View.GONE);
		llIsHave.setVisibility(View.GONE);
		
		txtNum.setText(String.valueOf(itemCount));
	}
	
	protected void initRequestData() {
		super.initRequestData();
		showLoading();
		new TaskReqAddressList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					Toast.makeText(AtyShopConfirmOrder.this, "访问服务器失败,请返回重新访问", Toast.LENGTH_SHORT).show();
					return;
				}
				if (GlobalRes.getInstance().getBeans().getAddressResult().getAddressDatas() != null) {
					address = GlobalRes.getInstance().getBeans().getAddressResult().getAddressDatas().get(0);
				}
				
				initView();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		});
		
//		if (isConnect == false) {
//			Toast.makeText(AtyShopConfirmOrder.this, "访问服务器失败,请返回重新访问", Toast.LENGTH_SHORT).show();
//		}
		
	}
	
	private void initView() {

		goods = GlobalRes.getInstance().getBeans().getGoodsResult().getGoodsData();
//		price = goods.getPrice();
		
		if (goods.getGift() == 1) {
			flGive.setVisibility(View.VISIBLE);
			llGiveUserName.setVisibility(View.VISIBLE);
		}
		
		if (!TextUtils.isEmpty(goods.getName())) {
			txtName.setText(goods.getName().toString());
		}
		
		if (goods.getType() == TYPE_VIRTUAL) {
			txtAddress.setVisibility(View.GONE);
			rlAddress.setVisibility(View.GONE);
		} else {
			txtAddress.setVisibility(View.VISIBLE);
			rlAddress.setVisibility(View.VISIBLE);
		}
		
		if (GlobalRes.getInstance().getBeans().getPayType() == PAYTYPE_CREDITS) {
			flCredit.setVisibility(View.VISIBLE);
			flBuy.setVisibility(View.VISIBLE);
			txtCredit.setText(String.valueOf(goods.getScorePrice()));
		} else {
			flMoney.setVisibility(View.VISIBLE);
			flBuy.setVisibility(View.VISIBLE);
			txtMoney.setText(String.valueOf(goods.getPrice()));
		}
		
//		if (goods.getLimitCount() == goods.getTakenCount()||goods.getLimitCount() < goods.getTakenCount()) {
//			flBuy.setVisibility(View.GONE);
//			llIsHave.setVisibility(View.VISIBLE);
//		}
		
		if (GlobalRes.getInstance().getBeans().isGive()) {
			flGive.setVisibility(View.VISIBLE);
			llGiveUserName.setVisibility(View.VISIBLE);
			flBuy.setVisibility(View.GONE);
		} else {
			flGive.setVisibility(View.GONE);
			llGiveUserName.setVisibility(View.GONE);
			flBuy.setVisibility(View.VISIBLE);
			if (goods.getLimitCount() == goods.getTakenCount()||goods.getLimitCount() < goods.getTakenCount()) {
				flBuy.setVisibility(View.GONE);
				llIsHave.setVisibility(View.VISIBLE);
			}
		}
		
		
		maxCount = goods.getLimitCount() - goods.getTakenCount();
		if(maxCount > goods.getStock()) {
			maxCount = goods.getStock();
		}
		if (!TextUtils.isEmpty(goods.getUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(goods.getUrl(), imgView));
		} else {
			imgView.setImageResource(R.drawable.head_male);
		}
		
		if (address != null) {
			txtAddress.setText(address.getRecipient()+ "  " + address.getPhone() + "\n" + address.getLocation() + address.getDetailed() + address.getZipCode());
		}
		
		if (goods.getPostType()) {
			txtPostType.setText("该商品包邮");
		} else {
			txtPostType.setText("该商品不包邮");
		}
		GlobalRes.getInstance().getBeans().setDefaultAddress(null);
	}
	
	private void createOrder() {
		int addressId = 0;
		if (address != null) {
			addressId = address.getAddressId();
		} else {
			if (GlobalRes.getInstance().getBeans().getGoodsResult().getGoodsData().getType() == TYPE_REAL) {
				AtyShopAddressList.startAty(AtyShopConfirmOrder.this);
				return;
			}
			
		}
		showLoading();
		new TaskReqCreateOrder(0, new CallbackNoticeView<Void, OrderSimpleResult>() {

			@Override
			public void refreshView(int tag, OrderSimpleResult result) {
				hideLoading();
				if (null == result) 
					return;
				AtyShopOrderPay.startAty(AtyShopConfirmOrder.this, result.getOrderData());
				finish();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, itemCount, goods.getGoodsId(), addressId, GlobalRes.getInstance().getBeans().getPayType());
	}
	
	private void setPrice(int num) {
		if (GlobalRes.getInstance().getBeans() == null) {
			return;
		}
		if (GlobalRes.getInstance().getBeans().getPayType() == PAYTYPE_CREDITS) {
			price = num*goods.getScorePrice();
			txtCredit.setText(String.valueOf(price));
		} else {
			price = num*goods.getPrice();
			price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			txtMoney.setText(String.valueOf(price));
		}
	}
	
	private void createGiftOrder() {
		if (txtGiveUserName.getText().toString().length() == 0) {
			AtyGiveUserList.startAty(AtyShopConfirmOrder.this);
			return;
		}
		
		showLoading();
		new TaskReqGiveGift(0, new CallbackNoticeView<Void, OrderSimpleResult>() {

			@Override
			public void refreshView(int tag, OrderSimpleResult result) {
				hideLoading();
				if (null == result)
					return;
				AtyShopOrderPay.startAty(AtyShopConfirmOrder.this, result.getOrderData());
				GlobalRes.getInstance().getBeans().setGiveUserInfoData(null);
				finish();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, GlobalRes.getInstance().getBeans().getGiveUserInfoData().getUserNo() ,itemCount, goods.getGoodsId(), GlobalRes.getInstance().getBeans().getPayType());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				this.finish();
				break;
			case R.id.flBuy:
				createOrder();
				break;
			case R.id.flGive:
				createGiftOrder();
				break;
			case R.id.imgSub:
				if (itemCount > 1) {
					itemCount --;
					txtNum.setText(String.valueOf(itemCount));
				}
				setPrice(itemCount);
				break;
			case R.id.imgAdd:
				if (itemCount == goods.getStock()) {
					Toast.makeText(AtyShopConfirmOrder.this, R.string.shopOrderTip11, Toast.LENGTH_SHORT).show();
				}
				if (itemCount < maxCount) {
					itemCount ++;
					txtNum.setText(String.valueOf(itemCount));
				} else {
					Toast.makeText(AtyShopConfirmOrder.this, R.string.shopOrderTip10, Toast.LENGTH_SHORT).show();
				}
				setPrice(itemCount);
				break;
			case R.id.rlAddress:
				AtyShopAddressList.startAty(AtyShopConfirmOrder.this);
				break;
			case R.id.llChooseUser:
				AtyGiveUserList.startAty(AtyShopConfirmOrder.this);
				break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (GlobalRes.getInstance().getBeans().getDefaultAddress() != null) {
			address = GlobalRes.getInstance().getBeans().getDefaultAddress();
			initView();
		}
		if (requestCode == AtyGiveUserList.REQUEST_GIVE_USER_LIST) {
			//TODO:Jeffrey 删除被邀请的用户
			if (GlobalRes.getInstance().getBeans().getGiveUserInfoData() == null) {
				txtGiveUserName.setText("");
				return;
			}
			txtGiveUserName.setText(GlobalRes.getInstance().getBeans().getGiveUserInfoData().getName());
		}
		
	}
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyShopConfirmOrder.class);
		context.startActivityForResult(i, REQUEST_SHOP_CONFIRM_ORDER);
	}

}
