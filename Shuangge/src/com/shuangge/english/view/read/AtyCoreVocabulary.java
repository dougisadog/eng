package com.shuangge.english.view.read;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.read.adapter.AdapterCoreWords;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 核心单词  
 * @author tovey
 *
 */
public class AtyCoreVocabulary extends AbstractAppActivity implements OnClickListener {
	
	private TextView txtBarTitleCn ,txtBarTitleEn, wordTitle;
	
	private AdapterCoreWords adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.aty_core_vocabulary);
 		
 		findViewById(R.id.btnBack).setOnClickListener(this);
 		txtBarTitleEn = (TextView) findViewById(R.id.txtBarTitleEn);
 		txtBarTitleEn.setText("Core Vocabulary");
 		txtBarTitleCn = (TextView) findViewById(R.id.txtBarTitleCn);
        txtBarTitleCn.setText("核心词汇");
        wordTitle = (TextView) findViewById(R.id.wordTitle);
        
        ListView lvCoreVocabulary = (ListView) findViewById(R.id.lvCoreVocabulary);
        adapter = new AdapterCoreWords(AtyCoreVocabulary.this);
        lvCoreVocabulary.setAdapter(adapter);
        
        bindDatas();
	}
	
	@SuppressWarnings("unchecked")
	private void bindDatas() {
		/*
		ReadResult result = GlobalRes.getInstance().getBeans().getReadData();
		if (null == result) return;
		Map<Long, ReadWordData> ReadWordDatas = result.getWordMap();
		Set<Long> ids= result.getCoreWordIds();
		if (null == ReadWordDatas || null == ids) return;
		for (Long id : ids) {
			ReadWordData rd = ReadWordDatas.get(id);
			if (null != rd) {
				adapter.getDatas().add(rd);
			}
		}
		*/
		adapter.getDatas().addAll((Collection<? extends ReadWordData>) GlobalRes.getInstance().getBeans().getReadData().getCoreWords());
		wordTitle.setText("单词(" + adapter.getDatas().size() + ")");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		default:
			break;
		}
		
	}
	
	
	public static void startAty(Context context) {
		context.startActivity(new Intent(context, AtyCoreVocabulary.class));
	}

}
