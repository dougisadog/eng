package com.shuangge.english.view.group;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.group.TaskReqRecommendMembers;
import com.shuangge.english.network.user.TaskReqSearchUsers;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.adapter.AdapterUser;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassInviteList extends AbstractAppActivity implements
		OnClickListener, OnKeyListener, OnItemClickListener	 {

	public static final int REQUEST_CLASS_INVITE_LIST = 1020;
	
	private boolean requesting = false;
	private ImageButton btnBack;
	private EditTextWidthTips txtSearch;
	
	private MyPullToRefreshListView refreshListView;
	private AdapterUser adapter;
	private boolean searching = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_class_invite_list);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtSearch = (EditTextWidthTips) findViewById(R.id.txtSearch);
		txtSearch.setOnKeyListener(this);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.PULL_FROM_START);
		refreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.refreshlvHeader1));  // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshlvHeader2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.refreshlvHeader3));//正在加载...

		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.isNoReFreshing()) {
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						if (searching) {
							requestSearchData();
						}
						else {
							requestData();
						}
						// Do work to refresh the list here.
						refreshListView.setStatusUp();
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterUser(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		requestData();
	}
	
	private void requestData() {
		if (requesting) {
			return;
		}
		requesting = true;
		showLoading();
		searching = false;
		new TaskReqRecommendMembers(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null == result || !result) {
					return;
				}
				refreshDatas(GlobalRes.getInstance().getBeans().getRecommendMemberData().getOtherInfoDatas());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void requestSearchData() {
		if (txtSearch.getVal().length() < 2) {
			Toast.makeText(this, R.string.classInviteErrTip1, Toast.LENGTH_SHORT).show();
			return;
		}
		if (requesting) {
			return;
		}
		requesting = true;
		showLoading();
		searching = true;
		new TaskReqSearchUsers(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null == result || !result) {
					return;
				}
				refreshDatas(GlobalRes.getInstance().getBeans().getSearchUsers().getInfoData());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, txtSearch.getVal());
	}

	private void refreshDatas(List<OtherInfoData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
	}
	
	private void addDatas(List<OtherInfoData> datas) {
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
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
			InputUitls.closeInputMethod(this, txtSearch);
			if (TextUtils.isEmpty(txtSearch.getVal())) {
				requestData();
				return true;
			}
			requestSearchData();
			return true;  
		}  
		return false; 
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		AtyClassInviteDetails.startAty(this, adapter.getItem(position - 1).getUserNo());
		AtyBrowseUserInfo.startAty(this, adapter.getItem(position - 1).getUserNo());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == AtyClassInviteDetails.REQUEST_CLASS_INVITE_DETAILS) {
//			//TODO:Jeffrey 删除被邀请的用户
//		}
		if (requestCode == AtyBrowseUserInfo.REQUEST_BROWSE_USER_INFO) {
			//TODO:Jeffrey 删除被邀请的用户
		}
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassInviteList.class);
		context.startActivityForResult(i, REQUEST_CLASS_INVITE_LIST);
	}


}
