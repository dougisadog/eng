package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.ClassData;
import com.shuangge.english.entity.server.group.ClassMemberResult;
import com.shuangge.english.entity.server.group.ClassResult;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

public class TaskReqClassCreate extends BaseTask<Object, Void, Boolean> {
	
	public TaskReqClassCreate(int tag, CallbackNoticeView<Void, Boolean> callback, Object... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Object... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		ClassResult result = HttpReqFactory.getServerResultByToken(ClassResult.class, ConfigConstants.CLASS_CREATE_URL,
				new ReqParam("name", params[0]), 
				new ReqParam("signature", params[1]), 
				new ReqParam("location", params[2]),
				new ReqParam("description", params[3]),
				new ReqParam("wechatNo",params[4]),
				new ReqParam("joinRule",params[5])); 
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			MyGroupListResult datas = new MyGroupListResult();
			datas.setMyClassAuth(ClassData.MANAGER);
			datas.getClassInfos().add(result.getClassInfo());
			GlobalRes.getInstance().getBeans().setMyGroupDatas(datas);
			GlobalRes.getInstance().getBeans().setCurrentMyClassNo(result.getClassInfo().getClassNo());
			
			ClassMemberResult result2 = new ClassMemberResult();
			result2.setMembers(result.getMembers());
			GlobalRes.getInstance().getBeans().setMemberData(result2);
			
			return true;
		}
		return false;
	}
	
}