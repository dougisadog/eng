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
import android.widget.ListView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType1;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackResDownload;
import com.shuangge.english.support.service.Type4DownLoadService;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.dialog.DialogConfirmFragment;
import com.shuangge.english.view.component.tree.INode;
import com.shuangge.english.view.download.adapter.AdapterDownload;
import com.shuangge.english.view.download.adapter.AdapterDownload.OnDownloadAllListener;
import com.shuangge.english.view.download.adapter.AdapterDownload.ViewHolder;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyDownload2 extends AbstractAppActivity implements OnClickListener, OnItemClickListener, OnDownloadAllListener {
	
	public static final int REQUEST_DONWLOADS = 1042;

	private ImageButton btnBack;
	
	private ListView lvDownloads;
	
	private AdapterDownload mAdapter;
	
	private String selectedId;
	
	private int allDownloadType2Size = 0;
	private int finishedDownloadType2Size = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	   
    @Override
    protected void onDestroy() {
    	super.onDestroy();
//    	GlobalResTypes.getInstance().clearCallbackType2();
//    	GlobalResTypes.getInstance().clearCallbackType4();
    	
//    	stopService(new Intent(this, Type4DownLoadService.class));
    }
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	GlobalResTypes.getInstance().setCallBackType4(callbackResDownload);
    	
    	if (null == selectedId) {
    		return;
    	}
		EntityResType2 type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(selectedId);
		if (null == type2) {
			return;
		}
		ViewHolder holder = mAdapter.getAllViewMap().get(type2.getId());
		if (null == holder) {
			return;
		}
		mAdapter.refreshStatus(type2, holder);
    }
    
	private CallbackResDownload callbackResDownload = new CallbackResDownload() {
		
		@Override
		public void progressHandler(String id, long progress, long max) {
		}
		
		@Override
		public void errorHandler(String id) {
		}
		
		@Override
		public void finishedHandler(String id) {
			if (null == mAdapter) {
				return;
			}
			EntityResType4 type4 = GlobalResTypes.ALL_TYPE4S_MAP.get(id);
			if (null == type4) {
				return;
			}
			EntityResType2 type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(type4.getParentId());
			if (null == type2) {
				return;
			}
			ViewHolder holder = mAdapter.getAllViewMap().get(type2.getId());
			if (null == holder) {
				return;
			}
			mAdapter.refreshStatus(type2, holder);
//			if (GlobalResTypes.getInstance().getType4QueueSize() == 0) {
//				stopService(new Intent(AtyDownload2.this, Type4DownLoadService.class));
//			}
		}

		@Override
		public void stopHandler(String id, long progress, long max) {
//			if (GlobalResTypes.getInstance().getType4QueueSize() == 0) {
//				stopService(new Intent(AtyDownload2.this, Type4DownLoadService.class));
//			}
		}

		@Override
		public void startHandler(String id, long max) {
		}

		@Override
		public void waitingHanler(String id) {
		}
		
	}; 
	
	private CallbackResDownload callbackResDownload2 = new CallbackResDownload() {
		
		@Override
		public void progressHandler(String id, long progress, long max) {
		}
		
		@Override
		public void errorHandler(String id) {
			hideLoading();
		}
		
		@Override
		public void finishedHandler(String id) {
			if (++finishedDownloadType2Size == allDownloadType2Size) {
				refreshData();
				hideLoading();
			}
		}
		
		@Override
		public void stopHandler(String id, long progress, long max) {
			
		}
		
		@Override
		public void startHandler(String id, long max) {
		}
		
		@Override
		public void waitingHanler(String id) {
		}
		
	}; 
	
	protected void initData() {
		super.initData();
		setContentView(R.layout.aty_download2);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		lvDownloads = (ListView) findViewById(R.id.lvDownloads);
		refreshData();
		GlobalResTypes.getInstance().setCallBackType4(callbackResDownload);
	}
	
	protected void refreshData() {
		//检测type2s 数据是否已加载
		EntityResType2 t2 = null;
		List<EntityResType2> unfinishedType2s = new ArrayList<EntityResType2>();
		for (String key : GlobalResTypes.ALL_TYPE2S_MAP.keySet()) {
			t2 = GlobalResTypes.ALL_TYPE2S_MAP.get(key);
			if (!t2.isFinished())
				unfinishedType2s.add(t2);
		}
		if (unfinishedType2s.size() > 0) {
			allDownloadType2Size = unfinishedType2s.size();
			showLoading(getString(R.string.loaddingTip2));
			GlobalResTypes.getInstance().setCallBackType2(callbackResDownload2);
			for (EntityResType2 entityResType2 : unfinishedType2s) {
				GlobalResTypes.getInstance().startDownload(entityResType2);
			}
		}
		String userLessonType1 = GlobalRes.getInstance().getBeans().getDefaultLessonId();
		EntityResType1 type1 = null;
		List<EntityResType2> type2s = new ArrayList<EntityResType2>();
		for (String key : GlobalResTypes.ALL_TYPE1S_MAP.keySet()) {
			type1 = GlobalResTypes.ALL_TYPE1S_MAP.get(key);
			if (type1.isBase() && !type1.getId().equals(userLessonType1)) {
				continue;
			}
			for (EntityResType2 type2 : type1.getType2s()) {
				type2s.add(type2);
			}
		}
		try {
			mAdapter = new AdapterDownload(this, this, new AdapterDownload.OnBeforeDownloadListener() {

				@Override
				public void onBeforeDownload(int type, String id) {
					networkCheckDialog(type, id);
				}
				
			});
			mAdapter.getDatas().addAll(type2s);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		lvDownloads.setAdapter(mAdapter);
		lvDownloads.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectedId = mAdapter.getItem(position).getId();
		AtyDownload2Details.startAty(this, selectedId);
	}
	
	@Override
	public void onDownloadAll(final String id) {
		downLoadAll(id);
	}
	
	private void downLoadAll(String id) {
		EntityResType2 type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(id);
		List<EntityResType4> entitys = type2.getType4s();
		if (null == entitys) {
			return;
		}
		type2.setDownloadAllStatus(EntityResType2.STATUS_START);
		ViewHolder holder = mAdapter.getAllViewMap().get(id);
		if (null != holder) {
			mAdapter.refreshStatus(type2, holder);
		}
		
		List<String> ids = new ArrayList<String>();
		for (EntityResType4 entity : entitys) {
			if (entity.getStatus() == EntityResType4.STATUS_NORMAL || entity.getStatus() == EntityResType4.STATUS_STOP) {
				entity.setStatus(INode.STATUS_CONNECTING);
//				GlobalResTypes.getInstance().startDownload(entity.getId());
				
				ids.add(entity.getId());
//				GlobalResTypes.getInstance().startDownloadWithService(this, entity.getId());
			}
		}
		GlobalResTypes.getInstance().startDownloadWithService(this, ids);
	}
	@Override
	public void onCancel(String id) {
		EntityResType2 type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(id);
		List<EntityResType4> entitys = type2.getType4s();
		if (null == entitys) {
			return;
		}
		type2.setDownloadAllStatus(EntityResType2.STATUS_STOP);
		ViewHolder holder = mAdapter.getAllViewMap().get(id);
		if (null != holder) {
			mAdapter.refreshStatus(type2, holder);
		}
		
		List<String> ids = new ArrayList<String>();
		for (EntityResType4 entity : entitys) {
			if (entity.getStatus() == EntityResType4.STATUS_START 
					|| entity.getStatus() == EntityResType4.STATUS_WAIT 
					|| entity.getStatus() == EntityResType4.STATUS_CONNECTING) {
				entity.setStatus(INode.STATUS_STOP);
//				GlobalResTypes.getInstance().stopDownload(entity.getId());
			
				ids.add(entity.getId());
//				GlobalResTypes.getInstance().stopDownloadWithService(this, entity.getId());
			}
		}
		GlobalResTypes.getInstance().stopDownloadWithService(this, ids);
//		GlobalResTypes.getInstance().stopAllQueueDownload();
		return;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBack:
			this.finish();
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
					if (type != 0) downLoadAll(id);
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
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyDownload2.class);
		context.startActivityForResult(i, REQUEST_DONWLOADS);
	}

}
