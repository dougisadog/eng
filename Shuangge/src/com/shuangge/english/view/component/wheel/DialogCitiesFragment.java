package com.shuangge.english.view.component.wheel;


import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DialogCitiesFragment extends BaseDialogCitys implements OnClickListener, OnWheelChangedListener {
	
	public static interface CallBackDialogCitiesWheel {
		
		public void cancel();
		public void submit(String data);
		
	}
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView txtDialogTitle, txtSubmit, txtCancel;
	private CallBackDialogCitiesWheel callback;
	
	private int level = 1;
	private String title;
	private String currentData;
	private boolean isFinished = false;
	
	public DialogCitiesFragment(CallBackDialogCitiesWheel callback, String title, String currentData) {
		super();
		this.callback = callback;
		this.title = title;
		this.currentData = currentData;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
    	setStyle(style, theme);
	}
	
	public DialogCitiesFragment(CallBackDialogCitiesWheel callback, String title, String currentData, boolean notSecrecy) {
		super();
		this.callback = callback;
		this.title = title;
		this.currentData = currentData;
		this.notSecrecy = notSecrecy;
		setCancelable(true);
		int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
		setStyle(style, theme);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_wheel_cities, null);
		setUpViews(v);
		setUpListener();
		setUpData();
		return v;
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	private void setUpViews(View v) {
		mViewProvince = (WheelView) v.findViewById(R.id.id_province);
		mViewProvince.setWheelBackground(R.drawable.wheel_bg_holo);
		mViewProvince.setWheelForeground(R.drawable.wheel_val_holo);
		mViewProvince.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
		mViewCity = (WheelView) v.findViewById(R.id.id_city);
		mViewCity.setWheelBackground(R.drawable.wheel_bg_holo);
		mViewCity.setWheelForeground(R.drawable.wheel_val_holo);
		mViewCity.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
		mViewCity.setVisibility(level > 0 ? View.VISIBLE : View.GONE);
		mViewDistrict = (WheelView) v.findViewById(R.id.id_district);
		mViewDistrict.setWheelBackground(R.drawable.wheel_bg_holo);
		mViewDistrict.setWheelForeground(R.drawable.wheel_val_holo);
		mViewDistrict.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
		mViewDistrict.setVisibility(level > 1 ? View.VISIBLE : View.GONE);
		
		txtDialogTitle = (TextView) v.findViewById(R.id.txtDialogTitle);
		txtDialogTitle.setText(title);
		txtSubmit = (TextView) v.findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		txtCancel = (TextView) v.findViewById(R.id.txtCancel);
		txtCancel.setOnClickListener(this);
	}
	
	private void setUpListener() {
    	mViewProvince.addChangingListener(this);
    	mViewCity.addChangingListener(this);
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
			isFinished = true;
			callback.submit(getVal());
		}
	};
	
	private void setUpData() {
		defaultStr = getString(R.string.secrecy);
		initProvinceDatas();
		mViewProvince.setViewAdapter(new WeelAdapter(getActivity(), mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		
		String[] strs = currentData.split(" ");
		String str = strs[0];
		int i = mProvinceDatas.length - 1;
		for (; i > 0; --i) {
			if (mProvinceDatas[i].equals(str))
				break;
		}
		mViewProvince.setCurrentItem(i);
		
		int pCurrent = 0;
		if (level > 1) {
			pCurrent = mViewProvince.getCurrentItem();
			mCurrentProviceName = mProvinceDatas[pCurrent];
			String[] cities = mCitisDatasMap.get(mCurrentProviceName);
			if (cities == null) {
				cities = new String[] { "" };
			}
			if (strs.length > 1) {
				str = strs[1];
			}
			for (i = cities.length - 1; i > 0; --i) {
				if (cities[i].equals(str))
					break;
			}
			mViewCity.setViewAdapter(new WeelAdapter(getActivity(), cities));
			mViewCity.setCurrentItem(i);
		}
		
		if (level > 2) {
			mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
			String[] areas = mDistrictDatasMap.get(mCurrentCityName);
			if (areas == null) {
				areas = new String[] { "" };
			}
			if (areas.length > 2) {
				str = strs[2];
			}
			for (i = areas.length - 1; i > 0; --i) {
				if (areas[i].equals(str))
					break;
			}
			mViewDistrict.setViewAdapter(new WeelAdapter(getActivity(), areas));
			mViewDistrict.setCurrentItem(i);
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (isFinished) {
			return;
		}
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
		mViewDistrict.setViewAdapter(new WeelAdapter(getActivity(), areas));
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
		mViewCity.setViewAdapter(new WeelAdapter(getActivity(), cities));
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
		switch (v.getId()) {
		case R.id.txtSubmit:
			isFinished = true;
			callback.submit(getVal());
			break;
		case R.id.txtCancel:
			callback.cancel();
			break;
		}
	}
	
	private String getVal() {
		String str = "";
		if (!TextUtils.isEmpty(mCurrentProviceName)) {
			str = mCurrentProviceName;
		}
		if (!TextUtils.isEmpty(mCurrentCityName) && !mCurrentCityName.equals(defaultStr) && !mCurrentCityName.equals(mCurrentProviceName)) {
			if (!TextUtils.isEmpty(str)) {
				str += " ";
			}
			str += mCurrentCityName;
		}
		if (!TextUtils.isEmpty(mCurrentDistrictName) && !mCurrentDistrictName.equals(defaultStr) && !mCurrentDistrictName.equals(mCurrentCityName)) {
			if (!TextUtils.isEmpty(str)) {
				str += " ";
			}
			str += mCurrentDistrictName;
		}
		return str;
	}
	
}
