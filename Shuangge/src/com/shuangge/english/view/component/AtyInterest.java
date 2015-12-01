package com.shuangge.english.view.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyInterest extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_INTEREST = 1030;
	
	public static final int CODE_INTEREST = 100;
	public static final String PARAM = "param";
	public final static String CALLBACK_DATAS = "callback datas";
	
	private ImageView btnSave;
	private ImageButton btnBack;
	private GridView gv;
	private AdapterInterest adapter;
	private Set<String> allDatas = new HashSet<String>();
	private Set<String> selected = new HashSet<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		
		setContentView(R.layout.aty_interest);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		gv = (GridView) findViewById(R.id.gv);
		adapter = new AdapterInterest(this);
		
		String[] datas = getResources().getStringArray(R.array.interest);
		for (int i = 0; i < datas.length; i++) {
			allDatas.add(datas[i]);
			adapter.getDatas().add(datas[i]);
		}
		String param = getIntent().getStringExtra(PARAM);
		if (!TextUtils.isEmpty(param)) {
			String[] strs = param.split("、");
			for (String str : strs) {
				if (allDatas.contains(str)) {
					selected.add(str);
				}
			}
		}
		gv.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnSave:
				Intent intent = new Intent();
				StringBuilder builder = new StringBuilder();
				int i = 0;
				for (String str : selected) {
					if (i++ > 0) {
						builder.append("、");
					}
					builder.append(str);
				}
				intent.putExtra(CALLBACK_DATAS, builder.toString());
				setResult(CODE_INTEREST, intent);
				this.finish();
				return;
			case R.id.btnBack:
				this.finish();
				return;
			default:
				break;
		}
		if (v instanceof TextView) {
			String str = ((TextView) v).getText().toString();
			if (selected.contains(str)) {
				selected.remove(str);
				((TextView) v).setBackgroundResource(R.drawable.btn_interest);
				((TextView) v).setTextColor(v.getResources().getColor(R.color.default_sub_info));
				return;
			}
			((TextView) v).setBackgroundResource(R.drawable.btn_interest_p);
			((TextView) v).setTextColor(v.getResources().getColor(R.color.white));
			selected.add(str);
		}
	}
	
	private class AdapterInterest extends ArrayAdapter<String> {

		private LayoutInflater mInflater;
		private List<String> datas = new ArrayList<String>();

		public AdapterInterest(Context context) {
			super(context, 0);
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public String getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView txtInterest = null;
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.item_interest, null, true);
			}
			txtInterest = (TextView) convertView.findViewById(R.id.txtInterest);
			String str = datas.get(position);
			if (selected.contains(str)) {
				txtInterest.setBackgroundResource(R.drawable.btn_interest_p);
				txtInterest.setTextColor(convertView.getResources().getColor(R.color.white));
			}
			else {
				txtInterest.setBackgroundResource(R.drawable.btn_interest);
				txtInterest.setTextColor(convertView.getResources().getColor(R.color.default_sub_info));
			}
			txtInterest.setText(str);
			txtInterest.setOnClickListener(AtyInterest.this);
			return convertView;
		}

		public List<String> getDatas() {
			return datas;
		}
		
	}
	
	/************************************************************************************************/
	
	public static void startAty(Activity context, String content) {
		Intent i = new Intent(context, AtyInterest.class);
		i.putExtra(AtyInterest.PARAM, content);
		context.startActivityForResult(i, REQUEST_INTEREST);
	}
}
