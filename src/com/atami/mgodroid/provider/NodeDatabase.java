package com.atami.mgodroid.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class NodeDatabase extends SQLiteOpenHelper {

	// Database name, version, and table names.
	static final String DATABASE_NAME = "Node.db";
	static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE_NODE_INDEXES = "create table "
			+ "node_indices ( _id integer primary key autoincrement, "
			+ "node_index_type text not null, "
			+ "node_title text not null, "
			+ "node_created integer not null, "
			+ "nid integer not null, "
			+ "is_sticky integer not null);";
	
	private static final String DATABASE_CREATE_NODES = "create table "
			+ "nodes ( _id integer primary key autoincrement, "
			+ "nid integer not null, "
			+ "title text not null, "
			+ "comment_count integer not null, "
			+ "created integer not null, "
			+ "body text not null, path text not null, link text not null);";

	private static final String DATABASE_CREATE_NODE_COMMENTS = "create table "
			+ "node_comments ( _id integer primary key autoincrement, "
			+ "nid integer not null, "
			+ "title text not null, "
			+ "created integer not null, "
			+ "body text not null, "
			+ "cid integer not null, "
			+ "pid integer not null, "
			+ "thread string not null);";

	public NodeDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Called when no database exists in disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_NODE_INDEXES);
		db.execSQL(DATABASE_CREATE_NODES);
		db.execSQL(DATABASE_CREATE_NODE_COMMENTS);
		Log.v("Provider", "Database is created");
		seedData(db);
	}

	// Called when there is a database version mismatch meaning that the
	// version
	// of the database on disk needs to be upgraded to the current version.
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log the version upgrade.
		Log.w("NodeDBAdapter", "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

		db.execSQL("drop table if it exists node_indices");
		db.execSQL("drop table if it exists nodes");
		db.execSQL("drop table if it exists node_comments");

		onCreate(db);
	}

	// Create sample data to use
	private void seedData(SQLiteDatabase db) {
		db.execSQL("insert into node_indices (node_index_type, node_title, node_created, nid, is_sticky) values ('MGoBoard', 'Test1', 1234567, 54362, 1);");
		db.execSQL("insert into node_indices (node_index_type, node_title, node_created, nid, is_sticky) values ('MGoBoard', 'Test2', 1234567, 54362, 0);");
		db.execSQL("insert into node_indices (node_index_type, node_title, node_created, nid, is_sticky) values ('MGoBoard', 'Test3', 1234567, 54362, 1);");
		db.execSQL("insert into node_indices (node_index_type, node_title, node_created, nid, is_sticky) values ('MGoBoard', 'Test4', 1234567, 54362, 1);");
	}
}
