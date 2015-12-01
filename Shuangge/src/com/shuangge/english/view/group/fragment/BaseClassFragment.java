package com.shuangge.english.view.group.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shuangge.english.view.AbstractAppActivity;

public class BaseClassFragment extends Fragment {

	protected View mainView;
	private boolean initizated = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public void initDatas() {
		if (!initizated) {
			onInitDatas();
		}
		initizated = true;
	}
	
	protected void onInitDatas() {
	}
	
	
	protected void showLoading() {
		if (null != getActivity()) {
			((AbstractAppActivity) getActivity()).showLoading();
		}
	}
	
	protected void hideLoading() {
		if (null != getActivity()) {
			((AbstractAppActivity) getActivity()).hideLoading();
		}
	}
	
	public boolean onBack() {
		return false;
	}
	
	public boolean onFinish() {
		return false;
	}
	
}
