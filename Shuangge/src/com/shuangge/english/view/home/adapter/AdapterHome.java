package com.shuangge.english.view.home.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.entity.server.lesson.Type2Data;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.utils.ViewUtils;

public class AdapterHome extends BaseAdapter implements GlobalRes.CallbackDisplayBitmap {

	private LayoutInflater mInflater;
	private List<Type2Data> datas = new ArrayList<Type2Data>();
	private SparseArray<ViewHolder> viewMap = new SparseArray<ViewHolder>();
	
	private int w;
	private int recommendPos = -1;
	
	public AdapterHome(Context context, Map<String, Type2Data> datas) {
		super();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Type2Data data = null;
		for (String key : datas.keySet()) {
			data = datas.get(key);
//			if (null == data.getDisplay() || data.getDisplay()) {
				this.datas.add(data);
//			}
		}
		Collections.sort(this.datas);
		w = (int) (AppInfo.getScreenWidth() / (this.datas.size() > 3 ? 3.5 : this.datas.size()));
	}
	
	public void setDatas(Map<String, Type2Data> datas) {
		datas.clear();
		Type2Data data = null;
		for (String key : datas.keySet()) {
			data = datas.get(key);
//			if (null == data.getDisplay() || data.getDisplay()) {
				this.datas.add(data);
//			}
		}
		Collections.sort(this.datas);
		w = (int) (AppInfo.getScreenWidth() / (this.datas.size() > 3 ? 3.5 : this.datas.size()));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_home, parent, false);
			holder = new ViewHolder();
			holder.imgLevel = (ImageView) convertView.findViewById(R.id.imgLevel);
			holder.imgAnim = (ImageView) convertView.findViewById(R.id.imgAnim);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			convertView.setTag(holder);
			FrameLayout.LayoutParams p = ViewUtils.setFrameMargins(convertView.findViewById(R.id.llBg), w, 
					FrameLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0);
			p.gravity = Gravity.CENTER;
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		Type2Data data = getItem(position);
		holder.txtName.setText(data.getName().replace("\\n", "\n"));
		holder.imgAnim.setVisibility(View.INVISIBLE);
		holder.imgLevel.setVisibility(View.INVISIBLE);
		String iconUrl = null;
		if (data.getEnabled() && data.getHasAuth()) {
			holder.txtName.setTextColor(0xff5cd4e3);
			iconUrl = data.getIconUrl1();
		}
		else if (!data.getEnabled()) {
			iconUrl = data.getIconUrl3();
			holder.txtName.setTextColor(0xffe9e9e9);
		}
		else {
			iconUrl = data.getIconUrl2();
			holder.txtName.setTextColor(0xffe9e9e9);
		}
		if (position == recommendPos) {
			holder.imgAnim.setVisibility(View.VISIBLE);
		}
		else {
			GlobalRes.DisplayBitmapParam param = new GlobalRes.DisplayBitmapParam(iconUrl, holder.imgLevel);
			param.setCallback(this);
			GlobalRes.getInstance().displayBitmap(param);
		}
		if (position < 3) {
			if (!TextUtils.isEmpty(data.getIconUrl4())) {
				GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(data.getIconUrl4(), holder.imgAnim));
			}
		}
		else {
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(data.getIconUrl1(), holder.imgAnim));
		}
		viewMap.put(position, holder);
		return convertView;
	}
	
	public ViewHolder getViewHolderByPos(int position) {
		ViewHolder holder = viewMap.get(position);
		if (null == holder) {
			return null;
		}
		return holder;
	}
	
	public void toRecommendByPos(int position) {
		this.recommendPos = position;
		ViewHolder holder = viewMap.get(recommendPos);
		if (null == holder) {
			return;
		}
		holder.imgAnim.setVisibility(View.VISIBLE);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setDuration(1000);
		holder.imgAnim.startAnimation(alphaAnimation);
	}
	
	public static class ViewHolder {

		public ImageView imgLevel, imgAnim;
		public TextView txtName;

	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Type2Data getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getItemPos(String id) {
		int position = 0;
		for(int i = 0;i < datas.size(); i++) {
			if (datas.get(i).getClientId().equals(id)) {
				position = i;
			}
		}
		return position;
	}
	
	@Override
	public void onComplete(DisplayBitmapParam param) {
		param.getImageView().setVisibility(View.VISIBLE);
	}
	
}
