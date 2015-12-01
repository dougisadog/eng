package com.shuangge.english.view.read;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.ReadInitResult;
import com.shuangge.english.network.read.TaskReqReadList;
import com.shuangge.english.network.read.TaskReqReadOverView;
import com.shuangge.english.network.read.TaskReqReadWordList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.ComponentTitleBar;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment.CallBackDialogConfirm;

/**
 * 四级课程 主入口
 * @author tovey
 */
public class AtyReadFourth extends AbstractAppActivity implements OnClickListener {
	
	
	private TextView tvHasBeenLearnNumber, 
	tvAllLearnNumber, tvPrimaryHasLearnWord,tvPrimaryAllWord,
	tvAdvancedHasLearnWord, tvAdvancedAllWord;
	private ImageButton btnBack;
	private ImageView ibPrimaryNext, ibAdvancedNext;
	private LinearLayout ibPrimaryNextParent, ibAdvancedNextParent;
	private ProgressBar pbScheduleOfLearn, pbPrimary, pbAdvanced;
	
	private ComponentTitleBar titleBar;
	
	private ReadInitResult readInitData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_read_fourth);
		titleBar = (ComponentTitleBar) findViewById(R.id.titleBar);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		
		//已学单词 的 文本显示 和 进度条的显示
		tvHasBeenLearnNumber = (TextView) findViewById(R.id.tvHasBeenLearnNumber);
		tvAllLearnNumber = (TextView) findViewById(R.id.tvAllLearnNumber);
		pbScheduleOfLearn = (ProgressBar) findViewById(R.id.pbScheduleOfLearn);   
		
		//文章阅读进度的控件
		
		//初级
		pbPrimary = (ProgressBar) findViewById(R.id.pbPrimary);
		tvPrimaryHasLearnWord = (TextView) findViewById(R.id.tvPrimaryHasLearnWord);
		tvPrimaryAllWord = (TextView) findViewById(R.id.tvPrimaryAllWord);
		ibPrimaryNext = (ImageView) findViewById(R.id.ibPrimaryNext);
		ibPrimaryNextParent = (LinearLayout) findViewById(R.id.ibPrimaryNextParent);
		
		
		//高级
		pbAdvanced = (ProgressBar) findViewById(R.id.pbAdvanced);
		tvAdvancedHasLearnWord = (TextView) findViewById(R.id.tvAdvancedHasLearnWord);
		tvAdvancedAllWord = (TextView) findViewById(R.id.tvAdvancedAllWord);
		ibAdvancedNext = (ImageView) findViewById(R.id.ibAdvancedNext);
		ibAdvancedNextParent = (LinearLayout) findViewById(R.id.ibAdvancedNextParent);
		
		//开始学习
		Button btnStartLearnEnglish = (Button) findViewById(R.id.btnStartLearnEnglish);
		//自主复习
		Button btnWrongWords = (Button) findViewById(R.id.btnWrongWords);
		btnWrongWords.setText(R.string.readCertificateWrongWordBtn);
		
		titleBar.setTitle(getString(R.string.readCertificateTitleEn), getString(R.string.readCertificateTitleCn));
		
		
		//以下是点击事件
		btnBack.setOnClickListener(this);                       //返回键
//		ibPrimaryNext.setOnClickListener(this);					//初级 下一个
		ibPrimaryNextParent.setOnClickListener(this);
		ibAdvancedNextParent.setOnClickListener(this);				//高级 下一个
		btnStartLearnEnglish.setOnClickListener(this);			//开始学习
		btnWrongWords.setOnClickListener(this);				//自主复习
		refreshData();
	}
	
	protected void refreshData() {
		readInitData = GlobalRes.getInstance().getBeans().getReadInitResult();
		
		if (null == readInitData || null == readInitData.getLearnWordNum()) return ;
		int total = readInitData.getWordNum();
		//单词进度
		tvHasBeenLearnNumber.setText("" + readInitData.getLearnWordNum());
		tvAllLearnNumber.setText("" + total);
		int pbScheduleOfLearnProgress = 0;
		if (total > 0) {
			pbScheduleOfLearnProgress = readInitData.getLearnWordNum() * pbScheduleOfLearn.getMax() / total;
		}
		pbScheduleOfLearn.setProgress(pbScheduleOfLearnProgress);
		
		tvPrimaryHasLearnWord.setText("" + readInitData.getLearnEasyNum());
		tvPrimaryAllWord.setText("" + readInitData.getEasyNum());
		int pbPrimaryProgress = 0;
		if (readInitData.getEasyNum() > 0) {
			pbPrimaryProgress = readInitData.getLearnEasyNum() * pbPrimary.getMax() / readInitData.getEasyNum();
		}
		pbPrimary.setProgress(pbPrimaryProgress);
		
		tvAdvancedHasLearnWord.setText("" + readInitData.getLearnHardNum());
		tvAdvancedAllWord.setText("" + readInitData.getHardNum());
		int pbAdvancedProgress = 0;
		if (readInitData.getNormalNum() > 0) {
			pbAdvancedProgress = readInitData.getLearnHardNum() * pbPrimary.getMax() / readInitData.getHardNum();
		}
		pbAdvanced.setProgress(pbAdvancedProgress);
		
	}

	


	@Override
	protected void onResume() {
		super.onResume();
		showLoading();
		new TaskReqReadOverView(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					Toast.makeText(AtyReadFourth.this, getString(R.string.timeout), Toast.LENGTH_SHORT).show();
					return;
				}
				refreshData();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
		return;
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
//		case R.id.fourthWordList:
//			AtyWordList.startAty(AtyReadFourth.this);
//			break;
//		case R.id.ibPrimaryNext:
		case R.id.ibPrimaryNextParent:
			//TODO 初级 - 文章阅读进度
			showLoading();
			new TaskReqReadList(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						return;
					}
					AtyReadList.startAty(AtyReadFourth.this, ReadInitResult.DEFAULT_READNO);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
			
			break;
		case R.id.ibAdvancedNextParent:
			//TODO 高级 - 文章阅读进度
			Toast.makeText(this, "暂未开放", Toast.LENGTH_LONG).show();
			break;
		case R.id.btnStartLearnEnglish:
			onLearnWordsClicked();
			break;
		case R.id.btnWrongWords:
			//TODO 自主复习 
			
			onWrongWordsClikcked();
			break;

		default:
			break;
		}
		
	}
	
	private void onLearnWordsClicked() {
		//开始学习
		showLoading();
		new TaskReqReadList(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				AtyReadList.startAty(AtyReadFourth.this, readInitData.getReadNo());
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
			
		});
	}
	
	private void onWrongWordsClikcked() {
			Integer lastid = 1;
			showLoading();
			new TaskReqReadWordList(0, new CallbackNoticeView<Void, Boolean>() {
				
				@Override
				public void refreshView(int tag, Boolean result) {
					hideLoading();
					if (null == result || !result) {
						showDialogConfirm ();
						return;
					}
					AtyWordList.startAty(AtyReadFourth.this);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			}, lastid);
	}
	
	private DialogConfirmFragment dialogNoneWordsConfirm;
	
	private void showDialogConfirm () {
		dialogNoneWordsConfirm = new DialogConfirmFragment( new CallBackDialogConfirm() {
			
			@Override
			public void onSubmit(int position) {
				dialogNoneWordsConfirm.dismiss();
				dialogNoneWordsConfirm = null;
				onLearnWordsClicked();
			}
			
			@Override
			public void onCancel() {
				dialogNoneWordsConfirm.dismiss();
				dialogNoneWordsConfirm = null;
			}

			@Override
			public void onKeyBack() {
				onCancel();
			}
			
		}, getString(R.string.readFourNoneWords), "", 0 , getString(R.string.readFourNoneWordsBtn1) , getString(R.string.readFourNoneWordsBtn2));
		dialogNoneWordsConfirm.showDialog(getSupportFragmentManager());
		
	}
	
	public static void startAty(Context context) {
		context.startActivity(new Intent(context, AtyReadFourth.class));
	}
	

}
