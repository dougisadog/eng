package com.shuangge.english.view.user;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.network.attention.TaskReqAttention;
import com.shuangge.english.network.attention.TaskReqAttentionCancel;
import com.shuangge.english.network.group.TaskReqClassApply;
import com.shuangge.english.network.secretmsg.TaskReqBlacklistCreate;
import com.shuangge.english.network.secretmsg.TaskReqBlacklistRemove;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.component.dialog.DialogWithContentFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.dialog.DialogWithContentFragment.CallBackDialogWithContent;
import com.shuangge.english.view.secretmsg.AtySecretDetails;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogUserSetting extends DialogFragment1 implements OnClickListener {
	

	private FrameLayout flBg;
	private FrameLayout flSendMsg = null, flCancel = null, flInvite, flAttention, flBlackList;
	
	private TextView txtAttention, txtBlackList;
	
	private boolean isAttentioned = false;
	
	private boolean isBlacked = false;
	
	private boolean requesting = false;
	
	private DialogWithContentFragment classApplyDialog;
	
	public DialogUserSetting() {
		super();
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE; 
    	setStyle(style, R.style.DialogBottomToTopTheme);
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		if (isAdded()) {
			return;
		}
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		int gravity = Gravity.CENTER;
		if (getTheme() == R.style.DialogTopToBottomTheme) {
			gravity = Gravity.TOP;
		}
		else if (getTheme() == R.style.DialogBottomToTopTheme)  {
			gravity = Gravity.BOTTOM;
		}
		dialog.getWindow().setGravity(Gravity.LEFT | gravity);
		return dialog;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.dialog_user_setting, null);

		flSendMsg = (FrameLayout) v.findViewById(R.id.flSendMsg);
		flSendMsg.setOnClickListener(this);
		flInvite = (FrameLayout) v.findViewById(R.id.flInvite);
		flInvite.setOnClickListener(this);
		flAttention = (FrameLayout) v.findViewById(R.id.flAttention);
		flAttention.setOnClickListener(this);
		flBlackList = (FrameLayout) v.findViewById(R.id.flBlackList);
		flBlackList.setOnClickListener(this);
		
		flCancel = (FrameLayout) v.findViewById(R.id.flCancel);
		flCancel.setOnClickListener(this);

		flBg = (FrameLayout) v.findViewById(R.id.flBg1);
		flBg.setOnClickListener(this);
		
		txtAttention = (TextView) v.findViewById(R.id.txtAttention);
		txtBlackList = (TextView) v.findViewById(R.id.txtBlackList);
		if (GlobalRes.getInstance().getBeans().getCurrentMyClassNo() == null) {
			flInvite.setVisibility(View.GONE);
		}
		
		OtherInfoData data = GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData();
		isAttentioned = data.getAttention();
		isBlacked = data.getBlackList();
		refreshAttentionedBtn();
		refreshBlackListBtn();
		return v;
	}
	
	private void refreshAttentionedBtn() {
		txtAttention.setText(isAttentioned ? R.string.attetionCancel : R.string.attetion);
	}
	
	private void refreshBlackListBtn() {
		txtBlackList.setText(isBlacked ? R.string.userInfoTip13 : R.string.userInfoTip12);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.flBg1:
//			finish();
			break;
		case R.id.flSendMsg:
			OtherInfoData data = GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData();
			GlobalRes.getInstance().getBeans().setCurrentFriendNo(data.getUserNo());
			GlobalRes.getInstance().getBeans().setCurrentFriendName(data.getName());
			AtySecretDetails.startAty(getActivity());
			getActivity().finish();
//			finish();
			break;
		case R.id.flInvite:
//			finish();
			invite();
			break;
		case R.id.flAttention:
			attention();
//			finish();
			break;
		case R.id.flBlackList:
			blackList();
//			finish();
			break;
		case R.id.flCancel:
			dismiss();
//			finish();
			break;
		}
	}
	
	private void invite() {
		if (null != classApplyDialog && classApplyDialog.isVisible()) {
			return;
		}
		classApplyDialog = new DialogWithContentFragment(new CallBackDialogWithContent() {
			
			@Override
			public void notice(String msg, final String wechatNo) {
				if (requesting) {
					return;
				}
				requesting = true;
				classApplyDialog.dismiss();
				classApplyDialog = null;
//				showLoading();
				new TaskReqClassApply(0, new CallbackNoticeView<Void, Boolean>() {

					@Override
					public void refreshView(int tag, Boolean result) {
						requesting = false;
//						hideLoading();
						
						if (null != result && result) {
							Toast.makeText(getActivity(), R.string.applySuccessTip, Toast.LENGTH_SHORT).show();
							CacheBeans beans = GlobalRes.getInstance().getBeans();
							if (!TextUtils.isEmpty(wechatNo) && TextUtils.isEmpty(beans.getMyGroupDatas().getClassInfos().get(0).getWechatNo())
									&& beans.getMyGroupDatas().getMyClassAuth() > ClassData.NORMAL) {
								beans.getMyGroupDatas().getClassInfos().get(0).setWechatNo(wechatNo);
							}
						}
					}

					@Override
					public void onProgressUpdate(int tag, Void[] values) {
					}
				
				}, msg, wechatNo);
			}
		}, getString(R.string.classApplyTitle), getString(R.string.classApplyTip));
		classApplyDialog.showDialog(getFragmentManager());
	}
	
	private void attention() {
		if (requesting) {
			return;
		}
		requesting = true;
//		showLoading();
		if (!isAttentioned) {
			new TaskReqAttention(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
//					hideLoading();
					if (null == result || !result || null == getActivity()) {
						return;
					}
					Toast.makeText(getActivity(), R.string.attetionSuccessTip, Toast.LENGTH_SHORT).show();
					isAttentioned = true;
					GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setAttention(isAttentioned);
					isBlacked = false;
					GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setBlackList(isBlacked);
					refreshAttentionedBtn();
					refreshBlackListBtn();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
			return;
		}
		new TaskReqAttentionCancel(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
//				hideLoading();
				if (null == result || !result) {
					return;
				}
				Toast.makeText(getActivity(), R.string.attetionCancelSuccessTip, Toast.LENGTH_SHORT).show();
				isAttentioned = false;
				GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setAttention(isAttentioned);
				refreshAttentionedBtn();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
		return;
	}
	
	private void blackList() {
		if (requesting) {
			return;
		}
		requesting = true;
//		showLoading();
		if (!isBlacked) {
			new TaskReqBlacklistCreate(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					requesting = false;
//					hideLoading();
					if (null == result || !result) {
						return;
					}
					Toast.makeText(getActivity(), R.string.blackListSuccessTip, Toast.LENGTH_SHORT).show();
					isBlacked = true;
					GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setBlackList(isBlacked);
					isAttentioned = false;
					GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setAttention(isAttentioned);
					refreshAttentionedBtn();
					refreshBlackListBtn();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().getUserNo());
			return;
		}
		new TaskReqBlacklistRemove(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				requesting = false;
//				hideLoading();
				if (null == result || !result) {
					return;
				}
				Toast.makeText(getActivity(), R.string.blackListCancelSuccessTip, Toast.LENGTH_SHORT).show();
				isBlacked = false;
				GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().setBlackList(isBlacked);
				refreshBlackListBtn();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, GlobalRes.getInstance().getBeans().getOtherInfoData().getInfoData().getUserNo());
		return;
	}

	
	public static void startAty(Context context, ImageView iv) {
		context.startActivity(new Intent(context, DialogUserSetting.class));
	}
	
	public static void startAty(Context context) {
		context.startActivity(new Intent(context, DialogUserSetting.class));
//		Intent i = new Intent(context, DialogUserSetting.class);
//		context.startActivity(i);
	}
}