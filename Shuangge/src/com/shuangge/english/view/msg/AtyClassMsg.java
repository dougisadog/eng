package com.shuangge.english.view.msg;

import java.util.Date;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.msg.UserGroupMsgData;
import com.shuangge.english.network.group.TaskReqGroupMsgReply;
import com.shuangge.english.network.msg.TaskReqGroupMsg;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogApplyWithJoinClassFragment;
import com.shuangge.english.view.component.dialog.DialogGroupApplyWithJoinClassFragment;
import com.shuangge.english.view.msg.adapter.AdapterClassMsg;
import com.shuangge.english.view.msg.adapter.AdapterClassMsg.CallBackClassMsg;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassMsg extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_CLASS_MSG = 1015;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;

	private MyPullToRefreshListView refreshListView;
	private AdapterClassMsg adapter;

	private Date timestamp;
	private Date lastDate;
	private int currentPosition;
	
	private Long _msgNo;
	private int _status;
	private String _msg;
	private String _wechat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_class_msg);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
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
						timestamp = lastDate;
						requestDatas();
					}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterClassMsg(this, new CallBackClassMsg() {
			
			@Override
			public void notice(int position, Long msgNo, final int status, String msg, int type) {
				if (requesting) {
					return;
				}
				requesting = true;
				currentPosition = position;
				_msgNo = msgNo;
				_status = status;
				_msg = msg;
				UserGroupMsgData entity = adapter.getDatas().get(position);
				_wechat = entity.getWechatNo();
				if (status == UserGroupMsgData.STATUS_AGREE) {
					if (AdapterClassMsg.TYPE_GROUP_APPLY == type) {
						weChatNoDialog();
					}
					else if(AdapterClassMsg.TYPE_USER_APPLY == type) {
						requestDatas(null, AdapterClassMsg.TYPE_USER_APPLY);
					}
					return;
				} 
				if (status == UserGroupMsgData.STATUS_REFUSE) {
					groupApplyDialog();
				}
			}
			
		});
		refreshListView.setAdapter(adapter);
		
		requestDatas();
	}
	
	private void requestDatas() {
		if (requesting) {
			return;
		}
		requesting = true;
		showLoading();
		new TaskReqGroupMsg(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				List<UserGroupMsgData> msgs = GlobalRes.getInstance().getBeans().getGroupMsgData().getClassMsgs();
				if (msgs.size() > 0) {
					lastDate = msgs.get(msgs.size() - 1).getCreateDate();
				}
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
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
	
	private void refreshDatas(List<UserGroupMsgData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
	}
	
	private void addDatas(List<UserGroupMsgData> datas) {
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
	

	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassMsg.class);
		context.startActivityForResult(i, REQUEST_CLASS_MSG);
	}
	private DialogApplyWithJoinClassFragment weChatNoDialog; 
	private void weChatNoDialog() {
		if (null == weChatNoDialog) {
			weChatNoDialog = new DialogApplyWithJoinClassFragment(new DialogApplyWithJoinClassFragment.CallBackDialogConfirmWithWeChatNo() {
				@Override
				public void submit(int position, String weChatNo) {
					if (null == weChatNoDialog) {
						return;
					}
					weChatNoDialog.dismiss();
					weChatNoDialog = null;
					requestDatas(weChatNo,AdapterClassMsg.TYPE_GROUP_APPLY);
				}
				
				@Override
				public void cancel() {
					weChatNoDialog.dismiss();
					weChatNoDialog = null;
					requesting = false;
				}
			}, _wechat, 0);
		}
		if (weChatNoDialog.isVisible()) {
			return;
		}
		weChatNoDialog.setCancelable(false);
		weChatNoDialog.showDialog(getSupportFragmentManager());
	}

	private void requestDatas(String weChatNo,int type) {
		showLoading();
		new TaskReqGroupMsgReply(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null == result || !result) {
					return;
				}
				adapter.getItem(currentPosition).setStatus(_status);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, _msgNo, _status, _msg, weChatNo, type);
	}
	
	private DialogGroupApplyWithJoinClassFragment groupApplyDialog;
	
	private void groupApplyDialog() {
		if (null == groupApplyDialog) {
			groupApplyDialog = new DialogGroupApplyWithJoinClassFragment(new DialogGroupApplyWithJoinClassFragment.CallBackDialogConfirmWithWeChatNo() {
				
				@Override
				public void submit(int position,String msg) {
					if (null == groupApplyDialog) {
						return;
					}
					groupApplyDialog.dismiss();
					groupApplyDialog = null;
					_msg = msg;
					requestDatas(null, AdapterClassMsg.TYPE_USER_APPLY);
				}
				
				@Override
				public void cancel() {
					groupApplyDialog.dismiss();
					groupApplyDialog = null;
					requesting = false;
				}
				
			}, 0);
		}
		if (groupApplyDialog.isVisible()) {
			return;
		}
		groupApplyDialog.setCancelable(false);
		groupApplyDialog.showDialog(getSupportFragmentManager());
	}
}
