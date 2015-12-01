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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.network.group.TaskReqRecommendClasses;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.binding.AtyBindingAccount;
import com.shuangge.english.view.component.ComponentTitleBar;
import com.shuangge.english.view.group.adapter.AdapterClass;
import com.shuangge.english.view.msg.AtyClassMsg;

/**
 * 班级推荐
 * @author Jeffrey
 *
 */
public class AtyClassRecommend extends AbstractAppActivity implements OnClickListener, OnItemClickListener, IPushMsgNotice {
	
	public static final int REQUEST_CLASS_RECOMMEND = 1003;
	
	private ImageButton btnBack;
	private ComponentTitleBar titleBar;
	private LinearLayout ll1;

	private MyPullToRefreshListView refreshListView;
	private AdapterClass adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_class_recommend);
		
		titleBar = (ComponentTitleBar) findViewById(R.id.titleBar);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		findViewById(R.id.btnMsgs).setOnClickListener(this);
		findViewById(R.id.btnMsgs2).setOnClickListener(this);
		
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll1.setOnClickListener(this);

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
						requestData();
						// Do work to refresh the list here.
						refreshListView.setStatusUp();
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterClass(this, true);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		
		notice();
		requestData();
	}
	
	public void notice() {
		boolean flag = GlobalRes.getInstance().getBeans().getMsgDatas().isClassMsg();
		if (flag) {
			titleBar.showMsgs();
		}
		else {
			titleBar.clearMsgs();
		}
	}
	
	private void requestData() {
		showLoading();
		new TaskReqRecommendClasses(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				refreshDatas(GlobalRes.getInstance().getBeans().getRecommendClasses().getClassInfos());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void refreshDatas(List<ClassData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
	}
	
	private void addDatas(List<ClassData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent i = new Intent(AtyClassRecommend.this, AtyBrowseClassInfo.class);
		i.putExtra(AtyBrowseClassInfo.PARAM_INDEX, adapter.getItem(position - 1).getClassNo());
		startActivity(i);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.ll1:
			AtyClassSearch.startAty(this);
			break;
		case R.id.btnMsgs: case R.id.btnMsgs2:
			AtyClassMsg.startAty(this);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		notice();
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AtyClassCreate.REQUEST_CLASS_CREATE 
				&& resultCode == AtyClassCreateTips.CODE_QUIT) {
			finish();
		}
		else if(requestCode == AtyBindingAccount.REQUEST_BINDING_ACCOUNT 
				&& resultCode == AtyBindingAccount.CODE_BINDING_ACCOUNT) {
			AtyClassCreate.startAty(this);
		}
	}
	
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassRecommend.class);
		context.startActivityForResult(i, REQUEST_CLASS_RECOMMEND);
	}

}
