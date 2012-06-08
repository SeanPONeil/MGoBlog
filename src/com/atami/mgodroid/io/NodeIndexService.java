package com.atami.mgodroid.io;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.atami.mgodroid.provider.NodeIndexProvider;
import com.atami.mgodroid.util.BlockingIntentService;

public class NodeIndexService extends BlockingIntentService {

	private static String TAG = "NodeIndexService";

	public static String GET_NEXT_PAGE = "get_next_page";
	public static String REFRESH = "node_index_refresh";
	public static String TYPE = "node_index_type";
	
	public static String RESULT = "result";
	public static String RESULT_RECEIVER = "receiver";
	public static final int STATUS_ERROR = 0;
	public static final int STATUS_COMPLETE = 1;
	public static final int STATUS_RUNNING = 2;
	
	private ResultReceiver mReceiver;

	public NodeIndexService() {
		super("NodeIndexService");
	}
	
	@Override
	protected void onHandleBlockingIntent(Intent intent) {
		String action = intent.getAction();
		String indexType = intent.getStringExtra(TYPE);
		mReceiver = intent.getParcelableExtra(RESULT_RECEIVER);
		mReceiver.send(STATUS_RUNNING, Bundle.EMPTY);
		Log.d(TAG, action + " running");
		
		if (action.equals(GET_NEXT_PAGE)) {
			getNextNodeIndexPage(indexType);
		} else if (action.equals(REFRESH)) {
			refreshIndexPage(indexType);
		}
		
		mReceiver.send(STATUS_COMPLETE, Bundle.EMPTY);
		Log.d(TAG, action + " completed");
	}

	// Drops all rows where node_type is type. This should be
	// used when a user refreshes a NodeIndexListFragment and
	// needs all fresh data
	private void dropNodeIndex(String type) {
		ContentResolver cr = getContentResolver();
		cr.delete(NodeIndexProvider.NODE_INDEX_URI, "node_index_type = ?",
				new String[] { type });
	}

	// Returns the page number to retrieve from MGoBlog based on
	// what is currently in the database.
	private int getMaxPage(String type) {
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(NodeIndexProvider.NODE_INDEX_URI,
				new String[] { "MAX(page) as maxPage" }, null, null, null);
		if (c.getCount() == 0) {
			return 0;
		}
		c.moveToFirst();
		return c.getInt(0) + 1;
	}

	private JSONArray getNodeIndexPage(int page, String type) {
		JSONArray index = new JSONArray();
		try {
			index = APIUtil.getNodeIndex(type, page, this);
			Log.v(TAG, index.toString());
		} catch(Exception e){
			e.printStackTrace();
			mReceiver.send(STATUS_ERROR, Bundle.EMPTY);
		}
		return index;
	}

	private void insertNodeIndex(JSONArray index, int page) {
		ContentResolver cr = getContentResolver();
		ArrayList<ContentValues> values = new ArrayList<ContentValues>();
		for (int i = 0; i < index.length(); i++) {
			ContentValues cv = new ContentValues();
			try {
				cv.put("node_index_type",
						index.getJSONObject(i).getString("type"));
				cv.put("node_title", index.getJSONObject(i).getString("title"));
				cv.put("node_created",
						index.getJSONObject(i).getString("created"));
				cv.put("nid", index.getJSONObject(i).getString("nid"));
				cv.put("is_sticky", index.getJSONObject(i).getString("sticky"));
				cv.put("page", page);
				values.add(cv);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		cr.bulkInsert(NodeIndexProvider.NODE_INDEX_URI,
				values.toArray(new ContentValues[0]));
	}

	// Gets the next node index page given the type, and
	// inserts it into the database.
	private void getNextNodeIndexPage(String type) {
		int page = getMaxPage(type);
		JSONArray index = getNodeIndexPage(page, type);
		insertNodeIndex(index, page);
	}

	//Gets the first node index page given the type, and
	//if successful drops all rows for that type and inserts
	//the retrieved page.
	private void refreshIndexPage(String type) {
		JSONArray index = getNodeIndexPage(0, type);
		if (index.length() != 0) {
			dropNodeIndex(type);
			insertNodeIndex(index, 0);
		}
	}
}
