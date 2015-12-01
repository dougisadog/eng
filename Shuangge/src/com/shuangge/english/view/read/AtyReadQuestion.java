package com.shuangge.english.view.read;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.cache.GlobalRes.DisplayBitmapParam;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadContentData;
import com.shuangge.english.entity.server.read.ReadQuestionData;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.entity.server.user.LessonTips;
import com.shuangge.english.network.read.TaskReqReadCore46Guide;
import com.shuangge.english.network.read.TaskReqReadResult;
import com.shuangge.english.receiver.IPushMsgNotice;
import com.shuangge.english.support.app.AnalyticsManager;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.app.ScreenObserver.ScreenStateListener;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.DensityUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.component.BaseShadowMask;
import com.shuangge.english.view.component.dialog.DialogLoadingFragment;
import com.shuangge.english.view.component.dialog.DialogLoadingFragment.ICallbackDialog;
import com.shuangge.english.view.read.component.ReadAnswer;
import com.shuangge.english.view.read.component.ReadContentText;
import com.shuangge.english.view.read.component.ReadQuestion;
import com.shuangge.english.view.read.fragment.BaseLessonType;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

/**
 * 阅读理解
 * @author tovey
 *
 */
public class AtyReadQuestion extends SlidingFragmentActivity implements ScreenStateListener, ICallbackDialog, IPushMsgNotice, OnClickListener {

	public static boolean isActive = true;
	private boolean isRunning = false;
	
	private LinearLayout llContainer , mainContainer ;
	private TextView txtTitle , learnBtn;
	private ImageView imgBg;
	
	private ReadResult result;
	private Set<IWord> notPassWords;
	private Set<Long> translateWordIds;
	private List<ReadContentData> contents;
	private List<Long> ids;
	private Long selectedId;
	
	public List<String> answerList;
	
	private DialogLoadingFragment loadingDialog;
	private SlidingMenu sm;
	
	private boolean checkResult = true; //
	
	private BaseShadowMask mask2 ,mask3;
	
	private LessonTips lessonTips;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		forceShowActionBarOverflowMenu();
		
		GlobalApp.getInstance().setActivity(this);
        GlobalApp.getInstance().addStackActivity(this);
        isRunning = true;
        
        //Menu
        setBehindContentView(R.layout.menu_read_question);
        
       
		sm = getSlidingMenu();
//			sm.setShadowWidthRes(R.dimen.shadow_width);
//			sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindWidth(AppInfo.getScreenWidth() - DensityUtils.dip2px(this, 50));
//			sm.setFadeDegree(0.5f);
		sm.setFadeEnabled(false);
		sm.setBgFadeEnabled(true);
		sm.setBgFadeDegree(0.8f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		lessonTips = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getLessonTips();
			sm.setOnOpenListener(new OnOpenListener() {
				@Override
				public void onOpen() {
					if (!lessonTips.getReadQuestionMenu()) {
						lessonTips.setReadQuestionMenu(true);
						mask3 = new BaseShadowMask(new BaseShadowMask.CallbackHomeMask() {
		
							@Override
							public void close() {
								if (null != mask3) { 
									mask3.hide(getSupportFragmentManager());
									mask3 = null;
								}
							}
		
						}, (ViewGroup) sm, getString(R.string.readQuestionGuide2), "OK");
						mask3.show(getSupportFragmentManager());
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
			});
		setContentView(R.layout.aty_read);
		
		findViewById(R.id.KeyWords).setVisibility(View.INVISIBLE);
		
		
		llContainer = (LinearLayout) findViewById(R.id.llContainer);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setVisibility(View.GONE);
		imgBg = (ImageView) findViewById(R.id.imgBg);
		imgBg.setVisibility(View.GONE);
		//findViewById(R.id.txtQuestion).setOnClickListener(this);
		findViewById(R.id.btnBack).setOnClickListener(this);
		
		learnBtn = (TextView) findViewById(R.id.learnWord);
		learnBtn.setOnClickListener(this);
		learnBtn.setText("开始做题");
		findViewById(R.id.txtSubmit).setOnClickListener(this);
		
		bindingDatas();
		refreshContent();
		initActicleView();
		refreshActicleView();
		
		mainContainer = (LinearLayout) findViewById(R.id.mainContainer);
		
		if (!lessonTips.getReadQuestion()) {
			lessonTips.setReadQuestion(true);
			mask2 = new BaseShadowMask(new BaseShadowMask.CallbackHomeMask() {
	
				@Override
				public void close() {
					if (null != mask2) { 
						mask2.hide(getSupportFragmentManager());
						mask2 = null;
					}
				}
	
			}, (ViewGroup) mainContainer.getParent(), getString(R.string.readQuestionGuide), "OK");
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
		selectedId = null;
		
		result.setReset(true);
		resetQuestions();
	}
	
	private void resetQuestions() {
		 if (result.isReset()) {
		    	for (int i = 0; i <  result.getQuestions().size(); i++) {
		    		ReadQuestionData readQuestionData = result.getQuestions().get(i);
		    		String tag = i + "-" + readQuestionData.getSelectedIndex();
		    		ImageView iv = (ImageView) ((LinearLayout) findViewById(R.id.llQuestionContainer)).findViewWithTag(tag);
		    		if (null == iv) continue;
		    		iv.setImageResource(R.drawable.item_answer);
		    		readQuestionData.setSelectedIndex(ReadQuestionData.ORGIN_SELECTED_STATE);
				}
		    	findViewById(R.id.txtSubmit).setBackgroundResource(R.drawable.bg_lesson);
		    	result.setReset(false);
		    }
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    //Retry 返回时 重置所有选项 并清空 缓存的 selectedIndex
	    resetQuestions();
	    
	    GlobalApp.getInstance().setCurrentRunningActivity(this);
	    getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    //从后台唤醒，进入前台
	    if (!isActive) {
	    	isActive = true;
		}
	    //友盟统计
	    AnalyticsManager.getInstance().onPageStart(this);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    isRunning = false;
	    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    pauseMusic();
	    //友盟统计
	    AnalyticsManager.getInstance().onPageEnd(this);
	    if (GlobalApp.getInstance().getCurrentRunningActivity().equals(this)) {
	        GlobalApp.getInstance().setCurrentRunningActivity(null);
	    }
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//进入后台
		if (!isAppOnForeground()) {
			isActive = false;
	    }
	}

	private void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {

        }
    }
	
	public void notice() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		/*case R.id.txtQuestion:
			toggle();
			break;*/
		case R.id.learnWord:
			toggle();
			break;
		case R.id.txtSubmit:
			//TODO
			checkResult = true;
			int readNo = GlobalRes.getInstance().getBeans().getReadData().getReadNo().intValue(); //文章id
			int rightNum = 0; //正确答案数量
			List<Integer> answers  = new ArrayList<Integer>(); //所有答案
			
			for (ReadQuestionData readQuestionData : result.getQuestions()) {
				if (readQuestionData.getSelectedIndex() == -1) {
					Toast.makeText(AtyReadQuestion.this, "尚有问题完成", Toast.LENGTH_SHORT).show();
					return;
				}
				if (readQuestionData.getRightIndex() != readQuestionData.getSelectedIndex() + 1) {
					checkResult = false;
				}
				else {
					rightNum++;
				}
				answers.add(readQuestionData.getSelectedIndex() + 1); // 传递答案位置 从1开始 故+1
			}
			showLoading();
			new TaskReqReadResult(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					AtyReadQuestionCheck.startAty(AtyReadQuestion.this, checkResult);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}

			}, readNo, rightNum, answers);
			
//			AtyReadQuestionCheck.startAty(AtyReadQuestion.this, checkResult);
			break;
		default:
			break;
		}
	}

	private void bindingDatas() {
		ids = new ArrayList<Long>();
		translateWordIds = new HashSet<Long>();
		notPassWords = new HashSet<IWord>();
		result = GlobalRes.getInstance().getBeans().getReadData();
		
		List<ReadQuestionData> readQuestionDatas = result.getQuestions();
		
		ReadAnswer.CallbackClick callbackClick = new ReadAnswer.CallbackClick() {
			
			@Override
			public void refreshButton() {
				for (ReadQuestionData readQuestionData : result.getQuestions()) {
					if (readQuestionData.getSelectedIndex() == -1) {
						return;
					}
				}
				TextView tv = (TextView) findViewById(R.id.txtSubmit);
				tv.setBackgroundResource(R.drawable.read_big_green);
//				Toast.makeText(AtyReadQuestion.this, tv.getText(), Toast.LENGTH_LONG).show();
			}
		};
		for (int i = 0; i < readQuestionDatas.size(); i++) {
			((LinearLayout) findViewById(R.id.llQuestionContainer)).addView(
					new ReadQuestion(this, i, readQuestionDatas.get(i), callbackClick), i);
		}
		
		
		if (!TextUtils.isEmpty(result.getTitle())) {
			txtTitle.setText(result.getTitle());
			txtTitle.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(result.getImg())) {
			GlobalRes.getInstance().displayBitmap(new DisplayBitmapParam(result.getImg(), imgBg));
			imgBg.setVisibility(View.VISIBLE);
		}
		
		
//		Pattern pattern = Pattern.compile("[a-zA-Z-/']+\\[(\\d)+(-\\d)?\\]");
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_WORD_PART + "(\\[(\\d)+(-\\d)?\\])?");
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
		
//		ReadWordData readWord = null;
//		for (Long id : data.getUserWordMap().keySet()) {
//			if (data.getUserWordMap().get(id).isEnable()) {
//				readWord = data.getWordMap().get(id);
//				if (null != readWord) {
//					notPassWords.add(readWord);
//					translateWordIds.add(readWord.getId());
//				}
//			}
//		}
		
	}

	private void initActicleView() {
		ReadContentText text = null;
		for (ReadContentData content : contents) {
			text = new ReadContentText(this);
			text.getTxt().setText(content.getContent2(), BufferType.SPANNABLE);
			// getEachWord(txt);

			text.getTxt().setMovementMethod(LinkMovementMethod.getInstance());
			text.getTxt().setTextSize(20);
			text.getTxt().setTextColor(Color.BLACK);
			int dp10 = DensityUtils.dip2px(this, 10);
			LinearLayout.LayoutParams lp = ViewUtils.setLinearMargins(text, 
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, dp10, dp10, dp10, 0);
			text.setLayoutParams(lp);
			llContainer.addView(text);
		}
	}
	
	private void refreshContent() {
		Pattern pattern = Pattern.compile(ReadPattern.PATTERN_WORD_PART + "\\[(\\d)+(-\\d)?\\]");
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
				id = ids.get(index++);
				if (id == -1 || null == (data = result.getWordMap().get(id))) {
					continue;
				}
//				String word = matcher.group();
				ClickableSpan cs = new WordClickableSpan(data);
				ssb.setSpan(cs, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (null != selectedId && selectedId == id.longValue()) {
					ssb.setSpan(new ForegroundColorSpan(0xFFFF9C00),
							matcher.start(), matcher.end(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					continue;
				}
				if (translateWordIds.contains(id.longValue())) {
					ssb.setSpan(new ForegroundColorSpan(0xFF5CBCB3), //原来代码是Color.GREEN
							matcher.start(), matcher.end(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
//			pattern = Pattern.compile("[\u4e00-\u9fa5（）；]+");
			pattern = Pattern.compile(ReadPattern.PATTERN_STRING_TRANSLATION);
			matcher = pattern.matcher(showContent);
			while (matcher.find()) {
				ssb.setSpan(new AbsoluteSizeSpan(24), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.setSpan(new ForegroundColorSpan(Color.GRAY),
						matcher.start(), matcher.end(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			text.setText(ssb);
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

		@Override
		public void onClick(View widget) {
			if (translateWordIds.contains(wordData.getReadNo().longValue())) {
				translateWordIds.remove(wordData.getReadNo());
				selectedId = null;
			} 
			else {
				selectedId = wordData.getReadNo();
				if (!notPassWords.contains(wordData))
					notPassWords.add(wordData);
				translateWordIds.add(wordData.getReadNo());
				
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
			refreshContent();
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
		context.startActivity(new Intent(context, AtyReadQuestion.class));
	}

	
	/******************************************************************************************************************************************/
	
 /*************************************************************************************************************************************************/
    
    /**
     * 程序是否在前台运行
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
                return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
            }
        }
        return false;
    }

	@Override
	public void onScreenOn() {
	}

	@Override
	public void onScreenOff() {
	}

	@Override
	public void onUserPresent() {
	}
    
	protected void pauseMusic() {  
	}
	
	
	/**********************************************************************************************************************/
	
	public void showLoading() {
    	showLoading(false);
    }
    
    public void showLoading(boolean cancelable) {
    	if (null == loadingDialog) {
	    	loadingDialog = new DialogLoadingFragment(cancelable, this);
    	}
    	if (loadingDialog.isVisible() || loadingDialog.isAdded()) {
    		return;
    	}
    	showLoadingDialog();
	}
    
    public void showLoading(String loadingInfo) {
    	showLoading(loadingInfo, false);
    }
    
    public void showLoading(String loadingInfo, boolean cancelable) {
    	if (null == loadingDialog) {
    		loadingDialog = new DialogLoadingFragment(loadingInfo, cancelable, this);
    	}
    	if (loadingDialog.isVisible() || loadingDialog.isAdded()) {
    		return;
    	}
    	showLoadingDialog();
    }
    
    private void showLoadingDialog() {
    	loadingDialog.showDialog(getSupportFragmentManager());
		if (isRunning)
			loadingDialog.onResumeFragments();
    }
    
    public void hideLoading() {
    	if (null == loadingDialog) {
    		return;
    	}
    	loadingDialog.hideDialog();
    	loadingDialog = null;
	}
    
    
    @Override
    protected void onPostResume() {
    	super.onPostResume();
    	isRunning = true;
    	if (null != loadingDialog) {
    		loadingDialog.onResumeFragments();
    	}
    }
    
    public boolean isLoading() {
    	return null != loadingDialog && loadingDialog.isAdded() && loadingDialog.isVisible();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != loadingDialog && loadingDialog.isAdded() && loadingDialog.isCancelable()) {
				hideLoading();
				return true;
			}
			if (onBack()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    
    public boolean onBack() {
		return false;
    }
    
    
    
}