package com.shuangge.english.view.component;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SexAge extends FrameLayout {
	
	public final static int SEX_NULL = 0;
	public final static int SEX_FEMALE = 2;
	public final static int SEX_MALE = 1;

	private TextView txtFemaleAge, txtMaleAge, txtAge;
	private RelativeLayout rlFemale, rlMale, rlNull;

	@SuppressLint("Recycle")
	public SexAge(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_sex_age, this);
        
        txtFemaleAge = (TextView) findViewById(R.id.txtFemaleAge);
        txtMaleAge = (TextView) findViewById(R.id.txtMaleAge);
        txtAge = (TextView) findViewById(R.id.txtAge);
        rlFemale = (RelativeLayout) findViewById(R.id.rlFemale);
        rlMale = (RelativeLayout) findViewById(R.id.rlMale);
        rlNull = (RelativeLayout) findViewById(R.id.rlNull);
	}
	
	public void setSexAndAge(Integer sex, Integer age) {
		rlFemale.setVisibility(View.GONE);
		rlMale.setVisibility(View.GONE);
		rlNull.setVisibility(View.GONE);
		if ((null == sex || sex == 0) && null == age) {
			return;
		}
		TextView txt = null;
		if (null != sex && sex > 0 && sex < 3) {
			switch (sex) {
			case SEX_FEMALE:
				rlFemale.setVisibility(View.VISIBLE);
				txt = txtFemaleAge;
				break;
			case SEX_MALE:
				rlMale.setVisibility(View.VISIBLE);
				txt = txtMaleAge;
				break;
			}
		} else {
			rlNull.setVisibility(View.VISIBLE);
			txt = txtAge;
		}
		txt.setVisibility(View.GONE);
		if (null != age) {
			txt.setText(age + "");
			txt.setVisibility(View.VISIBLE);
		}
		else {
			txt.setText("");
		}
	}
	
}
