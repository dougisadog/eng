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
 * 获取我的班级信息
 * @author Jeffrey
 *
 */
public class TaskReqMyClassDatas extends BaseTask<Integer, Void, Boolean> {
	
	public TaskReqMyClassDatas(int tag, CallbackNoticeView<Void, Boolean> callback, Integer... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Integer... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		MyGroupListResult result = HttpReqFactory.getServerResultByToken(MyGroupListResult.class, ConfigConstants.CLASS_MINE_URL);
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			GlobalRes.getInstance().getBeans().setMyGroupDatas(result);
			if (GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().size() > 0) {
				GlobalRes.getInstance().getBeans().setCurrentMyClassNo(GlobalRes.getInstance().getBeans().getMyGroupDatas().getClassInfos().get(0).getClassNo());
				ClassMemberResult result1 = HttpReqFactory.getServerResultByToken(ClassMemberResult.class, ConfigConstants.CLASS_GET_MEMBERS_URL,
						new ReqParam("classNo", GlobalRes.getInstance().getBeans().getCurrentMyClassNo()));
				if (null != result1 && result1.getCode() == RestResult.C_SUCCESS) {
					GlobalRes.getInstance().getBeans().setMemberData(result1);
					return true;
				}
			}
			else {
				return true;
			}
		}
		return false;
	}
	
}
