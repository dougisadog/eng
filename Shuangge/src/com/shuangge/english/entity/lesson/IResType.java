package com.shuangge.english.entity.lesson;

import java.io.File;

import com.shuangge.english.view.component.tree.INode;

public interface IResType extends INode {

	public String getLocalPath();

	public File getLocalFile();

	public double getVersion();

	public void setVersion(double version);
	
	public boolean isFinished();
	
	public String getUrl();
	
}
