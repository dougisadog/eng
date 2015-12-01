package com.shuangge.english.task;

import com.shuangge.english.dao.DaoResType4;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.support.service.BaseTask;

public class TaskUpdateResType4 extends BaseTask<String, Void, Boolean> {

	public TaskUpdateResType4(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		EntityResType4 entity = GlobalResTypes.ALL_TYPE4S_MAP.get(params[0]);
		DaoResType4 dao = new DaoResType4();
		dao.update(entity.getId(), entity);
		return null;
	}

}
