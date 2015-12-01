package com.shuangge.english.game.llk;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.server.read.IWord;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.read.ReadWordData;
import com.shuangge.english.entity.server.user.LessonTips;
import com.shuangge.english.game.llk.component.WordTile;
import com.shuangge.english.network.read.TaskReqReadCore46Guide;
import com.shuangge.english.network.read.TaskReqReadIWordRes;
import com.shuangge.english.network.read.TaskReqReadProgress;
import com.shuangge.english.support.app.AppInfo;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.support.utils.MathUtils;
import com.shuangge.english.support.utils.MediaPlayerMgr;
import com.shuangge.english.support.utils.SoundPoolMgr;
import com.shuangge.english.support.utils.SoundUtils;
import com.shuangge.english.support.utils.ViewUtils;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.ComponentTitleBar;
import com.shuangge.english.view.component.BaseShadowMask;
import com.shuangge.english.view.component.dialog.DialogAlertFragment;
import com.shuangge.english.view.home.component.HomeMask;
import com.shuangge.english.view.read.AtyReadQuestion;
import com.shuangge.english.view.read.fragment.BaseLessonType;

public class AtyLLK extends AbstractAppActivity implements OnClickListener {
	
	private static final int START = 0;
	private static final int SUCCESS = 1;
	private static final int TIME_OUT = 2;
	private static final int ERROR = 3;
	private static final int MAX_SIZE = 16;
	
	private LinearLayout bg;
	private FrameLayout flWordContainer;
	private ProgressBar pbTime;
	private TextView scoreView;
	
	private BaseShadowMask mask2;
	
	private ComponentTitleBar titleBar;
	private ImageButton btnBack;

	private List<IWord> notPassWords;
	private List<IWord> notPassWord2s;
	private List<IWord> allWords;
	private List<String> currentTileContents;
	private Map<String, IWord> currentTagMap;
	private List<IWord> rightWords;
	private List<IWord> wrongWords;
	private int currentFinishedNum = 0;
	
	private Set<String> currentWordsMap;    //当前一组连连看的单词
	private Set<String> currentTranslationMap; //当前一组连连看的翻译
	
	private WordTile firstTile, secondTile;
	
	private TimeCount timeCount;
	private Timer timer;
	private TextView txtTimer;
	private int state = START;
	
	private boolean flag = false; //时间条方向
	
	private int timeLength = 18;
	private int time = 18;
	
	private int perScore = 1;
	
	private int score = 50;
	private int originScore = 50;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		notPassWords = null;
		notPassWord2s = null;
		allWords = null;
		currentTileContents = null;
		currentTagMap = null;
		rightWords = null;
		wrongWords = null;
		flWordContainer = null;
		clearTimer();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopTimer();
	}
	
	
	private int resume = 0;
	@Override
	protected void onResume() {
		resume++;
		super.onResume();
		if (resume > 1) {
			restart();
		}
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.game_llk);
		flWordContainer = (FrameLayout) findViewById(R.id.flWordContainer);
		pbTime = (ProgressBar) findViewById(R.id.pbTime);
		txtTimer = (TextView) findViewById(R.id.txtTimer);
		txtTimer.setTextColor(Color.GRAY);
		
		scoreView = (TextView) findViewById(R.id.scoreView);
		scoreView.setText(score + "");
		scoreView.setVisibility(View.INVISIBLE);
		
		bg = (LinearLayout) findViewById(R.id.bg);
		
		titleBar = (ComponentTitleBar) findViewById(R.id.titleBar);
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		notPassWords = new ArrayList<IWord>();
		notPassWord2s = new ArrayList<IWord>();
		allWords = new ArrayList<IWord>();
		currentTileContents = new ArrayList<String>();
		currentTagMap = new HashMap<String, IWord>();
		rightWords = new ArrayList<IWord>();
		wrongWords = new ArrayList<IWord>();
		
		currentWordsMap = new HashSet<String>();
		currentTranslationMap = new HashSet<String>();
		
		Set<IWord> tempWords = GlobalRes.getInstance().getBeans().getNotPassWordsForRead();
		if (tempWords.size() == 0) {
			tempWords = GlobalRes.getInstance().getBeans().getReadData().getCoreWords();
		}
		for (IWord word : tempWords) {
			notPassWords.add(word);
		}
		Set<IWord> tempWord2s = GlobalRes.getInstance().getBeans().getNotPassWordsForLesson();
		for (IWord word : tempWord2s) {
			notPassWord2s.add(word);
		}
		Collections.sort(notPassWord2s);
		Map<Long, ReadWordData> map = GlobalRes.getInstance().getBeans().getReadData().getWordMap();
		for (Long key : map.keySet()) {
			allWords.add(map.get(key));
		}
		Collections.sort(allWords);
		
		buildCurrentWords(true);
		LessonTips lessonTips = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getLessonTips();
		if (!lessonTips.getReadLLK()) {
			lessonTips.setReadLLK(true);
			mask2 = new BaseShadowMask(new BaseShadowMask.CallbackHomeMask() {
				
				@Override
				public void close() {
					if (null != mask2) {
						mask2.hide(getSupportFragmentManager());
						mask2 = null;
						start();
					}
				}
				
			}, (ViewGroup) bg.getParent() , getString(R.string.readLLKGuide), getString(R.string.readLLKBtn));
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
		else {
			start();
		}
	}
	
	
	private OnClickListener onClickTile = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			WordTile w = (WordTile) v;
			if (w.getType() == WordTile.MATCHED) {
				return;
			}
			SoundPoolMgr.getInstance().playTapRightSnd();
			
			if (null != v.getTag(R.id.tag_third) 
					&& v.getTag(R.id.tag_third).toString().indexOf("word:") != - 1) {
//				MediaPlayerMgr.getInstance().playMp(BaseLessonType.getSoundLocalPath(((IWord) v.getTag(R.id.tag_first)).getId()));
				 
				IWord wordData = (IWord) v.getTag(R.id.tag_first);
				String path = BaseLessonType.getSoundLocalPath(wordData.getId());
				if (!new File(path).exists()) {
					SoundUtils.loadIWordRes(path, wordData.getSoundUrl(), null);
				}
				else {
					MediaPlayerMgr.getInstance().playMp(path);
				}
			}
			
			//第一次点击
			if (null == firstTile) {
				firstTile = (WordTile) v;
				firstTile.setSelected(true);
				return;
			}
			//如果点击相同的
			if (firstTile.equals(v)) {
				firstTile.setSelected(false);
				firstTile = null;
				return;
			}
			//第二次点击
			if (firstTile.getType() == ((WordTile) v).getType()) {
				firstTile.setSelected(false);
				firstTile = (WordTile) v;
				firstTile.setSelected(true);
				return;
			}
			secondTile = (WordTile) v;
			secondTile.setSelected(true);
			check();
		}
		
	};
	
	/**
	 * 判断两个wordTile1 是否匹配
	 * @param wordTile1 第一个选中WordTile
	 * @param wordTile2 第二个选中WordTile
	 * @return 
	 */
	private boolean checkMatch(WordTile wordTile1, WordTile wordTile2) {
		//若没有英文单词WordTile 则判定错误
		if ((wordTile1.getType() == WordTile.ENG
				|| wordTile2.getType() == WordTile.ENG)
				&& wordTile1.getType() != wordTile2.getType()) {
			ReadWordData llkWord1 = (ReadWordData) wordTile1.getTag(R.id.tag_first);
			ReadWordData llkWord2 = (ReadWordData) wordTile2.getTag(R.id.tag_first);
				return llkWord1.getWord().trim().equals(llkWord2.getWord().trim()) || llkWord1.getTranslation().trim().equals(llkWord2.getTranslation().trim());
		}
		return false;
	}
	
	private void check() {
		//right
//		if (secondTile.getTag(R.id.tag_second).toString().equals(firstTile.getTag(R.id.tag_second).toString())) {
		if (checkMatch(firstTile, secondTile)) {
			currentFinishedNum += 2;
			rightWords.add((IWord) secondTile.getTag(R.id.tag_first));
			onRight(firstTile, secondTile);
		}
		//wrong
		else {
			wrongWords.add((IWord) secondTile.getTag(R.id.tag_first));
			onWrong(firstTile, secondTile);
		}
		firstTile = null;
		secondTile = null;
		
		if (currentFinishedNum < currentTileContents.size()) {
			return;
		}
		if (state == TIME_OUT || state == ERROR) {
			showCurrentDialog();
			return;
		}
		//进入下一组
		if (notPassWords.size() > 0) {
			showNextDialog();
			return;
		}
		//过关
		onSuccess();
	}
	
	private void restart() {
		startTimer(time);
	}
	
	private void start() {
		state = START;
		currentFinishedNum = 0;
		firstTile = null;
		secondTile = null;
		score = originScore;
		scoreView.setText(score + "");
		flag = false;
		startTimer(timeLength);
	}
	
	/**
	 * 检测是否有重复单词 包括多义词 和同义词
	 * @param word
	 * @return
	 */
	private boolean checkOverLapsWord(IWord word) {
		if (!currentWordsMap.contains(word.getWord()) && !currentTranslationMap.contains(((ReadWordData)word).getTranslation().trim())) {
			currentWordsMap.add(word.getWord());
			currentTranslationMap.add(((ReadWordData)word).getTranslation().trim());
			return true;
		}
		return false;
		
	}

	//连连看 map的随机分配 当单词数量不足时依次从 notPassWords notPassWords2 allWords 中挑选
	private void buildCurrentWords(boolean nextFlag) {
		IWord word = null;
		int i = 0;
		int maxSize = MAX_SIZE >> 1;
		if (nextFlag) { 
			currentTileContents.clear();
			currentTagMap.clear();
			int size = notPassWords.size();
			Set<Long> ids = new HashSet<Long>();
			
			List<IWord> overLapsOrgin = new ArrayList<IWord>(); //储存所有 判定重复的word 位置
			for (i = 0; i < size && ids.size() < maxSize; i++) {
				word = notPassWords.remove(0);
				if (!checkOverLapsWord(word)) {
					overLapsOrgin.add(word);
					continue;
				}
				
				ids.add(word.getId());
				currentTileContents.add("word:" + word.getId());
				currentTileContents.add("img:" + word.getId());
				currentTagMap.put("word:" + word.getId(), word);
				currentTagMap.put("img:" + word.getId(), word);
			}
			if (ids.size() < maxSize) {
				i = 0;
				List<IWord> overLaps = new ArrayList<IWord>(); //储存所有 判定重复的word 位置
				while (ids.size() < maxSize) {
					if (overLapsOrgin.size() > 0) {
					for (int j = 0; j < overLapsOrgin.size() && ids.size() < maxSize; j++) {
						word = overLapsOrgin.get(j);
						currentTileContents.add("word:" + word.getId());
						currentTileContents.add("img:" + word.getId());
						currentTagMap.put("word:" + word.getId(), word);
						currentTagMap.put("img:" + word.getId(), word);
						ids.add(word.getId());
						}
					overLapsOrgin.clear();
					continue;
					}
					//当有大量重复词导致 超过wordMap的长度时 重新从 重复单词中依次获取单词
					if (i >= allWords.size()) {
						for (int j = 0; j < overLaps.size() && ids.size() < maxSize; j++) {
							word = overLaps.get(j);
							currentTileContents.add("word:" + word.getId());
							currentTileContents.add("img:" + word.getId());
							currentTagMap.put("word:" + word.getId(), word);
							currentTagMap.put("img:" + word.getId(), word);
							ids.add(word.getId());
						}
						break;
					}
					word = allWords.get(i++);
					if (!checkOverLapsWord(word)) {
						overLaps.add(word);
						continue;
					}
					if (ids.contains(word.getId().longValue())) {
						continue;
					}
					++size;
					currentTileContents.add("word:" + word.getId());
					currentTileContents.add("img:" + word.getId());
					currentTagMap.put("word:" + word.getId(), word);
					currentTagMap.put("img:" + word.getId(), word);
					ids.add(word.getId());
				}
			}
				
		} 
		MathUtils.ramdomList(currentTileContents);
		WordTile tile = null;
		if (flWordContainer.getChildCount() > 0) {
			for (i = 0; i < MAX_SIZE; i++) {
				tile = (WordTile) flWordContainer.getChildAt(i);
				if (i >= currentTileContents.size()) {
					tile.setVisibility(View.INVISIBLE);
				}
				else {
					String key = currentTileContents.get(i);
					IWord llkWord = currentTagMap.get(key);
					tile.setTag(R.id.tag_first, llkWord);
					tile.setTag(R.id.tag_second, llkWord.getWord());
					tile.setTag(R.id.tag_third, key);
					tile.setVisibility(View.VISIBLE);
					if (key.indexOf("word:") != - 1) {
						tile.setText(llkWord.getWord());
					}
					else {
//						tile.setImgUrl(llkWord.getImgUrl());
						ReadWordData readWordData = (ReadWordData)llkWord;
						tile.setTranslation(readWordData.getTranslation());
					}
				}
			}
			return;
		}
		int j = ((int) Math.sqrt(MAX_SIZE));
		int w = (int) (AppInfo.getScreenWidth() * 0.9 / j);
		int line = 0, row = 0;
		for (i = 0; i < MAX_SIZE; i++) {
			if (i % j == 0 && i != 0) {
				++line;
			}
			row = i % j;
			tile = new WordTile(this);
			tile.setOnClickListener(onClickTile);
			ViewUtils.setFrameMargins(tile, w, w, w * row, w * line, 0, 0);
			flWordContainer.addView(tile);
			if (i >= currentTileContents.size()) {
				tile.setVisibility(View.INVISIBLE);
			}
			else {
				String key = currentTileContents.get(i);
				IWord llkWord = currentTagMap.get(key);
				tile.setTag(R.id.tag_first, llkWord);
				tile.setTag(R.id.tag_second, llkWord.getWord());
				tile.setTag(R.id.tag_third, key);
				tile.setVisibility(View.VISIBLE);
				if (key.indexOf("word:") != - 1) {
					tile.setText(llkWord.getWord());
				}
				else {
//					tile.setImgUrl(llkWord.getImgUrl());
					ReadWordData readWordData = (ReadWordData)llkWord;
					tile.setTranslation(readWordData.getTranslation());
				}
			}
		}
		
		//初始化动画
	}
	
	private void onNextGroup() {
		buildCurrentWords(true);
		start();
	}
	
	private void onCurrentGroup() {
		buildCurrentWords(false);
		start();
	}
	
	
	private void onRight(WordTile... tiles) {
		for (WordTile tile : tiles) {
//			tile.setVisibility(View.INVISIBLE);
			tile.setMatched();
		}
		SoundPoolMgr.getInstance().playRightSnd();
		
		// 执行加分
		if (state != TIME_OUT) {
			score = score + perScore;
			scoreView.setText(score + "");
		}
//		Toast.makeText(this, "正确", Toast.LENGTH_SHORT).show();
	}
	
	private void onWrong(WordTile... tiles) {
//		state = ERROR;
		for (WordTile wordTile : tiles) {
			wordTile.setError();
		}

		SoundPoolMgr.getInstance().playWrongSnd();
//		Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
	}
	
	private void onSuccess() {
		state = SUCCESS;
		stopTimer();
		
		ReadResult rr = GlobalRes.getInstance().getBeans().getReadData();
		if (rr.getProgress() < ReadResult.PROGRESS_L4) {
			int readNo = GlobalRes.getInstance().getBeans().getReadData().getReadNo().intValue(); // 文章id
			showLoading();
			new TaskReqReadProgress(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					GlobalRes.getInstance().getBeans().getReadData().setProgress(ReadResult.PROGRESS_L4);
					AtyReadQuestion.startAty(AtyLLK.this);
					finish();
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}

			}, readNo, ReadResult.PROGRESS_L4);
		}
		else {
			AtyReadQuestion.startAty(this);
		    finish();
		}
//		Toast.makeText(this, "过关", Toast.LENGTH_SHORT).show();
	}
	
	private void onTimeOut() {
		state = TIME_OUT;
		
		SoundPoolMgr.getInstance().playDingSnd();
	}
	
	@SuppressLint("NewApi")
	private void startBackroundAnimation(int time) {
		Integer colorFrom = getResources().getColor(R.color.white);
//		0x80C90E0E
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, 0xC90E0E);
		colorAnimation.addUpdateListener(new AnimatorUpdateListener() {
		 @Override
		 public void onAnimationUpdate(ValueAnimator animator) {
			 bg.setBackgroundColor((Integer)animator.getAnimatedValue());
		 }
		});
		colorAnimation.setDuration(time);
		colorAnimation.start();
	}
	
	private void startTimer(int length) {
		if (!flag) {
		clearTimer();
		timeCount = new TimeCount(length*1000, length* 10);
		pbTime.setProgress(length / timeLength * pbTime.getMax());
		timeCount.start();
		}
		else {
			state = TIME_OUT;
		}
		
		txtTimer.setText(length + "s");
		time = length;
		timer = new Timer();
		timer.schedule(new MyTimerTask(), 0, 1000);
	}
	
	private void stopTimer() {
		if (null != timeCount)
			timeCount.cancel();
		if (null != timer)
			timer.cancel();
	}
	
	private void clearTimer() {
		stopTimer();
		timeCount = null;
		timer = null;
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
	}
	
	/*******************************************************************************************************************************************/
	
	class MyTimerTask extends TimerTask {
		
		@Override
		public void run() {
			Message msg = new Message(); 
			msg.what = 0;
			if (!flag) {
				if (--time < 0) {
					flag = true;
					time = 0;					
				}
			}
			if (flag) {
				time++;
				msg.what = 1;
			}
            handler.sendMessage(msg);
		}
		
	}
	
	private Handler handler = new Handler() {
		
		@Override 
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				txtTimer.setTextColor(Color.RED);
			}
			else {
				txtTimer.setTextColor(Color.GRAY);
			}
			txtTimer.setText((time) + "s");
		};
		
	};
	
	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			pbTime.setProgress((int) (millisUntilFinished / 180));
		}

		@Override
		public void onFinish() {
			if (null != pbTime)
				pbTime.setProgress(0);
//			Toast.makeText(AtyLLK.this, "时间到了", Toast.LENGTH_SHORT).show();
			
			stopTimer();
			showCurrentDialog();
//			onTimeOut();
		}
		
	}
	
	
	/*******************************************************************************************************************************************/
	
	private DialogAlertFragment dialogNext;
	private DialogAlertFragment dialogCurrent;
	
	private void showNextDialog() {
		stopTimer();
		if (null == dialogNext) {
			dialogNext = new DialogAlertFragment(new DialogAlertFragment.CallBackDialogConfirm() {
				@Override
				public void onSubmit(int position) {
					if (null == dialogNext) {
						return;
					}
					dialogNext.dismiss();
					dialogCurrent = null;
					onNextGroup();
				}
				
				@Override
				public void onKeyBack() {
				}
				
			}, getString(R.string.readLLKMoreWords), " ", 0);
		}
		if (dialogNext.isVisible()) {
			return;
		}
		dialogNext.setCancelable(false);
		dialogNext.showDialog(getSupportFragmentManager());
	}
	
	private void showCurrentDialog() {
		SoundPoolMgr.getInstance().playDingSnd();
		if (null == dialogCurrent) {
			dialogCurrent = new DialogAlertFragment(new DialogAlertFragment.CallBackDialogConfirm() {
				@Override
				public void onSubmit(int position) {
					if (null == dialogCurrent) {
						return;
					}
					dialogCurrent.dismiss();
					dialogCurrent = null;
					onCurrentGroup();
				}
				
				@Override
				public void onKeyBack() {
				}
				
			},  getString(R.string.readLLKTimeout), " ", 0);
		}
		if (dialogCurrent.isVisible()) {
			return;
		}
		dialogCurrent.setCancelable(false);
		dialogCurrent.showDialog(getSupportFragmentManager());
	}
	
	
	/*******************************************************************************************************************************************/
	
	public static void startAty(Context context) {
		context.startActivity(new Intent(context, AtyLLK.class));
	}

}
