package com.shuangge.english.view.component.wheel;

import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shuangge.english.view.AbstractAppActivity;


public class AtyWheel extends Activity implements OnClickListener {

	public static final String PARAMS_DATAS = "datas";
	public static final String PARAMS_CURRENT_DATA = "current data";
	public static final String CALLBACK_DATAS = "callback datas";
	
	private FrameLayout flBg;
	private WheelView wv; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.component_wheel);

		wv = (WheelView) findViewById(R.id.wv1);
//		wv.setDrawShadows(false);
		wv.setShadowColor(0xefE9E9E9, 0xcfE9E9E9, 0x3fE9E9E9);
		wv.setVisibleItems(7);
		wv.setWheelBackground(R.drawable.wheel_bg_holo);
		wv.setWheelForeground(R.drawable.wheel_val_holo);
		wv.setViewAdapter(new WeelAdapter(this, getIntent().getStringArrayExtra(PARAMS_DATAS)));
		wv.setCurrentItem(getIntent().getIntExtra(PARAMS_CURRENT_DATA, 0));
		wv.addClickingListener(new OnWheelClickedListener() {
			
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
//				wv.setCurrentItem(itemIndex);
				if (wheel.getCurrentItem() != itemIndex) {
					wv.scroll(itemIndex - wheel.getCurrentItem(), 1000);
					return;
				}
				callback();
			}
		});
		
		flBg = (FrameLayout) findViewById(R.id.flBg1);
		flBg.setOnClickListener(this);
		
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
		Intent i = new Intent();
		i.putExtra(CALLBACK_DATAS, wv.getCurrentItem());
		setResult(AbstractAppActivity.CODE_QUIT, i);
		this.finish();
	}

}
