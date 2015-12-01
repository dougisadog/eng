package com.shuangge.english.view.read;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.ReadInitResult;
import com.shuangge.english.entity.server.read.ReadListData;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.read.adapter.AdapterReadList;

public class AtyReadList extends AbstractAppActivity implements OnClickListener, OnItemClickListener{

	private ImageButton btnBack;

	private ListView lv;
	private AdapterReadList adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_read_list);
		RelativeLayout rlBg = (RelativeLayout) findViewById(R.id.rlBg);
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		//btnBack.setImageDrawable(R.drawable.s_btn_back1);
		btnBack.setOnClickListener(this);
//		TextView WordList = (TextView)findViewById(R.id.WordList);
		findViewById(R.id.rlBg).setBackgroundResource(R.color.titlebar_bg1);
		TextView txtBarTitleCn = (TextView) findViewById(R.id.txtBarTitleCn);
		txtBarTitleCn.setText("初级课程");
		TextView txtBarTitleEn = (TextView) findViewById(R.id.txtBarTitleEn);
		txtBarTitleEn.setText("Primary class");
		
		lv = (ListView) findViewById(R.id.lv);
		adapter = new AdapterReadList(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		
//		WordList.setOnClickListener(this);
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		adapter.getDatas().clear();
		if (null != beans && null != beans.getReadListData()) {
			adapter.getDatas().addAll(beans.getReadListData().getDatas());
			adapter.notifyDataSetChanged();
		}
		long readNo = getIntent().getLongExtra("readNo", ReadInitResult.DEFAULT_READNO);
		if (readNo != ReadInitResult.DEFAULT_READNO.longValue()) {
			AtyReadHome.startAty(AtyReadList.this, readNo);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
//		case R.id.WordList:
//			AtyWordList.startAty(AtyReadList.this);
//			
//			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		lv.setOnItemClickListener(null);
		ReadListData data = adapter.getItem(position);
		AtyReadHome.startAty(AtyReadList.this, data.getReadNo());
		lv.setOnItemClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		beans.getCurrentType4Data().refreshStar();
		adapter.notifyDataSetChanged();
	}
	
	/*******************************************************************************************************************************************/

	public static void startAty(Context context, Long readNo) {
		Intent i = new Intent(context, AtyReadList.class);
		i.putExtra("readNo", readNo);
		context.startActivity(i);
	}

}
