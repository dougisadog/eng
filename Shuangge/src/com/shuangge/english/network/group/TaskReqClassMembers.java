package com.shuangge.english.network.group;

import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.group.ClassMemberResult;
import com.shuangge.english.entity.server.group.MyGroupListResult;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;

/**
 * 获取班级成员
 * @author Jeffrey
 *
 */
public class TaskReqClassMembers extends BaseTask<Integer, Void, Boolean> {
	
	public TaskReqClassMembers(int tag, CallbackNoticeView<Void, Boolean> callback, Integer... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		Integer sortBy = 0;
		if (params.length > 0)
			sortBy = params[0];
		ClassMemberResult result = HttpReqFactory.getServerResultByToken(ClassMemberResult.class, ConfigConstants.CLASS_GET_MEMBERS_URL,
				new ReqParam("sortBy", sortBy),
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
