package com.shuangge.english.view.secretmsg.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassMemberData;
import com.shuangge.english.entity.server.user.OtherInfoData;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.ComponentLevel;

public class AdapterClassMember extends ArrayAdapter<ClassMemberData> {
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<ClassMemberData> datas = new ArrayList<ClassMemberData>();

	public AdapterClassMember(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterClassMember(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ClassMemberData getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.item_friend, null, true);
			holder = new UserViewHolder();
			holder.imgHead = (CircleImageView) convertView.findViewById(R.id.imgHead);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.level = (ComponentLevel) convertView.findViewById(R.id.level);
			holder.txtSignature = (TextView) convertView.findViewById(R.id.txtSignature);
			convertView.setTag(holder);
		} 
		else {
			holder = (UserViewHolder) convertView.getTag();
		}
		ClassMemberData entity = datas.get(position);
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
		holder.level.setLevel(entity.getHonorData().getLevel());
		return convertView;
	}
	
	public List<ClassMemberData> getDatas() {
		return datas;
	}

	public final class UserViewHolder {
		
		private CircleImageView imgHead;
		private TextView txtName, txtSignature;
		private ComponentLevel level;
		
	}

}
