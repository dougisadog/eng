package com.shuangge.english.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuangge.english.entity.lesson.EntityLessonDataCache;
import com.shuangge.english.entity.table.TableLessonDataCache;
import com.shuangge.english.support.database.dao.BaseDao;
import com.shuangge.english.support.database.table.NetworkTable;
import com.shuangge.english.support.debug.DebugPrinter;

public class DaoLessonDataCache extends BaseDao<EntityLessonDataCache> {

	public DaoLessonDataCache() {
		super(TableLessonDataCache.TABLE_NAME);
	}
	
	public List<EntityLessonDataCache> get(String loginName, String type5Id) {
		String sql = "select * from " + TableLessonDataCache.TABLE_NAME + " where " + 
		TableLessonDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
		TableLessonDataCache.TYPE5_ID + " = '" + type5Id + "'";
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        EntityLessonDataCache entity = null;
        List<EntityLessonDataCache> entitys = new ArrayList<EntityLessonDataCache>();
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, EntityLessonDataCache.class);
                    entitys.add(entity);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + EntityLessonDataCache.class + " 获取json失败");
                }
            }
        }
        c.close();
		return entitys;
	}
	
	public void update(EntityLessonDataCache entity) {
		String sql = "select * from " + TableLessonDataCache.TABLE_NAME + " where " + 
				TableLessonDataCache.LOGIN_NAME + " = '" + entity.getLoginName() + "' and " + 
				TableLessonDataCache.TYPE6_ID + " = '" + entity.getcType6() + "'";
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(TableLessonDataCache.LOGIN_NAME, entity.getLoginName());
        cv.put(TableLessonDataCache.TYPE5_ID, entity.getcType5());
        cv.put(TableLessonDataCache.TYPE6_ID, entity.getcType6());
        cv.put(NetworkTable.ENTITY, gson.toJson(entity.getEntity()));
        if (c.moveToNext()) {
//            String[] args = {entity.getLoginName(),  entity.getcType6()};
//            getWsd().update(getTableName(), cv, TableLessonDataCache.LOGIN_NAME + "=? and " + 
//            TableLessonDataCache.TYPE6_ID + "=?", args);
        }
        else {
            getWsd().insert(getTableName(), null, cv);
        }
        c.close();
	}
	
	public void delete(String loginName, String type5Id) {
		String sql = "select * from " + TableLessonDataCache.TABLE_NAME + " where " + 
				TableLessonDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableLessonDataCache.TYPE5_ID + " = '" + type5Id + "'";
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + TableLessonDataCache.class + " 获取删除不存在的 loginName=" + loginName + " type5Id=" + type5Id);
        	return;
        }
		String[] args = {loginName, type5Id};
        getWsd().delete(getTableName(), TableLessonDataCache.LOGIN_NAME + "=? and " + 
                TableLessonDataCache.TYPE6_ID + "=?", args);
    }
	
}