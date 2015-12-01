package com.shuangge.english.view.group.fragment;

import java.util.Date;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.post.PostData;
import com.shuangge.english.network.post.TaskReqAddNice;
import com.shuangge.english.network.post.TaskReqMyPostDatas;
import com.shuangge.english.network.post.TaskReqRemoveNice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.post.AtyPostDetails;
import com.shuangge.english.view.post.AtyPostEdit;
import com.shuangge.english.view.post.adapter.AdapterPostForSettings;
import com.shuangge.english.view.post.adapter.AdapterPostForSettings.CallbackPost;

/**
 * 
 * @author Jeffrey
 *
 */
public class FragmentPost extends BaseClassFragment implements OnClickListener, OnItemClickListener, CallbackPost {
	
	public static final int MODULE_CLASS_DETAILS = 2;
	public static final int CODE_SEND_POST = 1;
	public static final int CODE_DELETE_POST = 2;
	
	private MyPullToRefreshListView refreshListView;
	private AdapterPostForSettings adapter;
	
	private Date timestamp;
	private Date lastDate;
	
	private int currentPosition;
	
	public FragmentPost() {
		super();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_post, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		
		refreshListView = (MyPullToRefreshListView) mainView.findViewById(R.id.pullRefreshList);
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
						String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						timestamp = null;
						requestData();
					} else if (refreshListView.isFooterShown()) {
						String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusDown();
						timestamp = lastDate;
						requestData();
					}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterPostForSettings(getActivity(), this);
		initTop();
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
		requestData();
	}
	
	private void initTop() {
		if (adapter.getDatas().size() > 0) {
			adapter.getDatas().set(0, null);
			return;
		}
		adapter.getDatas().add(null);
	}
	
	private void refreshClassData() {
		adapter.notifyDataSetChanged();
	}
	
	private void requestData() {
		showLoading();
		new TaskReqMyPostDatas(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				List<PostData> posts = GlobalRes.getInstance().getBeans().getPostDatas().getPosts();
				if (posts.size() > 0) {
					lastDate = posts.get(posts.size() - 1).getCreateDate();
				}
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				if (refreshListView.isReFreshingForUp()) {
					refreshDatas(posts);
					return;
				}
				addDatas(posts);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, timestamp);
	}
	
	private void refreshDatas(List<PostData> datas) {
		adapter.getDatas().clear();
		initTop();
		addDatas(datas);
	}
	
	private void addDatas(List<PostData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		if (position == 1) {
//			startActivityForResult(new Intent(AtyPost.this, AtyClassInfo.class), MODULE_CLASS_DETAILS);
//			return;
//		}
		currentPosition = position - 1;
		PostData data = (PostData) adapter.getDatas().get(currentPosition);
		if (null == data) {
			return;
		}
		GlobalRes.getInstance().getBeans().setCurrentPostNo(data.getPostNo());
		AtyPostDetails.startAtyForMe(getActivity());
	}
	
	@Override
	public void onClick(View v) {
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refreshClassData();		
		if (requestCode == AtyPostEdit.REQUEST_EDIT_POST_MINE && resultCode == CODE_SEND_POST) {
			adapter.getDatas().add(1, GlobalRes.getInstance().getBeans().getPostDatas().getPosts().get(0));
			adapter.notifyDataSetChanged();
			refreshListView.onRefreshComplete2();
			return;
		}
		if (requestCode == AtyPostDetails.REQUEST_CLASS_POST_DETAILS_MINE) {
			if (resultCode == CODE_DELETE_POST) {
				adapter.getDatas().remove(currentPosition);
			}
			else if (null != GlobalRes.getInstance().getBeans().getPostData().getData()) {
				adapter.getDatas().set(currentPosition, GlobalRes.getInstance().getBeans().getPostData().getData());
			}
			adapter.notifyDataSetChanged();
			return;
		}
	}

	@Override
	public void addNice(final PostData post) {
		new TaskReqAddNice(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (null != adapter && null != result && result) {
					post.setNiceForMe(true);
					post.setNiceNum(post.getNiceNum() + 1);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, post.getPostNo());
	}
	
	@Override
	public void removeNice(final PostData post) {
		new TaskReqRemoveNice(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (null != adapter && null != result && result) {
					post.setNiceForMe(false);
					post.setNiceNum(post.getNiceNum() - 1);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, post.getPostNo());
	}
	
}
