package com.shuangge.english.view.read.adapter;

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
import com.shuangge.english.entity.server.read.ReadListData;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.view.component.CircleImageView;

public class AdapterReadList extends ArrayAdapter<ReadListData> {
	

	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<ReadListData> datas = new ArrayList<ReadListData>();
	private Integer selectedPosition;

	public AdapterReadList(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterReadList(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ReadListData getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.item_read_list, null, true);
			holder = new UserViewHolder();
			holder.imgHead = (ImageView) convertView.findViewById(R.id.imgHead);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtCoreWordCount = (TextView) convertView.findViewById(R.id.txtCoreWordCount);
			convertView.setTag(holder);
		} 
		else {
			holder = (UserViewHolder) convertView.getTag();
		}
		ReadListData entity = datas.get(position);
		if (null == holder) {
			DebugPrinter.e(datas.size() +  "-----" +position);
		}
		if (!TextUtils.isEmpty(entity.getTitle())) {
			holder.txtTitle.setText(entity.getTitle());
		}
		holder.txtCoreWordCount.setText(entity.getCoreWordCount() + "");
		if (!TextUtils.isEmpty(entity.getImgUrl())) {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(entity.getImgUrl(), holder.imgHead));
		}
		else {
			holder.imgHead.setImageBitmap(null);  
		}
		if(!TextUtils.isEmpty(entity.getDescription())) {
			holder.txtDescription.setText(entity.getDescription());
		}
		
		return convertView;
	}
	
	public List<ReadListData> getDatas() {
		return datas;
	}
	
	public final class UserViewHolder {
		
		private ImageView imgHead;
		private TextView txtTitle, txtDescription, txtCoreWordCount;
		
	}
	
	public void onSelected(Integer selectedPosition) {
		this.selectedPosition = selectedPosition;
		this.notifyDataSetChanged();
	}

}
