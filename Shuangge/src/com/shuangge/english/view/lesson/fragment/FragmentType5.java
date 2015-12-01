package com.shuangge.english.view.lesson.fragment;


import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType5;
import com.shuangge.english.entity.server.lesson.Type5Data;
import com.shuangge.english.view.component.RatingBarView;
import com.shuangge.english.view.lesson.AtyType5s;

/**
 * 
 * @author Jeffrey
 *
 */
public class FragmentType5 extends BaseType5 implements OnClickListener {
	
	private int pageNo;
	private boolean isLast;
	private String pageResId;
	private List<EntityResType5> datas;
	
	public FragmentType5(int pageNo, boolean isLast, String pageResId, List<EntityResType5> datas) {
		this.pageNo = pageNo;
		this.isLast = isLast;
		this.pageResId = pageResId;
		this.datas = datas;
	}
	
	
	/***************************************************************************************************************************************/
	/**
	/**  initialization
	/**
	/***************************************************************************************************************************************/
	
	private View mainView;
	private ImageView imgLessonTypeBg, imgLessonType;
	private LinearLayout llModule1, llModule2, llModule3;
	private ImageView imgModule1, imgModule2, imgModule3;
	private ImageView imgLine1, imgLine2, imgLine3, imgLine4;
	private ImageView imgLock1, imgLock2, imgLock3;
	private ImageView imgStart1, imgStart2, imgStart3;
	private RatingBarView rbStar1, rbStar2, rbStar3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mainView = inflater.inflate(R.layout.item_lesson_type5, (ViewGroup) getActivity().findViewById(R.id.vp), false);
		llModule1 = (LinearLayout) mainView.findViewById(R.id.llModule1);
		llModule2 = (LinearLayout) mainView.findViewById(R.id.llModule2);
		llModule3 = (LinearLayout) mainView.findViewById(R.id.llModule3);
		imgLine1 = (ImageView) mainView.findViewById(R.id.imgLine1);
		imgLine2 = (ImageView) mainView.findViewById(R.id.imgLine2);
		imgLine3 = (ImageView) mainView.findViewById(R.id.imgLine3);
		imgLine4 = (ImageView) mainView.findViewById(R.id.imgLine4);
		imgLock1 = (ImageView) mainView.findViewById(R.id.imgLock1);
		imgLock2 = (ImageView) mainView.findViewById(R.id.imgLock2);
		imgLock3 = (ImageView) mainView.findViewById(R.id.imgLock3);
		imgStart1 = (ImageView) mainView.findViewById(R.id.imgStart1);
		imgStart2 = (ImageView) mainView.findViewById(R.id.imgStart2);
		imgStart3 = (ImageView) mainView.findViewById(R.id.imgStart3);
		rbStar1 = (RatingBarView) mainView.findViewById(R.id.rbStar1);
		rbStar2 = (RatingBarView) mainView.findViewById(R.id.rbStar2);
		rbStar3 = (RatingBarView) mainView.findViewById(R.id.rbStar3);
		imgModule1 = (ImageView) mainView.findViewById(R.id.imgModule1);
		imgModule2 = (ImageView) mainView.findViewById(R.id.imgModule2);
		imgModule3 = (ImageView) mainView.findViewById(R.id.imgModule3);
		imgModule1.setOnClickListener(this);
		imgModule2.setOnClickListener(this);
		imgModule3.setOnClickListener(this);

		if (pageNo > 9)
			pageNo = 9;
		if (pageNo < 0)
			pageNo = 0;
		imgLessonTypeBg = (ImageView) mainView.findViewById(R.id.imgLessonTypeBg);
		imgLessonType = (ImageView) mainView.findViewById(R.id.imgLessonType);
		int resId = getResources().getIdentifier("icon_type5_bg" + (pageNo + 1), "drawable", "air.com.shuangge.phone.ShuangEnglish");
		imgLessonTypeBg.setImageResource(resId);
		resId = getResources().getIdentifier("icon_type5_" + pageResId, "drawable", "air.com.shuangge.phone.ShuangEnglish");
		imgLessonType.setImageResource(resId);
		
		llModule1.setVisibility(View.GONE);
		llModule2.setVisibility(View.GONE);
		llModule3.setVisibility(View.GONE);
		
		imgLine1.setVisibility(View.INVISIBLE);
		imgLine2.setVisibility(View.GONE);
		imgLine3.setVisibility(View.GONE);
		imgLine4.setVisibility(View.INVISIBLE);
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
		EntityResType5 data = null;
		EntityResType5 prevPageData = null;
		EntityResType5 nextPageData = null;
		Type5Data type5_1 = null;
		Type5Data type5_5 = null;
		
		int start = 0;
		int end = datas.size();
		if (pageNo != 0 || !isLast) {
			//不是最后一页 都显示最后一条线
			if (pageNo == 0 || (pageNo != 0 && !isLast)) {
				end--;
				nextPageData = datas.get(datas.size() - 1);
				type5_5 = beans.getUnlockDatas().getType5s().get(nextPageData.getId());
				imgLine4.setVisibility(View.VISIBLE);
				refreshLine(type5_5, imgLine4);
			}
			//不是第一页 都显示第一条线
			if (isLast || (pageNo != 0 && !isLast)) {
				start++;
				prevPageData = datas.get(0);
				type5_1 = beans.getUnlockDatas().getType5s().get(prevPageData.getId());
				imgLine1.setVisibility(View.VISIBLE);
				refreshLine(type5_1, imgLine1);
			}
		}
		
		List<EntityResType5> contents = datas.subList(start, end);
		
		Type5Data type5_2 = null;
		Type5Data type5_3 = null;
		Type5Data type5_4 = null;
		
		if (contents.size() > 0) {
			data = contents.get(0);
			type5_2 = beans.getUnlockDatas().getType5s().get(data.getId());
			int[] resIds = getResId(data.getResId());
			refreshModule(type5_2, imgLock1, imgStart1, rbStar1, imgModule1, resIds[0], resIds[1], resIds[2]);
			llModule1.setVisibility(View.VISIBLE);
			imgModule1.setTag(data);
		}
		if (contents.size() > 1) {
			refreshLine(type5_2, imgLine2);
			data = contents.get(1);
			type5_3 = beans.getUnlockDatas().getType5s().get(data.getId());
			int[] resIds = getResId(data.getResId());
			refreshModule(type5_3, imgLock2, imgStart2, rbStar2, imgModule2, resIds[0], resIds[1], resIds[2]);
			llModule2.setVisibility(View.VISIBLE);
			imgModule2.setTag(data);
			imgLine2.setVisibility(View.VISIBLE);
		}
		if (contents.size() > 2) {
			refreshLine(type5_3, imgLine3);
			data = contents.get(2);
			type5_4 = beans.getUnlockDatas().getType5s().get(data.getId());
			int[] resIds = getResId(data.getResId());
			refreshModule(type5_4, imgLock3, imgStart3, rbStar3, imgModule3, resIds[0], resIds[1], resIds[2]);
			llModule3.setVisibility(View.VISIBLE);
			imgModule3.setTag(data);
			imgLine3.setVisibility(View.VISIBLE);
		}
		
	}
	
	private int[] getResId(String resId) {
		resId = resId.replace("\r", "");
		int resId1 = getResources().getIdentifier("icon_lessontype5_".concat(resId), "drawable", "air.com.shuangge.phone.ShuangEnglish");
		int resId2 = getResources().getIdentifier("icon_lessontype5_".concat(resId).concat("_2"), "drawable", "air.com.shuangge.phone.ShuangEnglish");
		int resId3 = getResources().getIdentifier("icon_lessontype5_".concat(resId).concat("_3"), "drawable", "air.com.shuangge.phone.ShuangEnglish");
		if (resId1 == 0)
			resId1 = R.drawable.icon_lessontype5_3;
		if (resId2 == 0)
			resId2 = R.drawable.icon_lessontype5_3_2;
		if (resId3 == 0)
			resId3 = R.drawable.icon_lessontype5_3_3;
		return new int[] {resId1, resId2, resId3};
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		EntityResType5 data = (EntityResType5) v.getTag();
		if (null != data)
			((AtyType5s) getActivity()).startLesson(data.getId(), data.getName());
	}
	
}
