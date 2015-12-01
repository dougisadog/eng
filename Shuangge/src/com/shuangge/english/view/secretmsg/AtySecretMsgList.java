package com.shuangge.english.view.secretmsg;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgDeleteAll;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgList;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgSetStatus;
import com.shuangge.english.network.secretmsg.TaskSyncSecretMsgList;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.secretmsg.adapter.AdapterSecretMsg;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtySecretMsgList extends AbstractAppActivity implements
		OnClickListener, OnItemClickListener, OnItemLongClickListener, IPushMsgNotice {

	public static final int REQUEST_SECRET_MSG = 1077;
	private boolean requesting = false;
	private ImageButton btnBack;
	private MyPullToRefreshListView refreshListView;
	private AdapterSecretMsg adapter;
	public static Long msgNoSendToServer;
	private DialogConfirmFragment dialogDeleteReply;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_secret_msg_list);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		findViewById(R.id.btnMsgs).setOnClickListener(this);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.PULL_FROM_START);
		refreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.refreshlvHeader1));  // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshlvHeader2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.refreshlvHeader3));//正在加载...

		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.isNoReFreshing()) {
					if (refreshListView.isHeaderShown()) {
						String label = DateUtils.formatDateTime( getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						requestData();
					} else if (refreshListView.isFooterShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusDown();
						requestData();
					}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterSecretMsg(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		refreshListView.getRefreshableView().setLongClickable(true);
		refreshListView.getRefreshableView().setOnItemLongClickListener(this);
		final CacheBeans beans = GlobalRes.getInstance().getBeans();
		//非第一次 直接加载内存中数据 或者 同步服务器新的数据
		if (beans.getSecretListDatas() != null) {
			notice();
			refreshDatas();
			return;
		}
		//第一次 同步服务器数据到内存
		showLoading();
		new TaskSyncSecretMsgList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				refreshDatas();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void requestData() {
		if (requesting) {
			return;
		}
		requesting = true;
		new TaskReqSecretMsgList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (isFinishing())
					return;
				requesting = false;
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				refreshDatas();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void refreshDatas() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		beans.getMsgDatas().setSecretMsg(0);
		List<SecretMsgDetailData> msgs = beans.getSecretListDatas();
		if (msgs.size() == 0) {
			refreshListView.onRefreshComplete2();
			return;
		}
		int position = refreshListView.getRefreshableView().getLastVisiblePosition();
		adapter.getDatas().clear();
		addDatas(msgs);
		refreshListView.getRefreshableView().setSelection(position);
	}
	
	private void addDatas(List<SecretMsgDetailData> datas) {
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
		case R.id.btnMsgs:
			AtySecretFriendList.startAty(AtySecretMsgList.this);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SecretMsgDetailData data = adapter.getDatas().get(--position);
		Long friendNo = data.getFriendNo();
		
		GlobalRes.getInstance().getBeans().setCurrentFriendNo(friendNo);
		GlobalRes.getInstance().getBeans().setCurrentFriendName(data.getFriendName());
		AtySecretDetails.startAty(AtySecretMsgList.this);
		
		if (data.getStatus() == SecretMsgDetailData.STATUS_UNREAD) {
			new TaskReqSecretMsgSetStatus(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, data.getSecretMsgNo());
		}
		data.setStatus(SecretMsgDetailData.STATUS_READ);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refreshDatas();
		notice();
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtySecretMsgList.class);
		context.startActivityForResult(i, REQUEST_SECRET_MSG);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		GlobalRes.getInstance().getBeans().setCurrentFriendNo(((SecretMsgDetailData) adapter.getItem(position-1)).getFriendNo());
		dialogDeleteReply = new DialogConfirmFragment(callback2, getString(R.string.replyDeleteTipEn), getString(R.string.replyDeleteTipCn), position - 1);
		dialogDeleteReply.showDialog(getSupportFragmentManager());
		return true;
	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
			showLoading();
			hideDialogDeleteReply();
			adapter.getDatas().remove(position);
			adapter.notifyDataSetChanged();
			refreshListView.onRefreshComplete2();
			Toast.makeText(AtySecretMsgList.this, R.string.deleteSuccessTip, Toast.LENGTH_SHORT).show();
			new TaskReqSecretMsgDeleteAll(0, new CallbackNoticeView<Void, Boolean>() {

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
				
			}, GlobalRes.getInstance().getBeans().getCurrentFriendNo());
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
		dialogDeleteReply.dismiss();
		dialogDeleteReply = null;
	}

	@Override
	public void notice() {
		boolean flagSecret = GlobalRes.getInstance().getBeans().getMsgDatas().getSecretMsg() > 0;
		if (!flagSecret) {
			return;
		}
		requestData();
	}

}
