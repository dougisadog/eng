package com.shuangge.english.view.msg.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.msg.UserGroupMsgData;
import com.shuangge.english.support.utils.ClipboardUtils;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.group.AtyBrowseClassInfo;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

public class AdapterClassMsg extends ArrayAdapter<UserGroupMsgData> {
	
	public final static int TYPE_USER_APPLY = 1;
	public final static int TYPE_GROUP_APPLY = 2;

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<UserGroupMsgData> datas = new ArrayList<UserGroupMsgData>();
	private CallBackClassMsg callBack;

	public static interface CallBackClassMsg {

		public void notice(int position, Long msgNo, int status, String msg, int type);

	}

	public AdapterClassMsg(Context context, CallBackClassMsg callBack) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
		this.callBack = callBack;
	}

	public AdapterClassMsg(Context context, int resource,
			CallBackClassMsg callBack) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
		this.callBack = callBack;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public UserGroupMsgData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ClassMsgViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater
					.inflate(R.layout.item_class_msg, null, true);
			holder = new ClassMsgViewHolder();
			holder.txtClassName = (TextView) convertView.findViewById(R.id.txtClassName);
			holder.imgUserHead = (CircleImageView) convertView.findViewById(R.id.imgUserHead);
			holder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreateDate);
			holder.txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
			holder.llBtns = (LinearLayout) convertView.findViewById(R.id.llBtns);
			
			holder.flAccept = (FrameLayout) convertView.findViewById(R.id.flAccept);
			holder.flDecline = (FrameLayout) convertView.findViewById(R.id.flDecline);
			holder.flDone = (FrameLayout) convertView.findViewById(R.id.flDone);
			holder.txtDoneEn = (TextView) convertView.findViewById(R.id.txtDoneEn);
			holder.txtDoneCn = (TextView) convertView.findViewById(R.id.txtDoneCn);
			holder.flAccept.setTag(new ClassMsgDataHolder());
			holder.flDecline.setTag(holder.flAccept.getTag());
			holder.flAccept.setOnClickListener(clickAcceptBtnListener);
			holder.flDecline.setOnClickListener(clickDeclineBtnListener);
			
			holder.txtWeChat = (TextView) convertView.findViewById(R.id.txtWeChat);
			holder.imageView4 = (ImageView) convertView.findViewById(R.id.imageView4);
			holder.txtJoinMsg = (TextView) convertView.findViewById(R.id.txtJoinMsg);
			holder.txtJoinTitle = (TextView) convertView.findViewById(R.id.txtJoinTitle);
			
			convertView.setTag(holder);
		} else {
			holder = (ClassMsgViewHolder) convertView.getTag();
		}
		
		String userName = "";
		String groupName = "";
		
		UserGroupMsgData entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getGroupName())) {
			holder.txtClassName.setText(entity.getGroupName());
			groupName = entity.getGroupName();
		}
		holder.txtClassName.setTag(entity.getGroupNo());
		holder.txtClassName.setOnClickListener(clickClassHeadListener);
		holder.imgUserHead.setOnClickListener(clickUserHeadListener);
		holder.txtCreateDate.setText("");
		holder.txtWeChat.setVisibility(View.GONE);
		holder.imageView4.setVisibility(View.GONE);
		holder.txtJoinMsg.setVisibility(View.GONE);
		holder.txtJoinTitle.setVisibility(View.GONE);
		holder.txtWeChat.setOnClickListener(null);
		
		
		if (!TextUtils.isEmpty(entity.getTargetName2())) {
			userName = entity.getTargetName2();
		} 
		else if (!TextUtils.isEmpty(entity.getTargetName1())) {
			userName = entity.getTargetName1();
		}
		
		holder.imgUserHead.clear();
		if (null != entity.getTargetNo2()) {
			holder.imgUserHead.setTag(entity.getTargetNo2());
			if (!TextUtils.isEmpty(entity.getTargetHeadUrl2())) {
				GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getTargetHeadUrl2(), holder.imgUserHead));
			} 
		} 
		else if (null != entity.getTargetNo1()) {
			holder.imgUserHead.setTag(entity.getTargetNo1());
			if (!TextUtils.isEmpty(entity.getTargetHeadUrl1())) {
				GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getTargetHeadUrl1(), holder.imgUserHead));
			}
		}
		
		if (null != entity.getCreateDate()) {
			holder.txtCreateDate.setText(DateUtils.convert(entity.getCreateDate()));
		}
		if (!TextUtils.isEmpty(entity.getMessage())) {
			holder.txtJoinMsg.setText(entity.getMessage());
			holder.txtJoinMsg.setVisibility(View.VISIBLE);
			holder.txtJoinTitle.setVisibility(View.VISIBLE);
		}
		String msg = "";
		boolean showBtn = false;
		switch (entity.getType()) {
		case UserGroupMsgData.TYPE_CLASS_CREATE:
			msg = convertView.getResources().getString(R.string.classMsgTip1).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_CLASS_CHANGED:
			msg = convertView.getResources().getString(R.string.classMsgTip2).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_CLASS_TRANSFER:
			msg = convertView.getResources().getString(R.string.classMsgTip3).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_CLASS_JOIN:
			msg = convertView.getResources().getString(R.string.classMsgTip4).replace("ccc", groupName).replace("uuu", userName);
			if (!TextUtils.isEmpty(entity.getWechatNo())) {
				holder.txtWeChat.setText(entity.getWechatNo() + " (点击可以复制)");
				holder.txtWeChat.setTag(entity.getWechatNo());
				holder.txtWeChat.setOnClickListener(clickWechatNoListener);
			}
			else {
				holder.txtWeChat.setText(R.string.classMsgTip21);
			}
			holder.txtWeChat.setVisibility(View.VISIBLE);
			holder.imageView4.setVisibility(View.VISIBLE);
			break;
		case UserGroupMsgData.TYPE_CLASS_LEAVE:
			msg = convertView.getResources().getString(R.string.classMsgTip5).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_CLASS_KILL:
			msg = convertView.getResources().getString(R.string.classMsgTip6).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_USER_APPLY:
			msg = convertView.getResources().getString(R.string.classMsgTip7).replace("ccc", groupName).replace("uuu", userName);
//			if (!TextUtils.isEmpty(entity.getMessage())) {
//				msg += convertView.getResources().getString(R.string.classMsgTip9);
//				msg += entity.getMessage();
//			}
			if (!TextUtils.isEmpty(entity.getWechatNo())) {
				holder.txtWeChat.setText(entity.getWechatNo() + " (点击可以复制)");
				holder.txtWeChat.setTag(entity.getWechatNo());
				holder.txtWeChat.setOnClickListener(clickWechatNoListener);
			}
			else {
				holder.txtWeChat.setText(R.string.classMsgTip21);
			}
			holder.txtWeChat.setVisibility(View.VISIBLE);
			holder.imageView4.setVisibility(View.VISIBLE);
			((ClassMsgDataHolder) holder.flAccept.getTag()).type = TYPE_USER_APPLY;
			showBtn = true;
			break;
		case UserGroupMsgData.TYPE_GROUP_APPLY:
			msg = convertView.getResources().getString(R.string.classMsgTip8).replace("ccc", groupName).replace("uuu", userName);
			
//			if (!TextUtils.isEmpty(entity.getMessage())) {
//				msg += convertView.getResources().getString(R.string.classMsgTip9);
//				msg += entity.getMessage();
//			}
			if (!TextUtils.isEmpty(entity.getWechatNo())) {
				holder.txtWeChat.setText(entity.getWechatNo() + " (点击可以复制)");
				holder.txtWeChat.setTag(entity.getWechatNo());
				holder.txtWeChat.setOnClickListener(clickWechatNoListener);
			}
			else {
				holder.txtWeChat.setText(R.string.classMsgTip21);
			}
			holder.txtWeChat.setVisibility(View.VISIBLE);
			holder.imageView4.setVisibility(View.VISIBLE);
			((ClassMsgDataHolder) holder.flAccept.getTag()).type = TYPE_GROUP_APPLY;
			showBtn = true;
			break;
		case UserGroupMsgData.TYPE_USER_REFUSE:
			msg = convertView.getResources().getString(R.string.classMsgTip19).replace("ccc", groupName).replace("uuu", userName);
			if (!TextUtils.isEmpty(entity.getMessage())) {
				holder.txtJoinTitle.setText("拒绝理由:");
			}
			break;
		case UserGroupMsgData.TYPE_GROUP_REFUSE:
			msg = convertView.getResources().getString(R.string.classMsgTip20).replace("ccc", groupName).replace("uuu", userName);
			if (!TextUtils.isEmpty(entity.getMessage())) {
				holder.txtJoinTitle.setText("拒绝理由:");
			}
			break;
		case UserGroupMsgData.TYPE_CLASS_MANAGER:
			msg = convertView.getResources().getString(R.string.classMsgTip12).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_CLASS_MANAGER_OFF:
			msg = convertView.getResources().getString(R.string.classMsgTip13).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_CLASS_SUB_MANAGER:
			msg = convertView.getResources().getString(R.string.classMsgTip14).replace("ccc", groupName).replace("uuu", userName);
			break;
		case UserGroupMsgData.TYPE_CLASS_SUB_MANAGER_OFF:
			msg = convertView.getResources().getString(R.string.classMsgTip15).replace("ccc", groupName).replace("uuu", userName);
			break;
		}
		holder.txtMsg.setText(msg.toString());
		holder.llBtns.setVisibility(View.GONE);
		holder.flAccept.setVisibility(View.GONE);
		holder.flDecline.setVisibility(View.GONE);
		holder.flDone.setVisibility(View.GONE);
		if (showBtn) {
			holder.llBtns.setVisibility(View.VISIBLE);
			switch (entity.getStatus()) {
			case UserGroupMsgData.STATUS_AGREE:
				holder.txtDoneEn.setText(R.string.classMsgTip16En);
				holder.txtDoneCn.setText(R.string.classMsgTip16Cn);
				holder.flDone.setVisibility(View.VISIBLE);
				break;
			case UserGroupMsgData.STATUS_TIME_OUT:
				holder.txtDoneEn.setText(R.string.classMsgTip10En);
				holder.txtDoneCn.setText(R.string.classMsgTip10Cn);
				holder.flDone.setVisibility(View.VISIBLE);
				break;
			case UserGroupMsgData.STATUS_WAITING:
				holder.flAccept.setVisibility(View.VISIBLE);
				holder.flDecline.setVisibility(View.VISIBLE);
				ClassMsgDataHolder dataHolder = (ClassMsgDataHolder) holder.flAccept.getTag();
				dataHolder.msgNo = entity.getGroupMsgNo();
				dataHolder.position = position;
				break;
			case UserGroupMsgData.STATUS_JOINED_OTHER:
				holder.txtDoneEn.setText(R.string.classMsgTip23En);
				holder.txtDoneCn.setText(R.string.classMsgTip23Cn);
				holder.flDone.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(entity.getMessage())) {
					holder.txtJoinTitle.setText("申请理由:");
					holder.txtJoinMsg.setText(entity.getMessage());
				}
				break;
			case UserGroupMsgData.STATUS_REFUSE:
				holder.txtDoneEn.setText(R.string.classMsgTip18En);
				holder.txtDoneCn.setText(R.string.classMsgTip18Cn);
				holder.flDone.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(entity.getMessage())) {
					holder.txtJoinTitle.setText("申请理由:");
					holder.txtJoinMsg.setText(entity.getMessage());
				}
				break;
			case UserGroupMsgData.STATUS_JOINED:
				holder.txtDoneEn.setText(R.string.classMsgTip24En);
				holder.txtDoneCn.setText(R.string.classMsgTip24Cn);
				holder.flDone.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(entity.getMessage())) {
					holder.txtJoinTitle.setText("申请理由:");
					holder.txtJoinMsg.setText(entity.getMessage());
				}
				break;
			case UserGroupMsgData.STATUS_BE_REFUSED:
				holder.txtDoneEn.setText(R.string.classMsgTip25En);
				holder.txtDoneCn.setText(R.string.classMsgTip25Cn);
				holder.flDone.setVisibility(View.VISIBLE);
				if (!TextUtils.isEmpty(entity.getMessage())) {
					holder.txtJoinTitle.setText("申请理由:");
					holder.txtJoinMsg.setText(entity.getMessage());
				}
				break;
			}
		}
		return convertView;
	}

	private OnClickListener clickWechatNoListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ClipboardUtils.copy(v.getTag().toString());
			Toast.makeText(getContext(), R.string.copyWechat, Toast.LENGTH_SHORT).show();
		}
		
	};
	
	private OnClickListener clickAcceptBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null == callBack) {
				return;
			}
			ClassMsgDataHolder holder = (ClassMsgDataHolder) v.getTag();
			callBack.notice(holder.position, holder.msgNo, UserGroupMsgData.STATUS_AGREE, null, holder.type);
		}
		
	};
	
	private OnClickListener clickDeclineBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null == callBack) {
				return;
			}
			ClassMsgDataHolder holder = (ClassMsgDataHolder) v.getTag();
			callBack.notice(holder.position, holder.msgNo, UserGroupMsgData.STATUS_REFUSE, null, 0);
		}
		
	};
	
	private OnClickListener clickUserHeadListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null == v.getTag()) {
				return;
			}
			Long userNo = (Long) v.getTag();
			if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue() == userNo) {
				AtyUserInfo.startAty(getContext());
				return;
			}
			AtyBrowseUserInfo.startAty(getContext(), userNo);
		}

	};

	private OnClickListener clickClassHeadListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null == v.getTag()) {
				return;
			}
			long classNo = (Long) v.getTag();
//			if (null != GlobalRes.getInstance().getBeans().getCurrentMyClassNo() 
//					&& GlobalRes.getInstance().getBeans().getCurrentMyClassNo() .longValue() == classNo) {
//				getContext().startActivity(new Intent(getContext(), AtyClassInfo.class));
//				return;
//			}
			Intent i = new Intent(getContext(), AtyBrowseClassInfo.class);
			i.putExtra(AtyBrowseClassInfo.PARAM_INDEX, classNo);
			getContext().startActivity(i);
		}

	};

	public List<UserGroupMsgData> getDatas() {
		return datas;
	}

	public final class ClassMsgDataHolder {

		private Long msgNo;
		private int position;
		private int type;

	}

	public final class ClassMsgViewHolder {

		private TextView txtClassName;
		private CircleImageView imgUserHead;
		private TextView txtMsg, txtJoinMsg, txtJoinTitle;
		private TextView txtCreateDate;
		private LinearLayout llBtns;
		private FrameLayout flAccept, flDecline, flDone;
		private TextView txtDoneEn, txtDoneCn, txtWeChat;
		private ImageView imageView4;

	}

}
