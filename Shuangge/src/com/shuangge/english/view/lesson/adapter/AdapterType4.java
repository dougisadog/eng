package com.shuangge.english.view.lesson.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.server.lesson.Type4Data;
import com.shuangge.english.view.component.RatingBarView;

public class AdapterType4 extends ArrayAdapter<EntityResType4> {
	

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<EntityResType4> datas = new ArrayList<EntityResType4>();
	private Integer selectedPosition;

	public AdapterType4(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterType4(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public EntityResType4 getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LessonType4ViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_lesson_type4, null, true);
			holder = new LessonType4ViewHolder();
			holder.imgLessonType = (ImageView) convertView.findViewById(R.id.imgLessonType);
			holder.imgLock = (ImageView) convertView.findViewById(R.id.imgLock);
			holder.imgStart = (ImageView) convertView.findViewById(R.id.imgStart);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.rbStar = (RatingBarView) convertView.findViewById(R.id.rbStar);
			convertView.setTag(holder);
		} 
		else {
			holder = (LessonType4ViewHolder) convertView.getTag();
		}
		EntityResType4 entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getName())) {
			holder.txtTitle.setText(Html.fromHtml(entity.getName()));
		}
		int id = 0;
		String resId = entity.getId().replaceFirst(GlobalRes.getInstance().getBeans().getDefaultLessonId(), "101");
		if (null != selectedPosition && selectedPosition == position) {
			id = convertView.getContext().getResources().getIdentifier("icon_" + resId + "_p", "drawable", "air.com.shuangge.phone.ShuangEnglish");
			if (id == 0) {
				id = R.drawable.icon_core_p;
			}
			holder.txtTitle.setTextColor(Color.WHITE);
			convertView.setBackgroundColor(0xFF26C6DA);
		}
		else {
			id = convertView.getContext().getResources().getIdentifier("icon_" + resId, "drawable", "air.com.shuangge.phone.ShuangEnglish");
			if (id == 0) {
				id = R.drawable.icon_core;
			}
			holder.txtTitle.setTextColor(0xFF5F5F5F);
			convertView.setBackgroundColor(Color.WHITE);
		}
		holder.imgLessonType.setImageResource(id);
		
		Type4Data type4 = GlobalRes.getInstance().getBeans().getUnlockDatas().getType4s().get(entity.getId());
		if (null == type4) {
			holder.rbStar.setVisibility(View.GONE);
			holder.imgLock.setVisibility(View.VISIBLE);
			holder.imgStart.setVisibility(View.GONE);
		}
		else {
			holder.imgLock.setVisibility(View.GONE);
			holder.rbStar.setVisibility(View.VISIBLE);
			holder.imgStart.setVisibility(View.VISIBLE);
			holder.rbStar.setStar(type4.getStarNum());
		}
		return convertView;
	}
	
	public List<EntityResType4> getDatas() {
		return datas;
	}
	
	public final class LessonType4ViewHolder {
		
		private ImageView imgLessonType, imgLock, imgStart;
		private TextView txtTitle;
		private RatingBarView rbStar;

	}
	
	public void onSelected(Integer selectedPosition) {
		this.selectedPosition = selectedPosition;
		this.notifyDataSetChanged();
	}

}
