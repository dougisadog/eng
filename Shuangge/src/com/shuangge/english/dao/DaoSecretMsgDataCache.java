package com.shuangge.english.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDataCache;
import com.shuangge.english.entity.server.secretmsg.SecretMsgDetailData;
import com.shuangge.english.entity.table.TableSecretMsgDataCache;
import com.shuangge.english.support.database.dao.BaseDao;
import com.shuangge.english.support.database.table.IdTable;
import com.shuangge.english.support.database.table.NetworkTable;
import com.shuangge.english.support.debug.DebugPrinter;

public class DaoSecretMsgDataCache extends BaseDao<SecretMsgDataCache> {

	public static final int PAGE_SIZE = 20;
	
	public DaoSecretMsgDataCache() {
		super(TableSecretMsgDataCache.TABLE_NAME);
	}
	
//	public List<SecretMsgDetailData> get(String loginName, Integer friendNo) {
//		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
//				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
//				TableSecretMsgDataCache.FRIEND_NO + " = " + friendNo + "";
//		Cursor c = getRsd().rawQuery(sql, null);
//        Gson gson = new Gson();
//        SecretMsgDetailData entity = null;
//        List<SecretMsgDetailData> entitys = new ArrayList<SecretMsgDetailData>();
//        while (c.moveToNext()) {
//            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
//            if (!TextUtils.isEmpty(json)) {
//                try {
//                    entity = gson.fromJson(json, SecretMsgDetailData.class);
//                    entitys.add(entity);
//                } catch (JsonSyntaxException e) {
//                    e.printStackTrace();
//                    DebugPrinter.e("BaseDao - " + SecretMsgDetailData.class + " 获取json失败");
//                }
//            }
//        }
//        c.close();
//		return entitys;
//	}
	
	public List<SecretMsgDetailData> getMsgDetails(String loginName, Long friendNo, Date minTime) {
		String where = "";
		if (null != minTime)
			where = " and " + TableSecretMsgDataCache.SEND_TIME + " < " + minTime.getTime();
		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableSecretMsgDataCache.FRIEND_NO + " = " + friendNo + where + " order by " 
				+ TableSecretMsgDataCache.SEND_TIME + " desc limit 0," + PAGE_SIZE;
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        SecretMsgDetailData entity = null;
        List<SecretMsgDetailData> entitys = new ArrayList<SecretMsgDetailData>();
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, SecretMsgDetailData.class);
                    entity.setId(c.getLong(c.getColumnIndex(NetworkTable.ID)));
                    entitys.add(entity);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + SecretMsgDetailData.class + " 获取json失败");
                }
            }
        }
        c.close();
		return entitys;
	}
	
	public SecretMsgDetailData getLastMsg(String loginName, Long friendNo) {
		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableSecretMsgDataCache.FRIEND_NO + " = " + friendNo + " order by " 
				+ TableSecretMsgDataCache.ID + " desc limit 0, 1";
		Cursor c = getRsd().rawQuery(sql, null);
        Gson gson = new Gson();
        SecretMsgDetailData entity = null;
        if (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
            if (!TextUtils.isEmpty(json)) {
                try {
                    entity = gson.fromJson(json, SecretMsgDetailData.class);
                    entity.setId(c.getLong(c.getColumnIndex(NetworkTable.ID)));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    DebugPrinter.e("BaseDao - " + SecretMsgDetailData.class + " 获取json失败");
                }
            }
        }
        c.close();
		return entity;
	}
	
	public List<SecretMsgDetailData> getMsgList(String loginName) {
		String sql = "select * from (select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
					TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' order by " + TableSecretMsgDataCache.SEND_TIME + " asc" + ") s group by s." + 
					TableSecretMsgDataCache.FRIEND_NO + " order by s." + TableSecretMsgDataCache.SEND_TIME + " asc";
		Cursor c = getRsd().rawQuery(sql, null);
		Gson gson = new Gson();
		SecretMsgDetailData entity = null;
		List<SecretMsgDetailData> entitys = new ArrayList<SecretMsgDetailData>();
		while (c.moveToNext()) {
			String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
			if (!TextUtils.isEmpty(json)) {
				try {
					entity = gson.fromJson(json, SecretMsgDetailData.class);
					entity.setId(c.getLong(c.getColumnIndex(NetworkTable.ID)));
					entitys.add(entity);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
					DebugPrinter.e("BaseDao - " + SecretMsgDetailData.class + " 获取json失败");
				}
			}
		}
		c.close();
		return entitys;
	}
	
	public void updateStatusByFriendId(String loginName, Long friendNo) {
		String sql = "update " + TableSecretMsgDataCache.TABLE_NAME + 
				" set " + TableSecretMsgDataCache.STATUS + " = " + SecretMsgDetailData.STATUS_READ +
				" where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " +
				TableSecretMsgDataCache.FRIEND_NO + " = " + friendNo;
		getWsd().execSQL(sql);
	}
	
	public Long save(SecretMsgDataCache entity) {
        Gson gson = new Gson();
        ContentValues cv = new ContentValues();
        cv.put(TableSecretMsgDataCache.LOGIN_NAME, entity.getLoginName());
        cv.put(TableSecretMsgDataCache.SEND_TIME, entity.getSecretMsgdata().getSendTime().getTime());
        cv.put(TableSecretMsgDataCache.FRIEND_NO, entity.getSecretMsgdata().getFriendNo());
        cv.put(TableSecretMsgDataCache.SECRET_MSG_NO, entity.getSecretMsgdata().getSecretMsgNo());
        cv.put(TableSecretMsgDataCache.STATUS, entity.getSecretMsgdata().getStatus());
        cv.put(NetworkTable.ENTITY, gson.toJson(entity.getSecretMsgdata()));
        long id = getWsd().insert(getTableName(), null, cv);
        return id;
	}
	
	public Long update(Long id, SecretMsgDataCache entity) {
		Gson gson = new Gson();
		ContentValues cv = new ContentValues();
		cv.put(TableSecretMsgDataCache.LOGIN_NAME, entity.getLoginName());
		cv.put(TableSecretMsgDataCache.SEND_TIME, entity.getSecretMsgdata().getSendTime().getTime());
		cv.put(TableSecretMsgDataCache.FRIEND_NO, entity.getSecretMsgdata().getFriendNo());
		cv.put(TableSecretMsgDataCache.SECRET_MSG_NO, entity.getSecretMsgdata().getSecretMsgNo());
		cv.put(TableSecretMsgDataCache.STATUS, entity.getSecretMsgdata().getStatus());
		cv.put(NetworkTable.ENTITY, gson.toJson(entity.getSecretMsgdata()));
		String[] args = {id + ""};
		getWsd().update(getTableName(), cv, IdTable.ID + "=?", args);
		return id;
	}
	
	
	
	public Long getMaxMsgNo(String loginName) {
		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "'order by "+TableSecretMsgDataCache.SEND_TIME+" desc limit 0,1";
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	return null;
        }
        String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
        if (!TextUtils.isEmpty(json)) {
            try {
            	Gson gson = new Gson();
            	SecretMsgDetailData entity = gson.fromJson(json, SecretMsgDetailData.class);
            	return entity.getSecretMsgNo();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                DebugPrinter.e("BaseDao - " + SecretMsgDetailData.class + " 获取json失败");
            }
        }
        return null;
	}
	
	public void deleteAll(String loginName, Integer friendNo) {
		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableSecretMsgDataCache.FRIEND_NO + " = " + friendNo + " limit 0,1";
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + TableSecretMsgDataCache.class + " 获取删除不存在的 loginName=" + loginName + " friendNo=" + friendNo);
        	return;
        }
		String[] args = {loginName, friendNo.toString()};
        getWsd().delete(getTableName(), TableSecretMsgDataCache.LOGIN_NAME + "=? and " + 
        		TableSecretMsgDataCache.FRIEND_NO + "=?", args);
    }
	
	public void delete(Long id) {
		super.delete(id + "");
	}
	
	public void delete(String loginName, Integer secretMsgNo) {
		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableSecretMsgDataCache.SECRET_MSG_NO + " = " + secretMsgNo + "";
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + TableSecretMsgDataCache.class + " 获取删除不存在的 loginName=" + loginName + " friendNo=" + secretMsgNo);
        	return;
        }
		String[] args = {loginName, secretMsgNo.toString()};
        getWsd().delete(getTableName(), TableSecretMsgDataCache.LOGIN_NAME + "=? and " + 
        		TableSecretMsgDataCache.SECRET_MSG_NO + "=?", args);
    }
	
	public void delete(String loginName, Long sendTime) {
		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableSecretMsgDataCache.SECRET_MSG_NO + " = 0 and " +
				TableSecretMsgDataCache.SEND_TIME + " = " + sendTime;
		Cursor c = getRsd().rawQuery(sql, null);
		if (!c.moveToNext()) {
			return;
		}
		String[] args = {loginName, "0", sendTime.toString()};
		getWsd().delete(getTableName(), TableSecretMsgDataCache.LOGIN_NAME + "=? and " + 
				TableSecretMsgDataCache.SECRET_MSG_NO + "=? and " + TableSecretMsgDataCache.SEND_TIME + "=?", args);
	}
	
	public void deleteHistoryMsg(String loginName, Date time) {
		String sql = "select * from " + TableSecretMsgDataCache.TABLE_NAME + " where " + 
				TableSecretMsgDataCache.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableSecretMsgDataCache.SEND_TIME + " < " + time.getTime() + "";
		Cursor c = getRsd().rawQuery(sql, null);
        if (!c.moveToNext()) {
        	DebugPrinter.e("BaseDao - " + TableSecretMsgDataCache.class + " 获取删除不存在的 loginName=" + loginName );
        	return;
        }
        
		String[] args = {loginName, String.valueOf(time.getTime())};
        getWsd().delete(getTableName(), TableSecretMsgDataCache.LOGIN_NAME + "=? and " + 
        		TableSecretMsgDataCache.SEND_TIME + "=?", args);
    }
}