package com.shuangge.english.view.download.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.support.utils.Utility;
import com.shuangge.english.view.download.adapter.AdapterDownload.OnBeforeDownloadListener;

public class AdapterDownloadDetails extends ArrayAdapter<EntityResType4> {

	private LayoutInflater mInflater;
	private List<EntityResType4> datas = new ArrayList<EntityResType4>();
	private Map<String, ViewHolder> viewMap = new HashMap<String, ViewHolder>();
	
	private OnDownloadAllListener listener;
	private OnBeforeDownloadListener onBeforeDownloadListener;

	public AdapterDownloadDetails(Activity context, OnDownloadAllListener listener) {
		super(context, 0);
		this.listener = listener;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterDownloadDetails(Activity context, OnDownloadAllListener listener, OnBeforeDownloadListener onBeforeDownloadListener) {
		super(context, 0);
		this.listener = listener;
		this.mInflater = LayoutInflater.from(context);
		this.onBeforeDownloadListener = onBeforeDownloadListener;
	}
	
	public AdapterDownloadDetails(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public static interface OnDownloadAllListener {
		
		public void onDownload(String id);
		
		public void onCancel(String id);
		
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
		ViewHolder holder = null;
		EntityResType4 entity = datas.get(position);
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_download2_details, null, true);
			holder = new ViewHolder();
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.imgFinish = (ImageView) convertView.findViewById(R.id.imgFinish);
			holder.imgDownload = (ImageView) convertView.findViewById(R.id.imgDownload);
			holder.imgDownload.setOnClickListener(onClickListener);
			holder.imgCancel = (ImageView) convertView.findViewById(R.id.imgCancel);
			holder.imgCancel.setOnClickListener(onClickListener);
			holder.pbDownload = (ProgressBar) convertView.findViewById(R.id.pbDownload);
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
			if (viewMap.containsKey(holder.viewId))
				viewMap.remove(holder.viewId);
		}
		holder.viewId = entity.getId();
		holder.imgDownload.setTag(entity.getId());
		holder.imgCancel.setTag(entity.getId());
		holder.txtName.setText(entity.getName());
		String resId = entity.getId().replaceFirst(GlobalRes.getInstance().getBeans().getDefaultLessonId(), "101");
		int id = convertView.getContext().getResources().getIdentifier("icon_2_" + resId, "drawable", "air.com.shuangge.phone.ShuangEnglish");
		if (id == 0) {
			id = R.drawable.icon_2_core;
		}
		holder.imgIcon.setImageResource(id);
		refreshStatus(entity, holder);
		viewMap.put(holder.viewId, holder);
		return convertView;
	}
	
	
	public void refreshStatus(EntityResType4 entity, ViewHolder holder) {
		if (null == holder || null == entity) {
			return;
		}
		if (entity.isFinished()) {
			holder.imgFinish.setVisibility(View.VISIBLE);
			holder.imgDownload.setVisibility(View.GONE);
			holder.imgCancel.setVisibility(View.GONE);
			holder.pbDownload.setVisibility(View.GONE);
		}
		else {
			holder.imgFinish.setVisibility(View.GONE);
			if (entity.getStatus() == EntityResType4.STATUS_START || entity.getStatus() == EntityResType4.STATUS_WAIT) {
				holder.imgDownload.setVisibility(View.GONE);
				holder.imgCancel.setVisibility(View.VISIBLE);
				holder.pbDownload.setVisibility(View.VISIBLE);
				holder.pbDownload.setProgress((int) entity.getProgress());
				holder.pbDownload.setMax((int) entity.getMax());
			}
			else {
				holder.imgDownload.setVisibility(View.VISIBLE);
				holder.imgCancel.setVisibility(View.GONE);
				holder.pbDownload.setVisibility(View.GONE);	
			}
		}
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (null == listener) {
				return;
			}
			switch (v.getId()) {
			case R.id.imgDownload:
				if (Utility.isConnected(getContext())) {
					if (Utility.isWifi(getContext())) {
						if (GlobalRes.getInstance().getBeans().isWifiAutoDownLoad()) {
							listener.onDownload((String) v.getTag());
							break;
						}
						else {
							//wifi提示
							onBeforeDownloadListener.onBeforeDownload(2, (String) v.getTag());
						}
					}
					else if (GlobalRes.getInstance().getBeans().isCellularTipsDownLoad()){
						//非wifi提示
						onBeforeDownloadListener.onBeforeDownload(1, (String) v.getTag());
					}
					else {
						//非wifi提示 false
						listener.onDownload((String) v.getTag());
						break;
					}
				}
				else {
					//无网
					onBeforeDownloadListener.onBeforeDownload(0, (String) v.getTag());
				}
				break;
			case R.id.imgCancel:
				listener.onCancel((String) v.getTag());
				break;
			}
		}
		
	};

	public List<EntityResType4> getDatas() {
		return datas;
	}
	
	public Map<String, ViewHolder> getAllViewMap() {
		return viewMap;
	}

	public final class ViewHolder {
		
		public String viewId;

		private ImageView imgIcon, imgDownload, imgCancel, imgFinish;
		private TextView txtName;
		public ProgressBar pbDownload;
		

	}

}
