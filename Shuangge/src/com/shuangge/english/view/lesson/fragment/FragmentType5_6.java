package com.shuangge.english.view.lesson.fragment;


import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType5;
import com.shuangge.english.entity.server.lesson.Type5Data;
import com.shuangge.english.view.component.RatingBarView;
import com.shuangge.english.view.lesson.AtyType5s;


/**
 * 重点
 * @author Jeffrey
 *
 */
public class FragmentType5_6 extends BaseType5 implements OnClickListener {
	
	private EntityResType5 data1, data2;
	
	public FragmentType5_6() {
		super();
		List<EntityResType5> type5s = GlobalRes.getInstance().getBeans().getCurrentType4Data().getType5s();
		data1 = type5s.get(0);
		data2 = type5s.get(1);
	}
	
	
	/***************************************************************************************************************************************/
	/**
	/**  initialization
	/**
	/***************************************************************************************************************************************/
	
	private View mainView;
	private ImageView imgModule1, imgModule2;
	private ImageView imgLine1;
	private ImageView imgLock1, imgLock2;
	private ImageView imgStart1, imgStart2;
	private RatingBarView rbStar1, rbStar2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mainView = inflater.inflate(R.layout.item_lesson_type5_6, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		imgLine1 = (ImageView) mainView.findViewById(R.id.imgLine1);
		imgLock1 = (ImageView) mainView.findViewById(R.id.imgLock1);
		imgLock2 = (ImageView) mainView.findViewById(R.id.imgLock2);
		imgStart1 = (ImageView) mainView.findViewById(R.id.imgStart1);
		imgStart2 = (ImageView) mainView.findViewById(R.id.imgStart2);
		rbStar1 = (RatingBarView) mainView.findViewById(R.id.rbStar1);
		rbStar2 = (RatingBarView) mainView.findViewById(R.id.rbStar2);
		imgModule1 = (ImageView) mainView.findViewById(R.id.imgModule1);
		imgModule1.setOnClickListener(this);
		imgModule2 = (ImageView) mainView.findViewById(R.id.imgModule2);
		imgModule2.setOnClickListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) mainView.getParent(); 
        if (p != null) { 
            p.removeAllViewsInLayout(); 
        }
		return mainView;
	}
	
	public void onRefreshView() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		Type5Data type5_1 = beans.getUnlockDatas().getType5s().get(data1.getId());
		Type5Data type5_2 = beans.getUnlockDatas().getType5s().get(data2.getId());
		refreshModule(type5_1, imgLock1, imgStart1, rbStar1, imgModule1, R.drawable.icon_lessontype5_1, R.drawable.icon_lessontype5_1_2, R.drawable.icon_lessontype5_1_3);
		refreshModule(type5_2, imgLock2, imgStart2, rbStar2, imgModule2, R.drawable.icon_lessontype5_2, R.drawable.icon_lessontype5_2_2, R.drawable.icon_lessontype5_2_3);
		refreshLine(type5_2, imgLine1);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgModule1:
			((AtyType5s) getActivity()).startLesson(data1.getId(), data1.getName());
			break;
		case R.id.imgModule2:
			((AtyType5s) getActivity()).startLesson(data2.getId(), data2.getName());
			break;
		}
	}
	
}