package com.shuangge.english.view.read.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadContentData;
import com.shuangge.english.entity.server.read.ReadQuestionData;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.view.read.AtyRead;
import com.shuangge.english.view.read.ReadPattern;
import com.shuangge.english.view.read.component.ReadAnswer.CallbackClick;
import com.shuangge.english.view.read.fragment.BaseLessonType;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;
import android.view.View.OnClickListener;

public class ReadQuestion extends FrameLayout{
	
	private Context context;
	private LinearLayout ll;
	
	private Set<IWord> notPassWords;
	private Set<Long> translateWordIds;
	private List<Long> ids;
	private Long selectedId;
	
	private final String prefix;
	private String question; //带参数的 问题 串
	
	private String simpleQuestion; //解析后问题串
	
	private ReadResult result = GlobalRes.getInstance().getBeans().getReadData();
	
	
	//待传入参数 问题答案等参数
	public ReadQuestion(Context context, int questionPosition, ReadQuestionData readQuestionData, CallbackClick callbackClick) {
		super(context);
		this.context = context;
		// A. 类似的序号前缀 处理
		if (readQuestionData.getQuestion().indexOf(".") != -1 && readQuestionData.getQuestion().indexOf(".") < 3) {
			this.prefix = readQuestionData.getQuestion().substring(0,readQuestionData.getQuestion().indexOf(".") + 1);
			this.question = readQuestionData.getQuestion().substring(readQuestionData.getQuestion().indexOf(".") + 1,readQuestionData.getQuestion().length());
		}
		else {
			this.prefix = "";
			this.question = readQuestionData.getQuestion();
		}
		//用于匹配单字母开头的翻译匹配
		while (this.question.indexOf(" ") == 0) {
			this.question = this.question.substring(1,this.question.length());
		}
		this.question = " " + this.question;
		
		bindingDatas();
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//循环答案view 以及相关设置
		FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.item_read_question, this);
		fl = (FrameLayout) fl.getChildAt(0);
		ll = (LinearLayout) fl.getChildAt(0);
		ll.removeAllViews();
		TextView textView = new TextView(context);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		textView.setTextSize(18);
		textView.setTag(questionPosition);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		ll.addView(textView);
		
		String simpleQuestion = wordsTranslation(this.question);
		//将普通文本储存在 内存中
		readQuestionData.setSimpleQuestion(prefix + simpleQuestion);
		
		buildClickableSpan(textView);
		
		String[] answers = readQuestionData.getOptions().split("\r");
        
		for (int i = 0; i < answers.length; i++) {
			ll.addView(new ReadAnswer(context, answers[i], questionPosition, i, callbackClick));
		}
		
	}
	
	private void bindingDatas() {
		ids = new ArrayList<Long>();
		notPassWords = new HashSet<IWord>();
		notPassWords.addAll(GlobalRes.getInstance().getBeans().getNotPassWordsForRead());
//		notPassWords = GlobalRes.getInstance().getBeans().getNotPassWordsForRead();
		translateWordIds = new HashSet<Long>();
		
		//匹配带id参数的 单词 按顺序 排入ids
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_WORD_PART + "(" + ReadPattern.PATTERN_CODE_PART + ")?");
			Matcher matcher = pattern.matcher(question);
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
		
			//将所有有翻译的单词放入 未翻译 和已翻译列表
		ReadWordData readWord = null;
		for (int i = 0; i < ids.size(); i++) {
			readWord = result.getWordMap().get(ids.get(i));
			if (null != readWord) {
				notPassWords.add(readWord);
//				translateWordIds.add(readWord.getId());
			}
		}
				
	}
	
	//将带参数单词 翻译成正常 文（带翻译）
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
				
//				if (index != 0) {
					buffer.append(content.substring(index, matcher.start()));
//				}
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
			simpleQuestion = buffer.toString();
			return buffer.toString();
	}
	
	//重置所有单词的点击
	public void buildClickableSpan(View v) {
		
		int index = 0;
		Long id = null;
		
		TextView tv = (TextView) v;
		SpannableStringBuilder ssb = new SpannableStringBuilder(prefix + simpleQuestion);
//		Pattern pattern = Pattern.compile("[a-zA-Z-/'\u4e00-\u9fa5（）；]+");
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_STRING);
		Matcher matcher = pattern.matcher(simpleQuestion);
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
			
			ClickableSpan cs = new QuestionClickableSpan(data);
			ssb.setSpan(cs, matcher.start()+prefix.length(), matcher.end()+prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			//单词及翻译的换色规则  onclick中赋值selectedId 即当前单词id
			//从ids 库中进行匹配 （ids中的id 是 单独的 词组2单词 为1个id 记录）
			//选中状态
			if (null != selectedId && selectedId.longValue() == id.longValue()) {
				ssb.setSpan(new ForegroundColorSpan(0xFFFF9C00), matcher.start()+prefix.length(), matcher.end()+prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				continue;
			}
			//非选中 但处于翻译状态
			if (translateWordIds.contains(id)) {
				ssb.setSpan(new ForegroundColorSpan(0xFF5CBCB3), matcher.start()+prefix.length(), matcher.end()+prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
//		pattern = Pattern.compile("[\u4e00-\u9fa5（）；]+"); //匹配汉字
		pattern = Pattern.compile(ReadPattern.PATTERN_STRING_TRANSLATION);
		matcher = pattern.matcher(simpleQuestion);
		while (matcher.find()) {
			ssb.setSpan(new AbsoluteSizeSpan(24), matcher.start()+prefix.length(), matcher.end()+prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.GRAY), matcher.start()+prefix.length(), matcher.end()+prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		tv.setText(ssb);
	}
	
	class QuestionClickableSpan extends ClickableSpan {

		private ReadWordData wordData;

		public QuestionClickableSpan() {
		}

		public QuestionClickableSpan(ReadWordData wordData) {
			this.wordData = wordData;
		}

		//点击切换span里词组/单词 在 翻译状态translateWordIds key词所有 notPassWords的存在状态 
		@Override
		public void onClick(View widget) {
			if (translateWordIds.contains(wordData.getId().longValue())) {
				translateWordIds.remove(wordData.getId());
				if (notPassWords.contains(wordData))
					notPassWords.remove(wordData);
				selectedId = null;
			} 
			else {
				selectedId = wordData.getId();
				if (!notPassWords.contains(wordData))
					notPassWords.add(wordData);
				translateWordIds.add(wordData.getId());
			}
			
			wordsTranslation(question);
			buildClickableSpan(widget);
		}
		
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.GRAY);
			ds.setUnderlineText(false);
		}
	}
}
