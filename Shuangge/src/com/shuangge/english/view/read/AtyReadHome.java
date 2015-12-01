package com.shuangge.english.view.read;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.entity.server.user.LessonTips;
import com.shuangge.english.game.llk.AtyLLK;
import com.shuangge.english.network.read.TaskReqRead;
import com.shuangge.english.network.read.TaskReqReadCore46Guide;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.read.component.ReadHomeMask;

public class AtyReadHome extends AbstractAppActivity implements OnClickListener {

	private ImageButton btnBack;
	
	private int progress = 1;
	
	private ReadHomeMask mask2;
	
	private LinearLayout rlContainer;
	
	private int guideStep = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_read_home);
		Long readNo = this.getIntent().getLongExtra("readNo", 0);
		
		rlContainer = (LinearLayout) findViewById(R.id.rlContainer);
		showLoading();
		
		new TaskReqRead(0, new CallbackNoticeView<Void, Boolean>() {
			
			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					findViewById(R.id.rl1).setVisibility(View.GONE);
					findViewById(R.id.rl2).setVisibility(View.GONE);
					findViewById(R.id.rl3).setVisibility(View.GONE);
					findViewById(R.id.rl4).setVisibility(View.GONE);
					finish();
					return;
				}
				refreshUI();
				final LessonTips lessonTips = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getLessonTips();
				if (!lessonTips.getReadHome()) {
					mask2 = new ReadHomeMask(new ReadHomeMask.CallbackHomeMask() {
						
						@Override
						public void close() {
							if (null == mask2) return;
							guideStep++;
							if (guideStep < 5) {
								mask2.buildView(guideStep);
								mask2.show(getSupportFragmentManager());
							}
							else {
								mask2.hide(getSupportFragmentManager());
								mask2 = null;
								lessonTips.setReadHome(true);
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
						
					},  (ViewGroup)rlContainer.getParent());
					mask2.show(getSupportFragmentManager());
				}		
				
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		}, readNo);
	
		
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		

		findViewById(R.id.rl1).setOnClickListener(this);
		findViewById(R.id.rl2).setOnClickListener(this);
		findViewById(R.id.rl3).setOnClickListener(this);
		findViewById(R.id.rl4).setOnClickListener(this);
		
		
	}
	
	/**
	 * 更新 进度状态 箭头和锁的图标的显示
	 */
	private void refreshUI() {
		findViewById(R.id.ivReadingComprehensionNext).setVisibility(View.VISIBLE);
		findViewById(R.id.ivWordsLLKNext).setVisibility(View.VISIBLE);
		findViewById(R.id.ivLearnWordsNext).setVisibility(View.VISIBLE);
		
		findViewById(R.id.tvArticleReadLock).setVisibility(View.INVISIBLE);
		findViewById(R.id.tvLearnWordsLock).setVisibility(View.INVISIBLE);
		findViewById(R.id.tvWordsLLKLock).setVisibility(View.INVISIBLE);
		findViewById(R.id.tvReadingComprehensionLock).setVisibility(View.INVISIBLE);
		
		ReadResult rr  = GlobalRes.getInstance().getBeans().getReadData();
		if (null == rr) return;
		progress = rr.getProgress();
		if (progress < ReadResult.PROGRESS_L4) {
			findViewById(R.id.ivReadingComprehensionNext).setVisibility(View.INVISIBLE);
			findViewById(R.id.tvReadingComprehensionLock).setVisibility(View.VISIBLE);
		}
		if (progress < ReadResult.PROGRESS_L3) {
			findViewById(R.id.ivWordsLLKNext).setVisibility(View.INVISIBLE);
			findViewById(R.id.tvWordsLLKLock).setVisibility(View.VISIBLE);
		}
		if (progress < ReadResult.PROGRESS_L2) {
			findViewById(R.id.ivLearnWordsNext).setVisibility(View.INVISIBLE);
			findViewById(R.id.tvLearnWordsLock).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onResume() {
		refreshUI();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.rl1:
			AtyRead.startAty(this);
			break;
		case R.id.rl2:
			if (progress >= ReadResult.PROGRESS_L2) {
				AtyReadLesson.startAty(this);
			}
			else {
				Toast.makeText(this, getString(R.string.readHomeLockTip), Toast.LENGTH_LONG).show();
			}
//			AtyReadLesson.startAty(this);
			break;
		case R.id.rl3:
			if (progress >= ReadResult.PROGRESS_L3) {
				AtyLLK.startAty(this);
			}
			else {
				Toast.makeText(this, getString(R.string.readHomeLockTip), Toast.LENGTH_LONG).show();
			}
//			AtyLLK.startAty(this);
			break;
		case R.id.rl4:
			if (progress >= ReadResult.PROGRESS_L4) {
				AtyReadQuestion.startAty(this);
			}
			else {
				Toast.makeText(this, getString(R.string.readHomeLockTip), Toast.LENGTH_LONG).show();
			}
//			AtyReadQuestion.startAty(this);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/*******************************************************************************************************************************************/

	public static void startAty(Context context , Long readNo) {
		context.startActivity(new Intent(context, AtyReadHome.class).putExtra("readNo", readNo));
	}

}
