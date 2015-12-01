package com.shuangge.english.view.component.photograph;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.view.component.drag.DragGridView;

public class PhotosEditContainer extends DragGridView {

	public static final int MODULE_PHOTO = 100;

	private int position;
	private DragPhotoAdapter adapter;
	
	private boolean hasMeasured = false;
	private int MARGIN = 10;
	private int size = 0;
	
	private List<DragPhotoAdapter.PhotoParam> datas = new ArrayList<DragPhotoAdapter.PhotoParam>();
	
	public PhotosEditContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ViewTreeObserver vto = this.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!hasMeasured && PhotosEditContainer.this.getMeasuredWidth() > 0) {
					MARGIN = DensityUtils.dip2px(getContext(), MARGIN);
					size = (PhotosEditContainer.this.getMeasuredWidth() - MARGIN * 5) / 4;
					hasMeasured = true;
					adapter = new DragPhotoAdapter(getContext(), datas, size);
					setAdapter(adapter);
				}
				return true;
			}

		});
		
	}
	
	public void setUrls(List<String> urls, List<Long> nos, List<Integer> sortNos) {
		DragPhotoAdapter.PhotoParam param = null;
		for (int i = 0; i < urls.size(); i++) {
			param = new DragPhotoAdapter.PhotoParam();
			param.setUrl(urls.get(i));
			param.setNo(nos.get(i));
			datas.add(param);
		}
	}

	@Override
	protected void onClick(View v, int position) {
		this.position = position;
		Intent i = new Intent(getContext(), AtyPhotograph.class);
		if (null == adapter.getItem(position)) {
			i.putExtra(AtyPhotograph.NO_DELETE, true);
		}
		((Activity) getContext()).startActivityForResult(i, MODULE_PHOTO);
	}

	
	public void onActivityResult(int resultCode, Intent data) {
		switch (resultCode) {
		case AtyPhotograph.CODE_BITMAP:
			byte[] bytes = data.getByteArrayExtra(AtyPhotograph.CALLBACK_DATAS_BYTES);
			Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			DragPhotoAdapter.PhotoParam param = adapter.getItem(position);
			if (null != param) {
				param.setNo(null);
				param.setBytes(bytes);
				param.setPhoto(photo);
			}
			else {
				param = new DragPhotoAdapter.PhotoParam();
				param.setPhoto(photo);
				param.setBytes(bytes);
				adapter.addItem(param);
			}
			adapter.notifyDataSetChanged();
			break;
		case AtyPhotograph.CODE_DELETE:
			adapter.removeItem(position);
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
	
	public List<DragPhotoAdapter.PhotoParam> getDatas() {
		return datas;
	}
	
	public void recycle() {
		if (null != adapter) {
			adapter.recycle();
			adapter = null;
		}
	}
	
}
