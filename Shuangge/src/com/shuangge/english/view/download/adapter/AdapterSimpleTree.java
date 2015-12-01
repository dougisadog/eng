package com.shuangge.english.view.download.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import air.com.shuangge.phone.ShuangEnglish.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuangge.english.view.component.tree.AdapterTreeListView;
import com.shuangge.english.view.component.tree.INode;
import com.shuangge.english.view.component.tree.Node;


public class AdapterSimpleTree extends AdapterTreeListView implements OnClickListener {
	
	private CallbackTreeDownload callback;
	private Map<String, ViewHolder> viewMap = new HashMap<String, ViewHolder>();

	public AdapterSimpleTree(ListView mTree, Context context, List<INode> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
		super(mTree, context, datas, defaultExpandLevel);
	}
	
	public void setCallback(CallbackTreeDownload callback) {
		this.callback = callback;
	}
	
	public static interface CallbackTreeDownload {
		
		public void clickHanlder(Node node);
		
	}

	@Override
	public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.component_item_tree, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.imgIconTreeNode = (ImageView) convertView.findViewById(R.id.imgIconTreeNode);
			viewHolder.txtTreeNodeLable = (TextView) convertView.findViewById(R.id.txtTreeNodeLable);
			viewHolder.pbTreeNode = (ProgressBar) convertView.findViewById(R.id.pbTreeNode);
			viewHolder.txtBtn = (TextView) convertView.findViewById(R.id.txtBtn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			if (viewMap.containsKey(viewHolder.viewId))
				viewMap.remove(viewHolder.viewId);
		}
		if (node.isDownloadAll()) {
			node.setName(node.isClickedDownloadAll() ? "全部暂停" : "全部下载"); 
			viewHolder.viewId = node.getId() + "_dl";
		}
		else {
			viewHolder.viewId = node.getId();
		}
		if (node.getIcon() == -1) {
			viewHolder.imgIconTreeNode.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.imgIconTreeNode.setVisibility(View.VISIBLE);
			viewHolder.imgIconTreeNode.setImageResource(node.getIcon());
		}
		viewHolder.txtTreeNodeLable.setText(node.getName());
		if (node.isLeaf() && !node.isDownloadAll()) {
			Long p = 100L; 
			if (node.getStatus() != INode.STATUS_FINISH) {
				p = node.getProgress() * 100 / node.getMax();
			}
			viewHolder.pbTreeNode.setProgress(p.intValue());
			viewHolder.pbTreeNode.setMax(100);
			viewHolder.pbTreeNode.setVisibility(View.VISIBLE);
			viewHolder.txtBtn.setVisibility(View.VISIBLE);
//			viewHolder.txtBtn.setOnClickListener(this);
			viewHolder.txtBtn.setTag(node);
		}
		else {
			viewHolder.pbTreeNode.setVisibility(View.GONE);
			viewHolder.txtBtn.setVisibility(View.GONE);
		}
		refreshStatus(viewHolder.txtBtn, node.getStatus());
		viewMap.put(viewHolder.viewId, viewHolder);
		return convertView;
	}
	
	public static void refreshStatus(TextView v, int status) {
		switch (status) {
		case INode.STATUS_NORMAL:
			v.setText(R.string.downloadTip1);
			break;
		case INode.STATUS_CONNECTING:
			v.setText(R.string.downloadTip2);
			break;
		case INode.STATUS_WAIT:
			v.setText(R.string.downloadTip3);
			break;
		case INode.STATUS_START:
			v.setText(R.string.downloadTip4);
			break;
		case INode.STATUS_STOP:
			v.setText(R.string.downloadTip5);
			break;
		case INode.STATUS_INSTALL:
			v.setText(R.string.downloadTip6);
			break;
		case INode.STATUS_FINISH:
			v.setText(R.string.downloadTip7);
			break;
		}
	}

	public static class ViewHolder {
		
		public String viewId;
		
		public ImageView imgIconTreeNode;
		public TextView txtTreeNodeLable;
		public ProgressBar pbTreeNode;
		public TextView txtBtn;
		
	}

	@Override
	public void onClick(View v) {
		if (null != callback) {
			callback.clickHanlder((Node) v.getTag());
		}
	}

	public Map<String, ViewHolder> getViewMap() {
		return viewMap;
	}

}
