package com.shuangge.english.view.shop;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.shop.OrderData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.shop.TaskReqGoodsList;
import com.shuangge.english.network.shop.TaskReqOrderList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.shop.adapter.AdapterPurchaseRecordsItem;
import com.shuangge.english.view.user.adapter.AdapterUser;

public class AtyShopPurchaseRecords extends AbstractAppActivity implements
		OnClickListener, OnItemClickListener	 {
	
public static final int REQUEST_SHOP_PURCHASE_RECORDS = 1087;
	
	private boolean requesting = false;
	private ImageButton btnBack;
	private ImageView btnHelp, btnStore;
	public static String last;
	
	private MyPullToRefreshListView refreshListView;
	private AdapterPurchaseRecordsItem adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_shop_purchase_records);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnHelp = (ImageView) findViewById(R.id.btnHelp);
		btnHelp.setOnClickListener(this);
		btnHelp.setVisibility(View.GONE);
		btnStore = (ImageView) findViewById(R.id.btnStore);
		btnStore.setOnClickListener(this);
		btnStore.setVisibility(View.GONE);
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.DISABLED);
//		refreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.refreshlvHeader1));  // 下拉刷新...
//		refreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshlvHeader2)); //放开刷新...
//		refreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.refreshlvHeader3));//正在加载...
//
//		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				if (refreshListView.isNoReFreshing()) {
//						String label = DateUtils.formatDateTime(
//								getApplicationContext(),
//								System.currentTimeMillis(),
//								DateUtils.FORMAT_SHOW_TIME
//										| DateUtils.FORMAT_SHOW_DATE
//										| DateUtils.FORMAT_ABBREV_ALL);
//
//						// Update the LastUpdatedLabel
//						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//						// Do work to refresh the list here.
//						refreshListView.setStatusUp();
//				} else {
//					refreshListView.onRefreshComplete();
//				}
//			}
//
//		});
		
		adapter = new AdapterPurchaseRecordsItem(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		requestData();
	}
	
	private void requestData() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (beans.getGoodsDatasCredits().size() > 0) {
			AtyShopList.last = beans.getGoodsDatasCredits().get(beans.getGoodsDatasCredits().size()-1).getGoodsId();
		}
		showLoading();
		new TaskReqOrderList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				List<OrderData> orderDatas = GlobalRes.getInstance().getBeans().getOrderDatas();
				if (orderDatas.size() > 0) {
					refreshDatas(orderDatas);
				}
				orderDatas.clear();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, -1);
		
	}
	
	private void refreshDatas(List<OrderData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
	}
	
	private void addDatas(List<OrderData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		GlobalRes.getInstance().getBeans().setCurrentOrderId(((OrderData) adapter.getItem(position-1)).getOrderNo());
		AtyShopOrderDetails.startAty(AtyShopPurchaseRecords.this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnHelp:
			this.finish();
			break;
		case R.id.btnStore:
//			AtyShopList.startAty(AtyShopPurchaseRecords.this);
			this.finish();
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyShopPurchaseRecords.class);
		context.startActivityForResult(i, REQUEST_SHOP_PURCHASE_RECORDS);
	}

}
