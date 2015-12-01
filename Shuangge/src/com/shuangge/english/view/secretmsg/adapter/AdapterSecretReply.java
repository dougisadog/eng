package com.shuangge.english.view.secretmsg.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.support.utils.ImageUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.MaskImage;
import com.shuangge.english.view.component.photograph.AtyPhotoBrowser;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

public class AdapterSecretReply extends ArrayAdapter<SecretMsgDetailData> {
	
	private static final int TYPE_MINE = 0;
	private static final int TYPE_OTHER = 1;
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<SecretMsgDetailData> datas = new ArrayList<SecretMsgDetailData>();
	private long userNo;
	
	private String sendName;
	private String sendUrl;

	public AdapterSecretReply(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
		userNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo();
	}

	public AdapterSecretReply(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
		userNo = GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo();
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public SecretMsgDetailData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		SecretMsgDetailData data = datas.get(position);
		return userNo == data.getSenderNo() ? TYPE_MINE : TYPE_OTHER;
	}
	
	public int getViewTypeCount() {
		return 2;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SecretMsgViewHolder holder = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			if (type == TYPE_MINE) {
				convertView = mInflater.inflate(R.layout.item_secret_reply_mine, null, true);
			} 
			else {
				convertView = mInflater.inflate(R.layout.item_secret_reply, null, true);
			}
			holder = new SecretMsgViewHolder();
			holder.rlPic = (RelativeLayout) convertView.findViewById(R.id.rlPic);
			holder.rlContent = (RelativeLayout) convertView.findViewById(R.id.rlContent);
			holder.imgHead = (CircleImageView) convertView.findViewById(R.id.imgHead);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);
			holder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreateDate);
			holder.img1 = (MaskImage) convertView.findViewById(R.id.img1);
			holder.imgLoading = (ImageView) convertView.findViewById(R.id.imgLoading);
			holder.imgErr = (ImageView) convertView.findViewById(R.id.imgErr);
			convertView.setTag(holder);
			
			holder.imgHead.setOnClickListener(clickImgHeadListener);
			holder.img1.setOnClickListener(clickReplyListener);
		} 
		else {
			holder = (SecretMsgViewHolder) convertView.getTag();
		}
		
		SecretMsgDetailData data = datas.get(position);
		initData(data);
		
		holder.imgHead.setTag(data.getSenderNo());
		if (!TextUtils.isEmpty(sendUrl)) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(sendUrl, holder.imgHead));
		}
		else {
			holder.imgHead.clear();
		}
		
		if (type == TYPE_MINE) {
			if (data.getStatus() == SecretMsgDetailData.STATUS_LOADING) {
				holder.imgLoading.setVisibility(View.VISIBLE);
				Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(GlobalApp.getInstance().getActivity(), R.anim.loading_animation);
				holder.imgLoading.startAnimation(hyperspaceJumpAnimation);
			}
			else {
				holder.imgLoading.setVisibility(View.GONE);
				holder.imgLoading.clearAnimation();
			}
			holder.imgErr.setVisibility(data.getStatus() == SecretMsgDetailData.STATUS_ERR ? View.VISIBLE : View.GONE);
		}
		
		
		if (!TextUtils.isEmpty(sendName)) {
			holder.txtName.setText(sendName);
		}
		if (null != data.getSendTime()) {
			holder.txtCreateDate.setText(DateUtils.convert(data.getSendTime()));
		}
		if (!TextUtils.isEmpty(data.getContent())) {
			holder.txtContent.setText(data.getContent().toString());
			holder.rlPic.setVisibility(View.GONE);
			holder.rlContent.setVisibility(View.VISIBLE);
		}
		else if (!TextUtils.isEmpty(data.getPicUrl())) {
			holder.rlPic.setVisibility(View.VISIBLE);
			holder.rlContent.setVisibility(View.GONE);
			holder.img1.clear();
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(data.getPicUrl(), holder.img1));
			holder.img1.setTag(data.getPicUrl());
		}
		else if (!TextUtils.isEmpty(data.getLocalPath())) {
			holder.rlPic.setVisibility(View.VISIBLE);
			holder.rlContent.setVisibility(View.GONE);
			Bitmap bitmap = ImageUtils.readNormalPic(data.getLocalPath(), holder.img1.getWidth(), holder.img1.getHeight());
			holder.img1.setImageBitmap(bitmap);
			holder.img1.setTag(null);
		}
		return convertView;
	}
	
	private OnClickListener clickReplyListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null == v.getTag())
				return;
			ArrayList<String> urls = new ArrayList<String>();
			urls.add(v.getTag() + "");
			AtyPhotoBrowser.startAty(getContext(), 0, urls);
		}
	};
	
	private OnClickListener clickImgHeadListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			long userNo = (Long) v.getTag();
			if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getUserNo().longValue() == userNo) {
				AtyUserInfo.startAty(getContext());
				return;
			}
			AtyBrowseUserInfo.startAty(getContext(), userNo);
		}
		
	};
	
	public List<SecretMsgDetailData> getDatas() {
		return datas;
	}
	
	public void initData(SecretMsgDetailData data) {
		InfoData info = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
		if (data.getSenderNo().longValue() == info.getUserNo().longValue()) {
			sendName = info.getName();
			sendUrl = info.getHeadUrl();
		} 
		else {
			sendName = data.getFriendName();
			sendUrl = data.getFriendHeadUrl();
		}
	}
	
	public final class SecretMsgViewHolder {

		private CircleImageView imgHead;
		private TextView txtName;
		private TextView txtContent;
		private TextView txtCreateDate;
		private MaskImage img1;
		private RelativeLayout rlPic, rlContent;
		private ImageView imgLoading, imgErr;
	}

}
