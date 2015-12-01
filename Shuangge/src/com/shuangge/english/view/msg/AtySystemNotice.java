package com.shuangge.english.view.msg;

import java.util.Date;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import air.com.shuangge.phone.ShuangEnglish.wxapi.WXPayEntryActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.msg.NoticeData;
import com.shuangge.english.entity.server.shop.CashPayData;
import com.shuangge.english.network.msg.TaskReqSystemMsg;
import com.shuangge.english.network.shop.TaskReqOrderBuyWx;
import com.shuangge.english.network.shop.TaskReqReceiveGift;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.msg.adapter.AdapterSystemNotice;
import com.shuangge.english.view.msg.adapter.AdapterSystemNotice.SystemNoticetViewHolder;
import com.shuangge.english.view.msg.dialog.DialogShowMore;
import com.shuangge.english.view.shop.AtyShopOrderDetails;
import com.shuangge.english.view.shop.GoodsFunc;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtySystemNotice extends AbstractAppActivity implements OnClickListener, OnItemClickListener {

	public static final int REQUEST_SYS_MSG = 1041;
	
	private boolean requesting = false;
	private ImageButton btnBack;

	private MyPullToRefreshListView refreshListView;
	private AdapterSystemNotice adapter;
	private DialogShowMore dialogShowMore;
	
	private Date timestamp;
	private Date lastDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sys_notice);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		// set mode to BOTH
		refreshListView.setMode(Mode.BOTH);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.refreshlvFooter1)); // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.refreshlvFooter2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.refreshlvFooter3));//正在加载...
		refreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.refreshlvHeader1));  // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshlvHeader2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.refreshlvHeader3));//正在加载...

		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.isNoReFreshing()) {
					if (refreshListView.isHeaderShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						timestamp = null;
						requestDatas();
					} else if (refreshListView.isFooterShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusDown();
						timestamp = lastDate;
						requestDatas();
					}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterSystemNotice(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		requestDatas();
	}
	
	private void requestDatas() {
		if (requesting) {
			return;
		}
		requesting = true;
		showLoading();
		new TaskReqSystemMsg(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				List<NoticeData> msgs = GlobalRes.getInstance().getBeans().getSystemMsgData().getNotices();
				if (msgs.size() > 0) {
					lastDate = msgs.get(msgs.size() - 1).getCreateDate();
				}
				if (refreshListView.isReFreshingForUp()) {
					refreshDatas(msgs);
					return;
				}
				addDatas(msgs);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, GlobalRes.getInstance().getBeans().getCurrentMyClassNo(), timestamp);
	}
	
	private void refreshDatas(List<NoticeData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
	}
	
	private void addDatas(List<NoticeData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (adapter.getItem(position - 1).getNoticeType() == 1) {
			requestReceiveGift(adapter.getItem(position - 1).getOrderNo());
			return;
		}
		adapter.showMore((SystemNoticetViewHolder) view.getTag());
		showDialog(adapter.getItem(position - 1));
	}
	
	private void requestReceiveGift(Long orderNo) {
		new TaskReqReceiveGift(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null != result && result) {
					List<CashPayData> cashPayDatas = GlobalRes.getInstance().getBeans().getPayResult().getCashPayDatas();
					if (cashPayDatas != null && cashPayDatas.size() > 0 && cashPayDatas.get(0).getCode() == 0) {
						requestLessonData(cashPayDatas.get(0).getFunc());
//						GlobalRes.getInstance().getBeans().setCurrentOrderId(GlobalRes.getInstance().getBeans().getOrderSimpleResult().getOrderData().getOrderNo());
//						AtyShopOrderDetails.startAty(WXPayEntryActivity.this);
						
						return;
					}
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, orderNo);
	}
	
	private void requestLessonData(int func) {
		
		GoodsFunc goodsFunc = new GoodsFunc(func, new GoodsFunc.CallBackFunc() {
			
			@Override
			public void onCallBack() {
				// TODO Auto-generated method stub
				Toast.makeText(AtySystemNotice.this, "领取成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		
		});
	}
	
	
	private void showDialog(NoticeData data) {
		if (null == dialogShowMore) {
			dialogShowMore = new DialogShowMore();
		}
		if (dialogShowMore.isVisible()) {
			return;
		}
		dialogShowMore.setData(data);
		dialogShowMore.showDialog(getSupportFragmentManager());
	}

	private void hideDialog() {
		if (null == dialogShowMore) {
			return;
		}
		dialogShowMore.dismiss();
		dialogShowMore = null;
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtySystemNotice.class);
		context.startActivityForResult(i, REQUEST_SYS_MSG);
	}
}