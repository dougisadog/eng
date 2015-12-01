package com.shuangge.english.view.group;

import java.util.ArrayList;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassMemberData;
import com.shuangge.english.network.group.TaskReqClassMembers;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.group.adapter.AdapterClassMember;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassMemberForLeave extends AbstractAppActivity implements OnClickListener, OnItemClickListener {

	public static final int REQUEST_CLASS_MEMBER_FOR_LEAVE = 1013;
	
	public static final int CODE_LEAVE = 1;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private MyPullToRefreshListView refreshListView;
	private AdapterClassMember adapter;
	private TextView txtBarTitleEn, txtBarTitleCn;
	
	private DialogConfirmFragment dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_class_member);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtBarTitleEn = (TextView) findViewById(R.id.txtBarTitleEn);
		txtBarTitleCn = (TextView) findViewById(R.id.txtBarTitleCn);
		txtBarTitleEn.setText(R.string.classLeaveTitleEn);
		txtBarTitleCn.setText(R.string.classLeaveTitleCn);
		
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
			
		});
	}
	
	private void refreshDatas(List<ClassMemberData> datas) {
		List<ClassMemberData> noMyselfList = new ArrayList<ClassMemberData>();
		for (ClassMemberData data : datas) {
			if (data.getUserNo().longValue() == GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue()) {
				continue;
			}
			noMyselfList.add(data);
		}
		adapter.setDatas(noMyselfList);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				this.finish();
				break;
		}
	}
	
	private CallBackDialogConfirm callback = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
			hideDialog();
			GlobalRes.getInstance().getBeans().setCurrentUserNo(adapter.getItem(position).getUserNo());
			setResult(CODE_LEAVE);
			AtyClassMemberForLeave.this.finish();
		}
		
		@Override
		public void onCancel() {
			hideDialog();
		}

		@Override
		public void onKeyBack() {
			onCancel();
		}

	};
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String strEn = getString(R.string.classMemberDialogTip3En).replace("sb", adapter.getItem(position - 1).getName());
		String strCn = getString(R.string.classMemberDialogTip3Cn).replace("sb", adapter.getItem(position - 1).getName());
		dialog = new DialogConfirmFragment(callback, strEn, strCn, position - 1);
		dialog.showDialog(getSupportFragmentManager());
	}
	
	private void hideDialog() {
		if (null == dialog) {
			return;
		}
		dialog.dismiss();
		dialog = null;
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassMemberForLeave.class);
		context.startActivityForResult(i, REQUEST_CLASS_MEMBER_FOR_LEAVE);
	}
	
}
