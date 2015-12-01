package com.shuangge.english.entity.table;

import com.shuangge.english.support.database.table.IdTable;
import com.shuangge.english.support.database.table.RegisterTable;
import com.shuangge.english.support.database.table.RegisterTable.Column;

public class TableRegisterUtils {

	public static void registerAll() {
		
		RegisterTable.register(TableResType2.TABLE_NAME, TableResType2.ID, 
				new Column(TableResType2.COLUMN_ID, IdTable.TYPE_TEXT), 
				new Column(TableResType2.ENTITY, IdTable.TYPE_TEXT));
		
		RegisterTable.register(TableResType4.TABLE_NAME, TableResType4.ID,
				new Column(TableResType4.COLUMN_ID, IdTable.TYPE_TEXT), 
				new Column(TableResType4.COLUMN_PARENT_ID, IdTable.TYPE_TEXT), 
				new Column(TableResType4.ENTITY, IdTable.TYPE_TEXT));
		
		RegisterTable.register(TableUser.TABLE_NAME, TableUser.ID, 
				new Column(TableUser.ENTITY, IdTable.TYPE_TEXT));
		
		RegisterTable.register(TableLessonDataCache.TABLE_NAME, TableLessonDataCache.ID,
				new Column(TableLessonDataCache.LOGIN_NAME, IdTable.TYPE_TEXT), 
				new Column(TableLessonDataCache.TYPE5_ID, IdTable.TYPE_TEXT), 
				new Column(TableLessonDataCache.TYPE6_ID, IdTable.TYPE_TEXT), 
				new Column(TableLessonDataCache.ENTITY, IdTable.TYPE_TEXT));
		
		RegisterTable.register(TableSecretMsgDataCache.TABLE_NAME, TableSecretMsgDataCache.ID,
				new Column(TableSecretMsgDataCache.LOGIN_NAME, IdTable.TYPE_TEXT), 
				new Column(TableSecretMsgDataCache.FRIEND_NO, IdTable.TYPE_INTEGER), 
				new Column(TableSecretMsgDataCache.SEND_TIME, IdTable.TYPE_INTEGER),
				new Column(TableSecretMsgDataCache.SECRET_MSG_NO, IdTable.TYPE_INTEGER), 
				new Column(TableSecretMsgDataCache.STATUS, IdTable.TYPE_INTEGER), 
				new Column(TableSecretMsgDataCache.ENTITY, IdTable.TYPE_TEXT));
		
		RegisterTable.createIndex(TableSecretMsgDataCache.TABLE_NAME, TableSecretMsgDataCache.LOGIN_NAME, 1);
		RegisterTable.createIndex(TableSecretMsgDataCache.TABLE_NAME, TableSecretMsgDataCache.FRIEND_NO, 1);
		RegisterTable.createIndex(TableSecretMsgDataCache.TABLE_NAME, TableSecretMsgDataCache.SEND_TIME, 1);
		RegisterTable.createIndex(TableSecretMsgDataCache.TABLE_NAME, TableSecretMsgDataCache.SECRET_MSG_NO, 1);
		RegisterTable.createIndex(TableSecretMsgDataCache.TABLE_NAME, TableSecretMsgDataCache.STATUS, 1);
		
		RegisterTable.register(TableAttentionDataCache.TABLE_NAME, TableAttentionDataCache.ID,
				new Column(TableAttentionDataCache.LOGIN_NAME, IdTable.TYPE_TEXT), 
				new Column(TableAttentionDataCache.USER_NO, IdTable.TYPE_INTEGER), 
				new Column(TableAttentionDataCache.VERSION_NO, IdTable.TYPE_INTEGER),
				new Column(TableAttentionDataCache.ENTITY, IdTable.TYPE_TEXT));
		
		RegisterTable.createIndex(TableAttentionDataCache.TABLE_NAME, TableAttentionDataCache.LOGIN_NAME, 2);
		RegisterTable.createIndex(TableAttentionDataCache.TABLE_NAME, TableAttentionDataCache.USER_NO, 2);
		RegisterTable.createIndex(TableAttentionDataCache.TABLE_NAME, TableAttentionDataCache.VERSION_NO, 2);
		
		RegisterTable.register(TableVersionNo.TABLE_NAME, TableAttentionDataCache.ID,
				new Column(TableVersionNo.LOGIN_NAME, IdTable.TYPE_TEXT), 
				new Column(TableVersionNo.VERSION_NO, IdTable.TYPE_INTEGER));
		
		RegisterTable.createIndex(TableVersionNo.TABLE_NAME, TableVersionNo.LOGIN_NAME, 3);
		RegisterTable.createIndex(TableVersionNo.TABLE_NAME, TableVersionNo.VERSION_NO, 3);
		
//		RegisterTable.register(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.ID,
//				new Column(TableDateMsgDataCache.LOGIN_NAME, IdTable.TYPE_TEXT), 
//				new Column(TableDateMsgDataCache.USER_SEX, IdTable.TYPE_INTEGER), 
//				new Column(TableDateMsgDataCache.FRIEND_NAME, IdTable.TYPE_TEXT), 
//				new Column(TableDateMsgDataCache.SEND_TIME, IdTable.TYPE_INTEGER),
//				new Column(TableDateMsgDataCache.DATE_MSG_NO, IdTable.TYPE_INTEGER), 
//				new Column(TableDateMsgDataCache.STATUS, IdTable.TYPE_INTEGER), 
//				new Column(TableDateMsgDataCache.VERSION_NO, IdTable.TYPE_INTEGER),
//				new Column(TableDateMsgDataCache.ENTITY, IdTable.TYPE_TEXT));
//		
//		RegisterTable.createIndex(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.LOGIN_NAME, 4);
//		RegisterTable.createIndex(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.USER_SEX, 4);
//		RegisterTable.createIndex(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.VERSION_NO, 4);
//		RegisterTable.createIndex(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.FRIEND_NAME, 4);
//		RegisterTable.createIndex(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.SEND_TIME, 4);
//		RegisterTable.createIndex(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.DATE_MSG_NO, 4);
//		RegisterTable.createIndex(TableDateMsgDataCache.TABLE_NAME, TableDateMsgDataCache.STATUS, 4);
		
	}
	
}
