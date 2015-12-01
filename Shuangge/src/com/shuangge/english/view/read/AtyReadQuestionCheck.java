package com.shuangge.english.view.read;

import java.util.List;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.server.read.ReadInitResult;
import com.shuangge.english.entity.server.read.ReadListData;
import com.shuangge.english.entity.server.read.ReadQuestionData;
import com.shuangge.english.entity.server.read.ReadResult;
import com.shuangge.english.network.read.TaskReqReadList;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.read.component.ReadSimpleQuestion;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 阅读理解
 * @author doug
 *
 */
public class AtyReadQuestionCheck extends AbstractAppActivity implements OnClickListener {

	public static boolean isActive = true;
	
	private LinearLayout llContainer;
	public List<String> answerList;
	
	public static String CHECK_RESULT = "checkResult";
	
	private Long readNo; //目标文章id
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_read_question_check);
        
        findViewById(R.id.btnBack).setOnClickListener(this);
		findViewById(R.id.txtRetry).setOnClickListener(this);
		findViewById(R.id.txtExit).setOnClickListener(this);
		findViewById(R.id.txtNext).setOnClickListener(this);
        if (this.getIntent().getBooleanExtra(CHECK_RESULT, false)) {
        	findViewById(R.id.questionScroll).setVisibility(View.GONE);
        	findViewById(R.id.correctImage).setVisibility(View.VISIBLE);
        	return;
        }
        findViewById(R.id.questionScroll).setVisibility(View.VISIBLE);
    	findViewById(R.id.correctImage).setVisibility(View.GONE);
        
        llContainer = (LinearLayout) findViewById(R.id.llQuestionContainer);
        
        ReadResult result = GlobalRes.getInstance().getBeans().getReadData();
		List<ReadQuestionData> readQuestionDatas = result.getQuestions();
        for (int i = 0; i < readQuestionDatas.size(); i++) {
        	llContainer.addView(new ReadSimpleQuestion(this, i, readQuestionDatas.get(i)), i);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		llContainer = null;
	}
	
	
	public void notice() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.txtRetry:
//			Toast.makeText(AtyReadQuestionCheck.this, "Retry", Toast.LENGTH_SHORT).show();
			GlobalRes.getInstance().getBeans().getReadData().setReset(true);
			finish();
			break;	
		case R.id.txtExit:
//			Toast.makeText(AtyReadQuestionCheck.this, "Exit", Toast.LENGTH_SHORT).show();
			
			new TaskReqReadList(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					if (null == result || !result) {
						return;
					}
					AtyReadList.startAty(AtyReadQuestionCheck.this, ReadInitResult.DEFAULT_READNO);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
			
			break;
		case R.id.txtNext:
//			Toast.makeText(AtyReadQuestionCheck.this, "Next", Toast.LENGTH_SHORT).show();
			
			CacheBeans beans = GlobalRes.getInstance().getBeans();
			List<ReadListData> datas = beans.getReadListData().getDatas();
			for (int i = 0; i < datas.size(); i++) {
				if (beans.getReadData().getReadNo().longValue() == datas.get(i).getReadNo().longValue()) {
					if (i < datas.size()) {
						readNo =  datas.get(i+1).getReadNo();
					}
					else {
						readNo = ReadInitResult.DEFAULT_READNO;
					}
				}
			}
//					indexOf(GlobalRes.getInstance().getBeans().g)
			new TaskReqReadList(0, new CallbackNoticeView<Void, Boolean>() {

				@Override
				public void refreshView(int tag, Boolean result) {
					if (null == result || !result) {
						return;
					}
					AtyReadList.startAty(AtyReadQuestionCheck.this, readNo);
				}

				@Override
				public void onProgressUpdate(int tag, Void[] values) {
				}
				
			});
			break;
		default:
			break;
		}
	}


	/*******************************************************************************************************************************************/

	public static void startAty(Context context, boolean checkResult) {
		Intent intent = new Intent(context, AtyReadQuestionCheck.class);
		intent.putExtra("checkResult", checkResult);
		context.startActivity(intent);
	}

    public boolean onBack() {
		return false;
    }
    
    
    
}