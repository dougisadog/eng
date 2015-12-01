package com.shuangge.english.view.shop.fragment;

import java.util.Date;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.MyPullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.network.shop.TaskReqGoodsList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.secretmsg.AtySecretDetails;
import com.shuangge.english.view.secretmsg.adapter.AdapterFriend;
import com.shuangge.english.view.shop.AtyShopItemDetail;
import com.shuangge.english.view.shop.AtyShopList;
import com.shuangge.english.view.shop.adapter.AdapterShopItem;

/**
 * 
 * @author Jeffrey
 *
 */
public class FragmentShopCredits extends BaseShopFragment implements OnClickListener, OnItemClickListener {
	
	private static int TYPE_REAL = 1;
	private static int TYPE_VIRTUAL = 0;
	private static int TYPE_BOTH = -1;
	
	private static int PAYTYPE_CREDITS = 1;
	private static int PAYTYPE_CASH = 0;
	private static int PAYTYPE_BOTH = -1;
	
	private GridView gridList;
//	private MultiColumnListView listView;
	private MyPullToRefreshGridView pullRefreshList;
	private AdapterShopItem adapter;

	private View mainView;
	private boolean requesting = false;
	
	public FragmentShopCredits() {
		super();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_shop, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		adapter = new AdapterShopItem(getActivity());
		
//		gridList = (GridView) mainView.findViewById(R.id.gridList);
//		gridList.setAdapter(adapter);
//		gridList.setOnItemClickListener(this);
		
		pullRefreshList = (MyPullToRefreshGridView) mainView.findViewById(R.id.pullRefreshList);
		pullRefreshList.setAdapter(adapter);
		pullRefreshList.setOnItemClickListener(this);
		pullRefreshList.setMode(Mode.DISABLED);
//		listView = (MultiColumnListView) mainView.findViewById(R.id.listView);
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(null);
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
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (beans.getGoodsDatasCredits().size() > 0) {
			AtyShopList.last = beans.getGoodsDatasCredits().get(beans.getGoodsDatasCredits().size()-1).getGoodsId();
		}
		showLoading();
		new TaskReqGoodsList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				List<GoodsData> goods = GlobalRes.getInstance().getBeans().getGoodsDatasCredits();
				GlobalRes.getInstance().getBeans().setPayType(PAYTYPE_CREDITS);
				if (goods.size() > 0) {
					refreshDatas(goods);
				}
				goods.clear();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, TYPE_BOTH, PAYTYPE_CREDITS);
	}
	
	private void refreshDatas(List<GoodsData> datas) {
		if (adapter.getDatas() != null) {
			adapter.getDatas().clear();
		}
		addDatas(datas);
	}
	
	private void addDatas(List<GoodsData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
//		refreshListView.onRefreshComplete2();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AtyShopItemDetail.startAty(getActivity(), ((GoodsData) adapter.getItem(position)).getGoodsId(), PAYTYPE_CREDITS);
	}
	
}
