package com.shuangge.english.view.component;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.support.utils.ViewUtils;

public class ComponentMultiSwitch extends FrameLayout implements OnClickListener {

	private int selectedColor, unSelectedColor, selectedSize, unSelectedSize, multiSwitchMargin;
	private List<TextView> txts;
	private View selectedImg;
	private ComponentScrollView svMultiSwitch;
	private boolean initizated = false;
	
	private TextView currentTxt;
	private int currentIndex = 0;
	private int btnW;
	
	private onSelectedListener onSelectedListener;
	
	public interface onSelectedListener {
		
		public void onSelected(View v, int index);
		
	}
	
	
	public ComponentMultiSwitch(Context context) {
		super(context);
	}

	public ComponentMultiSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_multi_switch, this);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiSwitch);
		int strsId = a.getResourceId(R.styleable.MultiSwitch_multiSwitchOptions, -1);
		int bgId = a.getResourceId(R.styleable.MultiSwitch_multiSwitchBg, -1);
		int selectedImgId = a.getResourceId(R.styleable.MultiSwitch_multiSwitchSelected, -1);
		multiSwitchMargin = (int) a.getDimension(R.styleable.MultiSwitch_multiSwitchMargin, 0);
		selectedColor = a.getColor(R.styleable.MultiSwitch_multiSwitchSelectedColor, 0xff000000);
		unSelectedColor = a.getColor(R.styleable.MultiSwitch_multiSwitchUnSelectedColor, 0xff000000);
		selectedSize = a.getInt(R.styleable.MultiSwitch_multiSwitchSelectedSize, 14);
		unSelectedSize = a.getInt(R.styleable.MultiSwitch_multiSwitchUnSelectedSize, 14);
		a.recycle();
		if (strsId == -1) {
			return;
		}
		
		if (bgId != -1) {
			findViewById(R.id.multiSwitchBg).setBackgroundResource(bgId);
		}
		
		selectedImg = findViewById(R.id.imgMultiSwitch);
		if (selectedImgId != -1) {
			selectedImg.setBackgroundResource(selectedImgId);
		}
		
		svMultiSwitch = (ComponentScrollView) findViewById(R.id.svMultiSwitch);
		
		LinearLayout llMultiSwitch = (LinearLayout) findViewById(R.id.llMultiSwitch);
		txts = new ArrayList<TextView>();
		TextView txt = null;
		String[] strs = context.getResources().getStringArray(strsId);
		for (int i = 0 ; i < strs.length; ++i) {
			txt = new TextView(context);
			if (i == 0) {
				currentTxt = txt;
				txt.setTextColor(selectedColor);
				txt.setTextSize(selectedSize);
				txt.getPaint().setFakeBoldText(true);
			}
			else {
				txt.setTextColor(unSelectedColor);
				txt.setTextSize(unSelectedSize);
				txt.getPaint().setFakeBoldText(false);
			}
			txt.setText(strs[i]);
			txt.setGravity(Gravity.CENTER);
			txt.setTag(i);
			txt.setOnClickListener(this);
			llMultiSwitch.addView(txt);
			txts.add(txt);
			android.widget.LinearLayout.LayoutParams p = ViewUtils.setLinearMargins(txt, 0, LayoutParams.MATCH_PARENT, 0, 0, 0, 0);
			p.weight = 1;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!initizated) {
			btnW = getMeasuredWidth() / txts.size() - multiSwitchMargin;
			ViewUtils.setLinearMargins(selectedImg, btnW, LayoutParams.MATCH_PARENT, 
					multiSwitchMargin, multiSwitchMargin, multiSwitchMargin, multiSwitchMargin);
			initizated = true;
			if (currentIndex != 0) {
				setSelected(txts.get(currentIndex), false);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int index = setSelected((TextView) v, true);
		if (null != onSelectedListener) {
			onSelectedListener.onSelected(this, index);
		}
	}
	
	public int getVal() {
		int i = 0;
		for (TextView txt : txts) {
			if (txt.equals(currentTxt)) {
				return i; 
			}
			++i;
		}
		return 0;
	}
	
	public void setVal(int index) {
		if (index >= txts.size()) {
			return;
		}
		currentIndex = index;
		setSelected(txts.get(currentIndex), false);
	}
	
	private int setSelected(TextView txt, boolean scroll) {
		int index = (Integer) txt.getTag();
		currentTxt.setTextColor(unSelectedColor);
		currentTxt.setTextSize(unSelectedSize);
		currentTxt.getPaint().setFakeBoldText(false);
		currentTxt = (TextView) txt;
		currentTxt.setTextColor(selectedColor);
		currentTxt.setTextSize(selectedSize);
		currentTxt.getPaint().setFakeBoldText(true);
		
		if (scroll) {
			svMultiSwitch.smoothScrollTo(index * -btnW, 0);
		}
		else {
			svMultiSwitch.setTo(index * -btnW, 0);
		}
		return index;
	}

	public void setOnSelectedListener(onSelectedListener onSelectedListener) {
		this.onSelectedListener = onSelectedListener;
	}

}
