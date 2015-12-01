package com.shuangge.english.view.component.photograph;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.CircleImageView;

public class PhotosContainer extends LinearLayout {

	private boolean hasMeasured = true;
	private int size = 0;
	private static int MARGIN = 10;
	private LinearLayout llPhotoContainer;
	private List<CircleImageView> photos = new ArrayList<CircleImageView>();
	private ArrayList<String> urls = new ArrayList<String>();
	
	@SuppressLint("Recycle")
	public PhotosContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.component_photo_container, this);
		llPhotoContainer = (LinearLayout) findViewById(R.id.llPhotosContainer);

		size = DensityUtils.dip2px(getContext(), 40);
		/*
		ViewTreeObserver vto = this.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!hasMeasured && llPhotoContainer.getMeasuredWidth() > 0) {
//					size = (llPhotoContainer.getMeasuredWidth() - MARGIN * 5) / 4;
					hasMeasured = true;
					refreshPhoto();
				}
				return true;
			}

		});
		*/
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AtyPhotoBrowser.startAty(getContext(), (Integer) v.getTag(), urls);
		}
		
	};
	
	public void setUrls(List<String> urls) {
		this.urls.clear();
		for (int i = 0; i < urls.size(); i++) {
			this.urls.add(urls.get(i));
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
		for (String url : urls) {
			CircleImageView img = new CircleImageView(getContext(), R.drawable.head_male, 3, Color.WHITE);
			ViewUtils.setLinearMargins(img, size, size, i == 0 ? 0 : MARGIN, 0, 0, MARGIN);
			img.setTag(i++);
			img.setOnClickListener(onClickListener);
			llPhotoContainer.addView(img);
			photos.add(img);
			GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(url, img));
		}
	}
	
	public void removePhoto(int i) {
		photos.remove(i);
		llPhotoContainer.removeViewAt(i);
	}

}
