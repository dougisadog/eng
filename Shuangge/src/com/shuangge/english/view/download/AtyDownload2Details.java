package com.shuangge.english.view.download;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackResDownload;
import com.shuangge.english.support.service.Type4DownLoadService;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.tree.INode;
import com.shuangge.english.view.download.adapter.AdapterDownload;
import com.shuangge.english.view.download.adapter.AdapterDownloadDetails;
import com.shuangge.english.view.download.adapter.AdapterDownloadDetails.OnDownloadAllListener;
import com.shuangge.english.view.download.adapter.AdapterDownloadDetails.ViewHolder;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyDownload2Details extends AbstractAppActivity implements OnClickListener, OnItemClickListener, OnDownloadAllListener {
	
	public static final int REQUEST_DONWLOADS = 1042;
	public static final String PARAM_ID = "param id";

	private ImageButton btnBack;
	
	private ImageView imgIcon;
	private TextView txtName, txtLessonNum, txtDownloadAll, txtCancel, txtFinish;
	public ProgressBar pbDownloadAll;
	
	private ListView lvDownloads;
	
	private AdapterDownloadDetails mAdapter;
	
	private EntityResType2 type2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalResTypes.getInstance().clearCallbackType4();
	}
	
	private CallbackResDownload callbackResDownload = new CallbackResDownload() {
		
		@Override
		public void progressHandler(String id, long progress, long max) {
			ViewHolder holder = mAdapter.getAllViewMap().get(id);
			EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
			mAdapter.refreshStatus(entity, holder);
		}
		
		@Override
		public void errorHandler(String id) {
			ViewHolder holder = mAdapter.getAllViewMap().get(id);
			EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
			mAdapter.refreshStatus(entity, holder);
		}
		
		@Override
		public void finishedHandler(String id) {
			ViewHolder holder = mAdapter.getAllViewMap().get(id);
			EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
			mAdapter.refreshStatus(entity, holder);
			refreshStatus();
			
//			if (GlobalResTypes.getInstance().getType4QueueSize() == 0) {
//				stopService(new Intent(AtyDownload2Details.this, Type4DownLoadService.class));
//			}
		}

		@Override
		public void stopHandler(String id, long progress, long max) {
			ViewHolder holder = mAdapter.getAllViewMap().get(id);
			EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
			mAdapter.refreshStatus(entity, holder);
			
//			if (GlobalResTypes.getInstance().getType4QueueSize() == 0) {
//				stopService(new Intent(AtyDownload2Details.this, Type4DownLoadService.class));
//			}
		}

		@Override
		public void startHandler(String id, long max) {
			ViewHolder holder = mAdapter.getAllViewMap().get(id);
			EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
			mAdapter.refreshStatus(entity, holder);
		}

		@Override
		public void waitingHanler(String id) {
			ViewHolder holder = mAdapter.getAllViewMap().get(id);
			EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
			mAdapter.refreshStatus(entity, holder);
		}
		
	};
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_download2_details);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		imgIcon = (ImageView) findViewById(R.id.imgIcon);
		txtName = (TextView) findViewById(R.id.txtName);
		txtLessonNum = (TextView) findViewById(R.id.txtLessonNum);
		txtDownloadAll = (TextView) findViewById(R.id.txtDownloadAll);
		txtDownloadAll.setOnClickListener(this);
		txtCancel = (TextView) findViewById(R.id.txtCancel);
		txtCancel.setOnClickListener(this);
		txtFinish = (TextView) findViewById(R.id.txtFinish);
		
		pbDownloadAll = (ProgressBar) findViewById(R.id.pbDownloadAll);
		
		lvDownloads = (ListView) findViewById(R.id.lvDownloads);
		
		String type2Id = getIntent().getStringExtra(PARAM_ID);
		if (null == type2Id) {
			return;
		}
		type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(type2Id);
		if (null == type2) {
			return;
		}
		try {
			mAdapter = new AdapterDownloadDetails(this, this, new AdapterDownload.OnBeforeDownloadListener() {

				@Override
				public void onBeforeDownload(int type, String id) {
					networkCheckDialog(type, id);
				}
				
			});
			mAdapter.getDatas().addAll(type2.getType4s());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		lvDownloads.setAdapter(mAdapter);
		lvDownloads.setOnItemClickListener(this);
		
		txtName.setText(type2.getName());
		txtLessonNum.setText(type2.getType4s().size() + " lessons");
		
		refreshStatus();
		
		GlobalResTypes.getInstance().setCallBackType4(callbackResDownload);
	}
	
	public void refreshStatus() {
		boolean isAllFinished = true;
		int finishedNum = 0;
		for (EntityResType4 entityResType4 : type2.getType4s()) {
			if (!entityResType4.isFinished()) {
				isAllFinished = false;
				continue;
			}
			++finishedNum;
		}
		if (isAllFinished) {
			txtFinish.setVisibility(View.VISIBLE);
			txtDownloadAll.setVisibility(View.GONE);
			txtCancel.setVisibility(View.GONE);
			pbDownloadAll.setVisibility(View.GONE);
		}
		else {
			txtFinish.setVisibility(View.GONE);
			if (type2.getDownloadAllStatus() == EntityResType2.STATUS_START) {
				txtDownloadAll.setVisibility(View.GONE);
				txtCancel.setVisibility(View.VISIBLE);
				pbDownloadAll.setVisibility(View.VISIBLE);
				pbDownloadAll.setProgress(finishedNum);
				pbDownloadAll.setMax(type2.getType4s().size());
			}
			else {
				txtDownloadAll.setVisibility(View.VISIBLE);
				txtCancel.setVisibility(View.GONE);
				pbDownloadAll.setVisibility(View.GONE);	
			}
		}
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		EntityResType4 entity =  mAdapter.getItem(position);
		if (entity.getStatus() == EntityResType4.STATUS_NORMAL || entity.getStatus() == EntityResType4.STATUS_STOP) {
			entity.setStatus(INode.STATUS_CONNECTING);
			
//			GlobalResTypes.getInstance().startDownload(entity.getId());
			List<String> ids = new ArrayList<String>();
			ids.add(entity.getId());
			GlobalResTypes.getInstance().startDownloadWithService(this, ids);
			
			ViewHolder holder = mAdapter.getAllViewMap().get(entity.getId());
			if (null != holder) {
				mAdapter.refreshStatus(entity, holder);
			}
		}
		else if (entity.getStatus() == EntityResType4.STATUS_START 
				|| entity.getStatus() == EntityResType4.STATUS_WAIT 
				|| entity.getStatus() == EntityResType4.STATUS_CONNECTING) {
			entity.setStatus(INode.STATUS_STOP);
			
//			GlobalResTypes.getInstance().stopDownload(entity.getId());
			List<String> ids = new ArrayList<String>();
			ids.add(entity.getId());
			GlobalResTypes.getInstance().stopDownloadWithService(this, ids);
			
			ViewHolder holder = mAdapter.getAllViewMap().get(entity.getId());
			if (null != holder) {
				mAdapter.refreshStatus(entity, holder);
			}
		}
		
	}
	
	private void downLoad(String id) {
		EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
		
		List<String> ids = new ArrayList<String>();
		if (entity.getStatus() == EntityResType4.STATUS_NORMAL || entity.getStatus() == EntityResType4.STATUS_STOP) {
			entity.setStatus(INode.STATUS_CONNECTING);
			
			ids.add(entity.getId());
			
			ViewHolder holder = mAdapter.getAllViewMap().get(entity.getId());
			if (null != holder) {
				mAdapter.refreshStatus(entity, holder);
			}
		}
		GlobalResTypes.getInstance().startDownloadWithService(this, ids);		
	}
	
	@Override
	public void onDownload(String id) {
		downLoad(id);
	}
	
	@Override
	public void onCancel(String id) {
		EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
		
		List<String> ids = new ArrayList<String>();
		if (entity.getStatus() == EntityResType4.STATUS_START 
				|| entity.getStatus() == EntityResType4.STATUS_WAIT 
				|| entity.getStatus() == EntityResType4.STATUS_CONNECTING) {
			entity.setStatus(INode.STATUS_STOP);
			
//			GlobalResTypes.getInstance().stopDownload(entity.getId());
			ids.add(entity.getId());
			
			ViewHolder holder = mAdapter.getAllViewMap().get(entity.getId());
			if (null != holder) {
				mAdapter.refreshStatus(entity, holder);
			}
		}
		GlobalResTypes.getInstance().stopDownloadWithService(this, ids);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		case R.id.txtDownloadAll:
			List<EntityResType4> entitys = type2.getType4s();
			if (null == entitys) {
				break;
			}
			type2.setDownloadAllStatus(EntityResType2.STATUS_START);
			ViewHolder holder = null;
			
			List<String> ids = new ArrayList<String>();
			for (EntityResType4 entity : entitys) {
				if (entity.getStatus() == EntityResType4.STATUS_NORMAL || entity.getStatus() == EntityResType4.STATUS_STOP) {
					entity.setStatus(INode.STATUS_CONNECTING);
					
//					GlobalResTypes.getInstance().startDownload(entity.getId());
					
					ids.add(entity.getId());
					
					holder = mAdapter.getAllViewMap().get(entity.getId());
					if (null != holder) {
						mAdapter.refreshStatus(entity, holder);
					}
				}
			}
			GlobalResTypes.getInstance().startDownloadWithService(this, ids);
			refreshStatus();
			break;
		case R.id.txtCancel:
			entitys = type2.getType4s();
			if (null == entitys) {
				break;
			}
			type2.setDownloadAllStatus(EntityResType2.STATUS_STOP);
			
			List<String> stopIds = new ArrayList<String>();
			for (EntityResType4 entity : entitys) {
				if (entity.getStatus() == EntityResType4.STATUS_START 
						|| entity.getStatus() == EntityResType4.STATUS_WAIT 
						|| entity.getStatus() == EntityResType4.STATUS_CONNECTING) {
					entity.setStatus(INode.STATUS_STOP);
					
					stopIds.add(entity.getId());
//					GlobalResTypes.getInstance().stopDownload(entity.getId());
					
					holder = mAdapter.getAllViewMap().get(entity.getId());
					if (null != holder) {
						mAdapter.refreshStatus(entity, holder);
					}
				}
			}
			GlobalResTypes.getInstance().stopDownloadWithService(this, stopIds);
			refreshStatus();
			break;
		}
	}
	
	private DialogConfirmFragment networkCheckDialog; //下载点击弹出框
	private void networkCheckDialog(final int type, final String id) {
		hideLoading();
		String title = "";
		switch (type) {
		case 0: 
			title = getString(R.string.timeout);
			break;
		case 1:
			title = getString(R.string.downLoadWithoutWifi);
			break;
		case 2:
			title = getString(R.string.downLoadWithWifi);
			break;
		}
		if (null == networkCheckDialog) {
			GlobalResTypes.getInstance().stopDownloadForWords();
			networkCheckDialog = new DialogConfirmFragment(new DialogConfirmFragment.CallBackDialogConfirm() {
				@Override
				public void onSubmit(int position) {
					if (null == networkCheckDialog) {
						return;
					}
					networkCheckDialog.dismiss();
					networkCheckDialog = null;
					if (type != 0) downLoad(id);
				}
				
				@Override
				public void onKeyBack() {
					finish();
				}

				@Override
				public void onCancel() {
					networkCheckDialog.dismiss();
					networkCheckDialog = null;
				}
			}, title, " ", 0);
		}
		if (networkCheckDialog.isVisible()) {
			return;
		}
		networkCheckDialog.setCancelable(false);
		networkCheckDialog.showDialog(getSupportFragmentManager());
	}


	/************************************************************************************************/
	
	public static void startAty(Activity context, String id) {
		Intent i = new Intent(context, AtyDownload2Details.class);
		i.putExtra(PARAM_ID, id);
		context.startActivityForResult(i, REQUEST_DONWLOADS);
	}
}
