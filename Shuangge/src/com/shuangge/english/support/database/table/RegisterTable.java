package com.shuangge.english.support.database.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 所有表的注册
 * 
 * @author Jeffrey
 *
 */
public class RegisterTable {

	private static Map<String, String> createSqlMap = new HashMap<String, String>();
	private static Map<String, String> removeSqlMap = new HashMap<String, String>();
	private static Map<String, ArrayList<String>> createIndexs = new HashMap<String, ArrayList<String>>();
	private static Map<String, ArrayList<String>> removeIndexs = new HashMap<String, ArrayList<String>>();

	public static void register(String tableName, String id, Column ...columns) {
		StringBuilder builder = new StringBuilder();
		Column column = null;
		for (int i = 0; i < columns.length; ++i) {
			column = columns[i];
			builder.append(column.getName()).append(" ").append(column.getTypeAndpragma());
			if (i != columns.length - 1)
				builder.append(",");
		}
		createSqlMap.put(tableName, "CREATE TABLE "+tableName+"("+id+" integer primary key autoincrement, "+builder.toString()+ ");");
		removeSqlMap.put(tableName, "DROP TABLE IF EXISTS "+tableName+";");
	}
	
	public static void createIndex(String tableName, String column, int num) {
		StringBuilder builder = new StringBuilder();
		StringBuilder builder2 = new StringBuilder();
		
		String indexName = "";
		indexName = column + "_index" + num;
		builder.append("CREATE INDEX ");
		builder.append(indexName);
		builder.append(" ON ");
		builder.append(tableName);
		builder.append(" (");
		builder.append(column);
		builder.append(")");
		builder.append(";");
		
		builder2.append("DROP INDEX IF EXISTS ");
		builder2.append(indexName);
		
		ArrayList<String> list = createIndexs.get(tableName);
		if (null == list)
			list = new ArrayList<String>();
		list.add(builder.toString());
		createIndexs.put(tableName, list);
		
		
		list = removeIndexs.get(tableName);
		if (null == list)
			list = new ArrayList<String>();
		list.add(builder2.toString());
		removeIndexs.put(tableName, list);
	}

	public static Map<String, String> getCreateSqlMap() {
		return createSqlMap;
	}

	public static Map<String, String> getRemoveSqlMap() {
		return removeSqlMap;
	}

	public static Map<String, ArrayList<String>> getCreateIndexs() {
		return createIndexs;
	}

	public static Map<String, ArrayList<String>> getRemoveIndexs() {
		return removeIndexs;
	}


	/**
	 * 注册时 Table的列
	 * @author Jeffrey
	 *
	 */
	public static class Column {

		public Column(String name, String typeAndpragma) {
			this.name = name;
			this.typeAndpragma = typeAndpragma;
		}
		
		private String name;
		private String typeAndpragma;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTypeAndpragma() {
			return typeAndpragma;
		}

		public void setTypeAndpragma(String typeAndpragma) {
			this.typeAndpragma = typeAndpragma;
		}

	}

}
