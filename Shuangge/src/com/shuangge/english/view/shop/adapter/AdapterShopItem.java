package com.shuangge.english.view.shop.adapter;

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
import com.shuangge.english.entity.server.shop.GoodsData;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.utils.DateUtils;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;

public class AdapterShopItem extends ArrayAdapter<GoodsData> {

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<GoodsData> datas = new ArrayList<GoodsData>();
	public AdapterShopItem(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterShopItem(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public GoodsData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SystemNoticetViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_shop, null, true);
			holder = new SystemNoticetViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtMoney = (TextView) convertView.findViewById(R.id.txtMoney);
			holder.txtCredits = (TextView) convertView.findViewById(R.id.txtCredits);
			holder.txtEndTime = (TextView) convertView.findViewById(R.id.txtEndTime);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgView);
			holder.imgType = (ImageView) convertView.findViewById(R.id.imgType);
			holder.iconCredits = (ImageView) convertView.findViewById(R.id.iconCredits);
			holder.iconMoney = (ImageView) convertView.findViewById(R.id.iconMoney);
			
			convertView.setTag(holder);
		} 
		else {
			holder = (SystemNoticetViewHolder) convertView.getTag();
		}
		int w = (AppInfo.getScreenWidth() >> 1) - DensityUtils.dip2px(getContext(), 10); 
		RelativeLayout.LayoutParams params = ViewUtils.setRelativeMargins(holder.imgView, w, w);
		params.setMargins(0, 0, 0, 0);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		
		GoodsData entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getName())) {
			holder.txtName.setText(entity.getName().toString());
		}
	
		if (GlobalRes.getInstance().getBeans().getPayType() == 0) {
			holder.txtMoney.setText(String.valueOf(entity.getPrice()));
			holder.txtMoney.setVisibility(View.VISIBLE);
			holder.iconMoney.setVisibility(View.VISIBLE);
			holder.txtCredits.setVisibility(View.GONE);
			holder.iconCredits.setVisibility(View.GONE);
			
		} else {
			holder.txtCredits.setText(String.valueOf(entity.getScorePrice()));
			holder.txtCredits.setVisibility(View.VISIBLE);
			holder.iconCredits.setVisibility(View.VISIBLE);
			holder.txtMoney.setVisibility(View.GONE);
			holder.iconMoney.setVisibility(View.GONE);
			
		}
		if (null != entity.getOverTime()) {
			holder.txtEndTime.setText( "截止时间:" + DateUtils.convert(entity.getOverTime()));
		} else {
			holder.txtEndTime.setText("");
		}
		if (!TextUtils.isEmpty(entity.getUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getUrl(), holder.imgView));
		} else {
			holder.imgView.setImageResource(R.drawable.head_male);
		}
		return convertView;
	}
	
	public List<GoodsData> getDatas() {
		return datas;
	}
	
	public final class SystemNoticetViewHolder {
		
		private TextView txtEndTime, txtMoney, txtCredits, txtName;
		private ImageView imgView, imgType, iconCredits, iconMoney;

	}

}
