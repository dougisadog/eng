package com.shuangge.english.view.read.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadQuestionData;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.view.read.AtyRead;
import com.shuangge.english.view.read.AtyReadQuestion;
import com.shuangge.english.view.read.ReadPattern;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;
import android.view.View.OnClickListener;

public class ReadAnswer extends LinearLayout implements OnClickListener {

	private Context context;

	private Set<IWord> notPassWords;
	private Set<Long> translateWordIds;
	private List<Long> ids;
	private Long selectedId;
	private int questionPosition;
	private int position;

	private String prefix;
	private String answer;

	private String simpleAnswer;
	
	public static String PREFIX_SIGN = ")";

	private ReadResult result = GlobalRes.getInstance().getBeans()
			.getReadData();
	
	
	private CallbackClick callbackClick;
	
	public static interface CallbackClick {
			
			public void refreshButton();
			
		}

	/**
	 * 构建答案模板
	 * 
	 * @param context
	 * @param answer
	 *            答案内容
	 * @param position
	 *            答案所处位置
	 */
	public ReadAnswer(Context context, String answer, int questionPosition,
			int position, CallbackClick callbackClick) {
		super(context);
		this.context = context;
		this.position = position;
		this.questionPosition = questionPosition;
		this.callbackClick = callbackClick;
		//判断为 提示选项时
		if (result.getQuestions().get(questionPosition).getHelpIndex() - 1 == position) {
			this.setVisibility(View.GONE);
			List<String> answers = result.getQuestions().get(questionPosition).getAnswers();
			answers.add(position, answer);
			return;
		}
		
		//答案前缀处理 
		answer = answer.replace("#", "");
//		answer = answer.replace("*", "");
		// 内容前加 空格 用于匹配单字母开头的翻译匹配
		if (answer.indexOf(PREFIX_SIGN) != -1 && answer.indexOf(PREFIX_SIGN) < 3) {
			this.prefix = answer.substring(0, answer.indexOf(PREFIX_SIGN) + 1).trim() + " ";
			this.answer = answer.substring(answer.indexOf(PREFIX_SIGN) + 1, answer.length());
		} else {
			this.prefix = "";
			this.answer = answer;
		}
		//用于匹配单字母开头的翻译匹配
		while (this.answer.indexOf(" ") == 0) {
			this.answer = this.answer.substring(1,this.answer.length());
		}
		this.answer = " " + this.answer;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout ll = (LinearLayout) inflater.inflate(
				R.layout.item_read_answer, this);

		ll = (LinearLayout) ll.getChildAt(0);

		ImageView imageView = (ImageView) ll.getChildAt(0);
		imageView.setImageResource(R.drawable.item_answer);
		imageView.setTag(questionPosition + "-" + position);
		imageView.setOnClickListener(this);

		TextView textView = (TextView) ll.getChildAt(1);
		textView.setTextSize(18);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		textView.setText(this.answer, BufferType.SPANNABLE);

		bindingDatas();
		
		String simpleAnswer = wordsTranslation(this.answer);
		//将普通文本储存在 内存中
		List<String> answers = result.getQuestions().get(questionPosition).getAnswers();
		answers.add(position, prefix + simpleAnswer);
		buildClickableSpan(textView);

	}

	// 具体情况具体分析
	@Override
	public void onClick(View v) {
		List<ReadQuestionData> readQuestionDatas = GlobalRes.getInstance().getBeans().getReadData().getQuestions();
		if (readQuestionDatas.get(questionPosition).getSelectedIndex() == position) return;
		LinearLayout question = (LinearLayout) v.getParent().getParent().getParent();
		if (null == question) return;
		// 第一个是问题的文本 故从1开始 最后一条option为 提示 不参加点击事件
		for (int i = 0; i < question.getChildCount() - 2; i++) {
			ImageView imageView = (ImageView) question.findViewWithTag(questionPosition + "-" + i);
			if (null == imageView) continue;
			imageView.setImageResource(R.drawable.item_answer);
		}

		readQuestionDatas.get(questionPosition).setSelectedIndex(position);
			((ImageView) v).setImageResource(R.drawable.item_answer_selected);
			
			callbackClick.refreshButton();
			
	}


	private void bindingDatas() {
		ids = new ArrayList<Long>();
		notPassWords = new HashSet<IWord>();
		notPassWords.addAll(GlobalRes.getInstance().getBeans().getNotPassWordsForRead());
//		notPassWords = GlobalRes.getInstance().getBeans().getNotPassWordsForRead();
		translateWordIds = new HashSet<Long>();

		// 匹配带id参数的 单词 按顺序 排入ids
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_WORD_PART + "(" + ReadPattern.PATTERN_CODE_PART + ")?");
		Matcher matcher = pattern.matcher(answer);
		Long resId = null;
		String str = "";
		while (matcher.find()) {
			str = matcher.group();
			resId = -1L;
			if (str.indexOf("[") != -1) {
				str = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
				if (str.indexOf("-") != -1) {
					String[] strs = str.split("-");
					str = strs[0];
				}
				resId = Long.valueOf(str);
			}
			ids.add(resId);
		}

		// 将所有有翻译的单词放入 未翻译 和已翻译列表
		ReadWordData readWord = null;
		for (int i = 0; i < ids.size(); i++) {
			readWord = result.getWordMap().get(ids.get(i));
			if (null != readWord) {
				notPassWords.add(readWord);
			}
		}

	}

	// 将带参数单词 翻译成正常 文（带翻译）
	private String wordsTranslation(String content) {
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_WORD_PART + ReadPattern.PATTERN_CODE_PART);
		Matcher matcher = pattern.matcher(content);
		int index = 0;
		boolean phraseFlag = false;
		Long resId = null; 
		String str = "";
		String word = "";
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			phraseFlag = true;
			str = matcher.group();
			word = str.substring(0, str.indexOf("["));
			str = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
			if (str.indexOf("-") != -1) {
				String[] strs = str.split("-");
				phraseFlag = "1".equals(strs[1]);
				str = strs[0];
			}

//			if (index != 0) {
				buffer.append(content.substring(index, matcher.start()));
//			}
			index = matcher.end();
			buffer.append(word);

			resId = Long.valueOf(str);

			if (phraseFlag && translateWordIds.contains(resId)) {
				ReadWordData wordData = result.getWordMap().get(resId);
				if (null != wordData)
					buffer.append(wordData.getTranslation());
			}
		}
		if (content.length() > index) {
			buffer.append(content.substring(index));
		}
		simpleAnswer = buffer.toString();
		return buffer.toString();
	}

	// 重置所有单词的点击
	public void buildClickableSpan(View v) {

		int index = 0;
		Long id = null;

		TextView tv = (TextView) v;
		SpannableStringBuilder ssb = new SpannableStringBuilder(prefix
				+ simpleAnswer);
//		Pattern pattern = Pattern.compile("[a-zA-Z-/'\u4e00-\u9fa5（）；]+");
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_STRING);
		Matcher matcher = pattern.matcher(simpleAnswer);
		ReadWordData data = null;
		while (matcher.find()) {
			if (ids.size() == 0) {
				return;
			}
			if (index >= ids.size()) {
				return;
			}
			id = ids.get(index++);
			if (id == -1 || null == (data = result.getWordMap().get(id))) {
				continue;
			}

			ClickableSpan cs = new AnswerClickableSpan(data);
			ssb.setSpan(cs, matcher.start() + prefix.length(), matcher.end()
					+ prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			// 单词及翻译的换色规则 onclick中赋值selectedId 即当前单词id
			// 从ids 库中进行匹配 （ids中的id 是 单独的 词组2单词 为1个id 记录）
			// 选中状态
			if (null != selectedId && selectedId.longValue() == id.longValue()) {
				ssb.setSpan(new ForegroundColorSpan(0xFFFF9C00),
						matcher.start() + prefix.length(), matcher.end()
								+ prefix.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				continue;
			}
			// 非选中 但处于翻译状态
			if (translateWordIds.contains(id)) {
				ssb.setSpan(new ForegroundColorSpan(0xFF5CBCB3),
						matcher.start() + prefix.length(), matcher.end()
								+ prefix.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
//		pattern = Pattern.compile("[\u4e00-\u9fa5（）；]+"); // 匹配汉字
		pattern = Pattern.compile(ReadPattern.PATTERN_STRING_TRANSLATION);
		matcher = pattern.matcher(simpleAnswer);
		while (matcher.find()) {
			ssb.setSpan(new AbsoluteSizeSpan(24),
					matcher.start() + prefix.length(),
					matcher.end() + prefix.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.GRAY), matcher.start()
					+ prefix.length(), matcher.end() + prefix.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		tv.setText(ssb);
	}

	class AnswerClickableSpan extends ClickableSpan {

		private ReadWordData wordData;

		public AnswerClickableSpan() {
		}

		public AnswerClickableSpan(ReadWordData wordData) {
			this.wordData = wordData;
		}

		// 点击切换span里词组/单词 在 翻译状态translateWordIds key词所有 notPassWords的存在状态
		@Override
		public void onClick(View widget) {
			if (translateWordIds.contains(wordData.getId().longValue())) {
				translateWordIds.remove(wordData.getId());
				if (notPassWords.contains(wordData))
					notPassWords.remove(wordData);
				selectedId = null;
			} else {
				selectedId = wordData.getId();
				if (!notPassWords.contains(wordData))
					notPassWords.add(wordData);
				translateWordIds.add(wordData.getId());
			}

			wordsTranslation(answer);
			buildClickableSpan(widget);
		}

		//取消下划线 并设置颜色
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.GRAY);
			ds.setUnderlineText(false);
		}
	}

}
