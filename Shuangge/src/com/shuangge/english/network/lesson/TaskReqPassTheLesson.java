package com.shuangge.english.network.lesson;

import java.util.ArrayList;
import java.util.List;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.config.ConfigConstants;
import com.shuangge.english.dao.DaoLessonDataCache;
import com.shuangge.english.entity.lesson.EntityLessonDataCache;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.server.RestResult;
import com.shuangge.english.entity.server.lesson.PassLessonResult;
import com.shuangge.english.entity.server.lesson.UserScoreData;
import com.shuangge.english.support.http.HttpReqFactory;
import com.shuangge.english.support.http.HttpReqFactory.ReqParam;
import com.shuangge.english.support.service.BaseTask;
import com.shuangge.english.view.lesson.AtyLesson;

/**
 * 做课通知服务器
 * @author Jeffrey 
 * 
 */
public class TaskReqPassTheLesson extends BaseTask<EntityLessonDataCache, Void, EntityLessonDataCache> {
	
	public TaskReqPassTheLesson(int tag, CallbackNoticeView<Void, EntityLessonDataCache> callback, EntityLessonDataCache... params) {
		super(tag, callback, params);
	}
	
	@Override
	protected EntityLessonDataCache doInBackground(EntityLessonDataCache... params) {
		EntityLessonDataCache lessonData = params[0];
		DaoLessonDataCache dao = new DaoLessonDataCache();
		List<EntityLessonDataCache> datas = dao.get(lessonData.getLoginName(), lessonData.getcType5());
		datas.add(lessonData);
		List<String> cType6s = new ArrayList<String>();
		List<Integer> rightNums = new ArrayList<Integer>();
		List<Integer> wrongNums = new ArrayList<Integer>();
		List<String> lessonTypes = new ArrayList<String>();
		List<String> cType7AndStatus = new ArrayList<String>();
		for (EntityLessonDataCache data : datas) {
			cType6s.add(data.getcType6());
			rightNums.add(data.getRightNum());
			wrongNums.add(data.getWrongNum());
			lessonTypes.add(data.getLessonTypes());
			cType7AndStatus.add(data.getcType7AndStatus());
		}
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		PassLessonResult result = HttpReqFactory.getServerResultByToken(PassLessonResult.class, ConfigConstants.LESSON_PASS_URL,
				new ReqParam("cType5", lessonData.getcType5()),//GlobalRes.getInstance().getBeans().getCurrentType5Id()),
				new ReqParam("status", lessonData.getStatus()), //状态  0未完成最后一页  1完成左后一页
				new ReqParam("cType6s", cType6s),//GlobalRes.getInstance().getBeans().getCurrentType6Id()),
				new ReqParam("heartNum", lessonData.getHeartNum()), //目前心的数量
				new ReqParam("rightNums", rightNums), //该页正确题目数量
				new ReqParam("wrongNums", wrongNums), //该页错误题目数量
				new ReqParam("lessonTypes", lessonTypes), //该页正确的题型  ArrayList 类型
				new ReqParam("cType7AndStatus", cType7AndStatus),
				new ReqParam("frequency", GlobalRes.getInstance().getBeans().getCurrentFrequency())
		); //也一题 的 编号对错  例如: #0&1##1&1##2&1##3&1##4&1##5&1#    #代表 题目分隔符  &代表 题目编号和对错的间隔符 前面代表编号 后面代表对错  0错误 1正确
		if (null != result && result.getCode() == RestResult.C_SUCCESS) {
			UserScoreData scoreData = result.getUserScoreData();
			if (null != scoreData) {
				beans.getLoginData().getInfoData().setScore(scoreData.getScore());
				beans.getLoginData().getInfoData().setWeekScore(scoreData.getWeekScore());
				
				//TODO: 添加班级得分
			}
			GlobalRes.getInstance().getBeans().setCurrentFrequency(result.getType5().getFrequency());
			beans.getUnlockDatas().getType5s().put(beans.getCurrentType5Id(), result.getType5());
			dao.delete(beans.getLoginName(), beans.getCurrentType5Id());
			beans.setPassLessonDatas(result);
			List<EntityResType4> type4s = beans.getCurrentType4Datas();
			for (EntityResType4 entityResType4 : type4s) {
				entityResType4.refreshStar();
			}
			return null;
		}
		if (tag == AtyLesson.STATE_PASS) {
			dao.update(lessonData);
		}
		return lessonData;
	}
	
}
