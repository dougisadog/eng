package com.shuangge.english.view.component.wheel;


import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shuangge.english.view.AbstractAppActivity;

public class AtyCities extends BaseAtyCitys implements OnClickListener, OnWheelChangedListener {
	
	public static final String PARAMS_CURRENT_DATA = "current data";
	public static final String CALLBACK_PROVINCE = "callback province";
	public static final String CALLBACK_CITY = "callback city";
	public static final String CALLBACK_DISTRICT = "callback district";
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private FrameLayout flBg;
	
	private int level = 1;
	
	private boolean isFinished = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.component_wheel_cities);
		setUpViews();
		setUpListener();
		setUpData();
		
		flBg = (FrameLayout) findViewById(R.id.flBg1);
		flBg.setOnClickListener(this);
	}
	
	private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewProvince.setWheelBackground(R.drawable.wheel_bg_holo);
		mViewProvince.setWheelForeground(R.drawable.wheel_val_holo);
		mViewProvince.setShadowColor(0xefE9E9E9, 0xcfE9E9E9, 0x3fE9E9E9);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewCity.setWheelBackground(R.drawable.wheel_bg_holo);
		mViewCity.setWheelForeground(R.drawable.wheel_val_holo);
		mViewCity.setShadowColor(0xefE9E9E9, 0xcfE9E9E9, 0x3fE9E9E9);
		mViewCity.setVisibility(level > 0 ? View.VISIBLE : View.GONE);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		mViewDistrict.setWheelBackground(R.drawable.wheel_bg_holo);
		mViewDistrict.setWheelForeground(R.drawable.wheel_val_holo);
		mViewDistrict.setShadowColor(0xefE9E9E9, 0xcfE9E9E9, 0x3fE9E9E9);
		mViewDistrict.setVisibility(level > 1 ? View.VISIBLE : View.GONE);
	}
	
	private void setUpListener() {
    	// 添加change事件
    	mViewProvince.addChangingListener(this);
    	// 添加change事件
    	mViewCity.addChangingListener(this);
    	// 添加change事件
    	mViewDistrict.addChangingListener(this);
    	
    	mViewProvince.addClickingListener(oOnWheelClickedListener);
    	mViewCity.addClickingListener(oOnWheelClickedListener);
    	mViewDistrict.addClickingListener(oOnWheelClickedListener);
    }
	
	private OnWheelClickedListener oOnWheelClickedListener = new OnWheelClickedListener() {
		
		@Override
		public void onItemClicked(WheelView wheel, int itemIndex) {
//			wv.setCurrentItem(itemIndex);
			if (wheel.getCurrentItem() != itemIndex) {
				wheel.scroll(itemIndex - wheel.getCurrentItem(), 1000);
				return;
			}
			callback();
		}
	};
	
	private void setUpData() {
		defaultStr = getString(R.string.secrecy);
		initProvinceDatas();
		mViewProvince.setViewAdapter(new WeelAdapter(AtyCities.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (isFinished) {
			return;
		}
		//TODO: null指针异常 待修复
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new WeelAdapter(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new WeelAdapter(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private class WeelAdapter extends AbstractWheelTextAdapter {

		private String[] datas = null;
		
		/**
		 * Constructor
		 */
		protected WeelAdapter(Context context, String[] datas) {
			super(context, R.layout.item_wheel, NO_RESOURCE);
			this.datas = datas;
			setItemTextResource(R.id.txtWheelTitle);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return datas.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return datas[index];
		}
	}
	
	@Override
	public void onClick(View v) {
		callback();
	}
	
	private void callback() {
		isFinished = true;
		mViewProvince.removeChangingListener(this);
    	mViewCity.removeChangingListener(this);
    	mViewDistrict.removeChangingListener(this);
		Intent i = new Intent();
		if (!TextUtils.isEmpty(mCurrentProviceName)) {
			i.putExtra(CALLBACK_PROVINCE, mCurrentProviceName);
		}
		if (!TextUtils.isEmpty(mCurrentCityName) && !mCurrentCityName.equals(defaultStr) && !mCurrentCityName.equals(mCurrentProviceName)) {
			i.putExtra(CALLBACK_CITY, mCurrentCityName);
		}
		if (!TextUtils.isEmpty(mCurrentDistrictName) && !mCurrentDistrictName.equals(defaultStr) && !mCurrentDistrictName.equals(mCurrentCityName)) {
			i.putExtra(CALLBACK_DISTRICT, mCurrentDistrictName);
		}
		setResult(AbstractAppActivity.CODE_QUIT, i);
		this.finish();
	}
}
