package com.shuangge.english.view.component.tree;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class Node {
	
	private String id;
	private String pId;
	private String name;
	private boolean isExpand = false;
	private int icon;
	private List<Node> children = new ArrayList<Node>();
	private Node parent;
	private Long progress = 0L;
	private Long max = 0L;
	private int status = INode.STATUS_NORMAL;
	private boolean downloadAll;
	private boolean clickedDownloadAll;
	
	public Node() {
	}

	public Node(String id, String pId, String name) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}
	
	public Node(String id, String pId, String name, long progress, long max) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.progress = progress;
		this.max = max;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * 是否为跟节点
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return parent == null;
	}

	/**
	 * 判断父节点是否展开
	 * 
	 * @return
	 */
	public boolean isParentExpand() {
		if (parent == null)
			return false;
		return parent.isExpand();
	}

	/**
	 * 是否是叶子界点
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		return children.size() == 0;
	}

	/**
	 * 获取level
	 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * 设置展开
	 * 
	 * @param isExpand
	 */
	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
		if (!isExpand) {

			for (Node node : children) {
				node.setExpand(isExpand);
			}
		}
	}

	public Long getProgress() {
		return progress;
	}

	public void setProgress(Long progress) {
		this.progress = progress;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isDownloadAll() {
		return downloadAll;
	}

	public void setDownloadAll(boolean downloadAll) {
		this.downloadAll = downloadAll;
	}

	public boolean isClickedDownloadAll() {
		return clickedDownloadAll;
	}

	public void setClickedDownloadAll(boolean clickedDownloadAll) {
		this.clickedDownloadAll = clickedDownloadAll;
	}

}
