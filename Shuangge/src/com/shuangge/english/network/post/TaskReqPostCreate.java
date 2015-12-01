package com.shuangge.english.network.post;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.post.PostResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.post.UpdatePostData;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqFile;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

public class TaskReqPostCreate extends BaseTask<String, Void, Boolean> {
	
	public TaskReqPostCreate(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		
		UpdatePostData reqData = GlobalReqDatas.getInstance().getUpdatePostData();
		List<ReqFile> reqFiles = new ArrayList<ReqFile>();
		int sortNo = 0;
		for (PhotoParam photoData : reqData.getPhotoDatas()) {
			if (null != photoData.getNo()) {
				reqData.getPhotoNos().add(photoData.getNo());
				reqData.getSortNos().add(sortNo++);
				continue;
			}
			File file = FileUtils.createNewTempFileByUrl(sortNo + ".jpg");
			try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(photoData.getBytes());
                fos.flush();
                fos.close();
	        } catch (Exception e) {
                e.printStackTrace();
	        }
			reqFiles.add(new ReqFile(file.getName(), file, ReqFile.TYPE_IMAGE_PNG, "file" + sortNo));
			reqData.getFileNames().add(file.getName());
			reqData.getFileSortNos().add(sortNo++);
		}
		
		PostResult result = HttpReqFactory.getServerResultByToken(PostResult.class, ConfigConstants.POST_CREATE_URL,
				reqFiles,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentMyClassNo()), 
				new ReqParam("title", reqData.getTitle()), 
				new ReqParam("content", reqData.getContent()), 
				new ReqParam("top", reqData.getTop()),
				new HttpReqFactory.ReqParam("fileNames", reqData.getFileNames()),
				new HttpReqFactory.ReqParam("fileSortNos", reqData.getFileSortNos()));
		reqData.clear();
		for (ReqFile reqFile : reqFiles) {
			if(reqFile.getFile().exists()) {
				reqFile.getFile().delete();
			}
		}
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().getPostDatas().getPosts().add(0, result.getData());
			return true;
		}
		return false;
	}
	
}