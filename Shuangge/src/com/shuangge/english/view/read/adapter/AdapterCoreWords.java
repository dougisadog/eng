package com.shuangge.english.view.read.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.support.debug.DebugPrinter;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 核心单词 适配器
 * @author tovey
 *
 */
//public class AdapterCoreWords extends ArrayAdapter<ReadWordData> {
public class AdapterCoreWords extends ArrayAdapter {

	private static final String TAG = "wordItem";
	private LayoutInflater mInflater;

	 private List<ReadWordData> datas = new ArrayList<ReadWordData>();

	public AdapterCoreWords(Context context) {
		super(context, 0);
//		datas = getDataFromMapArrayList();
		this.mInflater = LayoutInflater.from(context);
	}

	public AdapterCoreWords(Context context, int resource) {
		super(context, resource);
//		datas = getDataFromMapArrayList();
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	/*
	 * public ReadWordData getItem(int position){ return datas.get(position); }
	 */
	public ReadWordData getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WordListViewHolder holder = null;
//		List<ReadWordData> dataFromMapArrayList = getDataFromMapArrayList();
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_read_core_words, null, true);
			holder = new WordListViewHolder();
			holder.txtWordItem = (TextView) convertView.findViewById(R.id.txtWordItem);
			holder.txtWordTranslation = (TextView) convertView.findViewById(R.id.txtWordTranslation);
			convertView.setTag(holder);
		} else {
			holder = (WordListViewHolder) convertView.getTag();
		}
		// ReadWordData readWordData = datas.get(position);

		//ArrayList<String> data = dataFromMapArrayList.get(position);

		if (null == holder) {
			DebugPrinter.e(datas.size() + "-----" + position);
		}
		if (!TextUtils.isEmpty(datas.get(position).getWord())) {
			holder.txtWordItem.setText(datas.get(position).getWord());
		}
		if (!TextUtils.isEmpty(datas.get(position).getTranslation())) {
			holder.txtWordTranslation.setText(datas.get(position).getTranslation());
		}
		return convertView;
	}

	/*
	 * public List<ReadWordData> getDatas(){ return datas; }
	 */

	public final class WordListViewHolder {
		TextView txtWordItem;
		TextView txtWordTranslation;
	}

	
	private static List<ReadWordData> getDataFromMapArrayList() {
		List<ReadWordData> list = new ArrayList<ReadWordData>();
		for (int i = 0; i < 15; i++) {
			ReadWordData und = new ReadWordData();
			und.setReadNo((long) i);
			und.setTranslation("word" + i);
			list.add(und);
		}
		return list;
	}
	
	public List<ReadWordData> getDatas() {
		return datas;
	}
}
