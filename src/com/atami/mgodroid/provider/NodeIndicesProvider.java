package com.atami.mgodroid.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class NodeIndicesProvider extends ContentProvider {

	private NodeDatabase db;

	public static final Uri NODE_INDICES_URI = Uri
			.parse("content://com.mgoblog.nodeprovider/node_indices");

	private static final int NODE_INDICES_ALL = 1;
	private static final int NODE_INDICES_TYPE = 2;

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI("com.mgoblog.nodeprovider", "node_indices",
				NODE_INDICES_ALL);
		sURIMatcher.addURI("com.mgoblog.nodeprovider", "node_indices/*",
				NODE_INDICES_TYPE);
	}

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
		db = new NodeDatabase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables("node_indices");


		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case NODE_INDICES_TYPE:
			queryBuilder.appendWhere("node_index_type = "
					+ uri.getLastPathSegment());
			break;
		case NODE_INDICES_ALL:
			// no filter
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(db.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
