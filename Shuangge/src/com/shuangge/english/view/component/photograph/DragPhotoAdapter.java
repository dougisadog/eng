package com.shuangge.english.view.component.photograph;

import java.util.Collections;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.CircleImageView;
import com.shuangge.english.view.component.drag.DragGridBaseAdapter;

/**
 * 
 * @author Jeffrey
 *
 */
public class DragPhotoAdapter extends BaseAdapter implements DragGridBaseAdapter {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_LAST_ITEM = 1;
	private static final int TYPE_MAX_COUNT = 2;  
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private int maxSize = 4;
	private List<PhotoParam> datas;
	private int mHidePosition = -1;
	private Context context;
	private int size = 0;

	public DragPhotoAdapter(Context context, List<PhotoParam> datas, int size) {
		this.datas = datas;
		this.context = context;
		this.size = size;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (datas.size() != maxSize) {
			return datas.size() + 1; 
		}
		return datas.size();
	}

	@Override
	public PhotoParam getItem(int position) {
		if (position == datas.size() && datas.size() != maxSize) {
			return null;
		}
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addItem(PhotoParam param) {
		datas.add(param);
	}
	
	public void removeItem(int position) {
		datas.remove(position);
	}
	
	@Override
	public int getItemViewType(int position) {
		if (datas.size() == maxSize) {
			return TYPE_ITEM;
		}
		return position == datas.size() ? TYPE_LAST_ITEM : TYPE_ITEM;
	}
	
	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}


	/**
	 * 由于复用convertView导致某些item消失了，所以这里不复用item，
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CircleImageView img = null;
		if (null == convertView) {
			if (getItemViewType(position) == TYPE_LAST_ITEM) {
				img = new CircleImageView(context, R.drawable.btn_addimg);
			}
			else {
				img = new CircleImageView(context, R.drawable.head_male);
			}
			ViewUtils.setFrameMargins(img, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, 0, 0, 0, 0);
			convertView = mInflater.inflate(R.layout.item_drag_photo, null, true);
			((FrameLayout) convertView.findViewById(R.id.fl)).addView(img);
			convertView.setTag(img);
		}
		ViewUtils.setAbsListViewWH(convertView, size, size);
		if (getItemViewType(position) == TYPE_ITEM) {
			img = (CircleImageView) convertView.getTag();
			PhotoParam param = datas.get(position);
			if (null != param.getPhoto()) {
				img.setBitmap(param.getPhoto());
			}
			else {
				GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(param.getUrl(), img));
			}
		}
		if (position == mHidePosition) {
			convertView.setVisibility(View.INVISIBLE);
		}
		else {
			convertView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	@Override
	public void reorderItems(int oldPosition, int newPosition) {
		if (oldPosition == -1 || oldPosition >= datas.size()) {
			return;
		}
		PhotoParam temp = datas.get(oldPosition);
		if (oldPosition < newPosition) {
			for (int i = oldPosition; i < newPosition; i++) {
				Collections.swap(datas, i, i + 1);
			}
		} 
		else if (oldPosition > newPosition) {
			for (int i = oldPosition; i > newPosition; i--) {
				Collections.swap(datas, i, i - 1);
			}
		}
		datas.set(newPosition, temp);
	}

	@Override
	public void setHideItem(int hidePosition) {
		this.mHidePosition = hidePosition;
		notifyDataSetChanged();
	}
	
	@Override
	public boolean cannotDrag(int position) {
		if (position == datas.size() && datas.size() != maxSize) {
			return true;
		}
		return false;
	}
	
	public void recycle() {
		for (PhotoParam photoParam : datas) {
			if (null == photoParam.getPhoto() || photoParam.getPhoto().isRecycled()) {
				continue;
			}
			photoParam.getPhoto().recycle();
		}
	}
	
	public static class PhotoParam {
		
		private String url;
		private Bitmap photo;
		private Long no;
		private byte[] bytes;
		
		public String getUrl() {
			return url;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}

		public Bitmap getPhoto() {
			return photo;
		}

		public void setPhoto(Bitmap photo) {
			this.photo = photo;
		}

		public Long getNo() {
			return no;
		}

		public void setNo(Long no) {
			this.no = no;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

	}

}
