package com.shuangge.english.view.secretmsg.fragment;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.network.secretmsg.TaskReqAttentions;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.secretmsg.AtySecretDetails;
import com.shuangge.english.view.secretmsg.adapter.AdapterFriend;

/**
 * 
 * @author Jeffrey
 *
 */
public class FragmentSecretAttentions extends BaseSecretFriendFragment implements OnClickListener, OnItemClickListener {
	
	public static Integer versionNo;
	private MyPullToRefreshListView refreshListView;
	private AdapterFriend adapter;
	
	private View mainView;
	
	public FragmentSecretAttentions() {
		super();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_secretmsg_fans, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		
		mainView.findViewById(R.id.llSearch).setOnClickListener(this);
		
		refreshListView = (MyPullToRefreshListView) mainView.findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.DISABLED);
//		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.refreshlvFooter1)); // 下拉刷新...
//		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.refreshlvFooter2)); //放开刷新...
//		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.refreshlvFooter3));//正在加载...
//		refreshListView.getLoadingLayoutProxy(true, false).setPullLabel(getString(R.string.refreshlvHeader1));  // 下拉刷新...
//		refreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getString(R.string.refreshlvHeader2)); //放开刷新...
//		refreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(getString(R.string.refreshlvHeader3));//正在加载...

//		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				if (refreshListView.isNoReFreshing()) {
//					if (refreshListView.isHeaderShown()) {
//						String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
//								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//						refreshListView.setStatusUp();
//						requestDatas();
//					} else if (refreshListView.isFooterShown()) {
//						String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
//								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//						requestDatas();
//					}
//				} else {
//					refreshListView.onRefreshComplete();
//				}
//			}
//
//		});
		
		adapter = new AdapterFriend(getActivity());
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		
		return mainView;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	protected void onInitDatas() {
		super.onInitDatas();
		requestDatas();
	}
	
	private void requestDatas() {
		showLoading();
		new TaskReqAttentions(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				List<AttentionData> datas = GlobalRes.getInstance().getBeans().getAttentionDatas();
				if (datas.size() == 0) {
					return;
				}
				refreshDatas(datas);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void refreshDatas(List<AttentionData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}
	
	private void addDatas(List<AttentionData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llSearch:
//			AtyClassSearch.startAty(getActivity());
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		adapter.showMore((AdapterSecretMsg.SystemNoticetViewHolder) view.getTag());
		GlobalRes.getInstance().getBeans().setCurrentFriendNo(((AttentionData) adapter.getItem(position-1)).getUserNo());
		GlobalRes.getInstance().getBeans().setCurrentFriendName(((AttentionData) adapter.getItem(position-1)).getName());
		AtySecretDetails.startAty(getActivity());
		
	}
	
}
