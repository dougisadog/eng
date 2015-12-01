package com.shuangge.english.view.rewards;

import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.LastWeekDedicateData;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.network.rewards.TaskReqRewardsLastweekRank;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.rewards.adapter.AdapterRewardsLastweekRank;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyRewardsLastweekRank extends AbstractAppActivity implements OnClickListener, OnItemClickListener, CallbackNoticeView<Void, Boolean> {

	public static final int REQUEST_CLASS_MEMBERS_REWARD_RANK = 1073;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private MyPullToRefreshListView refreshListView;
	private AdapterRewardsLastweekRank adapter;
	
	private TextView txtNo, txtName, txtScore;
	private ImageView imgNo;
	private CircleImageView imgHead;
	private RelativeLayout rlUserInfo;
	
	private int pageNo = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
		
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_class_reward_rank);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		txtNo = (TextView) findViewById(R.id.txtNo);
		txtName = (TextView) findViewById(R.id.txtName);
		txtScore = (TextView) findViewById(R.id.txtScore);
		imgNo = (ImageView) findViewById(R.id.imgNo);
		imgHead = (CircleImageView) findViewById(R.id.imgHead);
		rlUserInfo = (RelativeLayout) findViewById(R.id.rlUserInfo);
		rlUserInfo.setOnClickListener(this);
		rlUserInfo.setVisibility(View.GONE);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		refreshListView.setMode(Mode.BOTH);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.refreshlvFooter1)); // 下拉刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.refreshlvFooter2)); //放开刷新...
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.refreshlvFooter3));//正在加载...

		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshListView.isNoReFreshing()) {
					if (refreshListView.isHeaderShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						pageNo = 1;
						requestDatas();
					} else if (refreshListView.isFooterShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusDown();
						++pageNo;
						requestDatas();
					}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		adapter = new AdapterRewardsLastweekRank(this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
//		refreshDatas(GlobalRes.getInstance().getBeans().getMemberData().getMembers());
	}
	
	protected void initRuqestData() {
		super.initRequestData();
		requestDatas();
	}
	
	private void requestDatas() {
		showLoading();
		new TaskReqRewardsLastweekRank(0, AtyRewardsLastweekRank.this, pageNo);
	}
	
	private void initMyself(InfoData entity, Integer no, Integer score) {
		if (null == no || null == score) {
			rlUserInfo.setVisibility(View.GONE);
			return;
		}
		rlUserInfo.setVisibility(View.VISIBLE);
		txtNo.setVisibility(View.INVISIBLE);
		imgNo.setVisibility(View.INVISIBLE);
		if (null != no && no.intValue() > 0) {
			if (no.intValue() < 4) {
				switch (no.intValue()) {
				case 1:
					imgNo.setImageResource(R.drawable.icon_ranklist_no1);
					break;
				case 2:
					imgNo.setImageResource(R.drawable.icon_ranklist_no2);
					break;
				case 3:
					imgNo.setImageResource(R.drawable.icon_ranklist_no3);
					break;
				}
				imgNo.setVisibility(View.VISIBLE);
			}
			else {
				String noStr = no.toString();
				if (no > 999) {
					noStr = "999+";
				}
				txtNo.setText(noStr);
				txtNo.setVisibility(View.VISIBLE);
			}
		}
		if (!TextUtils.isEmpty(entity.getName())) {
			txtName.setText(entity.getName().toString());
		}
		if (null != score) {
			txtScore.setText(score.toString());
		}
		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(), imgHead));
		}
		else {
			imgHead.clear();
		}
	}
	
	private void refreshDatas(List<LastWeekDedicateData> datas) {
		adapter.getDatas().clear();
		addDatas(datas);
		refreshListView.getRefreshableView().setSelection(1);
	}
	
	private void addDatas(List<LastWeekDedicateData> datas) {
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
		case R.id.rlUserInfo:
			AtyUserInfo.startAty(this);
			break;
		}
	}
	
//	private void clearPrevType() {
//		pageNo = 1;
//	}
	
	@Override
	public void onProgressUpdate(int tag, Void[] values) {
		
	}

	@Override
	public void refreshView(int tag, Boolean result) {
		hideLoading();
		if (null == result || !result) {
			refreshListView.onRefreshComplete2();
			return;
		}
		if (refreshListView.isReFreshingForDown()) {
			addDatas(getRanklistData());
			return;
		}
		refreshDatas(getRanklistData());
//		refreshListView.startAnimation(animation);
	}
	
	private List<LastWeekDedicateData> getRanklistData() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (pageNo == 1) {
			initMyself(beans.getLoginData().getInfoData(), beans.getClassMemberRewardRankData().getMyNo(), beans.getClassMemberRewardRankData().getMyScore());
		}
		return beans.getClassMemberRewardRankData().getDatas();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		long userNo = adapter.getItem(position - 1).getUserNo();
		if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue() == userNo) {
			AtyUserInfo.startAty(this);
			return;
		}
		AtyBrowseUserInfo.startAty(this, userNo);
	}
	

	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyRewardsLastweekRank.class);
		context.startActivityForResult(i, REQUEST_CLASS_MEMBERS_REWARD_RANK);
	}
	
}
