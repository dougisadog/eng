package com.shuangge.english.view.read;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadContentData;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.entity.server.user.LessonTips;
import com.shuangge.english.network.read.TaskReqReadCore46Guide;
import com.shuangge.english.network.read.TaskReqReadProgress;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.BaseShadowMask;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.read.component.ReadContentText;
import com.shuangge.english.view.read.fragment.BaseLessonType;
/**
 * 1. 文章阅读
 * 
 *
 */
public class AtyRead extends AbstractAppActivity implements OnClickListener {

	private LinearLayout llContainer , mainContainer;
	private TextView txtTitle;
	private ImageView imgBg;
	
	private ReadResult result;
	private Set<IWord> notPassWords;
	private Set<Long> translateWordIds;
	private List<ReadContentData> contents;
	private List<Long> ids;
	private Long selectedId;
	
	private BaseShadowMask mask2;
	
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		llContainer = null;
		txtTitle = null;
		imgBg = null;
		contents = null;
		notPassWords = null;
		translateWordIds = null;
		ids = null;
		result = null;
		selectedId = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.learnWord:
			if (notPassWords.size() == 0) {
				showDialogConfirm();
			}
			else {
				onReadFinished();
			}
			break;
		case R.id.KeyWords:
			AtyCoreVocabulary.startAty(this);
			break;
		default:
			break;
		}
	}
	
	private void onReadFinished() {
		List<Integer> notPassWordIds = new ArrayList<Integer>(); //未通过单词
		for (IWord word : notPassWords) {
			notPassWordIds.add(word.getId().intValue());
		}
			int readNo = GlobalRes.getInstance().getBeans().getReadData().getReadNo().intValue(); //文章id
			showLoading();
			new TaskReqReadProgress(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
				if (GlobalRes.getInstance().getBeans().getReadData().getProgress() < ReadResult.PROGRESS_L2) {
					GlobalRes.getInstance().getBeans().getReadData().setProgress(ReadResult.PROGRESS_L2);
				}
				AtyReadLesson.startAty(AtyRead.this);
				finish();
					
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}

			}, readNo ,ReadResult.PROGRESS_L2, notPassWordIds);
	}
	
	private DialogConfirmFragment dialogNoneWordsConfirm;
	
	private void showDialogConfirm () {
		dialogNoneWordsConfirm = new DialogConfirmFragment( new CallBackDialogConfirm() {
			
			@Override
			public void onSubmit(int position) {
				dialogNoneWordsConfirm.dismiss();
				dialogNoneWordsConfirm = null;
			}
			
			@Override
			public void onCancel() {
				dialogNoneWordsConfirm.dismiss();
				dialogNoneWordsConfirm = null;
				onReadFinished();
			}

			@Override
			public void onKeyBack() {
				onCancel();
			}
			
		}, getString(R.string.readNoneWords), "", 0 , getString(R.string.readNoneWordsBtn1) , getString(R.string.readNoneWordsBtn2));
		dialogNoneWordsConfirm.showDialog(getSupportFragmentManager());
		
	}

	//绑定所有id按照布局顺序 若为词组 （fails[12312-0] to[12312-1 则只存入12312 若前后两者 advice[333] ... advice[333] 则按顺序添加]）  
	//并将notPassWords 的id 放入 translateWordIds (translateWordIds 和 notPassWords Set去重 当遍历ids时可以将所有重复单词设置 同样状态)
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_read);
		llContainer = (LinearLayout) findViewById(R.id.llContainer);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setVisibility(View.GONE);
		imgBg = (ImageView) findViewById(R.id.imgBg);
		imgBg.setVisibility(View.GONE);
		findViewById(R.id.btnBack).setOnClickListener(this);
		
		mainContainer = (LinearLayout) findViewById(R.id.mainContainer);
		
		findViewById(R.id.KeyWords).setOnClickListener(this);  //核心词汇						
		findViewById(R.id.learnWord).setOnClickListener(this); //单词学习
		ids = new ArrayList<Long>();
		translateWordIds = new HashSet<Long>();
		notPassWords = GlobalRes.getInstance().getBeans().getNotPassWordsForRead();
//		notPassWords.clear();
		result = GlobalRes.getInstance().getBeans().getReadData();
		
//		for (ReadContentData data : data.getContents()) {
//			data.setContent("a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1]. a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].a[1], b[2] c[3] d[4] e[15-0] f[15-0] g[15-1].");
//		}
//		
//		data.getWordMap().clear();
//		data.getWordMap().put(1l, new ReadWordData(1l, "a", "你"));
//		data.getWordMap().put(2l, new ReadWordData(2l, "b", "好"));
//		data.getWordMap().put(3l, new ReadWordData(3l, "c", "个"));
//		data.getWordMap().put(4l, new ReadWordData(4l, "d", "的"));
//		data.getWordMap().put(15l, new ReadWordData(15l, "gfg", "短语"));
//		data.getUserWordMap().clear();
		
		if (!TextUtils.isEmpty(result.getTitle())) {
			txtTitle.setText(result.getTitle());
			txtTitle.setVisibility(View.VISIBLE);	
//			txtTitle.setTextColor(0xFFD6D6D6);
		}
		if (!TextUtils.isEmpty(result.getImg())) {
			GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(result.getImg(), imgBg));
			imgBg.setVisibility(View.VISIBLE);
		}
		
		
//		Pattern pattern = Pattern.compile("[a-zA-Z-/']+\\[(\\d)+(-\\d)?\\]");
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_WORD_PART + "(" + ReadPattern.PATTERN_CODE_PART + ")?");
		contents = result.getContents();
		for (ReadContentData content : contents) {
			if (TextUtils.isEmpty(content.getContent())) {
				continue;
			}
			Matcher matcher = pattern.matcher(content.getContent());
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
		}
		
		for (IWord word : notPassWords) {
			if (null != word) {
				translateWordIds.add(word.getId());
			}
		}
		
		ReadWordData readWord = null;
		for (Long id : result.getUserWordMap().keySet()) {
			if (result.getUserWordMap().get(id).isEnable()) {
				readWord = result.getWordMap().get(id);
				if (null != readWord) {
					notPassWords.add(readWord);
					translateWordIds.add(readWord.getId());
				}
			}
		}
	
		refreshContent();
		initActicleView();
		refreshActicleView();
		LessonTips lessonTips = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getLessonTips();
		if (!lessonTips.getRead()) {
			lessonTips.setRead(true);
			mask2 = new BaseShadowMask(new BaseShadowMask.CallbackHomeMask() {
	
				@Override
				public void close() {
					if (null != mask2) { 
						mask2.hide(getSupportFragmentManager());
						mask2 = null;
					}
				}
	
			}, (ViewGroup) mainContainer.getParent(), getString(R.string.readGuide), getString(R.string.readLLKBtn));
			mask2.show(getSupportFragmentManager());
			new TaskReqReadCore46Guide(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
				}
				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}

			}, lessonTips.getCore46());
		}
	}

	//将content （带参数码） 放入view 默认黑色
	private void initActicleView() {
		ReadContentText text = null;
		for (ReadContentData content : contents) {
			text = new ReadContentText(this);
			text.getTxt().setText(content.getContent2(), BufferType.SPANNABLE);

			text.getTxt().setMovementMethod(LinkMovementMethod.getInstance());
			text.getTxt().setTextSize(20);
//			text.getTxt().setTextColor(Color.BLACK);
			text.getTxt().setTextColor(0xFF4e4e4e);
			text.setBackgroundColor(0xFFf8f8f8);
			int dp10 = DensityUtils.dip2px(this, 10);
			LinearLayout.LayoutParams lp = ViewUtils.setLinearMargins(text, 
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, dp10, dp10, dp10, 0);
			text.setLayoutParams(lp);
			llContainer.addView(text);
		}
	}
	
	
	//根据translateWordIds（已出现翻译的span 块）  遍历content（每个段落）
	//解析 参数码重组成带翻译的 content  并用 ReadContentData 的content2加以储存
	private void refreshContent() {
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_WORD_PART + ReadPattern.PATTERN_CODE_PART);
		contents = result.getContents();
		long time = System.currentTimeMillis();
		for (ReadContentData content : contents) {
			if (TextUtils.isEmpty(content.getContent())) {
				continue;
			}
			Matcher matcher = pattern.matcher(content.getContent());
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
				//段落 开头未匹配部分也加入  总显示buffer
//				if (index != 0) {
					buffer.append(content.getContent().substring(index, matcher.start()));
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
			if (content.getContent().length() > index) {
				buffer.append(content.getContent().substring(index));
			}
			content.setContent2(buffer.toString());
		}
		System.out.println("消耗：" + (time - System.currentTimeMillis()) + "ms");
	}

	// 将每个content 正则匹配字符串  单词 将view ReadContentText 中的匹配的所有单词 用
	//ClickableSpan 来替换 来实现点击切换翻译的效果 详见ClickableSpan 的onclick
	private void refreshActicleView() {
		long time = System.currentTimeMillis();
		ReadContentText text = null;
		int index = 0;
		Long id = null;
		for (int i = 0; i < llContainer.getChildCount() && i < contents.size(); ++i) {
			if (TextUtils.isEmpty(contents.get(i).getContent2())) {
				continue;
			}
			String showContent =  contents.get(i).getContent2();
			text = (ReadContentText) llContainer.getChildAt(i);
			SpannableStringBuilder ssb = new SpannableStringBuilder(showContent);
//			Pattern pattern = Pattern.compile("[a-zA-Z-/'\u4e00-\u9fa5（）；]+");
			Pattern pattern = Pattern.compile(ReadPattern.PATTERN_STRING);
			
			Matcher matcher = pattern.matcher(showContent);
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
				
//				ClickableSpan cs = clickableSpans.get(id.intValue());
//				if (null == cs) {
//					cs = new WordClickableSpan(data);
//					clickableSpans.add(cs);
//				}
//				String word = matcher.group();
				ClickableSpan cs = new WordClickableSpan(data);
				ssb.setSpan(cs, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				//单词及翻译的换色规则  onclick中赋值selectedId 即当前单词id
				//从ids 库中进行匹配 （ids中的id 是 单独的 词组2单词 为1个id 记录）
				//选中状态
				if (null != selectedId && selectedId == id.longValue()) {
					ssb.setSpan(new ForegroundColorSpan(0xFFFF9C00),
							matcher.start(), matcher.end(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					continue;
				}
				//非选中 但处于翻译状态
				if (translateWordIds.contains(id.longValue())) {
					ssb.setSpan(new ForegroundColorSpan(0xFF5CBCB3),
							matcher.start(), matcher.end(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
//			pattern = Pattern.compile("[\u4e00-\u9fa5（）；]+"); //匹配汉字
			pattern = Pattern.compile(ReadPattern.PATTERN_STRING_TRANSLATION);
			
			matcher = pattern.matcher(showContent);
			while (matcher.find()) {
				ssb.setSpan(new AbsoluteSizeSpan(24), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.GRAY),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			text.setText(ssb);
			//？？此处数据为空
			text.setUrl(contents.get(i).getImg());
		}
		System.out.println("消耗2：" + (time - System.currentTimeMillis()) + "ms");

	}
	
	
	class WordClickableSpan extends ClickableSpan {

		private ReadWordData wordData;

		public WordClickableSpan() {
		}

		public WordClickableSpan(ReadWordData wordData) {
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
//				MediaPlayerMgr.getInstance().playMp("http://tts.baidu.com/text2audio?lan=en&pid=101&ie=UTF-8&&spd=2&text=" + wordData.getWord());
//				MediaPlayerMgr.getInstance().playMp(BaseLessonType.getSoundLocalPath(wordData.getId()));
				String path = BaseLessonType.getSoundLocalPath(wordData.getId());
				if (!new File(path).exists()) {
					SoundUtils.loadIWordRes(path, wordData.getSoundUrl(), null);
				}
				else {
					MediaPlayerMgr.getInstance().playMp(path);
				}
			}
			//重置 每个content的 总text内容
			refreshContent();
			//将refreshContent() 结果的内容 每个单词转换span 并更新span状态
			refreshActicleView();
		}
		

		@Override
		public void updateDrawState(TextPaint ds) {
//			ds.setColor(Color.BLACK);
			ds.setUnderlineText(false);
		}

	}

	/*******************************************************************************************************************************************/

	public static void startAty(Context context) {
		context.startActivity(new Intent(context, AtyRead.class));
	}

}