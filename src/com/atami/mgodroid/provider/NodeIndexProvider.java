package com.atami.mgodroid.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class NodeIndexProvider extends ContentProvider {

	private NodeDatabase db;

	public static final Uri NODE_INDEX_URI = Uri
			.parse("content://com.mgoblog.nodeprovider/node_index");
	
	public static final String TABLE = "node_index";

	private static final int ALLROWS = 1;
	private static final int SINGLE_ROW = 2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.mgoblog.nodeprovider", TABLE, ALLROWS);
		uriMatcher.addURI("com.mgoblog.nodeprovider", TABLE + "/#",
				SINGLE_ROW);
	}

	@Override
	public boolean onCreate() {
		db = new NodeDatabase(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = "_id = "
					+ rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		default:
			break;
		}

		// To return the number of deleted items, you must specify a where
		// clause. To delete all rows and return a value, pass in "1"
		if (selection == null) {
			selection = "1";
		}

		// Execute the deletion
		int deleteCount = db.getWritableDatabase().delete(TABLE,
				selection, selectionArgs);

		// Notify any observers of the change in the data set
		getContext().getContentResolver().notifyChange(uri, null);

		return deleteCount;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = db.getWritableDatabase().insert(TABLE, null, values);
		if (id > -1) {
			// Construct and return the URI of the newly inserted row.
			Uri insertedId = ContentUris.withAppendedId(NODE_INDEX_URI, id);
			getContext().getContentResolver().notifyChange(insertedId, null);
			return insertedId;
		}
		return null;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase sqlDB = db.getWritableDatabase();
		sqlDB.beginTransaction();
		try {

			for (ContentValues cv : values) {
				long newID = sqlDB.insertOrThrow(TABLE, null, cv);
				if (newID <= 0) {
					throw new SQLException("Failed to insert row into " + uri);
				}
			}
			sqlDB.setTransactionSuccessful();
			getContext().getContentResolver().notifyChange(uri, null);
		} finally {
			sqlDB.endTransaction();
		}
		return values.length;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(TABLE);

		int uriType = uriMatcher.match(uri);
		switch (uriType) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere("_id = " + rowID);
		default:
			break;
		}

		Cursor cursor = queryBuilder.query(db.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = "_id = "
					+ rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		default:
			break;
		}

		int updateCount = db.getWritableDatabase().update(TABLE, values,
				selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}

	@Override
	public String getType(Uri uri) {
		// Return a string that identifies the MIME type
		// for a Content Provider URI
		switch (uriMatcher.match(uri)) {
		case ALLROWS:
			return "vnd.android.cursor.dir/vnd.mgoblog.node_index";
		case SINGLE_ROW:
			return "vnd.android.cursor.item/vnd.mgoblog.node_index";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
}
