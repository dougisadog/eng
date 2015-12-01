package com.shuangge.english.view.post.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.post.PostData;
import com.shuangge.english.entity.server.post.ReplyData;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.MaskImage;
import com.shuangge.english.view.component.photograph.AtyPhotoBrowser;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

public class AdapterReply extends ArrayAdapter<Object> {
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Object> datas = new ArrayList<Object>();

	public AdapterReply(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}

	public AdapterReply(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ReplyViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_reply, null, true);
			holder = new ReplyViewHolder();
			holder.imgHead = (CircleImageView) convertView.findViewById(R.id.imgHead);
			holder.imgTop = (ImageView) convertView.findViewById(R.id.imgTop);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
//			holder.sexAge = (SexAge) convertView.findViewById(R.id.sexAge);
			holder.txtFloorNum = (TextView) convertView.findViewById(R.id.txtFloorNum);
			holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);
			holder.txtSubTitle = (TextView) convertView.findViewById(R.id.txtSubTitle);
			holder.txtSubContent = (TextView) convertView.findViewById(R.id.txtSubContent);
			holder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreateDate);
			holder.llPicContainer = (LinearLayout) convertView.findViewById(R.id.llPicContainer);
			holder.txtPostTitle = (TextView) convertView.findViewById(R.id.txtPostTitle);
			holder.txtReplyNum = (TextView) convertView.findViewById(R.id.txtReplyNum);
			holder.imgs.add((MaskImage) convertView.findViewById(R.id.img1));
			holder.imgs.add((MaskImage) convertView.findViewById(R.id.img2));
			holder.imgs.add((MaskImage) convertView.findViewById(R.id.img3));
			holder.imgs.add((MaskImage) convertView.findViewById(R.id.img4));
			convertView.setTag(holder);
		} else {
			holder = (ReplyViewHolder) convertView.getTag();
		}
		Object data = datas.get(position);
		int index = 0;
		holder.txtSubContent.setVisibility(View.GONE);
		holder.txtSubTitle.setVisibility(View.GONE);
		holder.imgHead.setOnClickListener(clickImgHeadListener);
		if (data instanceof ReplyData) {
			ReplyData reply = (ReplyData) data;
			holder.imgTop.setVisibility(View.GONE);
			holder.txtReplyNum.setVisibility(View.GONE);
			holder.txtFloorNum.setVisibility(View.VISIBLE);
			holder.txtPostTitle.setVisibility(View.GONE);
			holder.imgHead.setTag(reply.getUserNo());
			if (!TextUtils.isEmpty(reply.getUserHeadUrl())) {
				GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(reply.getUserHeadUrl(), holder.imgHead));
			}
			else {
				holder.imgHead.clear();
			}
			if (!TextUtils.isEmpty(reply.getUserName())) {
				holder.txtName.setText(reply.getUserName().toString());
			}
			if (null != reply.getReplyContent() && !reply.isDeleted()) {
				holder.txtSubTitle.setText(getContext().getString(R.string.replySb) + " " + reply.getReplyFloorsNo() + 
						getContext().getString(R.string.postDetailsTip2) + " " + reply.getReplyUserName() + ":");
				holder.txtSubContent.setText(reply.isReplyDeleted() ? getContext().getString(R.string.replyTip) : reply.getReplyContent());
				holder.txtSubTitle.setVisibility(View.VISIBLE);
				holder.txtSubContent.setVisibility(View.VISIBLE);
			}
//			holder.sexAge.setSexAndAge(reply.getUserSex(), null);
//			holder.sexAge.setVisibility((null == reply.getUserSex() || reply.getUserSex() < 1 || reply.getUserSex() > 2) ? View.GONE : View.VISIBLE);
			if (!TextUtils.isEmpty(reply.getContent())) {
				holder.txtContent.setText(reply.isDeleted() ? getContext().getString(R.string.replyTip) : reply.getContent().toString());
			}
			if (null != reply.getCreateDate()) {
				holder.txtCreateDate.setText(DateUtils.convert(reply.getCreateDate()));
			}
			if (reply.getFloorsNo() > 0) {
				holder.txtFloorNum.setText(reply.getFloorsNo() + convertView.getResources().getString(R.string.postDetailsTip2));
			}
			index = 0;
			holder.llPicContainer.setVisibility(TextUtils.isEmpty(reply.getPicUrl()) ? View.GONE : View.VISIBLE);
			for (MaskImage img : holder.imgs) {
				if (!TextUtils.isEmpty(reply.getPicUrl()) && index++ == 0) {
					GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(reply.getPicUrl(), img));
					img.setVisibility(View.VISIBLE);
					img.setOnClickListener(clickReplyListener);
					img.setTag(reply.getPicUrl());
					continue;
				}
				img.setVisibility(View.GONE);
			}
			
		}
		else {
			PostData post = (PostData) data;
			holder.imgTop.setVisibility(View.VISIBLE);
			holder.txtReplyNum.setVisibility(View.VISIBLE);
			holder.txtFloorNum.setVisibility(View.GONE);
			holder.txtPostTitle.setVisibility(View.VISIBLE);
			holder.imgHead.setTag(post.getUserNo());
			if (!TextUtils.isEmpty(post.getUserHeadUrl())) {
				GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(post.getUserHeadUrl(), holder.imgHead));
			}
			else {
				holder.imgHead.clear();
			}
			if (!TextUtils.isEmpty(post.getUserName())) {
				holder.txtName.setText(post.getUserName().toString());
			}
			if (!TextUtils.isEmpty(post.getTitle())) {
				holder.txtPostTitle.setText(post.getTitle().toString());
			}
			else {
				holder.txtPostTitle.setVisibility(View.GONE);
			}
			holder.txtReplyNum.setText(post.getReplyNum() + convertView.getResources().getString(R.string.postDetailsTip4));
//			holder.sexAge.setSexAndAge(post.getUserSex(), null);
//			holder.sexAge.setVisibility((null == post.getUserSex() || post.getUserSex() == 0) ? View.GONE : View.VISIBLE);
			if (!TextUtils.isEmpty(post.getContent())) {
				holder.txtContent.setText(post.getContent().toString());
			}
			if (null != post.getCreateDate()) {
				holder.txtCreateDate.setText(DateUtils.convert(post.getCreateDate()));
			}
			holder.txtFloorNum.setText(convertView.getResources().getString(R.string.postDetailsTip1));
			holder.llPicContainer.setVisibility(post.getPicUrls().size() == 0 ? View.GONE : View.VISIBLE);
			int i = 0;
			for (MaskImage img : holder.imgs) {
				if (post.getPicUrls().size() > i && !TextUtils.isEmpty(post.getPicUrls().get(i))) {
					GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(post.getPicUrls().get(i), img));
					img.setVisibility(View.VISIBLE);
					img.setOnClickListener(clickPostListener);
					img.setTag(new Object[] {i++, post.getPicUrls()});
					continue;
				}
				img.setVisibility(View.GONE);
			}
		}
		
		return convertView;
	}
	
	private OnClickListener clickReplyListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
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
	
	private OnClickListener clickPostListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Object[] objs = (Object[]) v.getTag();
			AtyPhotoBrowser.startAty(getContext(), (Integer) objs[0], (ArrayList<String>) objs[1]);
		}
	};
	
	public List<Object> getDatas() {
		return datas;
	}
	
	public final class ReplyViewHolder {

		private CircleImageView imgHead;
		private TextView txtName;
		private TextView txtSubTitle;
		private TextView txtSubContent;
		private TextView txtFloorNum;
		private TextView txtContent;
		private TextView txtCreateDate;
		private LinearLayout llPicContainer;
		private List<MaskImage> imgs = new ArrayList<MaskImage>();
		
		private ImageView imgTop;
		
		private TextView txtPostTitle;
		private TextView txtReplyNum;

	}

}
