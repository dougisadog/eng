package com.shuangge.english.task;

import com.shuangge.english.dao.DaoResType2;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.lesson.GlobalResTypes;
import com.shuangge.english.support.service.BaseTask;

public class TaskUpdateResType2 extends BaseTask<String, Void, Boolean> {

	public TaskUpdateResType2(int tag, CallbackNoticeView<Void, Boolean> callback, String... params) {
		super(tag, callback, params);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		EntityResType2 entity = GlobalResTypes.ALL_TYPE2S_MAP.get(params[0]);
		DaoResType2 dao = new DaoResType2();
		dao.update(entity.getId(), entity);
		return null;
	}

}
