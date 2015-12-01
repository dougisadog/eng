package com.shuangge.english.view.secretmsg;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.MyPullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.dao.DaoSecretMsgDataCache;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.network.attention.TaskReqAttention;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgCreateWithMsg;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgCreateWithPic;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgDeleteDetail;
import com.shuangge.english.network.secretmsg.TaskReqSecretMsgDetails;
import com.shuangge.english.network.secretmsg.TaskSyncSecretMsgList;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.ComponentTitleBar;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.component.MaskImage;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.photograph.AtyPhotograph;
import com.shuangge.english.view.secretmsg.adapter.AdapterSecretReply;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

public class AtySecretDetails extends AbstractAppActivity implements 
	OnClickListener, OnItemLongClickListener, OnItemClickListener , IPushMsgNotice {

	public static final int REQUEST_SECRET_DETAILS = 1076;
	
	public static final int MODULE_PHOTO = 100;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private EditTextWidthTips etReplyContent;
	private ImageView btnAdd, btnSubmit, btnInfo;
	private MaskImage imgUpload;
	private ComponentTitleBar titleBar;
	
	private MyPullToRefreshListView refreshListView;
	private AdapterSecretReply adapter;
	
	private Date minTime;
	
	private DialogConfirmFragment dialogDeleteReply;
	private DialogConfirmFragment dialogAttention;
	
	private Long friendNo;
	private String friendName;
	private String friendHeadUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_secret_details);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		etReplyContent = (EditTextWidthTips) findViewById(R.id.etReplyContent);
		etReplyContent.setOnKeyListener(onKeyListener);
		btnSubmit = (ImageView) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		
		btnAdd = (ImageView) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		
		titleBar = (ComponentTitleBar) findViewById(R.id.titleBar);
		
		imgUpload = (MaskImage) findViewById(R.id.imgUpload);
		imgUpload.setOnClickListener(this);
		imgUpload.setVisibility(View.GONE);
		
		btnInfo = (ImageView) findViewById(R.id.btnInfo);
		btnInfo.setOnClickListener(this);
		
		refreshListView = (MyPullToRefreshListView) findViewById(R.id.pullRefreshList);
		// set mode to BOTH
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
						String label = DateUtils.formatDateTime( getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusUp();
						loadNextData();
					} else if (refreshListView.isFooterShown()) {
						String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						refreshListView.setStatusDown();
						requestData();
					}
				} else {
					refreshListView.onRefreshComplete();
				}
			}

		});
		
		refreshListView.setOnItemClickListener(this);
		refreshListView.getRefreshableView().setOnItemLongClickListener(this);
		
		adapter = new AdapterSecretReply(this);
		refreshListView.setAdapter(adapter);
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		friendNo = beans.getCurrentFriendNo();
		beans.setCurrentUserNo(friendNo);
		friendName = beans.getCurrentFriendName();
		friendHeadUrl = beans.getCurrentFriendHeadUrl();
		
		titleBar.setTitle(friendName, null);
		if (null == beans.getSecretListDatas()) {
			showLoading();
			new TaskSyncSecretMsgList(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						finish();
						return;
					}
					loadCurrentData();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
			return;
		}
		loadCurrentData();
	}
	
	private void loadCurrentData() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
		List<SecretMsgDetailData> datas = dao.getMsgDetails(beans.getLoginName(), friendNo, minTime);
		Collections.reverse(datas);
		beans.getSecretDetailsDatas().clear();
		beans.getSecretDetailsDatas().addAll(datas);
		refreshDatas();
		notice();
		refreshListView.getRefreshableView().setSelection(adapter.getCount() - 1);
	}
	
	private void loadNextData() {
		CacheBeans cacheBeans = GlobalRes.getInstance().getBeans();
		DaoSecretMsgDataCache dao = new DaoSecretMsgDataCache();
		List<SecretMsgDetailData> datas = dao.getMsgDetails(cacheBeans.getLoginName(), cacheBeans.getCurrentFriendNo(), minTime);
		cacheBeans.getSecretDetailsDatas().addAll(datas);
		Collections.sort(cacheBeans.getSecretDetailsDatas());
		Collections.reverse(cacheBeans.getSecretDetailsDatas());
		if (GlobalRes.getInstance().getBeans().getSecretDetailsDatas().size() == 0) {
			return;
		}
		int size = GlobalRes.getInstance().getBeans().getSecretDetailsDatas().size() - adapter.getCount();
		refreshDatas();
		refreshListView.getRefreshableView().setSelection(size);
		
//		new TaskLocalSecretMsgDetails(0, new CallbackNoticeView<Void, Boolean>() {
//
//			@Override
//			public void refreshView(int tag, Boolean result) {
//				if (null == result || !result) {
//					return;
//				}
//				if (GlobalRes.getInstance().getBeans().getSecretDetailsDatas().size() == 0) {
//					return;
//				}
//				int size = GlobalRes.getInstance().getBeans().getSecretDetailsDatas().size() - adapter.getCount();
//				refreshDatas();
//				refreshListView.getRefreshableView().setSelection(size);
//			}
//
//			@Override
//			public void onProgressUpdate(int tag, Void[] values) {
//			}
//			
//		}, minTime);
	}
	
	private void requestData() {
		if (requesting) {
			return;
		}
		requesting = true;
		new TaskReqSecretMsgDetails(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				if (isFinishing())
					return;
				requesting = false;
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				GlobalRes.getInstance().getBeans().getMsgDatas().setSecretMsg(0);
				refreshDatas();
				if (adapter.getCount() - refreshListView.getRefreshableView().getLastVisiblePosition() < 10) {
					refreshListView.getRefreshableView().setSelection(adapter.getCount() - 1);
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void refreshDatas() {
		adapter.getDatas().clear();
 		addDatas(GlobalRes.getInstance().getBeans().getSecretDetailsDatas());
 		if (adapter.getDatas().size() == 0) {
			return;
		}
		minTime = adapter.getDatas().get(0).getSendTime();
	}
	
	private void addDatas(List<SecretMsgDetailData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				GlobalRes.getInstance().getBeans().setCurrentFriendNo(null);
				this.finish();
				break;
			case R.id.btnAdd:
				Intent i = new Intent(AtySecretDetails.this, AtyPhotograph.class);
				i.putExtra(AtyPhotograph.NO_DELETE, true);
				startActivityForResult(i, MODULE_PHOTO);
				break;
			case R.id.imgUpload:
				startActivityForResult(new Intent(AtySecretDetails.this, AtyPhotograph.class), MODULE_PHOTO);
				break;
			case R.id.btnSubmit:
				sendMsg();
				break;
			case R.id.btnInfo:
				AtyBrowseUserInfo.startAty(AtySecretDetails.this, friendNo);
				break;
		}
	}
	
	private void sendMsg() {
		if (TextUtils.isEmpty(etReplyContent.getVal())) {
			Toast.makeText(this, getResources().getString(R.string.replyErrTip1), Toast.LENGTH_SHORT).show();
			return;
		}
		InputUitls.closeInputMethod(this, etReplyContent);
		String content = etReplyContent.getVal();
		SecretMsgDetailData msg = createOneMsg(content);
		new TaskReqSecretMsgCreateWithMsg(0, new CallbackNoticeView<Void, Integer>() {

			@Override
			public void refreshView(int tag, Integer result) {
				GlobalRes.getInstance().getBeans().getMsgDatas().setSecretMsg(0);
				refreshDatas();
				if (null == result) {
					return;
				}
				refreshListView.getRefreshableView().setSelection(adapter.getCount() - 1);
//				if (result == RestResult.C_ATTENTION_NO) {
//					dialogAttention = new DialogConfirmFragment(callback3, getString(R.string.attetionMsgEn), getString(R.string.attetionMsgCn),0);
//					dialogAttention.showDialog(getSupportFragmentManager());
//				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, friendNo, content, msg);
	}
	
	private SecretMsgDetailData createOneMsg(String content) {
		return createOneMsg(content, null);
	}
	
	private SecretMsgDetailData createOneMsg(Bitmap bitmap) {
		return createOneMsg(null, bitmap);
	}
	
	private SecretMsgDetailData createOneMsg(String content, Bitmap bitmap) {
		SecretMsgDetailData data = new SecretMsgDetailData();
		data.setSecretMsgNo(0L);
		data.setStatus(SecretMsgDetailData.STATUS_LOADING);
		data.setSendTime(com.shuangge.english.support.utils.DateUtils.getServerTime());
		data.setContent(content);
		data.setSenderNo(GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo());
		data.setReceiverNo(friendNo);
		data.setFriendNo(friendNo);
		data.setFriendName(friendName);
		data.setFriendHeadUrl(friendHeadUrl);
		GlobalRes.getInstance().getBeans().getSecretDetailsDatas().add(data);
		etReplyContent.setVal("");
		etReplyContent.clearFocus();
		btnAdd.setVisibility(View.VISIBLE);
		refreshDatas();
		refreshListView.getRefreshableView().setSelection(adapter.getCount() - 1);
		return data;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AtyPhotograph.CODE_BITMAP:
			byte[] bytes = data.getByteArrayExtra(AtyPhotograph.CALLBACK_DATAS_BYTES);
			Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			SecretMsgDetailData msg = createOneMsg(photo);
			new TaskReqSecretMsgCreateWithPic(0, new CallbackNoticeView<Void, Integer>() {

				@Override
				public void refreshView(int tag, Integer result) {
					GlobalRes.getInstance().getBeans().getMsgDatas().setSecretMsg(0);
					refreshDatas();
					if (null == result) {
						return;
					}
					refreshListView.getRefreshableView().setSelection(adapter.getCount() - 1);
//					if (result == RestResult.C_ATTENTION_NO) {
//						dialogAttention = new DialogConfirmFragment(callback3, getString(R.string.attetionMsgEn), getString(R.string.attetionMsgCn),0);
//						dialogAttention.showDialog(getSupportFragmentManager());
//					}
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, friendNo, bytes, msg);
			break;
//		case AtyPhotograph.CODE_DELETE:
//			GlobalRes.getInstance().getBeans().getCurrentPhotoParm().setBytes(null);
//			btnAdd.setVisibility(View.VISIBLE);
//			imgUpload.setVisibility(View.GONE);
//			break;
		case AtyUserInfo.REQUEST_USER_INFO:
			notice();
			break;
		case AtyBrowseUserInfo.REQUEST_BROWSE_USER_INFO:
			notice();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		etReplyContent.clearFocus();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		etReplyContent.clearFocus();
		dialogDeleteReply = new DialogConfirmFragment(callback2, getString(R.string.replyDeleteTipEn), getString(R.string.replyDeleteTipCn), position - 1);
		dialogDeleteReply.showDialog(getSupportFragmentManager());
		return true;
	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
			showLoading();
			SecretMsgDetailData prevData = null;
			SecretMsgDetailData data = (SecretMsgDetailData) adapter.getItem(position);
			GlobalRes.getInstance().getBeans().setCurrentSecretMsgNo(data.getSecretMsgNo());
			
			hideDialogDeleteReply();
			adapter.getDatas().remove(position);
			adapter.notifyDataSetChanged();
			if (adapter.getDatas().size() > 0)
				prevData = (SecretMsgDetailData) adapter.getItem(position - 1);
			Toast.makeText(AtySecretDetails.this, R.string.deleteSuccessTip, Toast.LENGTH_SHORT).show();
			new TaskReqSecretMsgDeleteDetail(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result) {
						return;
					}
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
					
				}
				
			}, position, data, adapter.getDatas().size() == position, prevData);
		}
		
		@Override
		public void onCancel() {
			hideDialogDeleteReply();
		}

		@Override
		public void onKeyBack() {
			onCancel();
		}
		
	};
	
	private void hideDialogDeleteReply() {
		dialogDeleteReply.dismiss();
		dialogDeleteReply = null;
	}
	
	private CallBackDialogConfirm callback3 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
			showLoading();
			dialogAttention.dismiss();
			dialogAttention = null;
			new TaskReqAttention(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideLoading();
					if (null == result || !result || null == AtySecretDetails.this) {
						return;
					}
					Toast.makeText(AtySecretDetails.this, R.string.attetionSuccessTip, Toast.LENGTH_SHORT).show();
//					GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setAttention(true);
//					GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setBlackList(false);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
		}
		
		@Override
		public void onCancel() {
			dialogAttention.dismiss();
			dialogAttention = null;
		}

		@Override
		public void onKeyBack() {
			onCancel();
		}
		
	};
	
	/************************************************************************************************/
	
	private OnKeyListener onKeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//				/*隐藏软键盘*/
//				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//				if (inputMethodManager.isActive()) {
//					inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//				}
				sendMsg();
				return true;
			}
			return false;
		}
	};
	
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtySecretDetails.class);
		context.startActivityForResult(i, REQUEST_SECRET_DETAILS);
	}
	
	public static void startAty(Context context) {
		Intent i = new Intent(context, AtySecretDetails.class);
		context.startActivity(i);
	}

	@Override
	public void notice() {
		boolean flagSecret = GlobalRes.getInstance().getBeans().getMsgDatas().getSecretMsg() > 0;
		if (flagSecret) {
			requestData();
		}
	}

}
