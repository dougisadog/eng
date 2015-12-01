package com.shuangge.english.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoSecretMsgDataCache;
import com.shuangge.english.dao.DaoUser;
import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.network.reqdata.GlobalReqDatas;
import com.shuangge.english.support.service.BaseTask;

public class TaskInitLocalUserData extends BaseTask<Void, Void, Boolean> {
	
	public TaskInitLocalUserData(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (ConfigConstants.DEBUG_NO_SERVER) {
			return true;
		}
		DaoUser dao = new DaoUser();
		EntityUser user = dao.get("1");
		if (null == user) {
			return false;
		}
		GlobalReqDatas.getInstance().setRequestLoginName(user.getLoginName());
		if (user.isLogout()) {
			return false;
		}
		GlobalReqDatas.getInstance().setRequestUID(user.getUid());
		GlobalReqDatas.getInstance().setRequestUIDType(user.getType());
		GlobalReqDatas.getInstance().setRequestPassword(user.getPassword());
		
		//删除时间过长聊天记录
		DaoSecretMsgDataCache daoMsg = new DaoSecretMsgDataCache();
		Date date = new Date();//当前日期
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置当前日期
		calendar.add(Calendar.MONTH, -ConfigConstants.SECRET_MSG_MONTH);//月份减一
		daoMsg.deleteHistoryMsg(user.getLoginName(), calendar.getTime());
		
		
		return true;
	}
	
	
}
