package com.shuangge.english.view.post.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.post.PostData;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.MaskImage;
import com.shuangge.english.view.group.AtyClassInvite;
import com.shuangge.english.view.group.AtyClassMember;
import com.shuangge.english.view.post.AtyPostEdit;
import com.shuangge.english.view.user.AtyBrowseUserInfo;
import com.shuangge.english.view.user.AtyUserInfo;

public class AdapterPostForSettings extends ArrayAdapter<Object> {
	
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_TOP_ITEM = 1;
	private static final int TYPE_MAX_COUNT = 2;  
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Object> datas = new ArrayList<Object>();

	private CallbackPost callback;
	private Activity aty;
	
	public static interface CallbackPost {
		
		public void addNice(PostData entity);
		
		public void removeNice(PostData entity);
		
	}
	
	public AdapterPostForSettings(Activity context) {
		super(context, 0);
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterPostForSettings(Activity context, CallbackPost callback) {
		super(context, 0);
		this.aty = context;
		this.callback = callback;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterPostForSettings(Context context, int resource) {
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
	
	@Override
	public int getItemViewType(int position) {
		return position == 0 ? TYPE_TOP_ITEM : TYPE_ITEM;
	}
	
	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}
	
	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		if (type == TYPE_TOP_ITEM) {
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.item_post2_top, null, true);
				convertView.findViewById(R.id.rlWritePost).setOnClickListener(clickWritePostListener);
			}
			return convertView;
		}
		PostViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_post2, null, true);
			holder = new PostViewHolder();
			holder.imgHead = (CircleImageView) convertView.findViewById(R.id.imgHead);
			holder.imgFirst = (MaskImage) convertView.findViewById(R.id.imgFirst);
			holder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreateDate);
			holder.imgTop = (ImageView) convertView.findViewById(R.id.imgTop);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtReplyNum = (TextView) convertView.findViewById(R.id.txtReplyNum);
			holder.txtNiceNum = (TextView) convertView.findViewById(R.id.txtNiceNum);
			holder.flNiceNum = (FrameLayout) convertView.findViewById(R.id.flNiceNum);
			holder.imgHead.setOnClickListener(clickImgHeadListener);
			convertView.setTag(holder);
		} 
		else {
			holder = (PostViewHolder) convertView.getTag();
		}
		PostData entity = (PostData) datas.get(position);
		holder.imgHead.setTag(entity.getUserNo());
		holder.imgTop.setVisibility(entity.getTop() ? View.VISIBLE : View.INVISIBLE);
		if (!TextUtils.isEmpty(entity.getTitle())) {
			holder.txtTitle.setText(entity.getTitle().toString());
		}
		if (null != entity.getCreateDate()) {
			holder.txtCreateDate.setText(DateUtils.convert(entity.getCreateDate()));
		}
		if (!TextUtils.isEmpty(entity.getUserName())) {
			holder.txtName.setText(entity.getUserName().toString());
		}
		if (!TextUtils.isEmpty(entity.getContent())) {
			holder.txtContent.setText(entity.getContent().toString());
		}
		holder.txtReplyNum.setText(" | " + entity.getReplyNum() + convertView.getResources().getString(R.string.postDetailsTip4));
		if (!TextUtils.isEmpty(entity.getUserHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getUserHeadUrl(), holder.imgHead));
		}
		else {
			holder.imgHead.clear();
		}
		if (!TextUtils.isEmpty(entity.getFirstPic())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getFirstPic(), holder.imgFirst));
			holder.imgFirst.setVisibility(View.VISIBLE);
		}
		else {
			holder.imgFirst.clear();
			holder.imgFirst.setVisibility(View.GONE);
		}
		if (entity.getNiceNum() <= 999) {
			holder.txtNiceNum.setText(entity.getNiceNum() + "");
		}
		else {
			holder.txtNiceNum.setText("999+");
		}
		holder.flNiceNum.setTag(entity);
		if (entity.isNiceForMe()) {
			holder.flNiceNum.setBackgroundResource(R.drawable.bg_nice_p);
			holder.flNiceNum.setOnClickListener(clickRemoveNiceListener);
		}
		else {
			holder.flNiceNum.setBackgroundResource(R.drawable.bg_nice);
			holder.flNiceNum.setOnClickListener(clickAddNiceListener);
		}
		return convertView;
	}
	
	private OnClickListener clickWritePostListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AtyPostEdit.startAtyForMe(aty);
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
	
	private OnClickListener clickAddNiceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			PostData entity = (PostData) v.getTag();
			if (null != callback) {
				callback.addNice(entity);
			}
		}
		
	};
	
	private OnClickListener clickRemoveNiceListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			PostData entity = (PostData) v.getTag();
			if (null != callback) {
				callback.removeNice(entity);
			}
		}
		
	};
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.rlInvite:
				AtyClassInvite.startAty(aty);
				break;
			case R.id.rlWritePost:
				AtyPostEdit.startAtyForMe(aty);
				break;
			case R.id.llMembers:
				if (GlobalRes.getInstance().getBeans().getCurrentClassNo().longValue() == GlobalRes.getInstance().getBeans().getCurrentMyClassNo().longValue()
					&& GlobalRes.getInstance().getBeans().getMyGroupDatas().getMyClassAuth() > ClassData.SUB_MANAGER) {
					AtyClassMember.startAty(aty);
				}
				else {
					AtyClassMember.startAty(aty);
				}
				break;
		}
		}
		
	};
	
	public List<Object> getDatas() {
		return datas;
	}

	public final class PostViewHolder {
		
		private MaskImage imgFirst;
		private CircleImageView imgHead;
		private ImageView imgTop;
		private TextView txtName;
		private TextView txtTitle, txtContent;
		private TextView txtReplyNum, txtNiceNum;
		private TextView txtCreateDate;
		private FrameLayout flNiceNum;
		private RelativeLayout rlWritePost;

	}
	
}
