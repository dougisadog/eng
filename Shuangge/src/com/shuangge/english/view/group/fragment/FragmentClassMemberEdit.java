package com.shuangge.english.view.group.fragment;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassMemberData;
import com.shuangge.english.network.group.TaskReqAssigningAuthority;
import com.shuangge.english.network.group.TaskReqClassMembers;
import com.shuangge.english.network.group.TaskReqKill;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.group.AtyClassInviteList;
import com.shuangge.english.view.group.adapter.AdapterClassMember;
import com.shuangge.english.view.group.dialog.DialogMemberEditFragment;
import com.shuangge.english.view.group.dialog.DialogMemberEditFragment.CallBackClassMemberEdit;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

/**
 * 
 * @author Jeffrey
 *
 */
public class FragmentClassMemberEdit extends BaseClassFragment implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

	private MyPullToRefreshListView refreshListView;
	private AdapterClassMember adapter;
	private Integer sortBy = 0;
	
	public FragmentClassMemberEdit() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_class_member, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		
		mainView.findViewById(R.id.llSearch).setOnClickListener(this);
		mainView.findViewById(R.id.ll1).setOnClickListener(this);
		mainView.findViewById(R.id.ll2).setOnClickListener(this);
		mainView.findViewById(R.id.ll3).setOnClickListener(this);
		mainView.findViewById(R.id.ll4).setOnClickListener(this);
		
		refreshListView = (MyPullToRefreshListView) mainView.findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.PULL_FROM_START);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.refreshlvFooter1)); // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.refreshlvFooter2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.refreshlvFooter3));//正在加载...

		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.isNoReFreshing()) {
						String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						requestData();
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterClassMember(getActivity());
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
//		refreshListView.getRefreshableView().setOnItemLongClickListener(this);
		return mainView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	protected void onInitDatas() {
		super.onInitDatas();
		requestData();
	}
	
	private DialogMemberEditFragment dialogMemberEdit;
	private DialogConfirmFragment dialogConfirm;
	private Long currentUserNo;
	private int currentPosition, authorityLevel;
	private CallBackClassMemberEdit callBack1 = new CallBackClassMemberEdit() {
		
		@Override
		public void kill(int position) {
			hideDialogMemberEdit();
			dialogConfirm = new DialogConfirmFragment(callBack2, getString(R.string.classMemberDialogTip1En),
					getString(R.string.classMemberDialogTip1Cn), position);
			dialogConfirm.showDialog(getActivity().getSupportFragmentManager());
		}
		
		@Override
		public void assigningAuthority(int position, int authorityLevel) {
			showLoading();
			hideDialogMemberEdit();
			FragmentClassMemberEdit.this.currentPosition = position;
			FragmentClassMemberEdit.this.authorityLevel = authorityLevel;
			new TaskReqAssigningAuthority(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
//					if (null != result && result) {
//						adapter.getItem(FragmentClassMemberEdit.this.currentPosition).setAuthLevel(FragmentClassMemberEdit.this.authorityLevel);
//						adapter.notifyDataSetChanged();
//					}
					if (null != result && result) {
						ClassMemberData data = adapter.getItem(FragmentClassMemberEdit.this.currentPosition);
						data.setAuthLevel(FragmentClassMemberEdit.this.authorityLevel);
						adapter.refreshDatas();
						adapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, authorityLevel, currentUserNo);
		}

		@Override
		public void profile(int position) {
			ClassMemberData data = adapter.getItem(position);
			if (null != data) {
				AtyBrowseUserInfo.startAty(getActivity(), data.getUserNo());
			}
		}
		
	};
	
	private CallBackDialogConfirm callBack2 = new CallBackDialogConfirm() {

		public void onKeyBack() {
			hideDialogConfirm();
		}
		
		public void onCancel() {
			hideDialogConfirm();
		}
		
		public void onSubmit(int position) {
			hideDialogConfirm();
			showLoading();
			FragmentClassMemberEdit.this.currentPosition = position;
			new TaskReqKill(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null != result && result) {
						Toast.makeText(getActivity(), R.string.successTipCn, Toast.LENGTH_SHORT).show();
						adapter.getDatas().remove(FragmentClassMemberEdit.this.currentPosition);
						adapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, currentUserNo);
		}
		
	};
	
	private void hideDialogMemberEdit() {
		if (null == dialogMemberEdit) {
			return;
		}
		dialogMemberEdit.dismiss();
		dialogMemberEdit = null;
	}
	
	private void hideDialogConfirm() {
		if (null == dialogConfirm) {
			return;
		}
		dialogConfirm.dismiss();
		dialogConfirm = null;
	}
	
	private void requestData() {
		showLoading();
		new TaskReqClassMembers(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
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
		ClassMemberData entity = adapter.getItem(--position);
		if (showMore(entity)) {
			currentUserNo = entity.getUserNo();
			dialogMemberEdit = new DialogMemberEditFragment(callBack1, position, entity.getAuthLevel());
			dialogMemberEdit.showDialog(getActivity().getSupportFragmentManager());
			return;
		}
		if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue() == userNo) {
			AtyUserInfo.startAty(getActivity());
			return;
		}
		AtyBrowseUserInfo.startAty(getActivity(), userNo);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		ClassMemberData entity = adapter.getItem(--position);
		if (showMore(entity)) {
			currentUserNo = entity.getUserNo();
			dialogMemberEdit = new DialogMemberEditFragment(callBack1, position, entity.getAuthLevel());
			dialogMemberEdit.showDialog(getActivity().getSupportFragmentManager());
		}
		return false;
	}
	
	private boolean showMore(ClassMemberData entity) {
		return GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() > entity.getAuthLevel();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llSearch:
			AtyClassInviteList.startAty(getActivity());
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
}
