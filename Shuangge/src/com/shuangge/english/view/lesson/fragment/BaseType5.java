package com.shuangge.english.view.lesson.fragment;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.shuangge.english.entity.server.lesson.Type5Data;
import com.shuangge.english.view.component.RatingBarView;

public abstract class BaseType5 extends Fragment {

	public BaseType5() {
		super();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		onRefreshView();
	}
	
	public void refreshView() {
		if (isResumed()) {
			onRefreshView();
		}
	}
	
	public abstract void onRefreshView();
	
	protected void refreshModule(Type5Data type5, View lockView, View startView, RatingBarView starView, ImageView moduleView, int resId1, int resId2, int resId3) {
		if (null == type5) {
			lockView.setVisibility(View.VISIBLE);
			startView.setVisibility(View.GONE);
			starView.setVisibility(View.GONE);
			moduleView.setImageResource(resId3);
//			moduleView.setClickable(true);
			moduleView.setClickable(false);
		}
		//未做过 可以做
		else if (null == type5.getRightRate() || !type5.getIsFinished()) {
			lockView.setVisibility(View.GONE);
			startView.setVisibility(View.VISIBLE);
			starView.setVisibility(View.GONE);
			moduleView.setImageResource(resId1);
			moduleView.setClickable(true);
		}
		//已做过
		else {
			lockView.setVisibility(View.GONE);
			startView.setVisibility(View.GONE);
			starView.setVisibility(View.VISIBLE);
			moduleView.setImageResource(resId2);
			if (type5.getRightRate() < 60) {
				starView.setStar(1);
			}
			else if (type5.getRightRate() < 100) {
				starView.setStar(2);
			}
			else {
				starView.setStar(3);
			}
			moduleView.setClickable(true);
		}
	}
	
	protected void refreshLine(Type5Data type5, ImageView lineView) {
		if (null != lineView) {
			if (null == type5 || null == type5.getRightRate()) {
				lineView.setImageResource(R.drawable.bg_line1);
			}
			else {
				lineView.setImageResource(R.drawable.bg_line2);
			}
		}
	}
	
}
