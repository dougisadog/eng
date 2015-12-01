package com.shuangge.english.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.shuangge.english.entity.server.secretmsg.LocalDataCache;
import com.shuangge.english.entity.table.TableVersionNo;
import com.shuangge.english.support.database.dao.BaseDao;
import com.shuangge.english.support.database.table.IdTable;

public class DaoVersionNoCache extends BaseDao<LocalDataCache> {

	public DaoVersionNoCache() {
		super(TableVersionNo.TABLE_NAME);
	}
	
	public Integer getVersionNo(String loginName) {
		String sql = "select * from " + TableVersionNo.TABLE_NAME + " where " + 
				TableVersionNo.LOGIN_NAME + " = '" + loginName + "'";
		Cursor c = getRsd().rawQuery(sql, null);
        Integer versionNo = null;
        if (c.moveToNext()) {
        	versionNo = c.getInt(c.getColumnIndex(TableVersionNo.VERSION_NO));
        }
        c.close();
		return versionNo;
	}
	
//	public Integer getMsgNo(String loginName) {
//		String sql = "select * from " + TableLocalData.TABLE_NAME + " where " + 
//				TableLocalData.LOGIN_NAME + " = '" + loginName + "";
//		Cursor c = getRsd().rawQuery(sql, null);
//        Gson gson = new Gson();
//        Integer msgNo = null;
//        if (c.moveToNext()) {
//            String json = c.getString(c.getColumnIndex(NetworkTable.ENTITY));
//            if (!TextUtils.isEmpty(json)) {
//                try {
//                	msgNo = gson.fromJson(json, Integer.class);
//                } catch (JsonSyntaxException e) {
//                    e.printStackTrace();
//                    DebugPrinter.e("BaseDao - " + Integer.class + " 获取json失败");
//                }
//            }
//        }
//        c.close();
//		return msgNo;
//	}
	
	public void updateVersionNo(String loginName, Integer versionNo) {
		String sql = "select * from " + TableVersionNo.TABLE_NAME + " where " + 
				TableVersionNo.LOGIN_NAME + " = '" + loginName + "' and " + 
				TableVersionNo.VERSION_NO + " = " + versionNo + "";
		Cursor c = getRsd().rawQuery(sql, null);
        ContentValues cv = new ContentValues();
        cv.put(TableVersionNo.VERSION_NO, versionNo);
        if (c.moveToNext()) {
            String[] args = {loginName, versionNo.toString() +""};
            getWsd().update(getTableName(), cv, TableVersionNo.LOGIN_NAME 
            		+ "=? and " + TableVersionNo.VERSION_NO + "=?", args);
        }
        else {
        	cv.put(TableVersionNo.LOGIN_NAME, loginName);
            getWsd().insert(getTableName(), IdTable.ID, cv);
        }
        c.close();
	}
	
//	public void updateMaxMsgNo(String loginName, Integer msgNo) {
//		String sql = "select * from " + TableLocalData.TABLE_NAME + " where " + 
//				TableLocalData.LOGIN_NAME + " = '" + loginName + "' and " + 
//				TableLocalData.MSG_NO + " = " + msgNo + "";
//		Cursor c = getRsd().rawQuery(sql, null);
//        ContentValues cv = new ContentValues();
//        if (c.moveToNext()) {
//            String[] args = {loginName, msgNo +""};
//            getWsd().update(getTableName(), cv, TableLocalData.LOGIN_NAME 
//            		+ "=? and " + TableLocalData.VERSION_NO + "=?", args);
//        }
//        else {
//        	cv.put(TableLocalData.LOGIN_NAME, loginName);
//    		cv.put(TableLocalData.MSG_NO, msgNo);
//            getWsd().insert(getTableName(), IdTable.ID, cv);
//        }
//        c.close();
//	}
	
}