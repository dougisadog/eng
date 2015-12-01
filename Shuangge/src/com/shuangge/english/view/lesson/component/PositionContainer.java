package com.shuangge.english.view.lesson.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.ViewUtils;

public class PositionContainer extends LinearLayout {
	
	private PositionChanger positinChanger;
	
	public PositionChanger getPositionChanger() {
		return positinChanger;
	}

	public void setPositionChanger(PositionChanger positionChanger) {
		this.positinChanger = positionChanger;
	}

	public interface PositionChanger {
		void refreshPositionView (int position);
	}

	public PositionContainer(Context context) {
		super(context);
		setOrientation(HORIZONTAL);
	}
	
	public PositionContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(HORIZONTAL);
    }
	
	private int defaultId = getContext().getResources().getIdentifier("icon_pos_default", "drawable", "air.com.shuangge.phone.ShuangEnglish");
	private int currentPos = 0;
	
	public void setSize(int size) {
		if (size == 0)
			return;
		if (size > 10)
			size = 10;
		removeAllViews();
		int l = DensityUtils.px2dip(getContext(), 10);
		int id; 
		ImageView img = null;
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setCurrentPos((int) v.getTag());
				if (null != positinChanger) {
					positinChanger.refreshPositionView((int) v.getTag());
				}
			}
		};
		for (int i = 0; i < size; i++) {
			img = new ImageView(getContext());
			if (i == 0)
				id = getContext().getResources().getIdentifier("icon_pos1", "drawable", "air.com.shuangge.phone.ShuangEnglish");
			else 
				id = defaultId;
			img.setImageResource(id);
			img.setTag(i);
			ViewUtils.setLinearMargins(img, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, i == 0 ? 0 : l, 0, 0, 0);
			img.setOnClickListener(onClickListener);
			addView(img);
		}
	}
	
	public void setCurrentPos(int pos) {
		try {
			ImageView img = (ImageView) getChildAt(currentPos);
			img.setImageResource(defaultId);
			currentPos = pos;
			img = (ImageView) getChildAt(pos);
			int id = getContext().getResources().getIdentifier("icon_pos" + (pos + 1), "drawable", "air.com.shuangge.phone.ShuangEnglish");
			img.setImageResource(id);
		} catch (Exception e) {}
	}

}
