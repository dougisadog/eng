package com.shuangge.english.view.group;

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
import android.widget.ImageButton;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassMemberData;
import com.shuangge.english.network.group.TaskReqClassMembers;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.group.adapter.AdapterClassMember;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassMember extends AbstractAppActivity implements OnClickListener, OnItemClickListener {

	public static final int REQUEST_CLASS_MEMBERS = 1009;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private MyPullToRefreshListView refreshListView;
	private AdapterClassMember adapter;
	
	private Integer sortBy = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_class_member);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		findViewById(R.id.ll1).setOnClickListener(this);
		findViewById(R.id.ll2).setOnClickListener(this);
		findViewById(R.id.ll3).setOnClickListener(this);
		findViewById(R.id.ll4).setOnClickListener(this);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.PULL_FROM_START);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.refreshlvFooter1)); // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.refreshlvFooter2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.refreshlvFooter3));//正在加载...

		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.isNoReFreshing()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						requestData();
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterClassMember(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		
		if (null != GlobalRes.getInstance().getBeans().getMemberData())
			refreshDatas(GlobalRes.getInstance().getBeans().getMemberData().getMembers());
	}
	
	private void requestData() {
		if (requesting) {
			return;
		}
		requesting = true;
		showLoading();
		new TaskReqClassMembers(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null != result && result) {
					refreshDatas(GlobalRes.getInstance().getBeans().getMemberData().getMembers());
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, sortBy);
	}
	
	private void refreshDatas(List<ClassMemberData> datas) {
		adapter.setDatas(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		long userNo = adapter.getItem(position - 1).getUserNo();
		if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue() == userNo) {
			AtyUserInfo.startAty(this);
			return;
		}
		AtyBrowseUserInfo.startAty(this, userNo);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				this.finish();
				break;
			case R.id.ll1:
				sortBy = 0;
				requestData();
				break;
			case R.id.ll2:
				sortBy = 1;
				requestData();
				break;
			case R.id.ll3:
				sortBy = 2;
				requestData();
				break;
			case R.id.ll4:
				sortBy = 4;
				requestData();
				break;
			}
	}

	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassMember.class);
		context.startActivityForResult(i, REQUEST_CLASS_MEMBERS);
	}
	
}
