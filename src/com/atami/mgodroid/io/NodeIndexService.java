package com.atami.mgodroid.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.atami.mgodroid.provider.NodeIndexProvider;

public class NodeIndexService extends IntentService {

	private static String TAG = "NodeIndexService";

	public static String GET_NEXT_PAGE = "get_next_page";
	public static String REFRESH = "node_index_refresh";
	public static String TYPE = "node_index_type";

	public NodeIndexService() {
		super("MGoBlog");
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	private void refreshIndexPage(String type) {
		JSONArray index = getNodeIndexPage(0, type);
		if (index.length() != 0) {
			dropNodeIndex(type);
			insertNodeIndex(index, 0);
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		String type = intent.getStringExtra(TYPE);
		Log.d(TAG, "Action = " + action);

		if (action.equals(GET_NEXT_PAGE)) {
			getNextNodeIndexPage(type);
		} else if (action.equals(REFRESH)) {
			refreshIndexPage(type);
		}
		NodeIndexServiceHelper.threadCompleted(type);
	}

}
