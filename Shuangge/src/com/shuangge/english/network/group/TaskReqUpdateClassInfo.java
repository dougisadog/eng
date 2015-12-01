package com.shuangge.english.network.group;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.ClassResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.group.UpdateClassData;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqFile;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

/**
 * 获取我的班级信息
 * @author Jeffrey
 *
 */
public class TaskReqUpdateClassInfo extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqUpdateClassInfo(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		
		UpdateClassData reqData = GlobalReqDatas.getInstance().getUpdateClassData();
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
		
		ClassResult result = HttpReqFactory.getServerResultByToken(ClassResult.class, ConfigConstants.CLASS_UPDATE_INFO_URL,
				reqFiles,
				new HttpReqFactory.ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentMyClassNo()),
				new HttpReqFactory.ReqParam("name", reqData.getName()),
				new HttpReqFactory.ReqParam("location", reqData.getLocation()),
				new HttpReqFactory.ReqParam("signature", reqData.getSignature()),
				new HttpReqFactory.ReqParam("description", reqData.getDescription()),
				new HttpReqFactory.ReqParam("photoNos", reqData.getPhotoNos()),
				new HttpReqFactory.ReqParam("sortNos", reqData.getSortNos()),
				new HttpReqFactory.ReqParam("fileNames", reqData.getFileNames()),
				new HttpReqFactory.ReqParam("fileSortNos", reqData.getFileSortNos()),
				new HttpReqFactory.ReqParam("wechatNo", reqData.getWechatNo()),
				new HttpReqFactory.ReqParam("joinRule", reqData.getJoinRule()));
		reqData.clear();
		for (ReqFile reqFile : reqFiles) {
			if(reqFile.getFile().exists()) {
				reqFile.getFile().delete();
			}
		}
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().set(0, result.getClassInfo());
			return true;
		}
		return false;
	}
	
}
