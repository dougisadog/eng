package com.shuangge.english.view.read.fragment;

import java.util.List;

import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.lesson.component.OptionVideo;
import com.shuangge.english.view.read.adapter.AdapterCoreWords;
import com.shuangge.english.view.read.component.MutiVideoOption;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 核心单词  
 * @author tovey
 *
 */
public class FragmentVideoTest extends Fragment implements OnClickListener {
	
	public FragmentVideoTest (List<Answer> answers) {
		if (null != answers && answers.size() > 4) {
			answers = answers.subList(0, 4);
		}
		this.answers = answers;
	}
	
	public FragmentVideoTest (List<Answer> answers, CallBackSelection callBackSelection) {
		if (answers.size() > 4) {
			answers = answers.subList(0, 4);
		}
		this.answers = answers;
		this.callBackSelection = callBackSelection;
	}
	
	private OptionVideo ov;
	
	private LinearLayout mainView, videoContainer, selectOptionsContainer;
	
	private GridView gv;
	
	private List<Answer> answers;
	
	public static enum OPTION_TYPE {
		TEXT, IMAGE, MUTI_VIDEO;
	}
	
	private CallBackSelection callBackSelection;
	
	public interface CallBackSelection {
		void rightSelected(Answer answer);
		
		void wrongSelected(Answer answer);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = (LinearLayout) inflater.inflate(R.layout.aty_video_fragment_test, container, false);
		videoContainer = (LinearLayout) mainView.findViewById(R.id.videoContainer);
		selectOptionsContainer = (LinearLayout) mainView.findViewById(R.id.selectOptionsContainer);
		
		TextView title = (TextView) mainView.findViewById(R.id.title);
		if (null != answers) {
			title.setText(answers.get(0).getName());
			title.setTextColor(Color.YELLOW);
			for (int i = 0; i < answers.size(); i++) {
				addOption(answers.get(i));
			}
		}
		return mainView;
	}
	
	private void bindDatas() {
		addVideo(AppInfo.getScreenWidth());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			break;
		default:
			break;
		}
		
	}
	
	private void addVideo(int w) {
		//TODO:暂时播放一个视频
		ov = new OptionVideo(getActivity(), "url", w);
		videoContainer.addView(ov);
		ViewUtils.setLinearMargins(ov, LayoutParams.MATCH_PARENT, w, 0, 0, 0, 0);
	}
	
	private void addOption(Answer answer) {
		OPTION_TYPE ot = answer.getOt();
		switch (ot) {
		case TEXT:
			TextView tv = new TextView(getActivity());
			tv.setText("hello");
			tv.setTextSize(20);
			 tv.setTextColor(0xFFFFFFFF);
			 tv.setGravity(Gravity.CENTER);
			 tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
			 tv.setPadding(10, 5, 10, 5);
			 
			 tv.setTag(answer);
			 tv.setOnClickListener(onClickListener);
			selectOptionsContainer.addView(tv);
			break;
		case IMAGE:
			ImageView iv = new ImageView(getActivity());
			iv.setBackgroundResource(R.drawable.ic_launcher);
			 iv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
			 iv.setPadding(10, 5, 10, 5);
			 iv.setTag(answer);
			 iv.setOnClickListener(onClickListener);
			selectOptionsContainer.addView(iv);
			break;
		case MUTI_VIDEO:
			MutiVideoOption mvo = new MutiVideoOption(getActivity(), answer);
			mvo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
			mvo.setPadding(10, 5, 10, 5);
			mvo.setTag(answer);
			mvo.setOnClickListener(onClickListener);
			selectOptionsContainer.addView(mvo);
	
	break;

		default:
			break;
		}
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Answer answer = (Answer) v.getTag();
			if (null == answer || !answer.isRight()) {
				callBackSelection.wrongSelected(answer);
			}
			else {
				callBackSelection.rightSelected(answer);
			}
			
		}
	};

}
