package com.shuangge.english.view.component.tree;

public interface INode {

	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_CONNECTING = 1;
	public static final int STATUS_WAIT = 2;
	public static final int STATUS_START = 3;
	public static final int STATUS_STOP = 4;
	public static final int STATUS_INSTALL = 9;
	public static final int STATUS_FINISH = 10;

	public String getId();

	public void setParentId(String parentId);

	public String getParentId();

	public String getName();

	public void setProgress(long progress);

	public long getProgress();

	public void setMax(long max);

	public long getMax();

	public void setStatus(int status);

	public int getStatus();

}
