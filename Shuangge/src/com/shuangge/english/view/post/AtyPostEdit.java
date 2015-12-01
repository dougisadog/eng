package com.shuangge.english.view.post;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.shuangge.english.network.post.TaskReqPostCreate;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.post.UpdatePostData;
import com.shuangge.english.support.service.BaseTask.CallbackNoticeView;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.EditTextWidthTips;
import com.shuangge.english.view.component.photograph.PhotosEditContainer;
import com.shuangge.english.view.group.AtyClassHome;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyPostEdit extends AbstractAppActivity implements OnClickListener {

	public static final int REQUEST_EDIT_POST_CLASS = 1001;
	public static final int REQUEST_EDIT_POST_MINE = 1011;
	
	private boolean requesting = false;

	private ImageButton btnBack;
	private ImageView btnSave;
	private EditTextWidthTips txtTitle, txtContent;
	private PhotosEditContainer photosEditContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_post_edit);

		// MARGIN = DensityUtils.dip2px(AtyUserInfo.this, MARGIN);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		txtTitle = (EditTextWidthTips) findViewById(R.id.txtTitle);
		txtContent = (EditTextWidthTips) findViewById(R.id.txtContent);

		photosEditContainer = (PhotosEditContainer) findViewById(R.id.photosEditContainer);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		photosEditContainer.recycle();
	}

	private void requestData() {
		if (requesting) {
			return;
		}
		UpdatePostData data = GlobalReqDatas.getInstance().getUpdatePostData();
		data.setTitle(txtTitle.getVal());
		data.setContent(txtContent.getVal());

		data.clear();
		data.setPhotoDatas(photosEditContainer.getDatas());

		showLoading();
		new TaskReqPostCreate(0, new CallbackNoticeView<Void, Boolean>() {

			@Override
			public void refreshView(int tag, Boolean result) {
				hideLoading();
				requesting = false;
				if (null != result && result) {
					Toast.makeText(AtyPostEdit.this, getString(R.string.postSuccessTip), Toast.LENGTH_SHORT).show();
					setResult(AtyClassHome.CODE_SEND_POST);
					AtyPostEdit.this.finish();
				}
			}

			@Override
			public void onProgressUpdate(int tag, Void[] values) {
			}

		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PhotosEditContainer.MODULE_PHOTO:
			photosEditContainer.onActivityResult(resultCode, data);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.btnSave:
			requestData();
			break;
		}
	}

	/************************************************************************************************/
	
	public static void startAtyForClass(Activity context) {
		Intent i = new Intent(context, AtyPostEdit.class);
		context.startActivityForResult(i, REQUEST_EDIT_POST_CLASS);
	}
	
	public static void startAtyForMe(Activity context) {
		Intent i = new Intent(context, AtyPostEdit.class);
		context.startActivityForResult(i, REQUEST_EDIT_POST_MINE);
	}
	
}
