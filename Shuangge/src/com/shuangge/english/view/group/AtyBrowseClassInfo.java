package com.shuangge.english.view.group;

import java.util.ArrayList;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.network.group.TaskReqClassData;
import com.shuangge.english.network.group.TaskReqClassMembers;
import com.shuangge.english.network.group.TaskReqUserApply;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.binding.AtyBindingAccount;
import com.shuangge.english.view.component.dialog.DialogConfirmWithWeChatNoFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmWithWeChatNoFragment.CallBackDialogConfirmWithWeChatNo;
import com.shuangge.english.view.component.photograph.AtyPhotoBrowser;
import com.shuangge.english.view.group.component.MembersContainer;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyBrowseClassInfo extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_BROWSE_CLASS = 1019;
	
	public static final String PARAM_INDEX = "param index";
	public final static int MODULE_MEMBERS = 3;
	
	private boolean requesting = false;
	
	private ImageButton btnBack;
	private MembersContainer membersContainer;
	private TextView txtMemberNum, txtLastRanklist, txtClassName, txtSignature, txtWeekRanklist, txtWeekScore;
	private RelativeLayout rlJoin;
	private LinearLayout llMembers, llBg;
	
	private ViewFlow viewFlow;
	private CircleFlowIndicator indic;
	private ArrayList<String> urls = new ArrayList<String>();
	
	private DialogConfirmWithWeChatNoFragment userApplyDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_browse_classinfo);
		llBg = (LinearLayout) findViewById(R.id.llBg);
		llBg.setVisibility(View.GONE);
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		viewFlow = (ViewFlow) findViewById(R.id.vf);
		indic = (CircleFlowIndicator) findViewById(R.id.vfiDic);
		
		membersContainer = (MembersContainer) findViewById(R.id.membersContainer);
		
		txtLastRanklist = (TextView) findViewById(R.id.txtLastRanklist);
		txtClassName = (TextView) findViewById(R.id.txtClassName);
		txtSignature = (TextView) findViewById(R.id.txtSignature);
		txtWeekRanklist = (TextView) findViewById(R.id.txtWeekRanklist);
		txtWeekScore = (TextView) findViewById(R.id.txtWeekScore);
		txtMemberNum = (TextView) findViewById(R.id.txtMemberNum);
		
		
		rlJoin = (RelativeLayout) findViewById(R.id.rlJoin);
		rlJoin.setOnClickListener(this);
		llMembers = (LinearLayout) findViewById(R.id.llMembers);
		llMembers.setOnClickListener(this);
		
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		rlJoin.setVisibility(beans.getMyGroupDatas().getClassInfos().size() == 0
				|| !beans.isMetenUser() ? View.VISIBLE : View.GONE);
	}
	
	protected void initRequestData() {
		super.initRequestData();
		if (requesting) {
			return;
		}
		requesting = true;
		showLoading();
		GlobalRes.getInstance().getBeans().setCurrentClassNo(getIntent().getLongExtra(PARAM_INDEX, -1));
		new TaskReqClassData(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				hideLoading();
				if (null == result || !result) {
					return;
				}
				llBg.setVisibility(View.VISIBLE);
				AlphaAnimation animation = new AlphaAnimation(0, 1);
				animation.setDuration(500);
				llBg.startAnimation(animation);
				ClassData data = GlobalRes.getInstance().getBeans().getClassData().getClassInfo();
				if (data.getPhotoUrls().size() > 0) {
					urls = (ArrayList<String>) data.getPhotoUrls();
				}
				if (data.getPhotoUrls().size() > 1) {
					indic.setVisibility(View.VISIBLE);
				}
				else {
					indic.setVisibility(View.GONE);
				}
				viewFlow.setAdapter(new ImageAdapter(AtyBrowseClassInfo.this));
				viewFlow.setFlowIndicator(indic);
				
				txtClassName.setText(data.getName() + "");
				txtMemberNum.setText("+" + data.getNum());
				txtWeekRanklist.setText(data.getNo() < 0 ? "无" : data.getNo() + "");
				txtWeekScore.setText(data.getWeekScore() + "");
				if (TextUtils.isEmpty(data.getSignature())) {
					txtSignature.setVisibility(View.GONE);
				}
				txtSignature.setText(TextUtils.isEmpty(data.getSignature()) ? "无" : data.getSignature());
				String lastRanklistStr = "100+";
				if (null != data.getLastWeekNo() && data.getLastWeekNo() > 0 && data.getLastWeekNo() <= 100) {
					lastRanklistStr = data.getLastWeekNo().toString();
				}
				txtLastRanklist.setText(lastRanklistStr);
				
				requestMembersData();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void requestMembersData() {
		if (requesting) {
			return;
		}
		requesting = true;
		new TaskReqClassMembers(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
				if (null != result && result) {
					membersContainer.setUrls(GlobalRes.getInstance().getBeans().getMemberData().getMembers());
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnBack:
				this.finish();
				break;
			case R.id.rlJoin:
				if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().isVisitor()) {
					startActivity(new Intent(AtyBrowseClassInfo.this, AtyBindingAccount.class));
					return;
				}
				if (null != userApplyDialog && userApplyDialog.isVisible()) {
					return;
				}
				ClassData data = GlobalRes.getInstance().getBeans().getClassData().getClassInfo();
				userApplyDialog = new DialogConfirmWithWeChatNoFragment(new CallBackDialogConfirmWithWeChatNo() {
					@Override
					public void cancel() {
						// TODO Auto-generated method stub
						userApplyDialog.dismiss();
						userApplyDialog = null;
					}
					@Override
					public void submit(int position, String msg,String weChatNo) {
						if (requesting) {
							return;
						}
						requesting = true;
						userApplyDialog.dismiss();
						userApplyDialog = null;
						showLoading();
						new TaskReqUserApply(0, new CallbackNoticeView<Void, Boolean>() {

							@Override
							public void refreshView(int tag, Boolean result) {
								requesting = false;
								hideLoading();
								if (null == result || !result) {
									return;
								}
								Toast.makeText(AtyBrowseClassInfo.this, R.string.applySuccessTip, Toast.LENGTH_SHORT).show();
								
							}

							@Override
							public void onProgressUpdate(int tag, Void[] values) {
							}
							
						}, msg,weChatNo);
					}
				}, data.getWechatNo(),0);
				userApplyDialog.showDialog(getSupportFragmentManager());
				break;
			case R.id.llMembers:
//				if (GlobalRes.getInstance().getBeans().getCurrentClassNo().longValue() == GlobalRes.getInstance().getBeans().getCurrentMyClassNo().longValue()
//					&& GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() > ClassData.SUB_MANAGER) {
//					startActivity(new Intent(AtyBrowseClassInfo.this, AtyClassMember.class));
//				}
//				else {
					startActivity(new Intent(AtyBrowseClassInfo.this, AtyClassMember.class));
//				}
				break;
		}
	}
	
	
	
	public class ImageAdapter extends BaseAdapter {
		
		public ImageAdapter(Context context) {
		}
	
		@Override
		public int getCount() {
			return urls.size() == 0 ? 1 : urls.size();
		}
	
		@Override
		public Object getItem(int position) {
			return position;
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView img = null;
			if (null == convertView) {
				img = new ImageView(AtyBrowseClassInfo.this);
				img.setScaleType(ScaleType.CENTER_CROP);
				img.setOnClickListener(onClickPicListener);
			}
			else {
				img = (ImageView) convertView;
			}
			img.setTag(position);
			if (urls.size() == 0) {
				img.setImageResource(R.drawable.bg_class_home);
			}
			else {
				GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(urls.get(position), img));
			}
			return img;
		}

	}
	
	private OnClickListener onClickPicListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (urls.size() == 0) {
				return;
			}
			AtyPhotoBrowser.startAty(AtyBrowseClassInfo.this, (Integer) v.getTag(), urls);
		}
		
	};
	
	/************************************************************************************************/
	
	public static void startAty(Activity context, Long classNo) {
		Intent i = new Intent(context, AtyBrowseClassInfo.class);
		i.putExtra(AtyBrowseClassInfo.PARAM_INDEX, classNo);
		context.startActivityForResult(i, REQUEST_BROWSE_CLASS);
	}

}
