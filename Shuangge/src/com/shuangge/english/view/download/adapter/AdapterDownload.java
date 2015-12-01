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
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.support.utils.Utility;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;

public class AdapterDownload extends ArrayAdapter<EntityResType2> {

	private LayoutInflater mInflater;
	private List<EntityResType2> datas = new ArrayList<EntityResType2>();
	private Map<String, ViewHolder> viewMap = new HashMap<String, ViewHolder>();
	
	private OnDownloadAllListener listener;
	private OnBeforeDownloadListener onBeforeDownloadListener;

	public AdapterDownload(Activity context, OnDownloadAllListener listener) {
		super(context, 0);
		this.listener = listener;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterDownload(Activity context, OnDownloadAllListener listener, OnBeforeDownloadListener onBeforeDownloadListener) {
		super(context, 0);
		this.listener = listener;
		this.mInflater = LayoutInflater.from(context);
		this.onBeforeDownloadListener = onBeforeDownloadListener;
	}
	
	public AdapterDownload(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public static interface OnDownloadAllListener {
		
		public void onDownloadAll(String id);
		
		public void onCancel(String id);
		
	}
	
	public static interface OnBeforeDownloadListener {
		
		public void onBeforeDownload(int type, String id);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public EntityResType2 getItem(int position) {
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
		EntityResType2 entity = datas.get(position);
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_download2, null, true);
			holder = new ViewHolder();
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
			holder.imgMore = (ImageView) convertView.findViewById(R.id.imgMore);
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtFinish = (TextView) convertView.findViewById(R.id.txtFinish);
			holder.txtLessonNum = (TextView) convertView.findViewById(R.id.txtLessonNum);
			holder.txtDownloadAll = (TextView) convertView.findViewById(R.id.txtDownloadAll);
			holder.txtDownloadAll.setOnClickListener(onClickListener);
			holder.txtCancel = (TextView) convertView.findViewById(R.id.txtCancel);
			holder.txtCancel.setOnClickListener(onClickListener);
			holder.pbDownloadAll = (ProgressBar) convertView.findViewById(R.id.pbDownloadAll);
			convertView.setTag(holder);
		} 
		else {
			holder = (ViewHolder) convertView.getTag();
			if (viewMap.containsKey(holder.viewId))
				viewMap.remove(holder.viewId);
		}
		holder.viewId = entity.getId();
		holder.txtDownloadAll.setTag(entity.getId());
		holder.txtCancel.setTag(entity.getId());
		holder.txtName.setText(entity.getName());
		holder.txtLessonNum.setText(entity.getType4s().size() + " lessons");
		refreshStatus(entity, holder);
		viewMap.put(holder.viewId, holder);
		return convertView;
	}
	
	
	public void refreshStatus(EntityResType2 entity, ViewHolder holder) {
		if (null == holder || null == entity) {
			return;
		}
		boolean isAllFinished = true;
		int finishedNum = 0;
		for (EntityResType4 entityResType4 : entity.getType4s()) {
			if (!entityResType4.isFinished()) {
				isAllFinished = false;
				continue;
			}
			++finishedNum;
		}
		if (isAllFinished) {
			holder.txtFinish.setVisibility(View.VISIBLE);
			holder.txtDownloadAll.setVisibility(View.GONE);
			holder.txtCancel.setVisibility(View.GONE);
			holder.pbDownloadAll.setVisibility(View.GONE);
		}
		else {
			holder.txtFinish.setVisibility(View.GONE);
			if (entity.getDownloadAllStatus() == EntityResType4.STATUS_START || entity.getDownloadAllStatus() == EntityResType4.STATUS_WAIT) {
				holder.txtDownloadAll.setVisibility(View.GONE);
				holder.txtCancel.setVisibility(View.VISIBLE);
				holder.pbDownloadAll.setVisibility(View.VISIBLE);
				holder.pbDownloadAll.setProgress(finishedNum);
				holder.pbDownloadAll.setMax(entity.getType4s().size());
			}
			else {
				holder.txtDownloadAll.setVisibility(View.VISIBLE);
				holder.txtCancel.setVisibility(View.GONE);
				holder.pbDownloadAll.setVisibility(View.GONE);	
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
			case R.id.txtDownloadAll:
				if (Utility.isConnected(getContext())) {
					if (Utility.isWifi(getContext())) {
						if (GlobalRes.getInstance().getBeans().isWifiAutoDownLoad()) {
							listener.onDownloadAll((String) v.getTag());
							break;
						}
						else {
							//wifi提示
						onBeforeDownloadListener.onBeforeDownload(2, (String) v.getTag());
						}
					}
					else if (GlobalRes.getInstance().getBeans().isCellularTipsDownLoad()){
						//非wifi提示 true
					onBeforeDownloadListener.onBeforeDownload(1, (String) v.getTag());
					}
					else {
						//非wifi提示 false
						listener.onDownloadAll((String) v.getTag());
						break;
					}
				}
				else {
					//无网
					onBeforeDownloadListener.onBeforeDownload(0, (String) v.getTag());
				}
				break;
			case R.id.txtCancel:
				listener.onCancel((String) v.getTag());
				break;
			}
		}
		
	};

	public List<EntityResType2> getDatas() {
		return datas;
	}
	
	public Map<String, ViewHolder> getAllViewMap() {
		return viewMap;
	}

	public final class ViewHolder {
		
		public String viewId;

		private ImageView imgIcon, imgMore;
		private TextView txtName, txtLessonNum, txtDownloadAll, txtCancel, txtFinish;
		public ProgressBar pbDownloadAll;
		

	}

}
