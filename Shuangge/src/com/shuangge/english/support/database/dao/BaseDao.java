package com.shuangge.english.support.database.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuangge.english.entity.table.TableRegisterUtils;
import com.shuangge.english.support.database.DatabaseHelper;
import com.shuangge.english.support.database.entity.BaseEntity;
import com.shuangge.english.support.database.table.IdTable;
import com.shuangge.english.support.database.table.NetworkTable;
import com.shuangge.english.support.debug.DebugPrinter;

/**
 * 
 * @author Jeffrey
 *
 * @param <Entity>
 * @param <Table>
 */
public abstract class BaseDao<Entity extends BaseEntity> {
	
	private String tableName;
	private Class<Entity> entityClass = null;
	
	static {
		TableRegisterUtils.registerAll();
	}
	
	@SuppressWarnings("unchecked")
	public BaseDao(String tableName) {
		this.tableName = tableName;
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class<Entity>) params[0];
	}
	
	public List<Entity> getAll() {
		String sql = "select * from " + tableName;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        String json = null;
        List<Entity> entitys = new ArrayList<Entity>();
        while (c.moveToNext()) {
            json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                	entitys.add(gson.fromJson(json, entityClass));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + entityClass + " 获取json失败");
                }
            }
        }
        c.close();
		return entitys;
	}
	
	public Entity get(String id) {
		String sql = "select * from " + tableName + " where " + IdTable.ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        Entity entity = null;
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, entityClass);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + entityClass + " 获取json失败");
                }
            }
        }
        c.close();
		return entity;
	}
	
	public void update(String id, Entity entity) {
		String sql = "select * from " + tableName + " where " + IdTable.ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(NetworkTable.ENTITY, gson.toJson(entity.getEntity()));
        if (c.moveToNext()) {
            String[] args = {id};
            getWsd().update(getTableName(), cv, IdTable.ID + "=?", args);
        }
        else {
            getWsd().insert(getTableName(), IdTable.ID, cv);
        }
        c.close();
	}
	
	public void delete(String id) {
		String sql = "select * from " + tableName + " where " + IdTable.ID + " = " + id;
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + entityClass + " 获取删除不存在的 id=" + id);
        	return;
        }
		String[] args = {id};
        getWsd().delete(getTableName(), IdTable.ID + "=?", args);
    }
	
	public SQLiteDatabase getRsd() {
		return DatabaseHelper.getInstance().getReadableDatabase();
	}
	
	public SQLiteDatabase getWsd() {
		return DatabaseHelper.getInstance().getWritableDatabase();
	}

	public String getTableName() {
		return tableName;
	}
	
}
