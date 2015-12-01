package com.shuangge.english.view.read.component;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.ReadQuestionData;
import com.shuangge.english.entity.server.read.ReadResult;
import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReadSimpleAnswer extends LinearLayout {

	private ReadResult result = GlobalRes.getInstance().getBeans()
			.getReadData();

	/**
	 * 构建答案模板
	 * 
	 * @param context
	 * @param answer
	 *            答案内容
	 * @param position
	 *            答案所处位置
	 */
	public ReadSimpleAnswer(Context context, int questionPosition,
			int position) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout fl = (LinearLayout) inflater.inflate(
				R.layout.item_read_answer, this);

		fl = (LinearLayout) fl.getChildAt(0);

		ImageView imageView = (ImageView) fl.getChildAt(0);
		
		ReadQuestionData  readQuestionData  = GlobalRes.getInstance().getBeans()
				.getReadData().getQuestions().get(questionPosition);
		
		TextView textView = (TextView) fl.getChildAt(1);
		textView.setTextSize(18);
		
		String answer = result.getQuestions().get(questionPosition).getAnswers().get(position);
		textView.setText(answer);
		if (readQuestionData.getRightIndex() - 1 == position) {
			imageView.setImageResource(R.drawable.icon_read_correct);
			textView.setTextColor(0xFF2BAFA4);
		}
		else if (readQuestionData.getSelectedIndex() == position){
			imageView.setImageResource(R.drawable.icon_read_wrong);
			textView.setTextColor(0xFFf05656);
		}
		else if (readQuestionData.getHelpIndex() - 1 == position){
			imageView.setVisibility(View.INVISIBLE);
			textView.setTextColor(0xFFFF9C00);
		}
		else {
			imageView.setVisibility(View.INVISIBLE);
		}

	}

}
