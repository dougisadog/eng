package com.shuangge.english.view.download;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.entity.lesson.EntityResType1;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.GlobalResTypes.CallbackResDownload;
import com.shuangge.english.view.AbstractAppActivity;
import com.shuangge.english.view.component.tree.AdapterTreeListView.OnTreeNodeClickListener;
import com.shuangge.english.view.component.tree.INode;
import com.shuangge.english.view.component.tree.Node;
import com.shuangge.english.view.download.adapter.AdapterSimpleTree;
import com.shuangge.english.view.download.adapter.AdapterSimpleTree.CallbackTreeDownload;

/**
 * 
 * @author Jeffrey
 *
 */
public class AtyDownload extends AbstractAppActivity implements OnClickListener, OnTreeNodeClickListener, CallbackTreeDownload {
	
	public static final int REQUEST_DONWLOADS = 1042;

	private ImageButton btnBack;
	
	private ListView lvDownloads;
	private List<INode> entitys = new ArrayList<INode>();
	
	private AdapterSimpleTree mAdapter;
	
	private int allDownloadType2Size = 0;
	private int finishedDownloadType2Size = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	private CallbackResDownload callbackResDownload = new CallbackResDownload() {
		
		@Override
		public void progressHandler(String id, long progress, long max) {
			Node node = mAdapter.getAllNodeMap().get(id);
			if (node.getStatus() != INode.STATUS_START) {
				return;
			}
			node.setStatus(INode.STATUS_START);
			node.setProgress(progress);
			node.setMax(max);
			setViewData(node);
		}
		
		@Override
		public void errorHandler(String id) {
			Node node = mAdapter.getAllNodeMap().get(id);
			node.setProgress(0L);
			node.setProgress(1L);
			node.setStatus(INode.STATUS_NORMAL);
			setViewData(node);
		}
		
		@Override
		public void finishedHandler(String id) {
			Node node = mAdapter.getAllNodeMap().get(id);
			node.setStatus(INode.STATUS_FINISH);
			setViewData(node);
		}

		@Override
		public void stopHandler(String id, long progress, long max) {
			Node node = mAdapter.getAllNodeMap().get(id);
			node.setProgress(progress);
			node.setMax(max);
			node.setStatus(INode.STATUS_STOP);
		}

		@Override
		public void startHandler(String id, long max) {
			Node node = mAdapter.getAllNodeMap().get(id);
			node.setMax(max);
			node.setStatus(INode.STATUS_START);
			setViewData(node);
		}

		@Override
		public void waitingHanler(String id) {
			Node node = mAdapter.getAllNodeMap().get(id);
			node.setStatus(INode.STATUS_WAIT);
			setViewData(node);
		}
		
	}; 
	
	private CallbackResDownload callbackResDownload2 = new CallbackResDownload() {
		
		@Override
		public void progressHandler(String id, long progress, long max) {
		}
		
		@Override
		public void errorHandler(String id) {
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
		setContentView(R.layout.aty_download);
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
			return;
		}
		String userLessonType1 = GlobalRes.getInstance().getBeans().getDefaultLessonId();
		EntityResType1 type1 = null;
		for (String key : GlobalResTypes.ALL_TYPE1S_MAP.keySet()) {
			type1 = GlobalResTypes.ALL_TYPE1S_MAP.get(key);
			if (type1.isBase() && !type1.getId().equals(userLessonType1)) {
				continue;
			}
			entitys.add(type1);
			for (EntityResType2 type2 : type1.getType2s()) {
				entitys.add(type2);
				for (EntityResType4 type4 : type2.getType4s()) {
					entitys.add(type4);
				}
			}
		}
		try {
			mAdapter = new AdapterSimpleTree(lvDownloads, this, entitys, 1);
			mAdapter.setOnTreeNodeClickListener(this);
			mAdapter.setCallback(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		lvDownloads.setAdapter(mAdapter);
	}
	
	@Override
	public void onClick(Node node, int position) {
		if (!node.isLeaf()) {
			return;
		}
		if (node.isDownloadAll()) {
			EntityResType2 type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(node.getParent().getId());
			List<EntityResType4> entitys = type2.getType4s();
			if (null != entitys) {
				if (node.isClickedDownloadAll()) {
					node.setClickedDownloadAll(false);
					refreshDownloadName(node);
					for (EntityResType4 entity : entitys) {
						node = mAdapter.getAllNodeMap().get(entity.getId());
						if (node.getStatus() == INode.STATUS_START || node.getStatus() == INode.STATUS_WAIT || node.getStatus() == INode.STATUS_CONNECTING) {
							node.setStatus(INode.STATUS_STOP);
							entity.setStatus(INode.STATUS_STOP);
							setViewData(node);
						}
					}
					GlobalResTypes.getInstance().stopAllQueueDownload();
					return;
				}
				node.setClickedDownloadAll(true);
				refreshDownloadName(node);
				for (EntityResType4 entity : entitys) {
					node = mAdapter.getAllNodeMap().get(entity.getId());
					if (node.getStatus() == INode.STATUS_NORMAL || node.getStatus() == INode.STATUS_STOP) {
						entity.setStatus(INode.STATUS_CONNECTING);
						node.setStatus(INode.STATUS_CONNECTING);
						setViewData(node);
						GlobalResTypes.getInstance().startDownload(node.getId());
					}
				}
			}
			return;
		}
		switch (node.getStatus()) {
		case INode.STATUS_NORMAL: case INode.STATUS_STOP:
			node.setStatus(INode.STATUS_CONNECTING);
			setViewData(node);
			GlobalResTypes.getInstance().startDownload(node.getId());
			break;
		case INode.STATUS_START:
			node.setStatus(INode.STATUS_STOP);
			setViewData(node);
			GlobalResTypes.getInstance().stopDownload(node.getId());
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBack:
			this.finish();
			break;
		}
	}

	@Override
	public void clickHanlder(Node node) {
		if (!node.isLeaf()) {
			return;
		}
	}
    
    /***************************************************************************************************************************/
    
    private void setViewData(Node node) {
    	AdapterSimpleTree.ViewHolder v = mAdapter.getViewMap().get(node.getId());
    	if (null == v)
    		return;
    	Long p = 100L; 
    	if (node.getStatus() != INode.STATUS_FINISH) {
    		p = node.getProgress() * 100 / node.getMax();
    	}
    	v.pbTreeNode.setProgress(p.intValue());
    	v.pbTreeNode.setMax(100);
    	AdapterSimpleTree.refreshStatus(v.txtBtn, node.getStatus());
    }
    
    private void refreshDownloadName(Node node) {
    	AdapterSimpleTree.ViewHolder v = mAdapter.getViewMap().get(node.getId() + "_dl");
    	node.setName(node.isClickedDownloadAll() ? "全部暂停" : "全部下载"); 
    	if (null == v)
    		return;
    	v.txtTreeNodeLable.setText(node.getName());
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	GlobalResTypes.getInstance().clearCallbackType2();
    	GlobalResTypes.getInstance().clearCallbackType4();
    }
    
	/************************************************************************************************/
	
	public static void startAty(Activity context) {
		Intent i = new Intent(context, AtyDownload.class);
		context.startActivityForResult(i, REQUEST_DONWLOADS);
	}
}
