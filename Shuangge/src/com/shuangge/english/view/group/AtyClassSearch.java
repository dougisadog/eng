package com.shuangge.english.view.group;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.network.group.TaskReqSearchClasses;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.ComponentTitleBar;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.group.adapter.AdapterClass;
import com.shuangge.english.view.msg.AtyClassMsg;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassSearch extends AbstractAppActivity implements OnClickListener, OnItemClickListener, OnKeyListener, IPushMsgNotice {
	
	public static final int REQUEST_CLASS_SEARCH = 1014;
	
	private boolean requesting = false;
	private ComponentTitleBar titleBar;
	private ImageButton btnBack;
	private EditTextWidthTips txtSearch;

	private MyPullToRefreshListView refreshListView;
	private AdapterClass adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_class_search);

		titleBar = (ComponentTitleBar) findViewById(R.id.titleBar);
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		findViewById(R.id.btnMsgs).setOnClickListener(this);
		findViewById(R.id.btnMsgs2).setOnClickListener(this);
		
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

						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						requestData();
						refreshListView.setStatusUp();
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterClass(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		notice();
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
		if (txtSearch.getVal().length() < 2) {
			Toast.makeText(this, R.string.classSearchErrTip1, Toast.LENGTH_SHORT).show();
			return;
		}
		if (requesting) {
			return;
		}
		requesting = true;
		showLoading();
		new TaskReqSearchClasses(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				refreshDatas(GlobalRes.getInstance().getBeans().getSearchClasses().getClassInfos());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, txtSearch.getVal());
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
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
			InputUitls.closeInputMethod(this, txtSearch);
			requestData();
			return true;  
		}  
		return false; 
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AtyBrowseClassInfo.startAty(this, adapter.getItem(position - 1).getClassNo());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnMsgs: case R.id.btnMsgs2:
			AtyClassMsg.startAty(this);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		notice();
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassSearch.class);
		context.startActivityForResult(i, REQUEST_CLASS_SEARCH);
	}

}
