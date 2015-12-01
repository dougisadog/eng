package com.shuangge.english.support.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shuangge.english.GlobalApp;
import com.shuangge.english.entity.table.TableAttentionDataCache;
import com.shuangge.english.entity.table.TableSecretMsgDataCache;
import com.shuangge.english.entity.table.TableVersionNo;
import com.shuangge.english.support.database.table.RegisterTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance = null;
    private static final String DATABASE_NAME = "sg.reader.db";
    private static final int DATABASE_VERSION = 2;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	createAllTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
        	return;
        }
        if (oldVersion == 1 && newVersion == 2) {
        	upgrade1To2(db);
        }
        else {
        	deleteAllTable(db);
            createAllTable(db);
        }
    }

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
        	instance = new DatabaseHelper(GlobalApp.getInstance());
        }
        return instance;
    }
    
    private void createAllTable(SQLiteDatabase db) {
    	for (String key : RegisterTable.getCreateSqlMap().keySet()) {
			db.execSQL(RegisterTable.getCreateSqlMap().get(key));
		}
    	for (String key : RegisterTable.getCreateIndexs().keySet()) {
    		for (String index : RegisterTable.getCreateIndexs().get(key)) {
    			db.execSQL(index);
			}
    	}
    }

    private void deleteAllTable(SQLiteDatabase db) {
    	for (String key : RegisterTable.getRemoveSqlMap().keySet()) {
			db.execSQL(RegisterTable.getRemoveSqlMap().get(key));
		}
    	for (String key : RegisterTable.getRemoveIndexs().keySet()) {
    		for (String index : RegisterTable.getRemoveIndexs().get(key)) {
    			db.execSQL(index);
			}
    	}
    }

    private void upgrade1To2(SQLiteDatabase db) {
    	db.execSQL(RegisterTable.getCreateSqlMap().get(TableAttentionDataCache.TABLE_NAME));
    	db.execSQL(RegisterTable.getCreateSqlMap().get(TableSecretMsgDataCache.TABLE_NAME));
    	db.execSQL(RegisterTable.getCreateSqlMap().get(TableVersionNo.TABLE_NAME));
    	
    	ArrayList<String> indexMap = RegisterTable.getCreateIndexs().get(TableAttentionDataCache.TABLE_NAME);
    	if (null != indexMap) {
    		for (String index : RegisterTable.getCreateIndexs().get(TableAttentionDataCache.TABLE_NAME)) {
    			db.execSQL(index);
    		}
    	}
    	indexMap = RegisterTable.getCreateIndexs().get(TableSecretMsgDataCache.TABLE_NAME);
    	if (null != indexMap) {
    		for (String index : RegisterTable.getCreateIndexs().get(TableSecretMsgDataCache.TABLE_NAME)) {
    			db.execSQL(index);
    		}
    	}
    	indexMap = RegisterTable.getCreateIndexs().get(TableVersionNo.TABLE_NAME);
    	if (null != indexMap) {
    		for (String index : RegisterTable.getCreateIndexs().get(TableVersionNo.TABLE_NAME)) {
    			db.execSQL(index);
    		}
    	}
    }

}