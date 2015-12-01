package com.shuangge.english.view.lesson;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants.UnlockType;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.server.lesson.Type2Data;
import com.shuangge.english.entity.server.lesson.Type4Data;
import com.shuangge.english.entity.server.user.LessonTips;
import com.shuangge.english.network.lesson.TaskReqLessonUnlock;
import com.shuangge.english.network.lesson.TaskReqLessonUnlockGuide;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogAlertFragment1;
import com.shuangge.english.view.component.dialog.DialogAlertFragment1.CallBackDialogConfirm;
import com.shuangge.english.view.component.dialog.DialogUnlock3BtnFragment;
import com.shuangge.english.view.component.dialog.DialogUnlock3BtnFragment.Unlock3BtnCallBackDialogConfirm;
import com.shuangge.english.view.component.dialog.DialogUnlockFragment;
import com.shuangge.english.view.component.dialog.DialogUnlockFragment.UnlockCallBackDialogConfirm;
import com.shuangge.english.view.lesson.adapter.AdapterType4;
import com.shuangge.english.view.lesson.component.DialogLessonUnlockTipFragment;
import com.shuangge.english.view.menu.AtyInvite;
import com.shuangge.english.view.shop.AtyShopList;

/**
 * 课程列表
 * @author Jeffrey
 *
 */
public class AtyType4s extends AbstractAppActivity implements OnClickListener, OnItemClickListener {

	private ImageButton btnBack;

	private ListView lv;
	private AdapterType4 adapter;
	private TextView txtKeyNum;
	private ImageView imageView1;
	private DialogAlertFragment1 tipDialog;
	private DialogUnlock3BtnFragment unlockDialog;
	private DialogUnlockFragment unlockDialog1;
	private DialogLessonUnlockTipFragment lessonTipDialog;
	private String type4Id;
	private LessonTips lessonTips;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_lesson_type4s);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		txtKeyNum = (TextView) findViewById(R.id.txtKeyNum);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		txtKeyNum.setOnClickListener(this);
		imageView1.setOnClickListener(this);

		lv = (ListView) findViewById(R.id.lv);
		adapter = new AdapterType4(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		refreshView();
	}
	
	private void refreshView() {
		if (getBeans().getCurrentType4Datas() == null)
			return;
		txtKeyNum.setText(!TextUtils.isEmpty(getBeans().getLoginData().getInfoData().getKeyNum().toString()) ? 
				getBeans().getLoginData().getInfoData().getKeyNum().toString() : "0");
		adapter.getDatas().clear();
		adapter.getDatas().addAll(getBeans().getCurrentType4Datas());
		adapter.notifyDataSetChanged();
		
		boolean goOn = getIntent().getBooleanExtra("goOn", false);
		if (goOn) {
			int position = 0; 
			for (EntityResType4 type4 : getBeans().getCurrentType4Datas()) {
				if (!type4.getId().equals(GlobalRes.getInstance().getBeans().getLastType4Id())) {
					position++;
					continue;
				}
				lv.setSelection(position);
				startLesson(position, true);
				break;
			}
		}
		lessonTips = GlobalRes.getInstance().getBeans().getLoginData().getSettingsData().getLessonTips();
		if (lessonTips.getKeyTip().equals("0")) {
			showTip();
		}
	}
	
	private void showTip() {
		if (null == lessonTipDialog) {
			String title = "课程解锁需要钥匙";
			String str = "钥匙获取： " + "\n" + "\n" + "         1.邀请好友注册绑定手机成功" + "\n" + "\n" + "         2.商城购买";
			
			lessonTipDialog = new DialogLessonUnlockTipFragment(new DialogLessonUnlockTipFragment.CallBackDialog() {
				
				@Override
				public void onOk() {
					if (null != lessonTipDialog) {
						lessonTipDialog.dismiss();
						lessonTipDialog = null;
						lessonTips.setKeyTip("1");
						new TaskReqLessonUnlockGuide(0, new CallbackNoticeView<Void, Boolean>() {

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

						}, lessonTips.getKeyTip());
					}
				}
				
			}, title, str);
		}
		if (lessonTipDialog.isVisible()) {
			return;
		}
		lessonTipDialog.setCancelable(false);
		lessonTipDialog.showDialog(getSupportFragmentManager());
			
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.txtKeyNum:
		case R.id.imageView1:
			showTipDialog();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		EntityResType4 entity = adapter.getDatas().get(position);
		if (null == entity) {
			return;
		}
		Type4Data type4 = GlobalRes.getInstance().getBeans().getUnlockDatas().getType4s().get(entity.getId());
		Type2Data type2 = GlobalRes.getInstance().getBeans().getUnlockDatas().getType2s().get(GlobalRes.getInstance().getBeans().getCurrentType2Id());
		
		if (null == type4) {
			if (UnlockType.auto.equals(type2.getUnlockType())) {
				Toast.makeText(this, R.string.lessonTip2, Toast.LENGTH_LONG).show();
				return;
			}
			if (GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getKeyNum() == 0) {
				showUnlockDialog();
				return;
			}
			type4Id = entity.getId();
			showUnlockDialog1();
//			unlockLesson(entity.getId(), position);
//			Toast.makeText(this, R.string.lessonTip2, Toast.LENGTH_LONG).show();
			return;
		}
		startLesson(position, false);
	}
	
	private void unlockLesson(String type4Id, final int position) {
		showLoading();
		new TaskReqLessonUnlock(0, new CallbackNoticeView<Void, Integer>() {

			@Override
			public void refreshView(int tag, Integer result) {
				hideLoading();
				if(result == null) {
					return;
				}
				switch (result) {
				case 0:
//					startLesson(position, false);
					Toast.makeText(AtyType4s.this, R.string.lessonTip6, Toast.LENGTH_LONG).show();
					AtyType4s.this.refreshView();
					break;
				case 1001:
					Toast.makeText(AtyType4s.this, R.string.lessonTip4, Toast.LENGTH_LONG).show();
					break;
				case 1002:
					Toast.makeText(AtyType4s.this, R.string.lessonTip5, Toast.LENGTH_LONG).show();
					break;
				}
				
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}
		}, type4Id);
	}
	
	private void startLesson(int position, boolean goOn) {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		EntityResType4 type4 = adapter.getItem(position);
		beans.setCurrentType4Data(type4);
		beans.setCurrentType4Id(type4.getId());
		adapter.onSelected(position);
		Intent i = new Intent(this, AtyType5s.class);
		i.putExtra("goOn", goOn);
		startActivityForResult(i, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		if (beans.getCurrentType4Data() != null) {			
			beans.getCurrentType4Data().refreshStar();
			adapter.notifyDataSetChanged();
		}
	}
	
	private void showUnlockDialog() {
		String str = "您当前没有钥匙，是否去获取";
		unlockDialog = new DialogUnlock3BtnFragment(callback3, str, "", 0);
		unlockDialog.showDialog(getSupportFragmentManager());
	}
	
	private void showUnlockDialog1() {
		String str = "您现在还拥有钥匙*" + GlobalRes.getInstance().getBeans().getLoginData().getInfoData().getKeyNum() + "，解锁本课程需要 钥匙*1，是否解锁？";
		unlockDialog1 = new DialogUnlockFragment(callback1, str, "", 0);
		unlockDialog1.showDialog(getSupportFragmentManager());
	}
	
	private void showTipDialog() {
		String str = "钥匙功能： 解锁新课程" + "\n" + "\n" + "获取途径：邀请新成员注册绑定⼿机成功后 获得，或者在商城购买获得";
		tipDialog = new DialogAlertFragment1(callback2, str, "", 0);
		tipDialog.showDialog(getSupportFragmentManager());
	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
//			showLoading();
			if(tipDialog != null) {
				tipDialog.dismiss();
				tipDialog = null;
			}
		}
		
		@Override
		public void onKeyBack() {
			if(tipDialog != null) {
				tipDialog.dismiss();
				tipDialog = null;
			}
		}
		
	};
	
	private Unlock3BtnCallBackDialogConfirm callback3 = new Unlock3BtnCallBackDialogConfirm() {

		@Override
		public void onKeyBack() {
			hideDialog();
		}

		@Override
		public void onBtn1() {
			hideDialog();
		}

		@Override
		public void onBtn2() {
			AtyShopList.startAty(AtyType4s.this);
			hideDialog();
		}

		@Override
		public void onBtn3() {
			AtyInvite.startAty(AtyType4s.this);
			hideDialog();
		}
		
	};
	
	private void hideDialog() {
		if(unlockDialog != null) {
			unlockDialog.dismiss();
			unlockDialog = null;
		}
	}
	
	private UnlockCallBackDialogConfirm callback1 = new UnlockCallBackDialogConfirm() {

		@Override
		public void onKeyBack() {
			hideDialog1();
		}

		@Override
		public void onCancel() {
			hideDialog1();
		}

		@Override
		public void onSubmit(int position) {
			hideDialog1();
			if (!TextUtils.isEmpty(type4Id)) {
				unlockLesson(type4Id, position);
			}
		}
		
	};
	
	private void hideDialog1() {
		if(unlockDialog1 != null) {
			unlockDialog1.dismiss();
			unlockDialog1 = null;
		}
	}
	
	

}
