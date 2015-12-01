package com.shuangge.english.network.user;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.user.InfoData;
import com.shuangge.english.entity.server.user.UpdateInfoResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.user.UpdateInfoData;
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
public class TaskReqUpdateDataInfo extends BaseTask<Void, Void, Boolean> {
	
	public TaskReqUpdateDataInfo(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		
		UpdateInfoData reqData = GlobalReqDatas.getInstance().getUpdateInfoData();
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
		
		UpdateInfoResult result = HttpReqFactory.getServerResultByToken(UpdateInfoResult.class, ConfigConstants.UPDATE_INFO_URL,
				reqFiles,
				new HttpReqFactory.ReqParam("name", reqData.getName()),
				new HttpReqFactory.ReqParam("sex", reqData.getSex()),
				new HttpReqFactory.ReqParam("birthday", reqData.getBirthday()),
				new HttpReqFactory.ReqParam("signature", reqData.getSignature()),
				new HttpReqFactory.ReqParam("emotion", reqData.getEmotion()),
				new HttpReqFactory.ReqParam("occupation", reqData.getOccupation()),
				new HttpReqFactory.ReqParam("income", reqData.getIncome()),
				new HttpReqFactory.ReqParam("location", reqData.getLocation()),
				new HttpReqFactory.ReqParam("interest", reqData.getInterest()),
				new HttpReqFactory.ReqParam("photoNos", reqData.getPhotoNos()),
				new HttpReqFactory.ReqParam("sortNos", reqData.getSortNos()),
				new HttpReqFactory.ReqParam("fileNames", reqData.getFileNames()),
				new HttpReqFactory.ReqParam("fileSortNos", reqData.getFileSortNos()));
		reqData.clear();
		for (ReqFile reqFile : reqFiles) {
			if(reqFile.getFile().exists()) {
				reqFile.getFile().delete();
			}
		}
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			InfoData data = GlobalRes.getInstance().getBeans().getLoginData().getInfoData();
			data.setName(reqData.getName());
			data.setSex(reqData.getSex());
			data.setAge(reqData.getAge());
			data.setBirthday(reqData.getBirthday());
			data.setSignature(reqData.getSignature());
			data.setEmotion(reqData.getEmotion());
			data.setIncome(reqData.getIncome());
			data.setOccupation(reqData.getOccupation());
			data.setLocation(reqData.getLocation());
			data.setInterest(reqData.getInterest());
			if (result.getPhotoUrls().size() > 0) {
				data.setHeadUrl(result.getPhotoUrls().get(0));
			}
			else {
				data.setHeadUrl(null);
			}
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setPhotoNos(result.getPhotoNos());
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setPhotoSortNos(result.getPhotoSortNos());
			GlobalRes.getInstance().getBeans().getLoginData().getInfoData().setPhotoUrls(result.getPhotoUrls());
			return true;
		}
		return false;
	}
	
}
