package com.shuangge.english.view.lesson;

import java.io.File;
import java.util.List;
import java.util.Locale;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.yunzhisheng.oraleval.sdk.IOralEvalSDK.Error;
import cn.yunzhisheng.oraleval.sdk.IOralEvalSDK.OfflineSDKPreparationError;

import com.meetme.android.horizontallistview.HorizontalListView;
import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityLessonDataCache;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackDownloadForPage;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.lesson.LessonFragment;
import com.shuangge.english.entity.server.lesson.Type5Data;
import com.shuangge.english.entity.server.lesson.Type6Data;
import com.shuangge.english.network.lesson.TaskReqLessonDetails;
import com.shuangge.english.network.lesson.TaskReqPassTheLesson;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.app.ScreenObserver;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.IOralEvalSDKMgr;
import com.shuangge.english.support.utils.IOralEvalSDKMgr.CallbackIOralEvalSDK;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr.OnCompletionListener;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.RatingBarView;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.component.wave.WaveformView;
import com.shuangge.english.view.lesson.adapter.AdapterBoss;
import com.shuangge.english.view.lesson.component.LessonPageContainer;
import com.shuangge.english.view.lesson.component.OptionImg;
import com.shuangge.english.view.lesson.fragment.LessonType01_02_03_07_10_11_15;

public class AtyBoss extends AbstractAppActivity implements OnClickListener, LessonPageContainer.CallbackPage, CallbackNoticeView<Void, EntityLessonDataCache> {
	
	public static final int REQUEST_LESSON_BOSS = 1081;

	/***************************************************************************************************************************************/
	/**
	/**  //TODO:课程数据
	/**
	/***************************************************************************************************************************************/
	
	private Type5Data type5;
	private List<Type6Data> type6Datas;

	/***************************************************************************************************************************************/
	/**
	/**  //TODO:UI
	/**
	/***************************************************************************************************************************************/
	
	private RelativeLayout rlQuestionTop, rlOptions02;
	private TextView txtQuestionTop;
	private ImageView imgQuestionTopPlay;
	private HorizontalListView hlv;
	private LessonPageContainer pageContainer;
	private AdapterBoss adapter;
	private RelativeLayout rlBg;
	private RatingBar rbHeart;
	private TextView txtScore, txtPath;
	private ImageView btnBack, imgScore;
	private WaveformView wv;
	private LinearLayout llRecordResult;
	private RatingBarView rbStar;
	private TextView txtRecordStar;
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:页面数据
	/**
	/***************************************************************************************************************************************/
	
	private boolean speechEnabled;
	
	private int maxHeartNum = 0;
	private int currentHeartNum = 0;
	private int currentScore = 0;
	
	private boolean initialized = false;
	private boolean canStart = false;

	private int currentPageNo = 0;
	private int finishedPageNo = -1;
	
	protected static final int STATE_START = 1;
	protected static final int STATE_STOP = 2;
	protected static final int STATE_FINISHED = 10;
	private int state = 0; 
	
	private static final int RESULT_RIGHT = 1;
	private static final int RESULT_WRONG = 0;
	private int result = RESULT_RIGHT;
	
	//jason 要求题型流程变化 临时补丁标记
	private int patchStep = 0;
	private String currentType;
	
	private ShowPicRunnable showPicR;
	
	private SparseArray<SmallPageData> pageDatas = new SparseArray<SmallPageData>();
	private DialogConfirmFragment dialogConfirm;
	
	
	private class SmallPageData {
		
		private String types;
		private Integer rightNum;
		private Integer wrongNum;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		state = STATE_FINISHED;
		stop02();
		MediaPlayerMgr.getInstance().destoryMp();
		clearRunnable();
		if (null != adapter) {
			adapter.recycle();
			adapter = null;
		}
		if (null != prevView) {
			prevView.clearAnimation();
			((OptionImg) prevView.getChildAt(0)).recycle();
			prevView = null;
		}
		starting = false;
	}
	
	private void toFinish() {
		GlobalResTypes.getInstance().stopDownloadForPage();
		MediaPlayerMgr.getInstance().destoryMp();
		finish();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopFramgent();
		GlobalApp.getInstance().getObserver().requestScreenStateUpdate(this, this);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK , "MyTag");
		wl.acquire();   
	}
	
	@Override
	public void onResume() {
		super.onResume();
		GlobalApp.getInstance().getObserver().stopScreenStateUpdate();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK , "MyTag");
		wl.setReferenceCounted(false);
		wl.release();  
		if (state != STATE_STOP || !initialized) {
			return;
		}
		state = STATE_START;
		if (null != currentData)
			onCurrentStepStart();
	}
	
	private void reset() {
		patchStep = 0;
		
		currentPageNo = 0;
		finishedPageNo = -1;
		
		currentHeartNum = maxHeartNum;
		
		type6Datas.clear();
		
		refreshHeart();
		currentScore = 0;
		refreshScore();
		
		LessonFragment.resetDatas();
		
		pageContainer.setCanClickedPageNo(0);
		pageContainer.refreshAllPageInfo();
		pageContainer.gotoPageNo(currentPageNo);
	}

	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_lesson_boss);
		btnBack = (ImageView) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		rlBg = (RelativeLayout) findViewById(R.id.rlBg);
		rlQuestionTop = (RelativeLayout) findViewById(R.id.rlQuestionTop);
		txtQuestionTop = (TextView) findViewById(R.id.txtQuestionTop);
		imgQuestionTopPlay = (ImageView) findViewById(R.id.imgQuestionTopPlay);
		rlOptions02 = (RelativeLayout) findViewById(R.id.rlOptions02);
		rlQuestionTop.setVisibility(View.GONE);
		rlOptions02.setVisibility(View.GONE);
		wv = (WaveformView) findViewById(R.id.wv);
		wv.setVisibility(View.GONE);
		
		llRecordResult = (LinearLayout) findViewById(R.id.llRecordResult);
		llRecordResult.setVisibility(View.GONE);
		rbStar = (RatingBarView) findViewById(R.id.rbStar);
		txtRecordStar = (TextView) findViewById(R.id.txtRecordStar);
		
		txtPath = (TextView) findViewById(R.id.txtPath);
		
		rbHeart = (RatingBar) findViewById(R.id.rbHeart);
		rbHeart.setVisibility(View.INVISIBLE);
		txtScore = (TextView) findViewById(R.id.txtScore);
		txtScore.setVisibility(View.INVISIBLE);
		imgScore = (ImageView) findViewById(R.id.imgScore);
		imgScore.setVisibility(View.INVISIBLE);
		
		pageContainer = (LessonPageContainer) findViewById(R.id.llPageContainer);
		pageContainer.setCallback(this);
		hlv = (HorizontalListView) findViewById(R.id.hlv);
		hlv.setEnabled(false);
		
		SoundPoolMgr.getInstance();
		
		showLoading(true);
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		speechEnabled = beans.isSpeechEnabled();
		type5 = beans.getUnlockDatas().getType5s().get(beans.getCurrentType5Id());
		
		txtPath.setText(beans.getCurrentType5Data().getName());
		
		if (null != type5) {
			if (!TextUtils.isEmpty(type5.getType6ClientId())) {
				try {
					//TODO: 兼容老数据   页数是从1开始  所以要页数  - 1	
					currentPageNo = Integer.parseInt(type5.getType6ClientId().substring(type5.getClientId().length()));
				}
				catch (Exception e) {
					currentPageNo = 0;
				}
			}
		}
		//获取页状态
		new TaskReqLessonDetails(0, new CallbackNoticeView<Void, List<EntityLessonDataCache>>() {

			@Override
			public void refreshView(int tag, List<EntityLessonDataCache> list) {
				if (null == list) {
					hideLoading();
					toFinish();
					Toast.makeText(AtyBoss.this, R.string.lessonErrTip, Toast.LENGTH_SHORT).show();
					return;
				}
				int pageNo = 0;
				type6Datas = GlobalRes.getInstance().getBeans().getLessonDetails().getType6s();
				for (Type6Data type6Data : type6Datas) {
					pageContainer.setPageInfo(pageNo++, type6Data.getCurrentWrongNum() == 0 ? 
							LessonPageContainer.STATE_RIGHT : LessonPageContainer.STATE_WRONG);
				}
				finishedPageNo = pageNo - 1;
				if (currentPageNo > pageNo) {
					currentPageNo = pageNo;
				}
				loadAllRes();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:loadAllRes
	/**
	/***************************************************************************************************************************************/
	
	private void loadAllRes() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		final String type1Id = beans.getCurrentType1Id();
		final String type2Id = beans.getCurrentType2Id();
		final String type4Id = beans.getCurrentType4Id();
		final String type5Id = beans.getCurrentType5Id();//"10103010001";
		GlobalResTypes.getInstance().startDownloadForPage(type1Id, type2Id, type4Id, type5Id, new CallbackDownloadForPage() {
			
			@Override
			public void finish(int pageNo) {
				showloading = false;
				hideLoading();
				pageContainer.setCompleteAllPage();
				
				//TODO:debug
//				pageContainer.setCanClickedPageNo(LessonFragment.LESSON_PAGES.size());
			}
			
			@Override
			public void error(int pageNo) {
			}
			
			@Override
			public void configComplete(File file) {
			}
			
			@Override
			public void complete(int pageNo) {
				if (null == pageContainer) {
					return;
				}
				if (!initialized) {
					adapter = new AdapterBoss(AtyBoss.this, LessonFragment.LESSON_PAGES);
					hlv.setAdapter(adapter);
					if (currentPageNo >= LessonFragment.LESSON_PAGES.size()) { 
						currentPageNo = 0;
					}
					initHeart();
					initScore();
					pageContainer.setPageNum(LessonFragment.LESSON_PAGES.size());
					int num = finishedPageNo + 1;
					if (num >= LessonFragment.LESSON_PAGES.size()) {
						num = LessonFragment.LESSON_PAGES.size() - 1;
					}
					pageContainer.setCanClickedPageNo(num);
				}
				initialized = true;
				pageContainer.setCompletePage(pageNo);
				if (!canStart && (pageNo == currentPageNo + 2 || pageNo == LessonFragment.LESSON_PAGES.size() - 1)) {
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
		pageDatas.clear();
		patchStep = 0;
		pageContainer.setTouchable(false);
		stopFramgent();
		
		jump();
	}
		
	/***************************************************************************************************************************************/
	/**
	/**  //TODO:framgent change startFramgent stopFramgent
	/**
	/***************************************************************************************************************************************/
	
	private void stopFramgent() {
		state = STATE_STOP;
		MediaPlayerMgr.getInstance().stopMp();
		SoundPoolMgr.getInstance().stopSnd();
		stop02();
		clearRunnable();
	}
	
	private void startFramgent() {
		result = RESULT_RIGHT;
		state = STATE_START;
		this.pass = false;
		currentData = adapter.getItem(step);
		List<LessonData> datas = currentData.getFragment().getDatas();
		if (datas.size() > 1 && speechEnabled) {
			if (patchStep == 0 && datas.get(0).equals(currentData)) {
				patchStep = 1;
				currentData = adapter.getItem(step);
			}
			else if (patchStep == 1) {
				patchStep = 2;
				currentData = adapter.getItem(++step);
			}
			else if (patchStep == 2) {
				patchStep = 3;
				currentData = adapter.getItem(--step);
			}
			else if (patchStep == 3) {
				patchStep = 0;
				currentData = adapter.getItem(++step);
			}
		}
		currentType = currentData.getType();
		anwserStr = currentData.getAnswer().getContent();
		anwserSoundPath = currentData.getAnswer().getLocalSoundPath();
		type02Count = 0;
		
		if (null != showPicR)
			handler.removeCallbacks(showPicR);
		showPicR = new ShowPicRunnable(patchStep != 1 ? true : false);
		handler.postDelayed(showPicR, 1000);
	}

	public void onFramgentFinished() {
		if (!isRunning()) {
			return;
		}
		
		//step 是从1开始的 0为空位
		if (step >= 1 && speechEnabled) {
			//第二张
			if (patchStep == 1) {
				next(0);
				startFramgent();
				return;
			}
			if (patchStep == 2) {
				next(0);
				startFramgent();
				return;
			}
			else if (patchStep == 3) {
				buildSmallPageInfo();
				next(0);
				startFramgent();
				return;
			}
		}
		
		buildSmallPageInfo();
		
		int newPageNo = adapter.getPageNoByStep(step + 1);
		if (currentPageNo < newPageNo) {
			currentPageNo = newPageNo;
			if (currentPageNo > finishedPageNo) {
				//加分
				finishedPageNo = currentPageNo - 1;
				requestForPass(buildPageInfo(0, finishedPageNo));
			}
			pageContainer.setCanClickedPageNo(currentPageNo);
			pageContainer.setCurrentPageNo(currentPageNo);
		}
		
		//结束 最后也有一个空位
		if (step >= adapter.getCount() - 2) {
			requestForSuccess(buildPageInfo(1, currentPageNo));
			return;
		}
		
		++step;
		int distance = adapter.getDistanceByStep(step);
		next(distance);
		startFramgent();
	}
	
	private void buildSmallPageInfo() {
		String type = currentData.getType().toUpperCase(Locale.getDefault());
		if (finishedPageNo < currentPageNo) { 
			int rightNum = 1;
			int wrongNum = 0;
			if (result == RESULT_WRONG || !speechEnabled) {
				if (type.indexOf("02") != -1) {
					rightNum = 0;
					wrongNum = 1;
				}
				else if (type.indexOf("01") != -1) {
					rightNum = 1;
					wrongNum = 0;
				}
				type = null;
			}
			buildSmallPageInfo(step, type, rightNum, wrongNum);
		}
	}
	
	public void onFirstWrong() {
		result = RESULT_WRONG;
		//死掉
		if (--currentHeartNum <= 0) {
			currentHeartNum = 0;
			requestForFail(buildPageInfo(2, currentPageNo));
		}
		refreshHeart();
	}

	private int step = 0;
	private ViewGroup prevView;
	// 当前显示内容
	private String anwserStr;
	// 回答正确后音频
	private String anwserSoundPath;
	// 判断是否正确的标识
	private boolean pass = false;

	private LessonData currentData;

	public LessonData getCurrentData() {
		return currentData;
	}

	private void next(int distance) {
		move(distance);
		if (null != prevView) {
			Animation scaleAnimation = new ScaleAnimation(1.35f, 1f, 1.35f, 1f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			// 设置动画时间
			scaleAnimation.setDuration(500);
			scaleAnimation.setFillAfter(true);
			((OptionImg) prevView.getChildAt(0)).setWrong();
//				prevView.setWrong();
			prevView.clearAnimation();
			prevView.startAnimation(scaleAnimation);
		}
	}
	
	private class ShowPicRunnable implements Runnable {
		
		private boolean goOn;
		
		public ShowPicRunnable(boolean goOn) {
			this.goOn = goOn;
		}
		
		@Override
		public void run() {
			hlv.setSelection(step);
			if (null == hlv.getSelectedView()) {
				return;
			}
			ViewGroup v = (ViewGroup) hlv.getSelectedView();
			prevView = v;
			Animation scaleAnimation = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
					0.5f);
			// 设置动画时间
			scaleAnimation.setDuration(500);
			scaleAnimation.setFillAfter(true);
			((OptionImg) v.getChildAt(0)).reset();
//			v.reset();
			scaleAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (!goOn) {
						txtQuestionTop.setText("");
						handler.removeCallbacks(r2);
						handler.postDelayed(r2, 2000);
						return;
					}
					onCurrentStepStart();
				}
			});
			v.clearAnimation();
			v.startAnimation(scaleAnimation);
		}
		
	};
	
	private void move(int distance) {
		if (distance == 0) {
			return;
		}
		int prevDistance = adapter.getDistanceByStep(step - 1);
		if ((distance - prevDistance) <= AppInfo.getScreenWidth() >> 1) {
			hlv.scrollTo(distance, 2000);
		}
		else {
			hlv.scrollTo(distance, 3000);
		}
	}

	private void jump() {
		resetBg();
		rlQuestionTop.setVisibility(View.GONE);
		rlOptions02.setVisibility(View.GONE);
		wv.setVisibility(View.GONE);
		llRecordResult.setVisibility(View.GONE);
		if (null != prevView) {
			if (null != prevView.getAnimation())
				prevView.getAnimation().cancel();
			prevView.clearAnimation();
			((OptionImg) prevView.getChildAt(0)).setWrong();
		}
		step = adapter.getStepByPageNo(currentPageNo);
		hlv.jumpTo(adapter.getDistanceByStep(step));
		hlv.setVisibility(View.INVISIBLE);
		if (null != hlv.getAnimation())
			hlv.getAnimation().cancel();
		hlv.clearAnimation();
		hlv.setVisibility(View.VISIBLE);
		Animation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		hlv.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				startFramgent();
				pageTouchable();				
			}
		});
		
	}
	
	private void clearRunnable() {
		handler.removeCallbacks(r02C);
		handler.removeCallbacks(r2);
		handler.removeCallbacks(rNext);
		if (null != showPicR)
			handler.removeCallbacks(showPicR);
	}

	protected void onCurrentStepStart() {
		wv.setVisibility(View.GONE);
		llRecordResult.setVisibility(View.GONE);
		if (!speechEnabled) {
			if (currentType.indexOf("02") != -1) {
				currentType = "01A";
			}
		}
		if ("01A".equals(currentType)) {
			txtQuestionTop.setText(anwserStr);
			imgQuestionTopPlay.setOnClickListener(this);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.GONE);
		}
		else if ("01B".equals(currentType)) {
			txtQuestionTop.setText(anwserStr);
			imgQuestionTopPlay.setVisibility(View.GONE);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.GONE);
		}
		else if ("01C".equals(currentType)) {
			txtQuestionTop.setVisibility(View.GONE);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.GONE);
		}
		else if ("01D".equals(currentType)) {
			rlQuestionTop.setVisibility(View.GONE);
			rlOptions02.setVisibility(View.GONE);
		}
		else if ("02A".equals(currentType)) {
			txtQuestionTop.setText(anwserStr);
			imgQuestionTopPlay.setOnClickListener(this);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.VISIBLE);
		}
		else if ("02B".equals(currentType)) {
			txtQuestionTop.setText("");
			imgQuestionTopPlay.setOnClickListener(this);
			rlQuestionTop.setVisibility(View.VISIBLE);
			rlOptions02.setVisibility(View.VISIBLE);
		}
		else if ("02C".equals(currentType)) {
			txtQuestionTop.setText("");
			rlQuestionTop.setVisibility(View.VISIBLE);
			imgQuestionTopPlay.setVisibility(View.GONE);
			rlOptions02.setVisibility(View.VISIBLE);
		}
		// 不需要播音频的题型
		if ("02C".equals(currentType)) {
			waitToStart02(1000);
			rlOptions02.setVisibility(View.VISIBLE);
			return;
		}
		// 等待2秒过的题型
		if (LessonType01_02_03_07_10_11_15.WAITING_TYPE_SET.contains(currentType)) {
			wait2Seconds();
			return;
		}
		if (LessonType01_02_03_07_10_11_15.NO_PLAYING_SOUND_TYPE_SET.contains(currentType)) {
			return;
		}
		MediaPlayerMgr.getInstance().playMp(getCurrentData().getAnswer().getLocalSoundPath(), onCompletionListener);
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
	
	private DialogConfirmFragment dialog;
	
	private boolean showloading = false;
	
	private void showDialog() {
		if (null == dialog) {
			dialog = new DialogConfirmFragment(new CallBackDialogConfirm() {
				
				@Override
				public void onSubmit(int position) {
					hideDialog();
					GlobalResTypes.getInstance().stopDownloadForPage();
					toFinish();
				}
				
				@Override
				public void onCancel() {
					hideDialog();
					if (!canStart && showloading) {
						showLoading(true);
					}
				}

				@Override
				public void onKeyBack() {
					onCancel();
				}
				
			}, getString(R.string.lessonTip1En),  getString(R.string.lessonTip1Cn), 0, R.style.DialogTopToBottomTheme);
		}
		if (dialog.isVisible()) {
			return;
		}
		dialog.showDialog(getSupportFragmentManager());
	}

	private void hideDialog() {
		if (null == dialog) {
			return;
		}
		dialog.dismiss();
		dialog = null;
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  request datas & buildSmallPageInfo & buildPageInfo
	/**
	/***************************************************************************************************************************************/
	
	private void buildSmallPageInfo(int key, String types, int rightNum, int wrongNum) {
		SmallPageData pageData = pageDatas.get(key);
		if (null == pageData) {
			pageData = new SmallPageData();
			pageData.types = types;
			pageData.rightNum = rightNum;
			pageData.wrongNum = wrongNum;
			pageDatas.put(key, pageData);
		}
	}
	
	private EntityLessonDataCache buildPageInfo(int status, int pageNo) {
		EntityLessonDataCache lessonData = new EntityLessonDataCache();
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		int pageRightNum = 0;
		int pageWrongNum = 0;
		String pageTypes = null;
		SmallPageData pageData = null;
		int key = 0;
		for (int i = 0; i < pageDatas.size(); ++i) {
			key = pageDatas.keyAt(i);
			pageData = pageDatas.get(key);
			if (null == pageData) {
				continue;
			}
			if (!TextUtils.isEmpty(pageData.types)) {
				if (!TextUtils.isEmpty(pageTypes)) {
					pageTypes += "_";
				}
				else {
					pageTypes = "";
				}
				pageTypes += pageData.types;
			}
			pageRightNum += pageData.rightNum; 
			pageWrongNum += pageData.wrongNum; 
		}
		pageDatas.clear();
		//TODO: 兼容老数据   页数是从1开始  所以要页数 +1
//		String type6Id = beans.getCurrentType5Id() + (finishedPageNo >= 10 ? finishedPageNo  : "0" + finishedPageNo);
		String type6Id = beans.getCurrentType5Id() + (pageNo + 1);
		lessonData.setData(beans.getLoginName(), 
				beans.getCurrentType5Id(), 
				type6Id, 
				status, 
				currentHeartNum, 
				pageRightNum, 
				pageWrongNum, 
				pageTypes, 
				null);
		pageContainer.setPageInfoAndRefresh(pageNo, pageWrongNum > 0 ? LessonPageContainer.STATE_WRONG : LessonPageContainer.STATE_RIGHT);
		return lessonData;
	}
	
	public static final int STATE_PASS = 0;
	public static final int STATE_FAIL = 1;
	public static final int STATE_SUCCESS = 2;
	
	private void requestForPass(EntityLessonDataCache data) {
		new TaskReqPassTheLesson(STATE_PASS, this, data);
	}
	
	private void requestForFail(EntityLessonDataCache data) {
		showLoading();
		stopFramgent();
		new TaskReqPassTheLesson(STATE_FAIL, this, data);
	}
	
	private void requestForSuccess(EntityLessonDataCache data) {
		showLoading();
		stopFramgent();
		new TaskReqPassTheLesson(STATE_SUCCESS, this, data);
	}
	
	@Override
	public void refreshView(int tag, EntityLessonDataCache result) {
		hideLoading();
		if (null != result && tag != STATE_PASS) {
			//TODO:显示给用户 是否要再请求一次
			showDialogConfirm(result,tag);
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
			toFinish();
		}
	}
	
	private void showDialogConfirm (EntityLessonDataCache data, int tag) {
		final int tag1 = tag;
		final EntityLessonDataCache data1 = data;
		dialogConfirm = new DialogConfirmFragment( new CallBackDialogConfirm() {
			
			@Override
			public void onSubmit(int position) {
//				showLoading();
				if (tag1 == STATE_FAIL) {
					requestForFail(data1);
				}
				else if (tag1 == STATE_SUCCESS) {
					requestForSuccess(data1);
				}
				dialogConfirm.dismiss();
				dialogConfirm = null;
			}
			
			@Override
			public void onCancel() {
				dialogConfirm.dismiss();
				dialogConfirm = null;
			}

			@Override
			public void onKeyBack() {
				onCancel();
			}
			
		}, getString(R.string.lessonTip6En), getString(R.string.lessonTip6Cn), 0);
		dialogConfirm.showDialog(getSupportFragmentManager());
		
	}
	
	/***************************************************************************************************************************************/
	/**
	/**  score & heart
	/**
	/***************************************************************************************************************************************/
	
	private void initScore() {
		if (null != type5 && null != type5.getCurrentScore()) {
			currentScore = type5.getCurrentScore();
		}
		refreshScore();
//		txtScore.setVisibility(View.VISIBLE);
//		imgScore.setVisibility(View.VISIBLE);
	}
	
	private void refreshScore() {
		txtScore.setText(currentScore + "");
	}
	
	private void initHeart() {
		for (List<LessonFragment> lessonFragments : LessonFragment.LESSON_PAGES) {
			for (LessonFragment lessonFragment : lessonFragments) {
				maxHeartNum += lessonFragment.getDatas().size();
			}
		}
		maxHeartNum = maxHeartNum / 4;
		if (maxHeartNum > 10) {
			maxHeartNum = 10;
		}
		else if (maxHeartNum < 2) {
			maxHeartNum = 2;
		}
		if (null != type5 && null != type5.getHeartNum() && type6Datas.size() != 0) {
			currentHeartNum = type5.getHeartNum();
			if (currentHeartNum < 0) {
				currentHeartNum = 0;
			}
		}
		else {
			getMaxHeartNum();
			currentHeartNum = maxHeartNum;
		}
		rbHeart.setNumStars(getMaxHeartNum());
		rbHeart.setStepSize((float) (maxHeartNum > 5 ? 0.5 : 1));
		refreshHeart();
		rbHeart.setVisibility(View.VISIBLE);
	}
	
	private void refreshHeart() {
		rbHeart.setRating(getCurrentHeartNum());
	}
	
	private int getMaxHeartNum() {
		if (maxHeartNum > 5) {
			if (maxHeartNum == 7 || maxHeartNum == 9) {
				maxHeartNum += 1;
			}
			return maxHeartNum / 2;
		}
		return maxHeartNum;
	}

	private float getCurrentHeartNum() {
		return (maxHeartNum - currentHeartNum) * rbHeart.getStepSize();
	}

	/***************************************************************************************************************************************/
	/**
	/** right or wrong or waiting
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
		handler.removeCallbacks(r1);
		handler.postDelayed(r1, 1000);
	}

	public void wrongHandler() {
		wrongBg();
		handler.removeCallbacks(r1);
		handler.postDelayed(r1, 1000);
	}

	private Runnable r1 = new Runnable() {

		@Override
		public void run() {
			if (!isRunning()) {
				return;
			}
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
	/**	 Thread wait 1 second, 2 seconds
	/**
	/****************************************************************************************************************************************/

	private Runnable r2 = new Runnable() {

		@Override
		public void run() {
			if (!isRunning()) {
				return;
			}
			onFramgentFinished();
		}

	};

	private Runnable r02C = new Runnable() {
		
		@Override
		public void run() {
			if (!isRunning()) {
				return;
			}
			try {
				SoundPoolMgr.getInstance().playDingSnd();
				if (null != wv) {
					Animation animation = new AlphaAnimation(0, 0.8f);
					animation.setDuration(1000);
					animation.setFillAfter(true);
					wv.startAnimation(animation);
					wv.setVisibility(View.VISIBLE);
				}
				Thread.sleep(200);
				start02();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	};
	
	public void waitToNext(int time) {
		handler.removeCallbacks(rNext);
		handler.postDelayed(rNext, time);
	}
	
	private Runnable rNext = new Runnable() {
		
		@Override
		public void run() {
			if (!isRunning()) {
				return;
			}
			onFramgentFinished();
		}
		
	};

	public void wait2Seconds() {
		handler.removeCallbacks(r2);
		handler.postDelayed(r2, 2000);
	}

	public void waitToStart02() {
		waitToStart02(0);
	}
	
	public void waitToStart02(int time) {
		if (null != wait1SecondThread)
			handler.removeCallbacks(wait1SecondThread);
		llRecordResult.setVisibility(View.GONE);
		handler.removeCallbacks(r02C);
		handler.postDelayed(r02C, time);
	}

	public void waitToFinish(Wait1SecondThread r) {
		if (null != wait1SecondThread)
			handler.removeCallbacks(wait1SecondThread);
		wait1SecondThread = r;
		handler.postDelayed(wait1SecondThread, 1000);
	}

	private Wait1SecondThread wait1SecondThread;

	private class Wait1SecondThread extends Thread {

		private boolean pass = false;

		public Wait1SecondThread(boolean pass) {
			this.pass = pass;
		}

		public void run() {
			if (!isRunning() || ScreenObserver.isScreenLocked(AtyBoss.this)) {
				return;
			}
			try {
				// 02C 题型特殊处理
				if ("02C".equals(currentType)) {
					if (!pass)
						waitToStart02();
					else
						onFramgentFinished();
					return;
				}
				AtyBoss.this.pass = pass;
				MediaPlayerMgr.getInstance().playMp(anwserSoundPath, onCompletionListener);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	};

	/***************************************************************************************************************************************/
	/**
	/** MediaPlayer onCompletionListener
	/**
	/***************************************************************************************************************************************/

	private OnCompletionListener onCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion() {
			if (!isRunning()) {
				return;
			}
			// 01题 直接过
			if (currentType.indexOf("01") != -1) {
				waitToNext(1200);
				return;
			}
			if (pass || !speechEnabled) {
				onFramgentFinished();
				return;
			}
			// 02题型 直接播音频
			if (currentType.indexOf("02") != -1) {
				start02();
				return;
			}
		}

	};

	/***************************************************************************************************************************************/
	/**
	/** IOralEvalSDKMgr 语音识别 02题型 
	/**
	/****************************************************************************************************************************************/

	private int type02Count = 0;
	private boolean isType02Finished = false;

	private void start02() {
		isType02Finished = false;
		if (TextUtils.isEmpty(anwserStr)) {
			return;
		}
		IOralEvalSDKMgr.getInstance().start(anwserStr, this,
				new CallbackIOralEvalSDK() {

					@Override
					public void onResult(boolean pass, int finalScore, String htmlStr) {
						if (isType02Finished || !isRunning()) {
							return;
						}
						stop02();
						if (!TextUtils.isEmpty(htmlStr)) {
							txtQuestionTop.setText(Html.fromHtml(htmlStr));
						}
						if (pass) {
							rightHandler();
							SoundPoolMgr.getInstance().playRightSnd();
							show02Result(pass, finalScore);
						} 
						else {
							wrongHandler();
							SoundPoolMgr.getInstance().playWrongSnd();
							show02Result(pass, finalScore);
						}
						//错3次 直接过
						if (++type02Count >= 3 && !pass) {
							onFirstWrong();
							pass = true;
						}
						waitToFinish(new Wait1SecondThread(pass));
					}

					@Override
					public void onError(Error error, OfflineSDKPreparationError ofError) {
						if (!isRunning()) {
							return;
						}
						stop02();
						if (error.toString().equals("AudioDevice")) {
							Toast.makeText(AtyBoss.this, R.string.speechTip1, Toast.LENGTH_SHORT).show();
						}
						else if (error.toString().equals("AudioDevice")) {
							Toast.makeText(AtyBoss.this, R.string.speechTip2, Toast.LENGTH_SHORT).show();
						}
						speechEnabled = false;
						onCurrentStepStart();
					}

					@Override
					public void onVoice(int num) {
						if (null != wv) {
							wv.updateAmplitude(num);
						}
					}
					
				});
	}
	
	public void stop02() {
		isType02Finished = true;
		IOralEvalSDKMgr.getInstance().stop();
		if (null != wv) {
			wv.clearAnimation();
			wv.setVisibility(View.GONE);
		}
		llRecordResult.setVisibility(View.GONE);
	}
	
	private void show02Result(boolean pass, int score) {
		wv.setVisibility(View.GONE);
		llRecordResult.clearAnimation();
		Animation animation = new AlphaAnimation(0, 0.8f);
		animation.setDuration(300);
		animation.setFillAfter(false);
		llRecordResult.startAnimation(animation);
		llRecordResult.setVisibility(View.VISIBLE);
		if (!pass) {
			txtRecordStar.setText("Try Again!");
			rbStar.setStar(1);
		}
		else if (score < 80) {
			txtRecordStar.setText("Great!");
			rbStar.setStar(2);
		}
		else {
			txtRecordStar.setText("Perfect!");
			rbStar.setStar(3);
		}
	}
	
	/***************************************************************************************************************************************/
	/**
	/** isRunning
	/**
	/****************************************************************************************************************************************/

	public boolean isRunning() {
		return state < STATE_FINISHED;
	}
	
	/************************************************************************************************/
	
	public static boolean starting = false;
	
	public static void startAty(Activity context) {
		if (starting) {
			return;
		}
		starting = true;
		Intent i = new Intent(context, AtyBoss.class);
		context.startActivityForResult(i, REQUEST_LESSON_BOSS);
	}
	
}
