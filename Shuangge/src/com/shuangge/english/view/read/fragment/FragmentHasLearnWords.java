package com.shuangge.english.view.read.fragment;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.UserNewWordsData;
import com.shuangge.english.network.read.TaskReqReadWordList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.read.adapter.AdapterWordList;

public class FragmentHasLearnWords extends BaseWordListFragment {

	public FragmentHasLearnWords() {
		super();
	}

	private AdapterWordList adapter;

	private MyPullToRefreshListView lvWordsList;
	
	private Integer currentPage = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_word_list, null);
		lvWordsList = (MyPullToRefreshListView) view
				.findViewById(R.id.lvWordsList);
		
		adapter = new AdapterWordList(getActivity());
		refreshDatas();
		lvWordsList.setAdapter(adapter);
		lvWordsList.setMode(Mode.DISABLED);
		lvWordsList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		lvWordsList.setMode(Mode.BOTH);
		lvWordsList.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.refreshlvFooter1)); // 下拉刷新...
		lvWordsList.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.refreshlvFooter2)); // 放开刷新...
		lvWordsList.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.refreshlvFooter3));// 正在加载...

		lvWordsList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (lvWordsList.isNoReFreshing()) {
					//下拉
					if (lvWordsList.isFooterShown()) {
						requestDatas(currentPage + 1);
					}
					else if (lvWordsList.isHeaderShown()) {
						requestDatas(currentPage > 0 ? currentPage - 1 : 0);
					}
				} else {
					lvWordsList.onRefreshComplete();
				}
			}

		});

		return view;
	}

	private void requestDatas(final Integer pageNo) {
//		showLoading();
		new TaskReqReadWordList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
//				hideLoading();
				lvWordsList.onRefreshComplete2();
				if (null == result || !result) {
					return;
				}
				refreshDatas();
				currentPage = pageNo;
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

		}, pageNo);
	}

	public AdapterWordList getAdapter() {
		return adapter;
	}

	// 切换fragment 加载数据
	@Override
	protected void onInitDatas() {
		super.onInitDatas();
	}

	@Override
	public void onResume() {
		refreshDatas();
		super.onResume();
	}

	@SuppressLint("UseValueOf")
	private void refreshDatas() {
		if (null != adapter) {
			List<UserNewWordsData> freshWords = GlobalRes.getInstance().getBeans().getReadNewWordsResult().getDatas();
			currentPage = new Integer(GlobalRes.getInstance().getBeans().getReadNewWordsResult().getPageNo());
			adapter.setData(freshWords);
		}
	}

}
