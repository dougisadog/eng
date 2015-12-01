package com.shuangge.english.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuangge.english.entity.lesson.EntityResType4;
import com.shuangge.english.entity.table.TableResType4;
import com.shuangge.english.support.database.dao.BaseDao;
import com.shuangge.english.support.database.table.IdTable;
import com.shuangge.english.support.database.table.NetworkTable;
import com.shuangge.english.support.debug.DebugPrinter;

/**
 * 资源包  本地记录
 * @author Jeffrey
 *
 */
public class DaoResType4 extends BaseDao<EntityResType4> {

	public DaoResType4() {
		super(TableResType4.TABLE_NAME);
	}
	
	public EntityResType4 get(String id) {
		String sql = "select * from " + TableResType4.TABLE_NAME + " where " + TableResType4.COLUMN_ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        EntityResType4 entity = null;
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, EntityResType4.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + EntityResType4.class + " 获取json失败");
                }
            }
        }
        c.close();
		return entity;
	}
	
	public List<EntityResType4> getByParentId(String parentId) {
		String sql = "select * from " + TableResType4.TABLE_NAME + " where " + TableResType4.COLUMN_PARENT_ID + " = " + parentId;
		Cursor c = getRsd().rawQuery(sql, null);
		Gson gson = new Gson();
		EntityResType4 entity = null;
		List<EntityResType4> entitys = new ArrayList<EntityResType4>();
		while (c.moveToNext()) {
			String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
			if (!TextUtils.isEmpty(json)) {
				try {
					entity = gson.fromJson(json, EntityResType4.class);
					entitys.add(entity);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
					DebugPrinter.e("BaseDao - " + EntityResType4.class + " 获取json失败");
				}
			}
		}
		c.close();
		return entitys;
	}
	
	public void update(String id, EntityResType4 entity) {
		String sql = "select * from " + TableResType4.TABLE_NAME + " where " + TableResType4.COLUMN_ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(TableResType4.COLUMN_ID, id);
        cv.put(TableResType4.COLUMN_PARENT_ID, entity.getParentId());
        cv.put(NetworkTable.ENTITY, gson.toJson(entity.getEntity()));
        if (c.moveToNext()) {
            String[] args = {id};
            getWsd().update(getTableName(), cv, TableResType4.COLUMN_ID + "=?", args);
        }
        else {
            getWsd().insert(getTableName(), TableResType4.COLUMN_ID, cv);
        }
        c.close();
	}
	
	public void delete(String id) {
		String sql = "select * from " + TableResType4.TABLE_NAME + " where " + TableResType4.COLUMN_ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + TableResType4.class + " 获取删除不存在的 id=" + id);
        	return;
        }
		String[] args = {id};
        getWsd().delete(getTableName(), IdTable.ID + "=?", args);
    }
	
	public void deleteFromParentId(String parentId) {
		String[] args = {parentId};
		getWsd().delete(getTableName(), TableResType4.COLUMN_PARENT_ID + "=?", args);
	}
	
}
