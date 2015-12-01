package com.shuangge.english.view.group.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.ClassMemberData;
import com.shuangge.english.support.utils.DateUtils;
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
		ClassMemeberViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_class_member, null, true);
			holder = new ClassMemeberViewHolder();
			holder.imgHead = (CircleImageView) convertView.findViewById(R.id.imgHead);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtJoinTime = (TextView) convertView.findViewById(R.id.txtJoinTime);
			holder.txtLastRequestTime = (TextView) convertView.findViewById(R.id.txtLastRequestTime);
			holder.txtSignature = (TextView) convertView.findViewById(R.id.txtSignature);
			holder.imgclassManager = (ImageView) convertView.findViewById(R.id.imgclassManager);
			holder.imgclassSubManager = (ImageView) convertView.findViewById(R.id.imgclassSubManager);
			holder.level = (ComponentLevel) convertView.findViewById(R.id.level);
			holder.rlTop = (RelativeLayout) convertView.findViewById(R.id.rlTop);
			holder.txtEnTop = (TextView) convertView.findViewById(R.id.txtEnTop);
			holder.txtCnTop = (TextView) convertView.findViewById(R.id.txtCnTop);
			holder.txtAllScore = (TextView) convertView.findViewById(R.id.txtAllScore);
			holder.txtWeekScore = (TextView) convertView.findViewById(R.id.txtWeekScore);
			holder.txtClassWeekScore = (TextView) convertView.findViewById(R.id.txtClassWeekScore);
			convertView.setTag(holder);
		} 
		else {
			holder = (ClassMemeberViewHolder) convertView.getTag();
		}
		ClassMemberData entity = datas.get(position);
//		holder.imgclassManager.setVisibility(entity.getAuthLevel() == ClassMemberData.MANAGER ? View.VISIBLE :View.GONE);
//		holder.imgclassSubManager.setVisibility(entity.getAuthLevel() == ClassMemberData.SUB_MANAGER ? View.VISIBLE :View.GONE);
		ClassMemberData prevEntity = null;
		if (position > 0) {
			prevEntity = datas.get(position - 1);
		}
		if (position == 0) {
			holder.rlTop.setVisibility(View.VISIBLE);
			holder.txtEnTop.setText(R.string.classMemberTip1En);
			holder.txtCnTop.setText(R.string.classMemberTip1Cn);
		}
		else if (null != prevEntity 
				&& prevEntity.getAuthLevel() > ClassMemberData.NORMAL 
				&& entity.getAuthLevel() == ClassMemberData.NORMAL) {
			holder.rlTop.setVisibility(View.VISIBLE);
			holder.txtEnTop.setText(R.string.classMemberTip2En);
			holder.txtCnTop.setText(R.string.classMemberTip2Cn);
		}
		else {
			holder.rlTop.setVisibility(View.GONE);
		}
		
		if (!TextUtils.isEmpty(entity.getName())) {
			holder.txtName.setText(entity.getName());
		}
		else {
			holder.txtName.setText("");
		}
//		if (!TextUtils.isEmpty(entity.getSignature())) {
//			holder.txtSignature.setText(entity.getSignature());
//		}
//		else {
//			holder.txtSignature.setText("");
//		}
		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(), holder.imgHead));
		}
		else {
			holder.imgHead.clear();
		}
		holder.level.setLevel(entity.getHonorData().getLevel());
//		if (showMore(entity)) {
//		}
//		else {
//		}
		holder.txtAllScore.setText(entity.getScore().toString());
		holder.txtWeekScore.setText(entity.getWeekScore().toString());
		holder.txtJoinTime.setText(DateUtils.convertToDatetime(entity.getJoinDate()) + " 加入班级");
		holder.txtClassWeekScore.setText(entity.getClassWeekScore().toString());
		holder.txtLastRequestTime.setText(DateUtils.convertToDatetime(entity.getLastRequestTime()) + " 访问");
		return convertView;
	}
	
	public List<ClassMemberData> getDatas() {
		return datas;
	}
	
	public void setDatas(List<ClassMemberData> datas) {
		List<ClassMemberData> mamangers = new ArrayList<ClassMemberData>();
		List<ClassMemberData> members = new ArrayList<ClassMemberData>();
		for (ClassMemberData entity : datas) {
			if (entity.getAuthLevel() > ClassMemberData.NORMAL) {
				mamangers.add(entity);
			}
			else {
				members.add(entity);
			}
		}
		mamangers.addAll(members);
		this.datas = mamangers;
	}
	
	public void refreshDatas() {
		List<ClassMemberData> mamangers = new ArrayList<ClassMemberData>();
		List<ClassMemberData> members = new ArrayList<ClassMemberData>();
		for (ClassMemberData entity : datas) {
			if (entity.getAuthLevel() > ClassMemberData.NORMAL) {
				mamangers.add(entity);
			}
			else {
				members.add(entity);
			}
		}
		mamangers.addAll(members);
		this.datas = mamangers;
	}

	public final class ClassMemeberViewHolder {
		
		private CircleImageView imgHead;
		private TextView txtName, txtSignature, txtEnTop, txtCnTop, txtAllScore, txtWeekScore, txtClassWeekScore, txtJoinTime, txtLastRequestTime;
		private ImageView imgclassManager, imgclassSubManager;
		private ComponentLevel level;
		private RelativeLayout rlTop;
		
	}

}
