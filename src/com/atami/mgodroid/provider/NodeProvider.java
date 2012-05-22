package com.atami.mgodroid.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class NodeProvider extends ContentProvider {
	
	private NodeDatabase db;

	public static final Uri NODES_URI = Uri
			.parse("content://com.mgoblog.nodeprovider/nodes");
	
	public static final String TABLE = "nodes";

	private static final int ALLROWS = 1;
	private static final int SINGLE_ROW = 2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.mgoblog.nodeprovider", "nodes", ALLROWS);
		uriMatcher.addURI("com.mgoblog.nodeprovider", "nodes/#",
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
		if(id > -1){
			//Construct and return the URI of the newly inserted row.
			Uri insertedId = ContentUris.withAppendedId(NODES_URI, id);
			getContext().getContentResolver().notifyChange(insertedId, null);
			return insertedId;
		}
		return null;
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
			return "vnd.android.cursor.dir/vnd.mgoblog.node";
		case SINGLE_ROW:
			return "vnd.android.cursor.item/vnd.mgoblog.node";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

}
