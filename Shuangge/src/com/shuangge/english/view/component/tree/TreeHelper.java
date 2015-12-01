package com.shuangge.english.view.component.tree;

import java.util.ArrayList;
import java.util.List;

import air.com.shuangge.phone.ShuangEnglish.R;

public class TreeHelper {
	
	/**
	 * 传入我们的普通bean，转化为我们排序后的Node
	 * 
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static List<Node> getSortedNodes(List<INode> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
		List<Node> result = new ArrayList<Node>();
		// 将用户数据转化为List<Node>
		List<Node> nodes = convetData2Node(datas);
		// 拿到根节点
		List<Node> rootNodes = getRootNodes(nodes);
		// 排序以及设置Node间关系
		for (Node node : rootNodes) {
			addNode(result, node, defaultExpandLevel, 1);
		}
		return result;
	}

	/**
	 * 过滤出所有可见的Node
	 * 
	 * @param nodes
	 * @return
	 */
	public static List<Node> filterVisibleNode(List<Node> nodes) {
		List<Node> result = new ArrayList<Node>();
		for (Node node : nodes) {
			// 如果为跟节点，或者上层目录为展开状态
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 将我们的数据转化为树的节点
	 * 
	 * @param datas
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private static List<Node> convetData2Node(List<INode> datas) throws IllegalArgumentException, IllegalAccessException {
		List<Node> nodes = new ArrayList<Node>();
		Node node = null;
		for (INode t : datas) {
			String id = t.getId();
			String pId = t.getParentId();
			String label = t.getName();
			long progress = t.getProgress();
			long max = t.getMax();
			int status = t.getStatus();
			if (max == 0) {
				node = new Node(id, pId, label);
			}
			else {
				node = new Node(id, pId, label, progress, max);
			}
			node.setStatus(status);
			nodes.add(node);
		}

		/**
		 * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
		 */
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				Node m = nodes.get(j);
				if (n.getId().equals(m.getpId())) {
					n.getChildren().add(m);
					m.setParent(n);
				} else if (m.getId().equals(n.getpId())) {
					m.getChildren().add(n);
					n.setParent(m);
				}
			}
		}
		// 设置图片
		for (Node n : nodes) {
			setNodeIcon(n);
		}
		return nodes;
	}

	private static List<Node> getRootNodes(List<Node> nodes) {
		List<Node> root = new ArrayList<Node>();
		for (Node node : nodes) {
			if (node.isRoot())
				root.add(node);
		}
		return root;
	}

	/**
	 * 把一个节点上的所有的内容都挂上去
	 */
	private static void addNode(List<Node> nodes, Node node,
			int defaultExpandLeval, int currentLevel) {

		nodes.add(node);
		if (defaultExpandLeval >= currentLevel) {
			node.setExpand(true);
		}
		if (node.isLeaf())
			return;
		Node n = null;
		Node n1 = null;
		for (int i = 0; i < node.getChildren().size(); i++) {
			//TODO:暂时为下载加上 全部下载
			n = node.getChildren().get(i);
			if (currentLevel == 2 && i == 0) {
				n1 = new Node(n.getpId(), n.getpId(), "全部下载");
				n1.setDownloadAll(true);
				n1.setParent(node);
				addNode(nodes, n1, defaultExpandLeval, currentLevel + 1);
			}
			addNode(nodes, n, defaultExpandLeval, currentLevel + 1);
		}
	}

	/**
	 * 设置节点的图标
	 * 
	 * @param node
	 */
	private static void setNodeIcon(Node node) {
		if (node.getChildren().size() > 0 && node.isExpand()) {
			node.setIcon(R.drawable.icon_tree_ex);
		} else if (node.getChildren().size() > 0 && !node.isExpand()) {
			node.setIcon(R.drawable.icon_tree_ec);
		} else
			node.setIcon(-1);

	}

}