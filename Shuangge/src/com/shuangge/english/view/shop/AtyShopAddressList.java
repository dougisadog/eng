package com.shuangge.english.view.shop;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.animation.IntArrayEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgDeleteAll;
import com.shuangge.english.network.shop.TaskReqAddressDelete;
import com.shuangge.english.network.shop.TaskReqAddressList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.secretmsg.AtySecretMsgList;
import com.shuangge.english.view.shop.adapter.AdapterAddressItem;
import com.shuangge.english.view.shop.adapter.AdapterPurchaseRecordsItem;
import com.shuangge.english.view.user.adapter.AdapterUser;

public class AtyShopAddressList extends AbstractAppActivity implements
		OnClickListener, OnItemLongClickListener {
	
public static final int REQUEST_SHOP_PURCHASE_RECORDS = 1084;
	
	private boolean requesting = false;
	private ImageButton btnBack;
	private MyPullToRefreshListView refreshListView;
	private AdapterAddressItem adapter;
	private DialogConfirmFragment dialogDelete;
	private Integer deleteId;
	private FrameLayout flNew;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_shop_address_list);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		flNew = (FrameLayout) findViewById(R.id.flNew);
		flNew.setOnClickListener(this);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.DISABLED);

		
		adapter = new AdapterAddressItem(this);
		refreshListView.setAdapter(adapter);
//		refreshListView.setOnItemClickListener(this);
		requestData();
	}
	
	public void requestData() {
		showLoading();
		new TaskReqAddressList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				if (GlobalRes.getInstance().getBeans().getAddressResult().getAddressDatas() != null) {
					List<AddressData> datas = GlobalRes.getInstance().getBeans().getAddressResult().getAddressDatas();
					refreshDatas(datas);
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
		
	}
	
	private void refreshDatas(List<AddressData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
	}
	
	private void addDatas(List<AddressData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}
	

//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		GlobalRes.getInstance().getBeans().setCurrentAddress((AddressData) adapter.getItem(position-1));
//		AtyShippingAddressEdit.startAty(AtyShopAddressList.this);
//	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		dialogDelete = new DialogConfirmFragment(callback2, getString(R.string.addressDeleteTipEn), getString(R.string.addressDeleteTipCn), position - 1);
		dialogDelete.showDialog(getSupportFragmentManager());
		deleteId = ((AddressData) adapter.getItem(position-1)).getAddressId();
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.flNew:
			GlobalRes.getInstance().getBeans().setCurrentAddress(null);
			AtyShippingAddressEdit.startAty(AtyShopAddressList.this);
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		requestData();
	}
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyShopAddressList.class);
		context.startActivityForResult(i, REQUEST_SHOP_PURCHASE_RECORDS);
	}

	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
			showLoading();
			hideDialogDeleteReply();
			adapter.getDatas().remove(position);
			adapter.notifyDataSetChanged();
			refreshListView.onRefreshComplete2();
			Toast.makeText(AtyShopAddressList.this, R.string.deleteSuccessTip, Toast.LENGTH_SHORT).show();
			new TaskReqAddressDelete(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, deleteId);
		}
		
		@Override
		public void onCancel() {
			hideDialogDeleteReply();
		}

		@Override
		public void onKeyBack() {
			onCancel();
		}
		
	};
	
	private void hideDialogDeleteReply() {
		dialogDelete.dismiss();
		dialogDelete = null;
	}

}
