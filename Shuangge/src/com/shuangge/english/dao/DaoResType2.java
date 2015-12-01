package com.shuangge.english.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuangge.english.entity.lesson.EntityResType2;
import com.shuangge.english.entity.table.TableResType2;
import com.shuangge.english.support.database.dao.BaseDao;
import com.shuangge.english.support.database.table.IdTable;
import com.shuangge.english.support.database.table.NetworkTable;
import com.shuangge.english.support.debug.DebugPrinter;

/**
 * 资源包  本地记录
 * @author Jeffrey
 *
 */
public class DaoResType2 extends BaseDao<EntityResType2> {

	public DaoResType2() {
		super(TableResType2.TABLE_NAME);
	}
	
	public EntityResType2 get(String id) {
		String sql = "select * from " + TableResType2.TABLE_NAME + " where " + TableResType2.COLUMN_ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        EntityResType2 entity = null;
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, EntityResType2.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + EntityResType2.class + " 获取json失败");
                }
            }
        }
        c.close();
		return entity;
	}
	
	public void update(String id, EntityResType2 entity) {
		String sql = "select * from " + TableResType2.TABLE_NAME + " where " + TableResType2.COLUMN_ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(TableResType2.COLUMN_ID, id);
        cv.put(NetworkTable.ENTITY, gson.toJson(entity.getEntity()));
        if (c.moveToNext()) {
            String[] args = {id};
            getWsd().update(getTableName(), cv, TableResType2.COLUMN_ID + "=?", args);
        }
        else {
            getWsd().insert(getTableName(), TableResType2.COLUMN_ID, cv);
        }
        c.close();
	}
	
	public void delete(String id) {
		String sql = "select * from " + TableResType2.TABLE_NAME + " where " + TableResType2.COLUMN_ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + TableResType2.class + " 获取删除不存在的 id=" + id);
        	return;
        }
		String[] args = {id};
        getWsd().delete(getTableName(), IdTable.ID + "=?", args);
    }
	
}
