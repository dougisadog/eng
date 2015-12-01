package com.shuangge.english.view.read.component;

import com.shuangge.english.entity.server.read.ReadQuestionData;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReadSimpleQuestion extends FrameLayout{
	
	
	//待传入参数 问题答案等参数
	public ReadSimpleQuestion(Context context, int questionPosition, ReadQuestionData readQuestionData) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//循环答案view 以及相关设置
		FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.item_read_question, this);
		fl = (FrameLayout) fl.getChildAt(0);
		LinearLayout ll = (LinearLayout) fl.getChildAt(0);
		ll.removeAllViews();
		TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		textView.setTextSize(18);
		textView.setText(readQuestionData.getSimpleQuestion());
		ll.addView(textView);
		
		String[] answers = readQuestionData.getOptions().split("\r");
        
		for (int i = 0; i < answers.length; i++) {
			// 正确的也解析
//			if (readQuestionData.isRight() && readQuestionData.getHelpIndex() - 1 == i) continue;
			ll.addView(new ReadSimpleAnswer(context, questionPosition, i));
		}
	}
	
}
