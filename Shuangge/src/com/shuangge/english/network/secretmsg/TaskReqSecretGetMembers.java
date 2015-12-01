package com.shuangge.english.network.secretmsg;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.ClassMemberResult;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.entity.server.msg.NoticeResult;
import com.shuangge.english.entity.server.secretmsg.AttentionInfoResult;
import com.shuangge.english.entity.server.secretmsg.SecretListResult;
import com.shuangge.english.entity.server.secretmsg.SecretMsgResult;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.network.reqdata.post.UpdateReplyData;
import com.shuangge.english.support.file.FileUtils;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqFile;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.component.photograph.DragPhotoAdapter.PhotoParam;

public class TaskReqSecretGetMembers extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqSecretGetMembers(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		
		ClassMemberResult result = HttpReqFactory.getServerResultByToken(ClassMemberResult.class, ConfigConstants.SECRET_GET_MEMBERS,
				new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentClassNo()));
		
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			MyGroupListResult r = GlobalRes.getInstance().getBeans().getMyGroupDatas();
			if (null != r && null !=  r.getClassInfos() && r.getClassInfos().size() > 0) {
				r.getClassInfos().get(0).setNum(GlobalRes.getInstance().getBeans().getMemberData().getMembers().size());
			}
			GlobalRes.getInstance().getBeans().setMemberData(result);
			return true;
		}
		return false;

	}
	
}
