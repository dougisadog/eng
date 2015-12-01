package com.shuangge.english.view.read.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;

public class ReadContentText extends LinearLayout {

	private TextView txt;
	private ImageView img;
	
	public ReadContentText(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		int dp10 = DensityUtils.dip2px(getContext(), 15);
		
		img = new ImageView(getContext());
		addView(img);
		LinearLayout.LayoutParams lp = ViewUtils.setLinearMargins(this, 
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0);
		img.setLayoutParams(lp);
		img.setScaleType(ScaleType.CENTER_CROP);
		img.setVisibility(View.GONE);
		
		txt = new TextView(context);
		txt.setBackgroundResource(R.drawable.bg_read_content);
		txt.setBackgroundColor(Color.WHITE);
		txt.setPadding(dp10, dp10, dp10, dp10);
		addView(txt);
		lp = ViewUtils.setLinearMargins(this, 
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0, 0, 0, 0);
		txt.setLayoutParams(lp);
		
	}
	
	public void setText(String text) {
		txt.setText(text);
	}
	
	public void setText(CharSequence text) {
		txt.setText(text);
	}
	
	public void setUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		img.setVisibility(View.VISIBLE);
		GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(url, img));
	}

	public TextView getTxt() {
		return txt;
	}
	
}
