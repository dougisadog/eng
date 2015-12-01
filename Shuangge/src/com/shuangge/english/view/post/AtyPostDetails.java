package com.shuangge.english.view.post;

import java.util.Date;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.entity.server.post.PostData;
import com.shuangge.english.entity.server.post.ReplyData;
import com.shuangge.english.network.post.TaskReqPostData;
import com.shuangge.english.network.post.TaskReqPostDelete;
import com.shuangge.english.network.post.TaskReqReply;
import com.shuangge.english.network.post.TaskReqReplyDatas;
import com.shuangge.english.network.post.TaskReqReplyDelete;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.InputUitls;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.component.MaskImage;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.dialog.DialogConfirmWithPwdFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmWithPwdFragment.CallBackDialogConfirmWithPwd;
import com.shuangge.english.view.component.photograph.AtyPhotograph;
import com.shuangge.english.view.group.AtyClassHome;
import com.shuangge.english.view.post.adapter.AdapterReply;

public class AtyPostDetails extends AbstractAppActivity implements OnClickListener, OnItemLongClickListener, OnItemClickListener {

	public static final int REQUEST_CLASS_POST_DETAILS_CLASS = 1008;
	public static final int REQUEST_CLASS_POST_DETAILS_MINE = 1010;
	
	public static final int MODULE_PHOTO = 100;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	
	private EditTextWidthTips etReplyContent;
	
	private ImageView btnAdd, btnSubmit, btnDelete;
	private MaskImage imgUpload;
	
	private MyPullToRefreshListView refreshListView;
	private AdapterReply adapter;
	
	private Date timestamp;
	private Date lastDate;
	
	private DialogConfirmWithPwdFragment dialogDeletePost;
	private DialogConfirmFragment dialogDeleteReply;
	
	private boolean canDetelePost = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_post_details);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnDelete = (ImageView) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);
		
		etReplyContent = (EditTextWidthTips) findViewById(R.id.etReplyContent);
		btnSubmit = (ImageView) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		
		btnAdd = (ImageView) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
		imgUpload = (MaskImage) findViewById(R.id.imgUpload);
		imgUpload.setOnClickListener(this);
		imgUpload.setVisibility(View.GONE);
		
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
		
		refreshListView.setOnItemClickListener(this);
		refreshListView.getRefreshableView().setOnItemLongClickListener(this);
		
		adapter = new AdapterReply(this);
		refreshListView.setAdapter(adapter);
		btnDelete.setVisibility(View.GONE);
	}
	
	protected void initRequestData() {
		super.initRequestData();
		requesting = true;
		showLoading();
		new TaskReqPostData(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				requesting = false;
				if (null == result || !result) {
					return;
				}
				//自己 和 管理员可以删除帖子
				if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() > ClassData.NORMAL 
						|| GlobalRes.getInstance().getBeans().getPostData().getData().getUserNo().longValue() == 
						GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue()) {
					btnDelete.setVisibility(View.VISIBLE);
					canDetelePost = true;
				}
				adapter.getDatas().add(GlobalRes.getInstance().getBeans().getPostData().getData());
				requestData(false);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void requestData() {
		requestData(true);
	}
	
	private void requestData(boolean showLoading) {
		if (showLoading) {
			if (requesting) {
				return;
			}
			requesting = true;
			showLoading();
		}
		new TaskReqReplyDatas(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				List<ReplyData> replys = GlobalRes.getInstance().getBeans().getReplyDatas().getReplys();
				if (replys.size() > 0) {
					lastDate = replys.get(replys.size() - 1).getCreateDate();
				}
				if (null == result || !result) {
					refreshListView.onRefreshComplete2();
					return;
				}
				if (refreshListView.isReFreshingForUp()) {
					refreshDatas(replys);
					return;
				}
				addDatas(replys);
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, timestamp);
	}
	
	private void refreshDatas(List<ReplyData> datas) {
		adapter.getDatas().clear();
		adapter.getDatas().add(GlobalRes.getInstance().getBeans().getPostData().getData());
		addDatas(datas);
	}
	
	private void addDatas(List<ReplyData> datas) {
		adapter.getDatas().addAll(datas);
		adapter.notifyDataSetChanged();
		refreshListView.onRefreshComplete2();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				this.finish();
				break;
			case R.id.btnDelete:
				dialogDeletePost = new DialogConfirmWithPwdFragment(callback1, getString(R.string.postDeleteTipEn),
						getString(R.string.postDeleteTipCn), 0);
				dialogDeletePost.showDialog(getSupportFragmentManager());
				break;
			case R.id.btnAdd:
				Intent i = new Intent(AtyPostDetails.this, AtyPhotograph.class);
				i.putExtra(AtyPhotograph.NO_DELETE, true);
				startActivityForResult(i, MODULE_PHOTO);
				break;
			case R.id.imgUpload:
				startActivityForResult(new Intent(AtyPostDetails.this, AtyPhotograph.class), MODULE_PHOTO);
				break;
			case R.id.btnSubmit:
				InputUitls.closeInputMethod(this, etReplyContent);
				if (TextUtils.isEmpty(etReplyContent.getVal())) {
					Toast.makeText(this, getResources().getString(R.string.replyErrTip1), Toast.LENGTH_SHORT).show();
					return;
				}
				if (requesting) {
					return;
				}
				requesting = true;
				GlobalReqDatas.getInstance().getUpdateReplyData().setContent(etReplyContent.getVal());
				new TaskReqReply(0, new CallbackNoticeView<Void, Boolean>() {

					@Override
					public void refreshView(int tag, Boolean result) {
						requesting = false;
						if (result) {
							PostData postData = GlobalRes.getInstance().getBeans().getPostData().getData();
							postData.setReplyNum(postData.getReplyNum() + 1);
							
							etReplyContent.setVal("");
							etReplyContent.clearFocus();
							GlobalReqDatas.getInstance().getUpdateReplyData().setReplyNo(null);
							etReplyContent.setTips(getString(R.string.replyPost));
							Toast.makeText(AtyPostDetails.this, getString(R.string.replyScuccessTip), Toast.LENGTH_SHORT).show();
							ReplyData data = GlobalRes.getInstance().getBeans().getReplyDatas().getReplys().get(
									GlobalRes.getInstance().getBeans().getReplyDatas().getReplys().size() - 1);
							adapter.getDatas().add(data);
							adapter.notifyDataSetChanged();
							refreshListView.onRefreshComplete2();
							//TODO:Jeffrey 分页之后判断 是否该跳转
							refreshListView.getRefreshableView().setSelection(adapter.getCount() - 1);
							btnAdd.setVisibility(View.VISIBLE);
							imgUpload.setVisibility(View.GONE);
							lastDate = data.getCreateDate();
						}
					}

					@Override
					public void onProgressUpdate(int tag, Void[] values) {
					}
				});
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AtyPhotograph.CODE_BITMAP:
			byte[] bytes = data.getByteArrayExtra(AtyPhotograph.CALLBACK_DATAS_BYTES);
			GlobalReqDatas.getInstance().getUpdateReplyData().getPhotoData().setBytes(bytes);
			Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			imgUpload.setBitmap(photo);
			btnAdd.setVisibility(View.GONE);
			imgUpload.setVisibility(View.VISIBLE);
			break;
		case AtyPhotograph.CODE_DELETE:
			GlobalReqDatas.getInstance().getUpdateReplyData().getPhotoData().setBytes(null);
			btnAdd.setVisibility(View.VISIBLE);
			imgUpload.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		etReplyContent.clearFocus();
		if (position == 1) {
			GlobalReqDatas.getInstance().getUpdateReplyData().setReplyNo(null);
			etReplyContent.setTips(getString(R.string.replyPost));
			return;
		}
		ReplyData data = (ReplyData)adapter.getItem(position - 1);
		etReplyContent.setTips(getString(R.string.replySb) + data.getUserName());
		GlobalReqDatas.getInstance().getUpdateReplyData().setReplyNo(data.getReplyNo());
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		etReplyContent.clearFocus();
		if (position == 1) {
			if (!canDetelePost) {
				return true;
			}
			dialogDeletePost = new DialogConfirmWithPwdFragment(callback1, getString(R.string.postDeleteTipEn),
					getString(R.string.postDeleteTipCn), 0);
			dialogDeletePost.showDialog(getSupportFragmentManager());
			return true;
		}
		if (((ReplyData) adapter.getItem(position - 1)).getUserNo().longValue() != 
				GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue()) {
			MyGroupListResult r = GlobalRes.getInstance().getBeans().getMyGroupDatas();
			if (null == r || r.getMyClassAuth() == ClassData.NORMAL) {
				return true;
			}
		}
		dialogDeleteReply = new DialogConfirmFragment(callback2, getString(R.string.replyDeleteTipEn), getString(R.string.replyDeleteTipCn), position - 1);
		dialogDeleteReply.showDialog(getSupportFragmentManager());
		return true;
	}
	
	private CallBackDialogConfirmWithPwd callback1 = new CallBackDialogConfirmWithPwd() {
		
		@Override
		public void submit(int position, String password) {
			if (requesting) {
				return;
			}
			requesting = true;
			new TaskReqPostDelete(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideDialogDeletePost();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyPostDetails.this, R.string.deleteSuccessTip, Toast.LENGTH_SHORT).show();
					AtyPostDetails.this.setResult(AtyClassHome.CODE_DELETE_POST);
					AtyPostDetails.this.finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, password);
		}
		
		@Override
		public void cancel() {
			hideDialogDeletePost();
		}
		
	};
	
	private int currentRelpyPosition;
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
			if (requesting) {
				return;
			}
			requesting = true;
			currentRelpyPosition = position;
			GlobalRes.getInstance().getBeans().setCurrentReplyNo(((ReplyData) adapter.getItem(position)).getReplyNo());
			new TaskReqReplyDelete(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
					hideDialogDeleteReply();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(AtyPostDetails.this, R.string.deleteSuccessTip, Toast.LENGTH_SHORT).show();
					PostData postData = GlobalRes.getInstance().getBeans().getPostData().getData();
					postData.setReplyNum(postData.getReplyNum() - 1);
					//TODO:jeffrey 删除回复 被回复的  也同步删除 优化
					ReplyData data = (ReplyData) adapter.getItem(currentRelpyPosition);
					data.setDeleted(true);
					adapter.notifyDataSetChanged();
					
					GlobalReqDatas.getInstance().getUpdateReplyData().setReplyNo(null);
					etReplyContent.setTips(getString(R.string.replyPost));
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
					
				}
				
			});
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
	
	private void hideDialogDeletePost() {
		dialogDeletePost.dismiss();
		dialogDeletePost = null;
	}
	
	private void hideDialogDeleteReply() {
		dialogDeleteReply.dismiss();
		dialogDeleteReply = null;
	}
	
	/************************************************************************************************/
	
	public static void startAtyForClass(Activity context) {
		Intent i = new Intent(context, AtyPostDetails.class);
		context.startActivityForResult(i, REQUEST_CLASS_POST_DETAILS_CLASS);
	}
	
	public static void startAtyForMe(Activity context) {
		Intent i = new Intent(context, AtyPostDetails.class);
		context.startActivityForResult(i, REQUEST_CLASS_POST_DETAILS_MINE);
	}

}
