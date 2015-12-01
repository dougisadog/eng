package com.shuangge.english.view.shop.fragment;

import java.util.Date;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.MyPullToRefreshGridView;
import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.dao.DaoAttentionDataCache;
import com.shuangge.english.entity.server.msg.UserGroupMsgData;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.group.TaskReqGroupMsgReply;
import com.shuangge.english.network.msg.TaskReqGroupMsg;
import com.shuangge.english.network.secretmsg.TaskReqAttentions;
import com.shuangge.english.network.secretmsg.TaskReqFans;
import com.shuangge.english.network.shop.TaskReqGoodsList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.component.dialog.DialogApplyWithJoinClassFragment;
import com.shuangge.english.view.component.dialog.DialogGroupApplyWithJoinClassFragment;
import com.shuangge.english.view.group.AtyClassSearch;
import com.shuangge.english.view.msg.adapter.AdapterClassMsg;
import com.shuangge.english.view.msg.adapter.AdapterClassMsg.CallBackClassMsg;
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
public class FragmentShopCash extends BaseShopFragment implements OnClickListener, OnItemClickListener {
	
	private static int TYPE_REAL = 1;
	private static int TYPE_VIRTUAL = 0;
	private static int TYPE_BOTH = -1;
	
	private static int PAYTYPE_CREDITS = 1;
	private static int PAYTYPE_CASH = 0;
	private static int PAYTYPE_BOTH = -1;
	
	private GridView gridList;
	private AdapterShopItem adapter;
	private MyPullToRefreshGridView pullRefreshList;
	private View mainView;
	private boolean requesting = false;
	
	public FragmentShopCash() {
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
		if (beans.getGoodsDatasCash().size() > 0) {
			AtyShopList.last = beans.getGoodsDatasCash().get(beans.getGoodsDatasCash().size()-1).getGoodsId();
		}
		showLoading();
		new TaskReqGoodsList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				List<GoodsData> goods = GlobalRes.getInstance().getBeans().getGoodsDatasCash();
				GlobalRes.getInstance().getBeans().setPayType(PAYTYPE_CASH);
				if (goods.size() > 0) {
					refreshDatas(goods);
				}
				goods.clear();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		}, TYPE_BOTH, PAYTYPE_CASH);
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
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		AtyShopItemDetail.startAty(getActivity(), ((GoodsData) adapter.getItem(position)).getGoodsId(), PAYTYPE_CASH);
	}
	
}
