package com.shuangge.english.view.msg.adapter;

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

import com.shuangge.english.entity.server.msg.NoticeData;
import com.shuangge.english.support.utils.DateUtils;

public class AdapterSystemNotice extends ArrayAdapter<NoticeData> {
	

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<NoticeData> datas = new ArrayList<NoticeData>();

	public AdapterSystemNotice(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterSystemNotice(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public NoticeData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SystemNoticetViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_sys_notice, null, true);
			holder = new SystemNoticetViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtDetails = (TextView) convertView.findViewById(R.id.txtDetails);
			holder.txtCreateDate = (TextView) convertView.findViewById(R.id.txtCreateDate);
			holder.txtGetGift = (TextView) convertView.findViewById(R.id.txtGetGift);
			holder.txtIsGet = (TextView) convertView.findViewById(R.id.txtIsGet);
			holder.txtReadMore = (TextView) convertView.findViewById(R.id.txtReadMore);
			convertView.setTag(holder);
		} 
		else {
			holder = (SystemNoticetViewHolder) convertView.getTag();
		}
		
		NoticeData entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getTitle())) {
			holder.txtTitle.setText(entity.getTitle().toString());
		}
		if (!TextUtils.isEmpty(entity.getDescription())) {
			holder.txtDetails.setText(entity.getDescription().toString());
		}
		if (null != entity.getCreateDate()) {
			holder.txtCreateDate.setText(DateUtils.convert(entity.getCreateDate()));
		}
		if (entity.getNoticeType() == 1) {
			holder.txtGetGift.setVisibility(View.VISIBLE);
			holder.txtReadMore.setVisibility(View.GONE);
			if(entity.getState() == 1) {
				holder.txtGetGift.setVisibility(View.GONE);
				holder.txtReadMore.setVisibility(View.GONE);
				holder.txtIsGet.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}
	
	public void showMore(SystemNoticetViewHolder holder) {
		holder.showDetails = !holder.showDetails;
	}
	
	public List<NoticeData> getDatas() {
		return datas;
	}
	
	public final class SystemNoticetViewHolder {
		
		private TextView txtTitle;
		private TextView txtCreateDate;
		private TextView txtDetails;
		private TextView txtGetGift, txtIsGet;
		private TextView txtReadMore;
		
		private boolean showDetails = false;


	}

}
