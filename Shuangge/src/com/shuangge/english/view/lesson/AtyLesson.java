package com.shuangge.english.view.lesson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityLessonDataCache;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackDownloadForPage;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackLessonRes;
import com.shuangge.english.entity.lesson.LessonData;
import com.shuangge.english.entity.lesson.LessonFragment;
import com.shuangge.english.entity.server.lesson.Type5Data;
import com.shuangge.english.entity.server.lesson.Type6Data;
import com.shuangge.english.entity.server.user.LessonTips;
import com.shuangge.english.network.lesson.TaskReqLessonDetails;
import com.shuangge.english.network.lesson.TaskReqPassTheLesson;
import com.shuangge.english.network.lesson.TaskReqResetLesson;
import com.shuangge.english.network.settings.TaskReqLessonTips;
import com.shuangge.english.support.app.ScreenObserver;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.IOralEvalSDKMgr;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.component.dialog.DialogConfirm3BtnFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;
import com.shuangge.english.view.lesson.adapter.AdapterLesson;
import com.shuangge.english.view.lesson.component.DialogLessonTipFragment;
import com.shuangge.english.view.lesson.component.LessonPageContainer;
import com.shuangge.english.view.lesson.component.LessonViewPager;
import com.shuangge.english.view.lesson.fragment.BaseLessonType;
import com.shuangge.english.view.lesson.fragment.BaseLessonType.CallbackLessonType;
import com.shuangge.english.view.lesson.fragment.LessonType01_02_03_07_10_11_15;
import com.shuangge.english.view.lesson.fragment.LessonType05_06_08_13_14;

public class AtyLesson extends AbstractAppActivity implements OnClickListener, LessonPageContainer.CallbackPage, CallbackLessonType, CallbackNoticeView<Void, EntityLessonDataCache> {

	public static final int REQUEST_LESSON = 1080;
	
	public static Set<String> NO_PLAYING_SOUND_TYPE_SET = new HashSet<String>();
	
	static {
		NO_PLAYING_SOUND_TYPE_SET.add("02C");
		NO_PLAYING_SOUND_TYPE_SET.add("03A");
		NO_PLAYING_SOUND_TYPE_SET.add("03B");
		NO_PLAYING_SOUND_TYPE_SET.add("05B");
		NO_PLAYING_SOUND_TYPE_SET.add("07A");
		NO_PLAYING_SOUND_TYPE_SET.add("10A");
		NO_PLAYING_SOUND_TYPE_SET.add("13A");
		NO_PLAYING_SOUND_TYPE_SET.add("14A");
	}

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
	
	private LessonViewPager viewPager;
	private LessonPageContainer pageContainer; //下方进度条
	private RelativeLayout rlBg;
	private RatingBar rbHeart;
	private TextView txtScore, txtPath;
	private ImageView btnBack, imgScore, imgBg;
	
	private BaseLessonType currentFragment;
	private BaseLessonType waitingFragment;
	
	private AdapterLesson adapter;
	
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
	
	private SparseArray<SmallPageData> pageDatas = new SparseArray<SmallPageData>();
	
	private DialogConfirmFragment dialogConfirm;
	
	private class SmallPageData {
		
		private String types;
		private Integer rightNum;
		private Integer wrongNum;
		
	}
	
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
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_lesson);
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

		viewPager = (LessonViewPager) findViewById(R.id.vp);
		adapter = new AdapterLesson(getSupportFragmentManager());
		viewPager.setOnPageChangeListener(onPageChangeListener);
		viewPager.setOffscreenPageLimit(1);
		
		SoundPoolMgr.getInstance();
		showLoading(true);
		showloading = true;
		CacheBeans beans = GlobalRes.getInstance().getBeans();
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
				if (isFinishing()) {
					return;
				}
				if (null == list) {
					hideLoading();
					setResult(0);
					toFinish();
					Toast.makeText(AtyLesson.this, R.string.lessonErrTip, Toast.LENGTH_SHORT).show();
					return;
				}
				int pageNo = 0;
				type6Datas = GlobalRes.getInstance().getBeans().getLessonDetails().getType6s();
				for (Type6Data type6Data : type6Datas) {
					pageContainer.setPageInfo(pageNo++, type6Data.getCurrentWrongNum() == 0 ? 
							LessonPageContainer.STATE_RIGHT : LessonPageContainer.STATE_WRONG);
				}
				int i = 0;
				for (EntityLessonDataCache data : list) {
					pageContainer.setPageInfo(pageNo++, data.getWrongNum() == 0 ? 
							LessonPageContainer.STATE_RIGHT : LessonPageContainer.STATE_WRONG);
					if (++i == list.size()) {
						if (!TextUtils.isEmpty(type5.getType6ClientId())) {
							try {
								//TODO: 兼容老数据   页数是从1开始  所以要页数  - 1
								currentPageNo = Integer.parseInt(data.getcType6().substring(type5.getClientId().length()));
							}
							catch (Exception e) {
								currentPageNo = 0;
							}
						}
					}
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
	//初始化加载数据
	private void loadAllRes() {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		final String type1Id = beans.getCurrentType1Id();
		final String type2Id = beans.getCurrentType2Id();
		final String type4Id = beans.getCurrentType4Id();
		final String type5Id = beans.getCurrentType5Id();//"10103010001";
		//下载课程
		GlobalResTypes.getInstance().startDownloadForPage(type1Id, type2Id, type4Id, type5Id, new CallbackDownloadForPage() {
			//重写下载回调的各种方法
			@Override
			public void finish(int pageNo) {
				if (null == pageContainer) {
					return;
				}
				showloading = false;
				hideLoading();
				pageContainer.setCompleteAllPage();
				
				//TODO:debug
//				pageContainer.setCanClickedPageNo(LessonFragment.LESSON_PAGES.size());
			}
			
			@Override
			public void error(int pageNo) {
				if (null == pageContainer) {
					return;
				}
				hideLoading();
			}
			
			@Override
			public void configComplete(File file) {
				DebugPrinter.i("configComplete");
			}
			
			@Override
			public void complete(int pageNo) {
				if (null == pageContainer) {
					return;
				}
				if (!initialized) {
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
				DebugPrinter.i("in lesson page size:" + LessonFragment.LESSON_PAGES.size() + " CompletePageNo:" + pageNo);
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
	//监听题目切换  执行
    //实现 LessonPageContainer （下方进度条） 的CallbackPage 接口 并在 LessonPageContainer的onclick中执行
	//或在 调用LessonPageContainer的 gotoPageNo方法 时执行
	@Override
	public void onPageSelected(int pageNo) {
		this.currentPageNo = pageNo;
		pageDatas.clear();
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
//		List<LessonFragment> lessonFragments = LessonFragment.LESSON_PAGES.get(currentPageNo);
//		GlobalResTypes.getInstance().prepareLoadLessonBitmaps(callbackLessonRes, lessonFragments.subList(0, 1));
		callbackLessonRes.onComplete();
	}
	
	/**
	 * 清除当前资源
	 */
	private void clearResForCurrentPage() {
		GlobalResTypes.getInstance().clearLessonBitmaps();
	}
	
	private CallbackLessonRes callbackLessonRes = new CallbackLessonRes() {
		
		@Override
		public void onComplete() {
			List<BaseLessonType> datas = new ArrayList<BaseLessonType>();
			List<LessonFragment> lessonFragments = LessonFragment.LESSON_PAGES.get(currentPageNo);
			String type = null;
			int i = 0;
			//导入题目资源
			for (LessonFragment lessonFragment : lessonFragments) {
				i = 0;
				for (LessonData data : lessonFragment.getDatas()) {
					type = data.getType().toUpperCase(Locale.getDefault());
					if (type.indexOf("01") != -1
							|| type.indexOf("02") != -1
							|| type.indexOf("03") != -1
							|| type.indexOf("07") != -1
							|| type.indexOf("10") != -1
							|| type.indexOf("11") != -1
							|| type.indexOf("15") != -1) {
						if (i == 0) {
							datas.add(new LessonType01_02_03_07_10_11_15(lessonFragment, AtyLesson.this));
						}
					}
					else {
						if (i == 0) {
							datas.add(new LessonType05_06_08_13_14(lessonFragment, AtyLesson.this));
						}
					}
					++i;
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

		//监听题目切换
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE && !ScreenObserver.isScreenLocked(AtyLesson.this)) {
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
		if (pageContainer.getCanClickedPageNo() > currentPageNo) {
			viewPager.setCanMoveItemNo(100);
		}
		if (fragmentState == STATE_PREPARE && null != waitingFragment) {
			if (null != currentFragment && !first) {
				currentFragment.reset();
			}
			currentFragment = waitingFragment;
			if (checkLessonTips(first)) {
				return;
			}
			currentFragment.start();
			fragmentState = STATE_START;
		}
	}

	//当nextStep 判断没有下一课程片段时 调用 在BaseLessonType 暴露的  onFramgentFinished中 即获取下一题
	@Override
	public void onFramgentFinished(String types, int rightNum, int wrongNum) {
		//数据
		if (finishedItemNo < viewPager.getCurrentItem() + 1) { 
			//加分
			if (finishedPageNo < currentPageNo) { 
				int key = currentPageNo * 100 + finishedItemNo;
				buildSmallPageInfo(key, types, rightNum, wrongNum);
			}
			finishedItemNo = viewPager.getCurrentItem() + 1;
		}
		
		// 下一个framgent
		if (viewPager.getCurrentItem() < adapter.getDatas().size() - 1) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			return;
		}
		// 下一页
		if (++currentPageNo < LessonFragment.LESSON_PAGES.size()) {
			if (null != pageContainer) {
				finishedItemNo = 0;
				if (currentPageNo > finishedPageNo) {
					//加分
					finishedPageNo = currentPageNo - 1;
					requestForPass(buildPageInfo(0, finishedPageNo));
				}
				pageContainer.setCanClickedPageNo(currentPageNo);
				pageContainer.gotoPageNo(currentPageNo);
			}
			return;
		}
		currentPageNo = LessonFragment.LESSON_PAGES.size();
		requestForSuccess(buildPageInfo(1, currentPageNo - 1));
	}
	
	@Override
	public void onFirstWrong() {
		//死掉
		if (--currentHeartNum <= 0) {
			currentHeartNum = 0;
			requestForFail(buildPageInfo(2, currentPageNo));
		}
		refreshHeart();
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
			showDialogConfirm(result, tag);
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
	
	private void showDialogConfirm (final EntityLessonDataCache data, final int tag) {
		dialogConfirm = new DialogConfirmFragment( new CallBackDialogConfirm() {
			
			@Override
			public void onSubmit(int position) {
//				showLoading();
				if (tag == STATE_FAIL) {
					requestForFail(data);
				}
				else if (tag == STATE_SUCCESS) {
					requestForSuccess(data);
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
	}
	
	public void wrongHandler() {
		wrongBg();
		handler.removeCallbacks(r);
		handler.postDelayed(r, 1000);
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
					showLoading();
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
	
	public static boolean starting = false;
	
	public static void startAty(Activity context) {
		if (starting) {
			return;
		}
		starting = true;
		Intent i = new Intent(context, AtyLesson.class);
		context.startActivityForResult(i, REQUEST_LESSON);
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
					if (null != currentFragment)
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
	
	private DialogLessonTipFragment dialogLessonTipFragment;
	
	private void showLessonTips(final boolean first, String type) {
		int resId = getResources().getIdentifier("lessonType" + type.substring(0, 2) + "Tip", "string", "air.com.shuangge.phone.ShuangEnglish");
		if (null == dialogLessonTipFragment) {
			dialogLessonTipFragment = new DialogLessonTipFragment(new DialogLessonTipFragment.CallBackDialog() {
				
				@Override
				public void onOk() {
					if (null != dialogLessonTipFragment) {
						dialogLessonTipFragment.dismiss();
						dialogLessonTipFragment = null;
					}
					startFramgent(first);
					imgBg.setVisibility(View.GONE);
				}
				
			}, getString(resId), getString(resId));
		}
		if (dialogLessonTipFragment.isVisible()) {
			return;
		}
		dialogLessonTipFragment.setCancelable(false);
		dialogLessonTipFragment.showDialog(getSupportFragmentManager());
		
//		imgBg.setImageBitmap(BlurUtils.convertToBlur(BlurUtils.convertViewToBitmap(rlBg)));
//		imgBg.setImageBitmap(BlurUtils.convertViewToBitmap(rlBg));
//		imgBg.setVisibility(View.VISIBLE);
	}
	
	private void requestLessonTipsData(String type) {
		new TaskReqLessonTips(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, type);
	}
	
	private boolean checkLessonTips(boolean first) {
		LessonTips lessonTips = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getLessonTips();
		String type = currentFragment.getCurrentType();
		if (TextUtils.isEmpty(type) || null == lessonTips) {
			return false;
		}
		boolean result = false;
		if (type.indexOf("01") != -1) {
			if (!lessonTips.getType01()) {
				lessonTips.setType01(true);
				result = true;
			}
		}
		else if (type.indexOf("02") != -1) {
			if (!lessonTips.getType02()) {
				lessonTips.setType02(true);
				result = true;
			}
		}
		else if (type.indexOf("03") != -1) {
			if (!lessonTips.getType03()) {
				lessonTips.setType03(true);
				result = true;
			}
		}
		else if (type.indexOf("05") != -1) {
			if (!lessonTips.getType05()) {
				lessonTips.setType05(true);
				lessonTips.setType06(true);
				lessonTips.setType13(true);
				result = true;
			}
		}
		else if (type.indexOf("06") != -1) {
			if (!lessonTips.getType06()) {
				lessonTips.setType05(true);
				lessonTips.setType06(true);
				lessonTips.setType13(true);
				result = true;
			}
		}
		else if (type.indexOf("13") != -1) {
			if (!lessonTips.getType13()) {
				lessonTips.setType05(true);
				lessonTips.setType06(true);
				lessonTips.setType13(true);
				result = true;
			}
		}
		else if (type.indexOf("07") != -1) {
			if (!lessonTips.getType07()) {
				lessonTips.setType07(true);
				result = true;
			}
		}
		else if (type.indexOf("08") != -1) {
			if (!lessonTips.getType08()) {
				lessonTips.setType08(true);
				result = true;
			}
		}
		else if (type.indexOf("10") != -1) {
			if (!lessonTips.getType10()) {
				lessonTips.setType10(true);
				lessonTips.setType11(true);
				result = true;
			}
		}
		else if (type.indexOf("11") != -1) {
			if (!lessonTips.getType11()) {
				lessonTips.setType10(true);
				lessonTips.setType11(true);
				result = true;
			}
		}
		else if (type.indexOf("14") != -1) {
			if (!lessonTips.getType14()) {
				lessonTips.setType14(true);
				result = true;
			}
		}
		if (result) {
			requestLessonTipsData(type);
			showLessonTips(first, type);
		}
		return result;
	}
	
}