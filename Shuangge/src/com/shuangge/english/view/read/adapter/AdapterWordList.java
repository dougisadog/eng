package com.shuangge.english.view.read.adapter;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuangge.english.entity.server.read.UserNewWordsData;
import com.shuangge.english.support.debug.DebugPrinter;

/**
 * 单词列表 适配器
 * @author doug
 *
 */
@SuppressWarnings("rawtypes")
public class AdapterWordList extends ArrayAdapter {
//public class AdapterWordList extends ArrayAdapter {

	private LayoutInflater mInflater;
	private List<UserNewWordsData> datas = new ArrayList<UserNewWordsData>();
	//private List<ReadWordData> datas = new ArrayList<ReadWordData>();
	
	
	public AdapterWordList(Context context){
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterWordList(Context context, int resource){
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}
	
	public UserNewWordsData getItem(int position){
		return datas.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WordListViewHolder holder = null;
		
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_read_word_list, null, true);
			holder = new WordListViewHolder();
			holder.wordItem = (TextView) convertView.findViewById(R.id.wordItem);
			holder.wordMemoryWeights = (TextView) convertView.findViewById(R.id.wordMemoryWeights);
			holder.wordTranslation = (TextView) convertView.findViewById(R.id.wordTranslation);
			holder.viewItem = (LinearLayout) convertView.findViewById(R.id.viewItem);
			convertView.setTag(holder);
		}
		else {
			holder = (WordListViewHolder) convertView.getTag();
		}
		
		if (null == holder) {
			DebugPrinter.e(datas.size() + "-----" + position);
		}
		if (position < datas.size()) {
			if (position%2 == 0){
				holder.viewItem.setBackgroundColor(0xFFF2F2F2);
			}
			else {
				holder.viewItem.setBackgroundColor(Color.WHITE);
			}
			//单词
			UserNewWordsData readWordData = datas.get(position);
			if ( !TextUtils.isEmpty(readWordData.getWord())) {
				//holder.wordItem.setText(readWordData.getWord());
				holder.wordItem.setText(readWordData.getWord());
			}
			//翻译
			if ( !TextUtils.isEmpty(readWordData.getTranslation())) {
				holder.wordTranslation.setText(readWordData.getTranslation());
			}
			//记忆权重
				holder.wordMemoryWeights.setText(String.valueOf(readWordData.getWeight()));
		}
		
		return convertView;
	}
	
	public final class WordListViewHolder{
		TextView wordItem;
		TextView wordTranslation;
		TextView wordMemoryWeights;
		LinearLayout viewItem;
	}
	
	public List<UserNewWordsData> getData() {
		return datas;
	}
	
	public void setData(List<UserNewWordsData> datas) {
		this.datas =datas;
		notifyDataSetChanged();
	}
	

}
