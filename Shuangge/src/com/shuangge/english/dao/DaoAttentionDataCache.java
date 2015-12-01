package com.shuangge.english.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuangge.english.entity.server.secretmsg.AttentionData;
import com.shuangge.english.entity.server.secretmsg.AttentionDataCache;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDataCache;
import com.shuangge.english.entity.table.TableAttentionDataCache;
import com.shuangge.english.support.database.dao.BaseDao;
import com.shuangge.english.support.database.table.IdTable;
import com.shuangge.english.support.database.table.NetworkTable;
import com.shuangge.english.support.debug.DebugPrinter;

public class DaoAttentionDataCache extends BaseDao<SecretMsgDataCache> {

	public DaoAttentionDataCache() {
		super(TableAttentionDataCache.TABLE_NAME);
	}
	
	public List<AttentionData> getList(String loginName) {
		String sql = "select * from " + TableAttentionDataCache.TABLE_NAME + " where " + 
				TableAttentionDataCache.LOGIN_NAME + " = '" + loginName + "' order by " + 
				TableAttentionDataCache.VERSION_NO + " desc";
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        AttentionData entity = null;
        List<AttentionData> entitys = new ArrayList<AttentionData>();
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, AttentionData.class);
                    entitys.add(entity);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + AttentionData.class + " 获取json失败");
                }
            }
        }
        c.close();
		return entitys;
	}
	
	public AttentionData get(String loginName, Long userNo) {
		String sql = "select * from " + TableAttentionDataCache.TABLE_NAME + " where " + 
				TableAttentionDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableAttentionDataCache.USER_NO + " = " + userNo + "";
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        AttentionData entity = null;
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, AttentionData.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + AttentionData.class + " 获取json失败");
                }
            }
        }
        c.close();
		return entity;
	}
	
	public void update(String loginName, AttentionDataCache entity) {
		String sql = "select * from " + TableAttentionDataCache.TABLE_NAME + " where " + 
				TableAttentionDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableAttentionDataCache.USER_NO + " = " + entity.getAttentionData().getUserNo() + "";
		Cursor c = getRsd().rawQuery(sql, null);
		Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(NetworkTable.ENTITY, gson.toJson(entity.getAttentionData()));
        if (c.moveToNext()) {
            String[] args = {loginName, entity.getAttentionData().getUserNo()+""};
            getWsd().update(getTableName(), cv, TableAttentionDataCache.LOGIN_NAME 
            		+ "=? and " + TableAttentionDataCache.USER_NO + "=?", args);
        }
        else {
        	cv.put(TableAttentionDataCache.LOGIN_NAME, loginName);
    		cv.put(TableAttentionDataCache.USER_NO, entity.getAttentionData().getUserNo());
    		cv.put(TableAttentionDataCache.VERSION_NO, entity.getAttentionData().getCurrVersionNo());
            getWsd().insert(getTableName(), IdTable.ID, cv);
        }
        c.close();
	}
	
	public void delete(String loginName, Long friendNo) {
		String sql = "select * from " + TableAttentionDataCache.TABLE_NAME + " where " + 
				TableAttentionDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableAttentionDataCache.USER_NO + " = " + friendNo + "";
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + TableAttentionDataCache.class + " 获取删除不存在的 loginName=" + loginName );
        	return;
        }
		String[] args = {loginName, friendNo.toString()};
        getWsd().delete(getTableName(), TableAttentionDataCache.LOGIN_NAME + "=? and " + 
        		TableAttentionDataCache.USER_NO + "=?", args);
    }
}