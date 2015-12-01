package com.shuangge.english.view.component.wheel;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class WeelAdapter extends AbstractWheelTextAdapter {

	private String[] datas = null;
	
	
	/**
	 * Constructor
	 */
	public WeelAdapter(Context context, String[] datas) {
		super(context, R.layout.item_wheel, NO_RESOURCE);
		this.datas = datas;
		setItemTextResource(R.id.txtWheelTitle);
	}
	
	public WeelAdapter(int resId, Context context, String[] datas) {
		super(context, resId, NO_RESOURCE);
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
