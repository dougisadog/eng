package com.shuangge.english.view.group.component;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.entity.server.group.ClassMemberData;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.CircleImageView;

public class MembersContainer extends LinearLayout {

	private boolean hasMeasured = true;
	private int num; 
	private int size = 0;
	private int MARGIN = 15;
	private LinearLayout llPhotosContainer;
	private List<CircleImageView> photos = new ArrayList<CircleImageView>();
	private List<String> urls = new ArrayList<String>();
	
	@SuppressLint("Recycle")
	public MembersContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.component_photo_container, this);
		llPhotosContainer = (LinearLayout) findViewById(R.id.llPhotosContainer);

		MARGIN = DensityUtils.dip2px(getContext(), MARGIN);
		/*
		ViewTreeObserver vto = this.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					int width = llPhotosContainer.getMeasuredWidth();
					size = DensityUtils.dip2px(getContext(), 50);
					num = (width) / (MARGIN + size);
					hasMeasured = true;
					refreshPhoto();
				}
				return true;
			}

		});
		*/
		int width = AppInfo.getScreenWidth() - DensityUtils.dip2px(context, 40 + 50);
		size = DensityUtils.dip2px(getContext(), 50);
		num = (width) / (MARGIN + size);
	}
	
	public void setUrls(List<ClassMemberData> datas) {
		this.urls.clear();
		for (int i = 0; i < datas.size(); i++) {
			this.urls.add(datas.get(i).getHeadUrl());
		}
		if (!hasMeasured) {
			return;
		}
		refreshPhoto();
	}
	
	private void refreshPhoto() {
		int i = photos.size() - 1;
		while (i >= 0) {
			removePhoto(i);
			i--;
		}
		i = 0;
		if (urls.size() > num) {
			urls = urls.subList(0, num);
		}
		for (String url : urls) {
			CircleImageView img = new CircleImageView(getContext(), R.drawable.head_male);
			ViewUtils.setLinearMargins(img, size, size,	i == 0 ? 0 : MARGIN, 0, 0, 0);
			img.setTag(i++);
			llPhotosContainer.addView(img);
			photos.add(img);
			GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(url, img));
		}
	}
	
	public void removePhoto(int i) {
		photos.remove(i);
		llPhotosContainer.removeViewAt(i);
	}
	
	public void removeAllPhotos() {
		while (photos.size() > 0) {
			photos.remove(0);
		}
		llPhotosContainer.removeAllViews();
	}

	
}
