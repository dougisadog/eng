package com.shuangge.english.view.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ComponentLevel extends FrameLayout {
	
	private TextView txtLv;

	public ComponentLevel(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_lv, this);
        
        txtLv = (TextView) findViewById(R.id.txtLv);
	}
	
	public void setLevel(int level) {
		txtLv.setText("LVL " + level);
	}
	
}
