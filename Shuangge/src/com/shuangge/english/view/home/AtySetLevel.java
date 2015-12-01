package com.shuangge.english.view.home;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtySetLevel extends AbstractAppActivity implements OnClickListener {
	
	public static final int REQUEST_CODE = 1000;
	
	private LinearLayout llLevel1, llLevel2, llLevel3, llLevel4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_set_level);
		
		llLevel1 = (LinearLayout) findViewById(R.id.llLevel1);
		llLevel1.setOnClickListener(this);
		llLevel2 = (LinearLayout) findViewById(R.id.llLevel2);
		llLevel2.setOnClickListener(this);
		llLevel3 = (LinearLayout) findViewById(R.id.llLevel3);
		llLevel3.setOnClickListener(this);
		llLevel4 = (LinearLayout) findViewById(R.id.llLevel4);
		llLevel4.setOnClickListener(this);
	
	}
	
	@Override
	public void onClick(View v) {
		try {
			InfoData data = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
			switch (v.getId()) {
			case R.id.llLevel1:
				data.setLevel(1);
				break;
			case R.id.llLevel2:
				data.setLevel(2);
				break;
			case R.id.llLevel3:
				data.setLevel(3);
				break;
			case R.id.llLevel4:
				data.setLevel(4);
				break;
			}
		} catch (Exception e) {
		} finally {
			finish();
		}
	}
	
	@Override
	public boolean onBack() {
		return true;
	}
	
	public static void show(Activity activity) {
		activity.startActivityForResult(new Intent(activity, AtySetLevel.class), REQUEST_CODE);
	}
	
}
