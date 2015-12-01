package com.shuangge.english.view.shop.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.entity.server.shop.AddressData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.ComponentLevel;
import com.shuangge.english.view.shop.AtyShippingAddressEdit;
import com.shuangge.english.view.user.AtyBrowseUserInfo;

public class AdapterGiveUser extends ArrayAdapter<AttentionData> implements OnClickListener {
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<AttentionData> datas = new ArrayList<AttentionData>();
	private Activity aty;
	public AdapterGiveUser(Activity context) {
		super(context, 0);
		this.aty = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterGiveUser(Activity context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public AttentionData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		UserViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_give_user, null, true);
			holder = new UserViewHolder();
			holder.imgHead = (CircleImageView) convertView.findViewById(R.id.imgHead);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.level = (ComponentLevel) convertView.findViewById(R.id.level);
			holder.txtSignature = (TextView) convertView.findViewById(R.id.txtSignature);
			holder.imgUnChoose = (ImageView) convertView.findViewById(R.id.imgUnChoose);
			holder.imgChoosed = (ImageView) convertView.findViewById(R.id.imgChoosed);
			holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
			holder.imgHead.setOnClickListener(this);
			holder.rl.setOnClickListener(this);
			convertView.setTag(holder);
		} 
		else {
			holder = (UserViewHolder) convertView.getTag();
		}
		AttentionData entity = datas.get(position);
		if (null == holder) {
			DebugPrinter.e(datas.size() +  "-----" +position);
		}
		if (!TextUtils.isEmpty(entity.getName())) {
			holder.txtName.setText(entity.getName());
		}
		if (!TextUtils.isEmpty(entity.getSignature())) {
			holder.txtSignature.setText(entity.getSignature());
		}
//		if (!TextUtils.isEmpty(entity.getWeekScore())) {
//			holder.txtScore.setText(entity.getWeekScore());
//		}
//		else {
//			holder.txtScore.setText("");
//		}
		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(), holder.imgHead));
		}
		else {
			holder.imgHead.clear();
		}
		holder.level.setLevel(entity.getLevel());
		holder.imgHead.setTag(entity.getUserNo());
		holder.rl.setTag(entity);
		return convertView;
	}
	
	public List<AttentionData> getDatas() {
		return datas;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgHead:
			AtyBrowseUserInfo.startAty(getContext(), (Long) v.getTag());
			break;
		case R.id.rl:
			GlobalRes.getInstance().getBeans().setGiveUserInfoData((AttentionData) v.getTag());
//			holder.imgChoosed.setVisibility(View.VISIBLE);
//			holder.imgUnChoose.setVisibility(View.GONE);
//			aty.setResult(resultCode, data)
			aty.finish();
			break;
		}
		
	}

	public final class UserViewHolder {
		
		private CircleImageView imgHead;
		private TextView txtName, txtSignature;
		private ComponentLevel level;
		private ImageView imgUnChoose, imgChoosed;
		private RelativeLayout rl;
	}

}
