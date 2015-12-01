package com.shuangge.english.dao;

import com.shuangge.english.entity.EntityUser;
import com.shuangge.english.entity.table.TableUser;
import com.shuangge.english.support.database.dao.BaseDao;

public class DaoUser extends BaseDao<EntityUser> {

	public DaoUser() {
		super(TableUser.TABLE_NAME);
	}

}
