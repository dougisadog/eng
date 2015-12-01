package com.shuangge.english.view.rewards.adapter;

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
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.group.LastWeekDedicateData;
import com.shuangge.english.view.component.CircleImageView;

public class AdapterRewardsLastweekRank extends ArrayAdapter<LastWeekDedicateData> {
	

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<LastWeekDedicateData> datas = new ArrayList<LastWeekDedicateData>();

	public AdapterRewardsLastweekRank(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterRewardsLastweekRank(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public LastWeekDedicateData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		PersonalRanklistViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_personal_ranklist, null, true);
			holder = new PersonalRanklistViewHolder();
//			convertView.findViewById(R.id.rlRanklistItem).setOnTouchListener(new OnTouchListener() {
//				
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					return false;
//				}
//			});
			
			holder.imgHead1 = (CircleImageView) convertView.findViewById(R.id.imgHead1);
			holder.imgHead2 = (CircleImageView) convertView.findViewById(R.id.imgHead2);
			holder.txtNo = (TextView) convertView.findViewById(R.id.txtNo);
			holder.imgNo = (ImageView) convertView.findViewById(R.id.imgNo);
			holder.txtName1 = (TextView) convertView.findViewById(R.id.txtName1);
			holder.txtName2 = (TextView) convertView.findViewById(R.id.txtName2);
			holder.txtScore1 = (TextView) convertView.findViewById(R.id.txtScore1);
			holder.txtScore2 = (TextView) convertView.findViewById(R.id.txtScore2);
			convertView.setTag(holder);
		} 
		else {
			holder = (PersonalRanklistViewHolder) convertView.getTag();
		}
		LastWeekDedicateData entity = datas.get(position);
		holder.txtNo.setVisibility(View.INVISIBLE);
		holder.imgNo.setVisibility(View.INVISIBLE);
		holder.imgHead1.setVisibility(View.GONE);
		holder.imgHead2.setVisibility(View.GONE);
		holder.txtScore1.setVisibility(View.GONE);
		holder.txtScore2.setVisibility(View.GONE);
		holder.txtName1.setVisibility(View.GONE);
		holder.txtName2.setVisibility(View.GONE);
		
		TextView txtName = null;
		CircleImageView imgHead = null;
		TextView txtScore = null;
		
		if (null != entity.getNo()) {
			if (entity.getNo().intValue() <= 0 || entity.getNo().intValue() >= 4) {
				convertView.findViewById(R.id.rlRanklistItem).setBackgroundColor(0xfff7f7f7);
				holder.txtNo.setVisibility(View.VISIBLE);
				txtName = holder.txtName2;
				imgHead = holder.imgHead2;
				txtScore = holder.txtScore2;
				if (entity.getNo().intValue() <= 0) {
					holder.txtNo.setText(R.string.noRanking);
				}
				else {
					holder.txtNo.setText(entity.getNo().toString());
				}
			}
			else {
				switch (entity.getNo().intValue()) {
				case 1:
					holder.imgNo.setImageResource(R.drawable.icon_ranklist_no1);
					break;
				case 2:
					holder.imgNo.setImageResource(R.drawable.icon_ranklist_no2);
					break;
				case 3:
					holder.imgNo.setImageResource(R.drawable.icon_ranklist_no3);
					break;
				}
				convertView.findViewById(R.id.rlRanklistItem).setBackgroundColor(0xffffffff);
				holder.imgNo.setVisibility(View.VISIBLE);
				txtName = holder.txtName1;
				imgHead = holder.imgHead1;
				txtScore = holder.txtScore1;
			}
		}
		if (null != entity.getNo()) {
			holder.txtNo.setText(entity.getNo().toString());
		}
		imgHead.setVisibility(View.VISIBLE);
		txtScore.setVisibility(View.VISIBLE);
		txtName.setVisibility(View.VISIBLE);
		
		if (!TextUtils.isEmpty(entity.getName())) {
			txtName.setText(entity.getName().toString());
		}
		if (null != entity.getScore()) {
			txtScore.setText(entity.getScore().toString());
		}
		if (!TextUtils.isEmpty(entity.getHeadUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getHeadUrl(), imgHead));
		}
		else {
			imgHead.clear();
		}
		return convertView;
	}
	
	public List<LastWeekDedicateData> getDatas() {
		return datas;
	}

	public final class PersonalRanklistViewHolder {
		
		private TextView txtNo;
		private ImageView imgNo;
		private TextView txtName1, txtName2;
		private CircleImageView imgHead1, imgHead2;
		private TextView txtScore1, txtScore2;

	}

}
