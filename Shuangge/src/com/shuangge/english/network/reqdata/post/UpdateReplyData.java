package com.shuangge.english.network.reqdata.post;

import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

public class UpdateReplyData {

	private Long postNo;
	private Long replyNo;
	private String content;

	private String fileName;

	private PhotoParam photoData = new PhotoParam();

	public Long getPostNo() {
		return postNo;
	}

	public void setPostNo(Long postNo) {
		this.postNo = postNo;
	}

	public Long getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(Long replyNo) {
		this.replyNo = replyNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public PhotoParam getPhotoData() {
		return photoData;
	}
	
	public void clear() {
		photoData.setBytes(null);
		fileName = null;
	}

}
