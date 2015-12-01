package com.shuangge.english.task;

import java.util.List;

import com.shuangge.english.cache.CacheBeans;
import com.shuangge.english.cache.GlobalRes;
import com.shuangge.english.dao.DaoResType4;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.entity.lesson.Phrase;
import com.shuangge.english.support.debug.DebugPrinter;
import com.shuangge.english.support.service.BaseTask;

public class TaskGetType4sByType2Id extends BaseTask<Void, Void, Boolean> {

	public TaskGetType4sByType2Id(int tag, CallbackNoticeView<Void, Boolean> callback, Void... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		CacheBeans beans = GlobalRes.getInstance().getBeans();
		EntityResType2 type2 = GlobalResTypes.ALL_TYPE2S_MAP.get(beans.getCurrentType2Id());
		if (null == type2) {
			DebugPrinter.e("TaskGetType4sByType2Id 配置文件解析错误!");
			return false;
		}
		Phrase.parse(type2.getLocalPath());
		DaoResType4 dao4 = new DaoResType4();
		beans.setCurrentType4Datas(dao4.getByParentId(beans.getCurrentType2Id()));
		if (beans.getCurrentType4Datas().size() == 0) {
			//如果解析错误 重新解析
			GlobalResTypes.parseType2Zip(type2);
			beans.setCurrentType4Datas(dao4.getByParentId(beans.getCurrentType2Id()));
		}
		//计算解锁星星
		List<EntityResType4> type4s = beans.getCurrentType4Datas();
		for (EntityResType4 entityResType4 : type4s) {
			entityResType4.refreshStar();
		}
		return true;
	}
	
}
