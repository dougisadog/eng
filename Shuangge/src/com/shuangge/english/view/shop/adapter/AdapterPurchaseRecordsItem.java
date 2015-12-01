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
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.shop.OrderData;
import com.shuangge.english.support.utils.DateUtils;

public class AdapterPurchaseRecordsItem extends ArrayAdapter<OrderData> {
	
	private final static int UNPAID = 0;
	private final static int PAID = 1;
	private final static int SENT = 2;
	public final static int RECEUVED = 3;
	private final static int CANCELED = 99;
	public final static int NOT_ENOUGH_STOCK = -1;  //库存不足
	public final static int TIME_OUT = -2;			//订单超时
	public final static int UNCHECKED = -99; 	    //订单待验证


	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<OrderData> datas = new ArrayList<OrderData>();
	public AdapterPurchaseRecordsItem(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterPurchaseRecordsItem(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public OrderData getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.item_shop_mine, null, true);
			holder = new SystemNoticetViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
			holder.txtStatus1 = (TextView) convertView.findViewById(R.id.txtStatus1);
			holder.txtStatus2 = (TextView) convertView.findViewById(R.id.txtStatus2);
			holder.txtStatus3 = (TextView) convertView.findViewById(R.id.txtStatus3);
			holder.txtStatus4 = (TextView) convertView.findViewById(R.id.txtStatus4);
			holder.txtStatus5 = (TextView) convertView.findViewById(R.id.txtStatus5);
			holder.txtStatus.setVisibility(View.GONE);
			holder.txtStatus1.setVisibility(View.GONE);
			holder.txtStatus2.setVisibility(View.GONE);
			holder.txtStatus3.setVisibility(View.GONE);
			holder.txtStatus4.setVisibility(View.GONE);
			holder.txtStatus5.setVisibility(View.GONE);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgView);
			convertView.setTag(holder);
		} 
		else {
			holder = (SystemNoticetViewHolder) convertView.getTag();
		}
		
		OrderData entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getGoodsName())) {
			holder.txtName.setText(entity.getGoodsName().toString());
		}
//		if (entity.getState() == 0) {
//			holder.txtStatus.setText("");
//		}
		
		if (null != entity.getEditTime()) {
			holder.txtTime.setText("下单时间: " + DateUtils.convert(entity.getEditTime()));
		}
		if (!TextUtils.isEmpty(entity.getGoodsPic())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getGoodsPic(), holder.imgView));
		} else {
			holder.imgView.setImageResource(R.drawable.head_male);
		}
		
		switch(entity.getState()) {
		case UNPAID:
//			holder.txtStatus.setText(this.getContext().getString(R.string.shopOrderTip1));
			holder.txtStatus.setVisibility(View.VISIBLE);
			holder.txtStatus1.setVisibility(View.GONE);
			holder.txtStatus2.setVisibility(View.GONE);
			holder.txtStatus3.setVisibility(View.GONE);
			holder.txtStatus4.setVisibility(View.GONE);
			holder.txtStatus5.setVisibility(View.GONE);
//			holder.txtStatus.setTextColor(0xff7f21);
			break;
		case PAID:
//			holder.txtStatus3.setText(this.getContext().getString(R.string.shopOrderTip4));
			holder.txtStatus3.setVisibility(View.VISIBLE);
			holder.txtStatus.setVisibility(View.GONE);
			holder.txtStatus1.setVisibility(View.GONE);
			holder.txtStatus2.setVisibility(View.GONE);
			holder.txtStatus4.setVisibility(View.GONE);
			holder.txtStatus5.setVisibility(View.GONE);
			break;
		case SENT:
//			holder.txtStatus.setText(this.getContext().getString(R.string.shopOrderTip2));
//			holder.txtStatus.setTextColor(0x41a6e6);
			holder.txtStatus1.setVisibility(View.VISIBLE);
			holder.txtStatus.setVisibility(View.GONE);
			holder.txtStatus2.setVisibility(View.GONE);
			holder.txtStatus3.setVisibility(View.GONE);
			holder.txtStatus4.setVisibility(View.GONE);
			holder.txtStatus5.setVisibility(View.GONE);
			break;
		case RECEUVED:
//			holder.txtStatus.setText(this.getContext().getString(R.string.shopOrderTip3));
//			holder.txtStatus.setTextColor(0x62c8a5);
			holder.txtStatus2.setVisibility(View.VISIBLE);
			holder.txtStatus.setVisibility(View.GONE);
			holder.txtStatus1.setVisibility(View.GONE);
			holder.txtStatus3.setVisibility(View.GONE);
			holder.txtStatus4.setVisibility(View.GONE);
			holder.txtStatus5.setVisibility(View.GONE);
			break;
		case NOT_ENOUGH_STOCK:
			holder.txtStatus5.setVisibility(View.VISIBLE);
			holder.txtStatus.setVisibility(View.GONE);
			holder.txtStatus1.setVisibility(View.GONE);
			holder.txtStatus2.setVisibility(View.GONE);
			holder.txtStatus3.setVisibility(View.GONE);
			holder.txtStatus4.setVisibility(View.GONE);
//			holder.txtStatus.setText(this.getContext().getString(R.string.shopOrderTip5));
			break;
		case TIME_OUT:
//			holder.txtStatus.setText(this.getContext().getString(R.string.shopOrderTip5));
			holder.txtStatus4.setVisibility(View.VISIBLE);
			holder.txtStatus.setVisibility(View.GONE);
			holder.txtStatus1.setVisibility(View.GONE);
			holder.txtStatus2.setVisibility(View.GONE);
			holder.txtStatus3.setVisibility(View.GONE);
			holder.txtStatus5.setVisibility(View.GONE);
			break;
	}
		
		return convertView;
	}
	
	public List<OrderData> getDatas() {
		return datas;
	}
	
	public final class SystemNoticetViewHolder {
		
		private TextView txtTime, txtStatus, txtName, txtStatus1, txtStatus2, txtStatus3, txtStatus4, txtStatus5;
		private ImageView imgView;

	}

}
