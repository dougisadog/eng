package com.shuangge.english.view.component.photograph;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.viewpaper.CircleViewPagerIndicator;
import com.shuangge.english.view.component.viewpaper.HackyViewPager;


public class AtyPhotoBrowser extends AbstractAppActivity implements OnClickListener, OnViewTapListener, OnLongClickListener {

	public static final int REQUEST_PHOTO_BROWSER = 1027;
	
	public static final String URLS = "urls";
	public static final String INDEX = "index";
	
	private HackyViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_photo_browser);
		Bundle extras = getIntent().getExtras();
		viewPager = (HackyViewPager) findViewById(R.id.vf);
		ImageAdapter adapter = new ImageAdapter(extras.getStringArrayList("urls"));
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(extras.getInt("index"));
		CircleViewPagerIndicator indic = (CircleViewPagerIndicator) findViewById(R.id.vfiDic);
		viewPager.setViewPagerIndicator(indic);
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}
	
	@Override
	public void onViewTap(View view, float x, float y) {
		finish();
	}
	
	@Override
	public boolean onLongClick(View v) {
		PhotoView pv = (PhotoView) v;
		AtySavePictures.startAty(this, pv);
		return false;
	}

	
	public class ImageAdapter extends PagerAdapter {

		private List<String> urls;
		
		public ImageAdapter(List<String> urls) {
			this.urls = urls;
		}
		
		@Override
		public int getCount() {
			return urls.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			((LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_photoview, null);
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			GlobalRes.getInstance().displayBitmap(new GlobalRes.DisplayBitmapParam(urls.get(position), photoView));
			photoView.setOnViewTapListener(AtyPhotoBrowser.this);
			photoView.setOnLongClickListener(AtyPhotoBrowser.this);
			photoView.setLongClickable(true);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		public List<String> getUrls() {
			return urls;
		}

		public void setUrls(List<String> urls) {
			this.urls = urls;
		}

	}
	
	/************************************************************************************************/
	
	public static void startAty(Context context, int index, ArrayList<String> urls) {
		Intent i = new Intent(context, AtyPhotoBrowser.class);
		i.putExtra(INDEX, index);
		i.putStringArrayListExtra(URLS, urls);
		context.startActivity(i);
	}

}