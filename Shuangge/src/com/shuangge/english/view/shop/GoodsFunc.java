package com.shuangge.english.view.shop;

import com.shuangge.english.network.shop.TaskReqObtainLesson;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;

public class GoodsFunc extends AbstractAppActivity {
	
	public static interface CallBackFunc {
		public void onCallBack();
	}
	
	private CallBackFunc callBack;
	
	public GoodsFunc(){
	}
	
	public GoodsFunc(int func,CallBackFunc callBack) {
		this.callBack = callBack;
		//课程解锁1~99
		if (func < 100 && func > 0) {
			requestLessonData();
			return;
		}
//		switch (func) {
//			case 1:
//				requestLessonData();
//				break;
//			case 2:
//				requestLessonData();
//				break;
//			case 3:
//				requestLessonData();
//				break;
//		}
		
	}
	
	private void requestLessonData() {
		showLoading();
		new TaskReqObtainLesson(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				if (null == result || !result) {
					return;
				}
				callBack.onCallBack();
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
				
			}
		});
	}

}
