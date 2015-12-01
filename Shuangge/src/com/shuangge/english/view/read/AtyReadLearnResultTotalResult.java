package com.shuangge.english.view.read;

import com.shuangge.english.view.AbstractAppActivity;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 测试总结果  
 * @author tovey
 *
 */
public class AtyReadLearnResultTotalResult extends AbstractAppActivity implements OnClickListener {
	
	private TextView titleName;
	private TextView totalScore;
	private TextView coreVocabulary;
	private TextView newLearnWords;
	private TextView reviewWords;
	private TextView basicScore;
	private TextView firstNewResult;
	private Button showScore;
	private Button retry;
	private Button Exit;
	private Button next;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_read_learn_result_total_result);
		
		titleName = (TextView) findViewById(R.id.titleName);  //文章的名称
		totalScore = (TextView) findViewById(R.id.totalScore); //总得分
		coreVocabulary = (TextView) findViewById(R.id.coreVocabulary); //核心词汇
		newLearnWords = (TextView) findViewById(R.id.newLearnWords);   //新学单词
		reviewWords = (TextView) findViewById(R.id.reviewWords);   //复习单词
		basicScore = (TextView) findViewById(R.id.basicScore);    //基础得分
		firstNewResult = (TextView) findViewById(R.id.firstNewResult);   //首次通关奖励
		
		showScore = (Button) findViewById(R.id.showScore); //牛逼成绩炫一下 
		showScore.setOnClickListener(this);
		retry = (Button) findViewById(R.id.retry);   //retry, 重新开始
		retry.setOnClickListener(this);
		Exit = (Button) findViewById(R.id.Exit);  //Exit, 退出 
		Exit.setOnClickListener(this);
		next = (Button) findViewById(R.id.next); //next, 下一个
		next.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.showScore:
			
			break;
		case R.id.retry:
			
			break;
		case R.id.Exit:
			
			break;
		case R.id.next:
			
			break;
		default:
			break;
		}
		
	}
	
	public static void startAty(Context context) {
		context.startActivity(new Intent(context, AtyReadLearnResultTotalResult.class));
	}

}
