package com.shuangge.english.view.login;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.OnViewFlowTouchedListener;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;


public class AtyGuide extends AbstractAppActivity implements OnClickListener, ViewSwitchListener, OnViewFlowTouchedListener {

	private static final int[] ids = { R.drawable.guide_page_1, R.drawable.guide_page_2, R.drawable.guide_page_3, R.drawable.guide_page_4};
	private ViewFlow viewFlow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_guide);
		
		viewFlow = (ViewFlow) findViewById(R.id.vf);
		viewFlow.setAdapter(new ImageAdapter(this));
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.vfiDic);
		viewFlow.setFlowIndicator(indic);
		viewFlow.setOnViewSwitchListener(this);
		viewFlow.setOnViewFlowTouchedListener(this);
	}


	public class ImageAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
	
		public ImageAdapter(Context context) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	
		@Override
		public int getCount() {
			return ids.length;
		}
	
		@Override
		public Object getItem(int position) {
			return position;
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.item_guide, null);
			}
			ImageView img = (ImageView) convertView.findViewById(R.id.imgView);
			img.setImageResource(ids[position]);
			ImageButton btn = (ImageButton) convertView.findViewById(R.id.btnStart);
			if (position == ids.length - 1) {
				ViewUtils.setRelativeMargins(btn, RelativeLayout.LayoutParams.WRAP_CONTENT, 
						RelativeLayout.LayoutParams.WRAP_CONTENT, 0, AppInfo.getScreenWidth() * 30 / 56, 0, 0);
//				btn.setVisibility(View.VISIBLE);
				btn.setOnClickListener(AtyGuide.this);
				convertView.setOnClickListener(AtyGuide.this);
			} 
			else {
//				btn.setVisibility(View.GONE);
			}
			return convertView;
		}

	}
	
	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, AtyLogin.class));
		this.finish();
	}

	private float prevX;
	private boolean goNext = false;

	@Override
	public void onSwitched(View view, int position) {
		goNext = position == ids.length - 1;
	}

	@Override
	public void onMouseDown(MotionEvent e) {
		prevX = e.getX();
	}

	@Override
	public void onMouseMove(MotionEvent e) {
	}

	@Override
	public void onMouseUp(MotionEvent e) {
		if (!goNext) {
			return;
		}
		if (prevX - e.getX() > (AppInfo.getScreenWidth() / 4)) {
			startActivity(new Intent(this, AtyLogin.class));
			this.finish();
		}
	}


}
