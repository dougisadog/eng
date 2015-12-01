package com.shuangge.english.view.read;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackDownloadForPage;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackLessonRes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.lesson.LessonFragment;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadNewWordsResult;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.UserNewWordsData;
import com.shuangge.english.game.llk.AtyLLK;
import com.shuangge.english.network.read.TaskReqReadNotPassedWords;
import com.shuangge.english.network.read.TaskReqReadProgress;
import com.shuangge.english.support.app.ScreenObserver;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.IOralEvalSDKMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.support.utils.Utility;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.component.dialog.DialogConfirm3BtnFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.lesson.AtyFailResult;
import com.shuangge.english.view.lesson.AtySuccessResult;
import com.shuangge.english.view.lesson.component.LessonPageContainer;
import com.shuangge.english.view.read.adapter.AdapterReadLesson;
import com.shuangge.english.view.read.component.ReadLessonViewPager;
import com.shuangge.english.view.read.fragment.BaseLessonType;
import com.shuangge.english.view.read.fragment.BaseLessonType.CallbackLessonType;
import com.shuangge.english.view.read.fragment.ReadLessonType01;
import com.shuangge.english.view.read.fragment.ReadLessonType02;

public class AtyReadLesson extends AbstractAppActivity implements OnClickListener, LessonPageContainer.CallbackPage, CallbackLessonType, CallbackNoticeView<Void, Boolean> {
	
	public static final int REQUEST_READ_LESSON = 1090;

	/***************************************************************************************************************************************/
	/**
	/**  //TODO:课程数据
	/**
	/***************************************************************************************************************************************/
	
	private ReadLessonWord[] readLessonWords;
	
	public static class ReadLessonWord implements IWord {
		
		public static final int TYPE_01 = 1;
		public static final int TYPE_02 = 2;
		
		private Long id;
		private String word;
		private String imgUrl;
		private int type = TYPE_01;
		private String soundUrl;
		private String mnemonics;
		private int frequency;
		
		public String getWord() {
			return word;
		}
		
		public void setWord(String word) {
			this.word = word;
		}
		
		public int getType() {
			return type;
		}
		
		public void setType(int type) {
			this.type = type;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getSoundUrl() {
			return soundUrl;
		}

		public void setSoundUrl(String soundUrl) {
			this.soundUrl = soundUrl;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getMnemonics() {
			return mnemonics;
		}

		public void setMnemonics(String mnemonics) {
			this.mnemonics = mnemonics;
		}

		public int getFrequency() {
			return frequency;
		}

		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}
		
		@Override
		public int compareTo(IWord another) {
			int result = another.getFrequency() - frequency;
			if (result > 0)
				return 1;
			if (result < 0)
				return -1;
			return 0;
		}

		@Override
		public String getTranslation() {
			return "";
		}
		
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:UI
	/**
	/***************************************************************************************************************************************/
	
	private ReadLessonViewPager viewPager;
	private LessonPageContainer pageContainer;
	private RelativeLayout rlBg;
	private RatingBar rbHeart;
	private TextView txtScore, txtPath;
	private ImageView btnBack, imgScore, imgBg;
	
	private BaseLessonType currentFragment;
	private BaseLessonType waitingFragment;
	
	private AdapterReadLesson adapter;
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:页面数据
	/**
	/***************************************************************************************************************************************/
	
	private int maxHeartNum = 0;
	private int currentHeartNum = 0;
	private int currentScore = 0;

	private int currentPageNo = 0;
	private int finishedPageNo = -1;
	private int finishedItemNo = 0; 

	private static final int STATE_STOP = 0;
	private static final int STATE_PREPARE = 1;
	private static final int STATE_START = 2;
	
	private int fragmentState = STATE_PREPARE;
	
	private boolean initialized = false;
	private boolean canStart = false;
	private boolean isResume = false;
	
	private Set<String> currentWordsMap;    //当前一组连连看的单词
	private Set<String> currentTranslationMap; //当前一组连连看的翻译
	
	private Integer[] result; //答题结果
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:onCreate onDestroy
	/**
	/***************************************************************************************************************************************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		IOralEvalSDKMgr.getInstance().stop();
		MediaPlayerMgr.getInstance().destoryMp();
		currentFragment = null;
		waitingFragment = null;
		viewPager = null;
		pageContainer = null;
		rlBg = null;
		adapter = null;
		starting = false;
	}
	
	public void toFinish() {
		GlobalResTypes.getInstance().stopDownloadForPage();
		MediaPlayerMgr.getInstance().destoryMp();
		clearResForCurrentPage();
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK , "MyTag");
		GlobalApp.getInstance().getObserver().requestScreenStateUpdate(this, this);
		wl.acquire();  
		
		if (null != viewPager && viewPager.isMoving()) {
			startFramgent(false);
		}
		
		if (isResume)
			finishSettingDialog();
		
		isResume = true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK , "MyTag");
		GlobalApp.getInstance().getObserver().stopScreenStateUpdate();
		wl.setReferenceCounted(false);
		wl.release(); 
		
		if (null != currentFragment)
			currentFragment.pause();
	}
	
	private void reset() {
		isResume = false;
		finishedItemNo = 0;
		currentPageNo = 0;
		finishedPageNo = -1;
		
		currentHeartNum = maxHeartNum;
		
		refreshHeart();
		currentScore = 0;
		refreshScore();
		
		LessonFragment.resetDatas();
		
		pageContainer.setCanClickedPageNo(0);
		pageContainer.refreshAllPageInfo();
		pageContainer.gotoPageNo(currentPageNo);
	}
	
	/**
	 * 检测是否有重复单词 包括多义词 和同义词
	 * @param word
	 * @return
	 */
	private boolean checkOverLapsWord(IWord word) {
		if (!currentWordsMap.contains(word.getWord()) && !currentTranslationMap.contains(word.getTranslation().trim())) {
			currentWordsMap.add(word.getWord());
			currentTranslationMap.add(word.getTranslation().trim());
			return true;
		}
		return false;
		
	}
	
	/**
	 * 根据位置 获取 所属页码（从 1开始 默认 4个单词一组  8个位置 为1页）
	 * @param totalSize
	 * @return
	 */
	private int getPageSize(int index) {
		if (index == 0) return 1;
		return (int)Math.ceil((float)index / 8);
	}
	
	/**
	 * 智能排序 将同义词 多义词 放入队列末尾
	 * @param 需要排序的队列
	 */
	private void wordsSmartSort (Set<IWord> notpassword) {
		Iterator<IWord> wordDatas = notpassword.iterator();
		int i = 0;
		List<IWord> currentWord = new ArrayList<IWord>();  //当前一组的
		List<IWord> overLapsWords = new ArrayList<IWord>();  //重复集合
		int size = 4;
		boolean flag = false;
		Set<IWord> correctWords = new TreeSet<IWord>(); //结果集
		while (wordDatas.hasNext()) {
			if (i % size == 0 && i != 0 && !flag) {
				i += size;
				if (readLessonWords.length - i < size * 2) {
					size = (readLessonWords.length - i) >> 1;
					flag = true;
				}
			}
			if (currentWord.size() >= size) {
				currentWord.clear();
				currentWordsMap.clear();
				currentTranslationMap.clear();
			}
			IWord word = wordDatas.next();
			correctWords.add(word);
			
			if (!checkOverLapsWord(word)) {
				overLapsWords.add(word);
				continue;
			}
			else {
				currentWord.add(word);
			}
			i++;
		}
		correctWords.addAll(currentWord);
		notpassword = correctWords;
	}
	
	private void initUIState() {
		setContentView(R.layout.aty_read_lesson);
		learnType = getIntent().getIntExtra(LEARN_TYPE, LEARNTYPE_DEFAULT);
		
		rlBg = (RelativeLayout) findViewById(R.id.rlBg);
		btnBack = (ImageView) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		txtPath = (TextView) findViewById(R.id.txtPath);
		rbHeart = (RatingBar) findViewById(R.id.rbHeart);
		rbHeart.setVisibility(View.INVISIBLE);
		txtScore = (TextView) findViewById(R.id.txtScore);
		txtScore.setVisibility(View.INVISIBLE);
		imgScore = (ImageView) findViewById(R.id.imgScore);
		imgScore.setVisibility(View.INVISIBLE);
		
		imgBg = (ImageView) findViewById(R.id.imgBg);
		imgBg.setVisibility(View.GONE);
		
		pageContainer = (LessonPageContainer) findViewById(R.id.llPageContainer);
		pageContainer.setCallback(this);
		
		viewPager = (ReadLessonViewPager) findViewById(R.id.vp);
		adapter = new AdapterReadLesson(getSupportFragmentManager());
		viewPager.setOnPageChangeListener(onPageChangeListener);
		viewPager.setOffscreenPageLimit(1);
		txtPath.setText("单词学习");
	}
	
	private void initWords() {
		int wordSize = 0;
		
//		Iterator<IWord> wordDatas = GlobalRes.getInstance().getBeans().getNotPassWordsForRead().iterator();
		Set<IWord> wds = null; 
		int size = 1;
		wds = new HashSet<IWord>();
		if (learnType != LEARNTYPE_DEFAULT) {
			//TODO 数据源
			int maxWordSize = 8;
			
			ReadNewWordsResult result = GlobalRes.getInstance().getBeans().getReadNewWordsResult();
			
			if (null == result) return;
			List<UserNewWordsData> freshWords = new ArrayList<UserNewWordsData>();
			//防止内存覆盖
			if (learnType == LEARNTYPE_WRONG_WORDS) {
				freshWords = result.getDatas();
			}
			if (learnType == LEARNTYPE_WRONG_WORDS_RETRY) {
				freshWords = result.getCacheDatas();
			}
			if (freshWords.size() == 0) {
				finish();
				return;
			}
			wordSize = Math.min(maxWordSize, freshWords.size());
			for (int i = 0; i < wordSize; i++) {
				wds.add((IWord) freshWords.get(i));
			}

		} else {
			//若一个单词未选 则使用核心单词补充
			if (GlobalRes.getInstance().getBeans().getNotPassWordsForRead().size() == 0) {
				wds = GlobalRes.getInstance().getBeans().getReadData().getCoreWords();
			}
			else {
				wds = GlobalRes.getInstance().getBeans().getNotPassWordsForRead();
			}
			wordSize = wds.size();
		}
		size = wordSize < 4 ? wordSize : 4;
		readLessonWords = new ReadLessonWord[wordSize * 2];
		wordsSmartSort(wds);
		Iterator<IWord> wordDatas = wds.iterator();
		ReadLessonWord readData = null;
		IWord wordData = null;
		int i = 0;
		boolean flag = false;
		
		while (wordDatas.hasNext()) {
			if (i % size == 0 && i != 0 && !flag) {
				i += size;
				if (readLessonWords.length - i < size * 2) {
					size = (readLessonWords.length - i) >> 1;
					flag = true;
				}
			}
			wordData = wordDatas.next();
			readData = new ReadLessonWord();
			readData.id = wordData.getId();
			readData.word = wordData.getWord();
			readData.imgUrl = wordData.getImgUrl();
			readData.soundUrl = wordData.getSoundUrl();
			readData.type = ReadLessonWord.TYPE_01;
			readData.mnemonics = wordData.getMnemonics();
			readData.frequency = wordData.getFrequency();
			readLessonWords[i] = readData;
			readData = new ReadLessonWord();
			readData.id = wordData.getId();
			readData.word = wordData.getWord();
			readData.imgUrl = wordData.getImgUrl();
			readData.soundUrl = wordData.getSoundUrl();
			readData.type = ReadLessonWord.TYPE_02;
			readData.mnemonics = wordData.getMnemonics();
			readData.frequency = wordData.getFrequency();
			readLessonWords[i + size] = readData;
			++i;
		}
	}
	
	protected void initData() {
		super.initData();
		initUIState();
		
		SoundPoolMgr.getInstance();
		currentWordsMap = new HashSet<String>();
		currentTranslationMap = new HashSet<String>();
		
		showLoading(true);
		showloading = true;
//		int wordSize = GlobalRes.getInstance().getBeans().getNotPassWordsForRead().size();

		
		initWords();
		currentPageNo = 0;
		//获取页状态
//		loadAllRes();
		if (readLessonWords.length/2 < 4 && learnType == LEARNTYPE_DEFAULT) {
			loadCoreWords();
		}
		else {
			loadAllRes();
		}
		result = new Integer[readLessonWords.length];
	}
	
	
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:loadAllRes
	/**
	/***************************************************************************************************************************************/
	private IWord[] Iword;
	
	private void loadCoreWords() {
		if (null == Iword || Iword.length == 0) {
			Iword = new IWord[4];
			Set<IWord> coreWords = GlobalRes.getInstance().getBeans().getReadData().getCoreWords();
			int i = 0;
			for (IWord iWord2 : coreWords) {
				if (i < 4) {
					Iword[i] = iWord2;
					i++;
				}
				else break;
			}
		}
		
		if (!Utility.isConnected(this)) {
			resReloadDialog(0);
			return;
		}
	GlobalResTypes.getInstance().startDownloadForWords(Iword, new CallbackDownloadForPage() {
			
			@Override
			public void finish(int fragmentNo) {
				loadAllRes();
			}
			
			@Override
			public void error(int fragmentNo) {
				hideLoading();
			}
			
			@Override
			public void configComplete(File file) {
			}
			
			@Override
			public void complete(int fragmentNo) {
			}
			
		});
	}
	private void loadAllRes() {
		if (null == pageContainer) {
			return;
		}
		
		if (!Utility.isConnected(this)) {
			resReloadDialog(1);
			return;
		}
		GlobalResTypes.getInstance().startDownloadForWords(readLessonWords, new CallbackDownloadForPage() {
			
			@Override
			public void finish(int fragmentNo) {
				if (null == pageContainer) {
					return;
				}
				showloading = false;
				hideLoading();
//				pageContainer.setCompleteAllPage();  
				
				//TODO:默认开启所有下载完成页的可点击跳转  正式时注释掉
//				pageContainer.setCanClickedPageNo(getPageSize(fragmentNo));
			}
			
			@Override
			public void error(int fragmentNo) {
				if (null == pageContainer) {
					return;
				}
				hideLoading();
			}
			
			@Override
			public void configComplete(File file) {
			}
			
			@Override
			public void complete(int fragmentNo) {
				if (null == pageContainer) {
					return;
				}
				if (!initialized) {
					initHeart();
					initScore();
					pageContainer.setPageNum(getPageSize(readLessonWords.length));
					pageContainer.setCanClickedPageNo(0);
				}
				initialized = true;
				pageContainer.setCompletePage(getPageSize(fragmentNo));
				if (!canStart) {
					canStart = true;
					showloading = false;
					hideLoading();
					pageContainer.gotoPageNo(currentPageNo);
				}
			}
			
		});
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:page change
	/**
	/***************************************************************************************************************************************/
	
	@Override
	public void onPageSelected(int pageNo) {
		this.currentPageNo = pageNo;
		pageContainer.setTouchable(false);
		stopFramgent();
		loadResForCurrentPage();
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  prepare load one page bitmaps
	/**
	/***************************************************************************************************************************************/
	
	/**
	 * 加载当前页资源
	 */
	private void loadResForCurrentPage() {
//		clearResForCurrentPage();
		List<LessonFragment> lessonFragments = LessonFragment.LESSON_PAGES.get(viewPager.getCurrentItem());
		GlobalResTypes.getInstance().prepareLoadLessonBitmaps(callbackLessonRes, lessonFragments.subList(0, 1));
		callbackLessonRes.onComplete();
	}
	
	/**
	 * 清除当前资源
	 */
	private void clearResForCurrentPage() {
		GlobalResTypes.getInstance().clearLessonBitmaps();
	}
	
	/**
	 * @param currentIndex 当前单词在数组中位置
	 * @param readLessonWords 单词数组
	 * @return 该单词所属的单词  组
	 */
	private Set<IWord> getMyGroupWords(int currentIndex , ReadLessonWord[] readLessonWords){
		Set<IWord> myGroupWords = new HashSet<IWord>();
		if (readLessonWords.length < 8) {
			for (int i = readLessonWords.length/2; i < readLessonWords.length; i++) {
				myGroupWords.add(readLessonWords[i]);
			}
			return myGroupWords;
		}
//		String m = "";
		
		//处于正常8个一组范围内
		if (currentIndex < readLessonWords.length - readLessonWords.length%8) {
			myGroupWords.add(readLessonWords[currentIndex]);
			List<ReadLessonWord> arr = Arrays.asList(readLessonWords);
			int index = arr.indexOf(readLessonWords[currentIndex]);
			
			int myGroupWordsIndex = (index + 1)%8 - 1; //当前单词 在该组中的位置4~7
			if (myGroupWordsIndex == -1) {     //当前单词为该组最后一个时 用于 匹配循环条件
				myGroupWordsIndex = 7;
			}
			else if (myGroupWordsIndex < 4) { //数据出错 type2应归属readLessonWords 的后半段
				DebugPrinter.e("单词" + readLessonWords[currentIndex].getWord() + "相对于所属单词组位置不合法");
				return myGroupWords;
			}
			for (int i = currentIndex - myGroupWordsIndex + 4; i < currentIndex - myGroupWordsIndex + 8 && i < readLessonWords.length; i++) {
				myGroupWords.add(readLessonWords[i]);
//				m = m + readLessonWords[i].getWord()+ " ";
			}
		}
		//处于末位
		else {
			int leftAll = readLessonWords.length - readLessonWords.length%8; //包括type01 type02 残余的总数量
			for (int i = leftAll + (readLessonWords.length%8)/2; i < readLessonWords.length; i++) {
				myGroupWords.add(readLessonWords[i]);
			}
		}
//		System.out.println(m);
		return myGroupWords;
	}
	
	private CallbackLessonRes callbackLessonRes = new CallbackLessonRes() {
		
		@Override
		public void onComplete() {
			if (readLessonWords.length == 0) return;
			final List<BaseLessonType> datas = new ArrayList<BaseLessonType>();
//			final ReadLessonWord wordData = readLessonWords[currentPageNo];
			for (int i = currentPageNo * 8; i < Math.min(currentPageNo * 8 + 8, readLessonWords.length); i++) {
				if (readLessonWords[i].type == ReadLessonWord.TYPE_01) {
					datas.add(new ReadLessonType01(readLessonWords[i], AtyReadLesson.this));
				}
				else {
					if (learnType == LEARNTYPE_DEFAULT) {
						datas.add(new ReadLessonType02(readLessonWords[i], AtyReadLesson.this));
					}
					//错题本进入
					else {
//						datas.add(new ReadLessonType02(readLessonWords[i], AtyReadLesson.this , getMyGroupWords(i, readLessonWords)));
						List<UserNewWordsData> userNewWordsDatas = GlobalRes.getInstance().getBeans().getReadNewWordsResult().getDatas();
						Set<IWord> words = new HashSet<IWord>(userNewWordsDatas.size());
						words.addAll(userNewWordsDatas);
						datas.add(new ReadLessonType02(readLessonWords[i], AtyReadLesson.this , words));
					}
				}
			}
			
			adapter.setDatas(getSupportFragmentManager(), datas);
			viewPager.setAdapter(adapter);
			
			stopFramgent();
			prepareFramgent();
			startFramgent(true);
			
			pageTouchable();
		}

	};
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:framgent change prepareFramgent startFramgent stopFramgent
	/**
	/***************************************************************************************************************************************/
	
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			stopFramgent();
			prepareFramgent();
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE && !ScreenObserver.isScreenLocked(AtyReadLesson.this)) {
				viewPager.setMoving(false);
				startFramgent(false);
			}
		}
	};
	
	private void stopFramgent() {
		if (null != currentFragment) {
			currentFragment.stop();
		}
		fragmentState = STATE_STOP;
	}
	
	private void prepareFramgent() {
		waitingFragment = adapter.getDatas().get(viewPager.getCurrentItem());
		fragmentState = STATE_PREPARE;
	}
	
	private void startFramgent(boolean first) {
		if (first) {
			finishedItemNo = 0;
		}
		viewPager.setCanMoveItemNo(finishedItemNo);
//		if (pageContainer.getCanClickedPageNo() > currentPageNo) {
//			viewPager.setCanMoveItemNo(currentPageNo);
//		}
		if (fragmentState == STATE_PREPARE && null != waitingFragment) {
			if (null != currentFragment && !first) {
				currentFragment.reset();
			}
			currentFragment = waitingFragment;
			currentFragment.start();
			fragmentState = STATE_START;
		}
	}

	private DialogConfirmFragment resReloadDialog; //资源未下载完成的弹出dialog
	
	private void resReloadDialog(final int type) {
		hideLoading();
		if (null == resReloadDialog) {
			GlobalResTypes.getInstance().stopDownloadForWords();
			resReloadDialog = new DialogConfirmFragment(new DialogConfirmFragment.CallBackDialogConfirm() {
				@Override
				public void onSubmit(int position) {
					if (null == resReloadDialog) {
						return;
					}
					resReloadDialog.dismiss();
					resReloadDialog = null;
					if (null != currentFragment) {
						currentFragment.resume();
					}
					if (type == 0) {
						loadCoreWords();
					}
					else if (type == 1) {
						loadAllRes();
					}
				}
				
				@Override
				public void onKeyBack() {
					finish();
				}

				@Override
				public void onCancel() {
					resReloadDialog.dismiss();
					resReloadDialog = null;
					finish();
				}
			}, getString(R.string.timeout), " ", 0);
		}
		if (resReloadDialog.isVisible()) {
			return;
		}
		resReloadDialog.setCancelable(false);
		resReloadDialog.showDialog(getSupportFragmentManager());
	}
	
	@Override
	public void onFramgentFinished(int result) {
		int totalSize = adapter.getDatas().size();
		int currentPageSize = 4; //当前页包含fragment大小
		//当为最后一页 且 不满8个时 
		if (currentPageNo == pageContainer.getPageNum() - 1) {
			if (totalSize%8 > 0) {
				currentPageSize = totalSize%8 /2;
			}
		}
		if (result ==  LessonData.RESULT_WRONG) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() - currentPageSize);
			//错误 答案 跳返回至 ReadLessonType01的 fragment 更新目标 targetStep
		    adapter.getItem(viewPager.getCurrentItem()).setTarget(BaseLessonType.TARGET_STEP);
			return;
		}
		else if (result ==  2) {
			//错误 进入 ReadLessonType01 时 next 返回 对应的ReadLessonType02 fragment 重置targetStep
			adapter.getItem(viewPager.getCurrentItem()).setTarget(BaseLessonType.DEFAULT_TARGET_STEP);
			viewPager.setCurrentItem(viewPager.getCurrentItem() + currentPageSize);
			return;
		}
		//数据
		if (finishedItemNo < viewPager.getCurrentItem() + 1) { 
			finishedItemNo = viewPager.getCurrentItem() + 1;
		}
		
		// 下一个framgent
		if (viewPager.getCurrentItem() < adapter.getDatas().size() - 1) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			return;
		}
		// 下一页
		if (++currentPageNo < pageContainer.getPageNum()) {
			if (null != pageContainer) {
				finishedItemNo = 0;
				if (currentPageNo > finishedPageNo) {
					//加分
					finishedPageNo = currentPageNo - 1;
					pageContainer.setPageInfoAndRefresh(finishedPageNo, result == 0 ? LessonPageContainer.STATE_WRONG : LessonPageContainer.STATE_RIGHT);
					requestForPass();
				}
				
				pageContainer.setCanClickedPageNo(currentPageNo);
				pageContainer.gotoPageNo(currentPageNo);
			}
			return;
		}
		currentPageNo = pageContainer.getPageNum();
		pageContainer.setPageInfoAndRefresh(currentPageNo - 1, result == 0 ? LessonPageContainer.STATE_WRONG : LessonPageContainer.STATE_RIGHT);
		requestForSuccess();
	}
	
	@Override
	public void onFirstWrong() {
		//死掉
		if (--currentHeartNum <= 0) {
			currentHeartNum = 0;
			pageContainer.setPageInfoAndRefresh(currentPageNo, LessonPageContainer.STATE_WRONG);
			requestForFail();
		}
		refreshHeart();
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  request datas 
	/**
	/***************************************************************************************************************************************/
	
	public static final int STATE_PASS = 0;
	public static final int STATE_FAIL = 1;
	public static final int STATE_SUCCESS = 2;
	
	private void requestForPass() {
	}
	
	private void requestForFail() {
		showLoading();
		stopFramgent();
		
		finish();
	}
	
	private void requestForSuccess() {
		showLoading();
		stopFramgent();
		
		if (learnType == LEARNTYPE_DEFAULT) {
			ReadResult rr = GlobalRes.getInstance().getBeans().getReadData();
			if (rr.getProgress() < ReadResult.PROGRESS_L3) {
				int readNo = GlobalRes.getInstance().getBeans().getReadData().getReadNo().intValue(); //文章id
				new TaskReqReadProgress(0, new CallbackNoticeView<Void, Boolean>() {
	
					@Override
					public void refreshView(int tag, Boolean result) {
						hideLoading();
						if (null == result || !result) {
							return;
						}
						GlobalRes.getInstance().getBeans().getReadData().setProgress(ReadResult.PROGRESS_L3);
						AtyLLK.startAty(AtyReadLesson.this);
						finish();
					}
	
					@Override
					public void onProgressUpdate(int tag, Void[] values) {
					}
	
				}, readNo ,ReadResult.PROGRESS_L3);
			}
			else {
				AtyLLK.startAty(AtyReadLesson.this);
				finish();
			}
			
		}
		else {
			//TODO 返回结果
			Integer[] m = result;
			int rightNum = 0;
			int wrongNum = 0;
			List<Integer> passedWordIds = new ArrayList<Integer>();
			for (int i = 0; i < m.length; i++) {
				if (readLessonWords[i].getType() == 2) {
					//TODO 
					passedWordIds.add(readLessonWords[i].getId().intValue());
					if (null == m[i]) {
//						hideLoading();
//						Toast.makeText(this, "题目完成不完全", Toast.LENGTH_LONG).show();
//						return;	
					}
					else if (m[i] == 1) {
						rightNum++;
					}
					else if (m[i] == 2) {
						wrongNum++;
					}
				}
			}
			final int rightRate = 100 * rightNum/ (rightNum + wrongNum);
			
			new TaskReqReadNotPassedWords(0, new CallbackNoticeView<Void, Boolean>() {
				
				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					AtyReadSuccessResult.startAty(AtyReadLesson.this, rightRate);
					finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, rightRate, passedWordIds);
			
		}
	}
	
	@Override
	public void refreshView(int tag, Boolean result) {
		hideLoading();
		if (null == result || !result) {
			//TODO:显示给用户 是否要再请求一次
			return;
		}
		switch (tag) {
		case STATE_FAIL:
			//失败
			startActivityForResult(new Intent(this, AtyFailResult.class), 0);
			break;
		case STATE_SUCCESS:
			//成功
			startActivityForResult(new Intent(this, AtySuccessResult.class), 0);
			break;
		default:
			break;
		}
	}

	@Override
	public void onProgressUpdate(int tag, Void[] values) {
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			reset();
		}
		else if (resultCode == 2) {
			setResult(1);
			toFinish();
		}
		else {
			setResult(0);
			toFinish();
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  score & heart
	/**
	/***************************************************************************************************************************************/
	
	private void initScore() {
		currentScore = 0;
		refreshScore();
//		txtScore.setVisibility(View.VISIBLE);
//		imgScore.setVisibility(View.VISIBLE);
	}
	
	private void refreshScore() {
		txtScore.setText(currentScore + "");
	}
	
	private void initHeart() {
		currentHeartNum = maxHeartNum = 10;
		rbHeart.setNumStars(5);
		rbHeart.setStepSize(0.5f);
		refreshHeart();
//		rbHeart.setVisibility(View.VISIBLE);
	}
	
	private void refreshHeart() {
		rbHeart.setRating(getCurrentHeartNum());
	}
	
	private float getCurrentHeartNum() {
		return (maxHeartNum - currentHeartNum) * rbHeart.getStepSize();
	}

	/***************************************************************************************************************************************/
	/**
	/**  right or wrong or waiting
	/**
	/***************************************************************************************************************************************/
	
	
	public void pageTouchable() {
		handler.removeCallbacks(pageTouchableR);
		handler.postDelayed(pageTouchableR, 500);
	}
	
	private Runnable pageTouchableR = new Runnable() {
		
		@Override
		public void run() {
			if (null != pageContainer) {
				pageContainer.setTouchable(true);
			}
		}
		
	};
	
	/***************************************************************************************************************************************/
	/**
	/**  right or wrong or waiting
	/**
	/***************************************************************************************************************************************/
	
	private Handler handler = new Handler();
	
	public void rightHandler() {
		rightBg();
		handler.removeCallbacks(r);
		handler.postDelayed(r, 1000);
		
		if (null == result[viewPager.getCurrentItem()]) {
			result[viewPager.getCurrentItem()] = 1;
		}
	}
	
	public void wrongHandler() {
		wrongBg();
		handler.removeCallbacks(r);
		handler.postDelayed(r, 1000);
		
		result[viewPager.getCurrentItem()] = 2;
	}
	
	private Runnable r = new Runnable() {
		
		@Override
		public void run() {
			resetBg();
		}
		
	};
	
	private void resetBg() {
		setResToBg(R.drawable.bg_lesson_blue);
	}
	
	private void rightBg() {
		setResToBg(R.drawable.bg_lesson_green);
	}
	
	private void wrongBg() {
		setResToBg(R.drawable.bg_lesson_red);
	}
	
	private void setResToBg(int resId) {
		if (null != rlBg) {
			try {
				rlBg.setBackgroundResource(resId);
			}
			catch (OutOfMemoryError e) {
				DebugPrinter.e(e.getMessage());
			}
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  onScreenOff onScreenOn
	/**
	/***************************************************************************************************************************************/
	
	@Override
	public void onScreenOff() {
		super.onScreenOff();
		if (null != currentFragment && currentFragment.isRunning() && ScreenObserver.isScreenLocked(this)) {
			currentFragment.pause();
		}
	}
	
	@Override
	public void onScreenOn() {
		super.onScreenOn();
//		if (null != currentFragment && !currentFragment.isDestroy() && !ScreenObserver.isScreenLocked(this)) {
//			currentFragment.resume();
//		}
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			showDialog();
			break;
		}
	}
	
	@Override
	public boolean onBack() {
		showDialog();
		return true;
	}
	
	private DialogConfirm3BtnFragment exitDialog;
	
	private boolean showloading = false;
	
	private void showDialog() {
		if (null == exitDialog) {
			exitDialog = new DialogConfirm3BtnFragment(new DialogConfirm3BtnFragment.CallBackDialogConfirm() {
				
				@Override
				public void onBtn3() {
					hideDialog();
					setResult(0);
					toFinish();
				}
				
				@Override
				public void onBtn1() {
					hideDialog();
					if (!canStart && showloading) {
						showLoading(true);
					}
				}
				
				@Override
				public void onBtn2() {
//					showLoading();
					reset();
					hideDialog();
					/*
					new TaskReqResetLesson(0, new CallbackNoticeView<Void, Boolean>() {

						@Override
						public void refreshView(int tag, Boolean result) {
							hideDialog();
							hideLoading();
							if (null == result || !result) {
								return;
							}
							reset();
						}

						@Override
						public void onProgressUpdate(int tag, Void[] values) {
						}
						
					});
					*/
				}

				@Override
				public void onKeyBack() {
					onBtn1();
				}
				
			}, getString(R.string.lessonTip1En),  getString(R.string.lessonTip1Cn), R.style.DialogTopToBottomTheme);
		}
		if (exitDialog.isVisible()) {
			return;
		}
		exitDialog.showDialog(getSupportFragmentManager());
	}

	private void hideDialog() {
		if (null == exitDialog) {
			return;
		}
		exitDialog.dismiss();
		exitDialog = null;
	}
	
	
	
	
	public boolean isMoving() {
		return null != viewPager && viewPager.isMoving();
	}
	
	/************************************************************************************************/
	
	private static int learnType = 0;
	public static int LEARNTYPE_DEFAULT = 0;
	public static int LEARNTYPE_WRONG_WORDS = 1;
	public static int LEARNTYPE_WRONG_WORDS_RETRY = 2;
	private static String LEARN_TYPE = "learnType";
	
	private static boolean starting = false;
	
	public static void startAty(Activity context) {
		if (starting) {
			return;
		}
		starting = true;
		Intent i = new Intent(context, AtyReadLesson.class);
		context.startActivityForResult(i, REQUEST_READ_LESSON);
	}
	
	/**
	 * 开启Activity
	 * @param context 上下文
	 * @param learnType 0为默认  1为通过单词列表进入学习（正常数据） 2为单词列表retry（旧数据）
	 */
	public static void startAty(Activity context,int learnType) {
		if (starting) {
			return;
		}
		starting = true;
		Intent i = new Intent(context, AtyReadLesson.class);
		i.putExtra(LEARN_TYPE, learnType);
		context.startActivityForResult(i, REQUEST_READ_LESSON);
	}
	
	/************************************************************************************************/
	
	private DialogAlertFragment finishDialog;
	
	private void finishSettingDialog() {
		if (null == finishDialog) {
			finishDialog = new DialogAlertFragment(new DialogAlertFragment.CallBackDialogConfirm() {
				@Override
				public void onSubmit(int position) {
					if (null == finishDialog) {
						return;
					}
					finishDialog.dismiss();
					finishDialog = null;
					currentFragment.resume();
				}
				
				@Override
				public void onKeyBack() {
					
				}
			}, getString(R.string.settingTip), " ", 0);
		}
		if (finishDialog.isVisible()) {
			return;
		}
		finishDialog.setCancelable(false);
		finishDialog.showDialog(getSupportFragmentManager());
	}
	
	
	/************************************************************************************************/
	
}
