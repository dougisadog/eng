package com.shuangge.english.view.group;

import java.util.ArrayList;
import java.util.Date;
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
import android.widget.Toast;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.post.PostData;
import com.shuangge.english.entity.server.user.RewardsData;
import com.shuangge.english.network.post.TaskReqAddNice;
import com.shuangge.english.network.post.TaskReqClassPostDatas;
import com.shuangge.english.network.post.TaskReqRemoveNice;
import com.shuangge.english.network.rewards.TaskReqRewardsShare;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.ComponentTitleBar;
import com.shuangge.english.view.component.photograph.AtyPhotoBrowser;
import com.shuangge.english.view.post.AtyPostDetails;
import com.shuangge.english.view.post.AtyPostEdit;
import com.shuangge.english.view.post.adapter.AdapterPostForClass;
import com.shuangge.english.view.post.adapter.AdapterPostForClass.CallbackPost;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyClassHome extends AbstractAppActivity implements OnClickListener, OnItemClickListener, IPushMsgNotice, CallbackPost {
	
	public static final int REQUEST_CLASS_HOME = 1004;
	
	public static final int MODULE_SEND_POST = 1;
	public static final int CODE_SEND_POST = 1;
	public static final int CODE_DELETE_POST = 2;
	
	private ComponentTitleBar titleBar;
	private ImageButton btnBack;

	private MyPullToRefreshListView refreshListView;
	private AdapterPostForClass adapter;
	
	private Date timestamp;
	private Date lastDate;
	
	private int currentPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		adapter = null;
		refreshListView = null;
	}
	
	public void notice() {
		boolean flag = GlobalRes.getInstance().getBeans().getMsgDatas().isClassMsg();
		if (flag) {
			titleBar.showSettingMsgs();
		}
		else {
			titleBar.clearSettingMsgs();
		}
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_class_home);
		titleBar = (ComponentTitleBar) findViewById(R.id.titleBar);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		findViewById(R.id.btnSettings).setOnClickListener(this);
		findViewById(R.id.btnSettings2).setOnClickListener(this);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
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
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						timestamp = null;
						requestData();
					} else if (refreshListView.isFooterShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
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
		
		adapter = new AdapterPostForClass(this, this);
		refreshListView.setAdapter(adapter);
		refreshListView.setOnItemClickListener(this);
		
		notice();
		initClassSummary();
		adapter.notifyDataSetChanged();
		requestData();
	}
	
	private void refreshClassData() {
		notice();
		initClassSummary();
		adapter.notifyDataSetChanged();
	}
	
	private void requestData() {
		showLoading();
		new TaskReqClassPostDatas(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				List<PostData> posts = GlobalRes.getInstance().getBeans().getPostDatas().getPosts();
				if (posts.size() > 0) {
					lastDate = posts.get(posts.size() - 1).getCreateDate();
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
	
	private void initClassSummary() {
		if (adapter.getDatas().size() > 0) {
			adapter.getDatas().set(0, GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0));
			return;
		}
		adapter.getDatas().add(GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0));
	}
	
	private void refreshDatas(List<PostData> datas) {
		adapter.getDatas().clear();
		initClassSummary();
		addDatas(datas);
	}
	
	private void addDatas(List<PostData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == 1) {
			return;
		}
		currentPosition = position - 1;
		PostData postData = (PostData) adapter.getDatas().get(currentPosition);
		GlobalRes.getInstance().getBeans().setCurrentPostNo(postData.getPostNo());
		AtyPostDetails.startAtyForClass(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
//		case R.id.llSearchClass:
//			startActivityForResult(new Intent(AtyClassHome.this, AtyClassSearch.class), 0);
//			break;
		case R.id.btnSettings:
		case R.id.btnSettings2:
			AtyClassSettings.startAty(this);
			break;
		case R.id.rlInvite:
			AtyClassInvite.startAty(this);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refreshClassData();		
		if (requestCode == AtyPostEdit.REQUEST_EDIT_POST_CLASS && resultCode == CODE_SEND_POST) {
			adapter.getDatas().add(1, GlobalRes.getInstance().getBeans().getPostDatas().getPosts().get(0));
			adapter.notifyDataSetChanged();
			refreshListView.onRefreshComplete2();
			return;
		}
		if (requestCode == AtyClassSettings.REQUEST_CLASS_SETTINGS && resultCode == CODE_QUIT) {
			this.finish();
			return;
		}
		if (requestCode == AtyPostDetails.REQUEST_CLASS_POST_DETAILS_CLASS) {
			if (resultCode == CODE_DELETE_POST) {
				adapter.getDatas().remove(currentPosition);
			}
			else if (null != GlobalRes.getInstance().getBeans().getPostData() && 
					null != GlobalRes.getInstance().getBeans().getPostData().getData()) {
				adapter.getDatas().set(currentPosition, GlobalRes.getInstance().getBeans().getPostData().getData());
			}
			adapter.notifyDataSetChanged();
			return;
		}
	}
	
	
	/************************************************************************************************/
	
	public void setCanScoll(boolean canScroll) {
		refreshListView.setCanScroll(canScroll);
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

	@Override
	public void showBg(ArrayList<String> urls, int index) {
		AtyPhotoBrowser.startAty(this, (Integer) index, urls);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		BaseResp resp = GlobalRes.getInstance().getBeans().getWxResp();
		if(resp == null) {
			return;
		}
		if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
			GlobalRes.getInstance().getBeans().setWxResp(null);
			showLoading();
			classShare();
		}
	}
	
	private boolean classShare(){
		new TaskReqRewardsShare(0, new CallbackNoticeView<Void, Integer>() {

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

			@Override
			public void refreshView(int tag, Integer result) {
				hideLoading();
				if (null == result || result == TaskReqRewardsShare.STAUTS_NETWROK_ERR) {
					Toast.makeText(AtyClassHome.this, R.string.dialogRewardsErrTip, Toast.LENGTH_SHORT).show();
					return;
				}
				if (result == TaskReqRewardsShare.STAUTS_SUCCESS) {
					GlobalRes.getInstance().getBeans().getLoginData().getRewardsData().setAuth(RewardsData.RECEIVED);
					adapter.getClassRewards().setVisibility(View.GONE);
					double getMoney = GlobalRes.getInstance().getBeans().getLoginData().getRewardsData().getPersonRewards();
					//如果返回0元没有
					if (getMoney != 0) {
						Toast.makeText(AtyClassHome.this, "上周您获得的"+ getMoney +"元奖学金已打入个人账户，请在个人中心查看", Toast.LENGTH_LONG).show();
					}
					return;
				}
				Toast.makeText(AtyClassHome.this, "您已经领取过，请勿重复领取!", Toast.LENGTH_LONG).show();	
			}
			
		});
		
		return false;
		}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyClassHome.class);
		context.startActivityForResult(i, REQUEST_CLASS_HOME);
	}

}
