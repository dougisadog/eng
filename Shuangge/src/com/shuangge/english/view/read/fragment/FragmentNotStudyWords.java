package com.shuangge.english.view.read.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.entity.server.read.UserNewWordsData;
import com.shuangge.english.view.read.adapter.AdapterWordList;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentNotStudyWords extends BaseWordListFragment {
	public FragmentNotStudyWords(){
		super();
	}
	
	private AdapterWordList adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_word_list, null); 
		ListView lvWordsList = (ListView) view.findViewById(R.id.lvWordsList);
		
		adapter = new AdapterWordList(getActivity());
		List<UserNewWordsData> freshWords = GlobalRes.getInstance().getBeans().getReadNewWordsResult().getDatas();
		adapter.getData().clear();
		adapter.getData().addAll(freshWords);
		lvWordsList.setAdapter(adapter);
		
		return view;
	}


	public AdapterWordList getAdapter() {
		return adapter;
	}
	
	//切换fragment 加载数据
	@Override
	protected void onInitDatas() {
		// TODO Auto-generated method stub
		super.onInitDatas();
	}
}
