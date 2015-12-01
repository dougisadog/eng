package com.shuangge.english.network.post;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.ReplyResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.post.UpdateReplyData;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqFile;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

public class TaskReqReply extends BaseTask<String, Void, Boolean> {
	
	public TaskReqReply(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		
		UpdateReplyData reqData = GlobalReqDatas.getInstance().getUpdateReplyData();
		List<ReqFile> reqFiles = new ArrayList<ReqFile>();
		PhotoParam photoData = reqData.getPhotoData();
		if (null != photoData && null != photoData.getBytes()) {
			File file = FileUtils.createNewTempFileByUrl("0.jpg");
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(photoData.getBytes());
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			reqFiles.add(new ReqFile(file.getName(), file, ReqFile.TYPE_IMAGE_PNG, "file0"));
			reqData.setFileName(file.getName());
		}
		
		String url = null == reqData.getReplyNo() ? ConfigConstants.POST_REPLY_URL : ConfigConstants.POST_SUB_REPLY_URL;
		
		ReplyResult result = HttpReqFactory.getServerResultByToken(ReplyResult.class, url,
				reqFiles,
				new ReqParam("postNo", GlobalRes.getInstance().getBeans().getCurrentPostNo()), 
				new ReqParam("content", reqData.getContent()), 
				new ReqParam("replyNo", reqData.getReplyNo()), 
				new HttpReqFactory.ReqParam("fileName", reqData.getFileName()));
		GlobalReqDatas.getInstance().getUpdateReplyData().clear();
		for (ReqFile reqFile : reqFiles) {
			if(reqFile.getFile().exists()) {
				reqFile.getFile().delete();
			}
		}
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().getReplyDatas().getReplys().add(result.getData());
			return true;
		}
		return false;
	}
	
}