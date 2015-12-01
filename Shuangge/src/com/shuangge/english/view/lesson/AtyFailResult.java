package com.shuangge.english.view.lesson;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.view.AbstractAppActivity;

/**
 * 课程列表
 * @author Jeffrey
 *
 */
public class AtyFailResult extends AbstractAppActivity implements OnClickListener {

	private TextView txtAllScore, txtAgain, txtBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_lesson_fail_result);
		txtBack = (TextView) findViewById(R.id.txtBack);
		txtBack.setOnClickListener(this);
		txtAgain = (TextView) findViewById(R.id.txtAgain);
		txtAgain.setOnClickListener(this);
		txtAllScore = (TextView) findViewById(R.id.txtAllScore);
		txtAllScore.setText(GlobalRes.getInstance().getBeans().getPassLessonDatas().getSummaryData().getBaseScore() + "");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtBack:
			setResult(0);
			this.finish();
			break;
		case R.id.txtAgain:
			setResult(1);
			this.finish();
			break;
		}
	}

}
