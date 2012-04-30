package com.atami.mgodroid.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.util.Log;

public class NodeProvider extends ContentProvider {

	// The index (key) column name for use in where clauses.
	public static final String KEY_ID = "_id";

	// The name and column index of each column in your database.
	// These should be descriptive.
	public static final String KEY_COLUMN_1_NAME = "KEY_COLUMN_1_NAME";
	
	public static final String KEY_NODE_INDEX_TYPES_NAME_COLUMN = "name";
	
	public static final String KEY_NODE_INDEXES_TYPE_ID_COLUMN = "node_index_type_id";
	public static final String KEY_NODE_INDEXES_TITLE_COLUMN = "title";
	public static final String KEY_NODE_INDEXES_CREATED_COLUMN = "created";
	public static final String KEY_NODE_INDEXES_NID_COLUMN = "nid";
	public static final String KEY_NODE_INDEXES_STICKY_COLUMN = "sticky";
	
	public static final String KEY_NODES_NID_COLUMN = "nid";
	public static final String KEY_NODES_TITLE_COLUMN = "title";
	public static final String KEY_NODES_COMMENT_COUNT_COLUMN = "comment_count";
	public static final String KEY_NODES_CREATED_COLUMN = "created";
	public static final String KEY_NODES_BODY_COLUMN = "body";
	public static final String KEY_NODES_PATH_COLUMN = "path";
	public static final String KEY_NODES_LINK_COLUMN = "link";
	
	public static final String KEY_NODE_COMMENTS_NID_COLUMN = "nid";
	public static final String KEY_NODE_COMMENTS_TITLE_COLUMN = "title";
	public static final String KEY_NODE_COMMENTS_CREATED_COLUMN = "created";
	public static final String KEY_NODE_COMMENTS_BODY_COLUMN = "body";
	public static final String KEY_NODE_COMMENTS_CID_COLUMN = "cid";
	public static final String KEY_NODE_COMMENTS_PID_COLUMN = "pid";
	public static final String KEY_NODE_COMMENTS_THREAD_COLUMN = "thread";

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static class MySQLiteOpenHelper extends SQLiteOpenHelper {

		// Database name, version, and table names.
		private static final String DATABASE_NAME = "Node.db";
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_TABLE_NODE_INDEX_TYPES = "node_index_types";
		private static final String DATABASE_TABLE_NODE_INDEXES = "node_indexes";
		private static final String DATABASE_TABLE_NODES = "nodes";
		private static final String DATABASE_TABLE_NODE_COMMENTS = "node_comments";

		// SQL Statements to create a new database.
		private static final String DATABASE_CREATE_NODE_INDEX_TYPES = "create table "
				+ DATABASE_TABLE_NODE_INDEX_TYPES
				+ " ("
				+ KEY_ID
				+ " integer primary key autoincrement, "
				+ KEY_NODE_INDEX_TYPES_NAME_COLUMN
				+ " text not null);";
		
		private static final String DATABASE_CREATE_NODE_INDEXES = "create table "
				+ DATABASE_TABLE_NODE_INDEXES
				+ " ("
				+ KEY_ID
				+ " integer primary key autoincrement, "
				+ KEY_NODE_INDEXES_TYPE_ID_COLUMN
				+ " integer not null, "
				+ KEY_NODE_INDEXES_TITLE_COLUMN
				+ " text not null, "
				+ KEY_NODE_INDEXES_CREATED_COLUMN
				+" integer not null, "
				+ KEY_NODE_INDEXES_NID_COLUMN
				+ " integer not null, "
				+ KEY_NODE_INDEXES_STICKY_COLUMN
				+ " integer not null);";
		
		private static final String DATABASE_CREATE_NODES = "create table "
				+ DATABASE_TABLE_NODES
				+ " ("
				+ KEY_ID
				+ " integer primary key autoincrement, "
				+ KEY_NODES_NID_COLUMN
				+ " integer not null, "
				+ KEY_NODES_TITLE_COLUMN
				+ " text not null, "
				+ KEY_NODES_COMMENT_COUNT_COLUMN
				+ " integer not null, "
				+ KEY_NODES_CREATED_COLUMN
				+ " integer not null, "
				+ KEY_NODES_BODY_COLUMN
				+ " text not null, "
				+ KEY_NODES_PATH_COLUMN
				+ " text not null, "
				+ KEY_NODES_LINK_COLUMN
				+ " text not null);";
		
		private static final String DATABASE_CREATE_NODE_COMMENTS = "create table "
				+ DATABASE_TABLE_NODE_COMMENTS
				+ " ("
				+ KEY_ID
				+ " integer primary key autoincrement, "
				+ KEY_NODE_COMMENTS_NID_COLUMN
				+ " integer not null, "
				+ KEY_NODE_COMMENTS_TITLE_COLUMN
				+ " text not null, "
				+ KEY_NODE_COMMENTS_CREATED_COLUMN
				+ " integer not null, "
				+ KEY_NODE_COMMENTS_BODY_COLUMN
				+ " text not null, "
				+ KEY_NODE_COMMENTS_CID_COLUMN
				+ " integer not null, "
				+ KEY_NODE_COMMENTS_PID_COLUMN
				+ " integer not null, "
				+ KEY_NODE_COMMENTS_THREAD_COLUMN
				+ " text not null);";

		public MySQLiteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// Called when no database exists in disk and the helper class needs
		// to create a new one.
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_NODE_INDEX_TYPES);
			_db.execSQL(DATABASE_CREATE_NODE_INDEXES);
			_db.execSQL(DATABASE_CREATE_NODES);
			_db.execSQL(DATABASE_CREATE_NODE_COMMENTS);
		}

		// Called when there is a database version mismatch meaning that the
		// version
		// of the database on disk needs to be upgraded to the current version.
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Log the version upgrade.
			Log.w("NodeDBAdapter", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", which will destroy all old data");

			// Upgrade the existing database to conform to the new version.
			// Multiple
			// previous versions can be handled by comparing _oldVersion and
			// _newVersion
			// values.

			// The simplest case is to drop the old table and create a new one.
			_db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_NODE_INDEX_TYPES);
			_db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_NODE_INDEXES);
			_db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_NODES);
			_db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_NODE_COMMENTS);

			// Create a new one.
			onCreate(_db);
		}
	}

}
