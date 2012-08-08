package com.atami.mgodroid.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.atami.mgodroid.provider.NodeIndexProvider;

public class NodeIndexService extends IntentService {

	//private static String TAG = "NodeIndexService";

	public static String GET_NEXT_PAGE = "get_next_page";
	public static String REFRESH = "node_index_refresh";
	public static String TYPE = "node_index_type";

	public static final int STATUS_ERROR = 0;
	public static final int STATUS_COMPLETE = 1;
	public static final int STATUS_RUNNING = 2;

	private int nodeIndexType;

	public NodeIndexService() {
		super("NodeIndexService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		nodeIndexType = intent.getIntExtra(TYPE, -1);
		// mReceiver.send(STATUS_RUNNING, Bundle.EMPTY);
		// Log.d(TAG, action + " running");

		try {
			if (action.equals(GET_NEXT_PAGE)) {
				JSONArray index;
				index = API.getNodeIndex(nodeIndexType, getNextPageNumber(),
						false);
				insertNodeIndex(index);
			} else if (action.equals(REFRESH)) {
				JSONArray index = API.getNodeIndex(nodeIndexType, 0, false);
				dropNodeIndex();
				insertNodeIndex(index);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// mReceiver.send(STATUS_COMPLETE, Bundle.EMPTY);
		// Log.d(TAG, action + " completed");
	}

	/**
	 * Drops all rows associated with the current node index type.
	 */
	private void dropNodeIndex() {
		ContentResolver cr = getContentResolver();
		cr.delete(NodeIndexProvider.CONTENT_URI,
				NodeIndexProvider.WHERE[nodeIndexType],
				NodeIndexProvider.WHERE_ARGS[nodeIndexType]);
	}

	/**
	 * Queries the Node Index Content Provider for the current count of rows for
	 * the current node index type. This value is divided by the page size (20)
	 * to get the value of the next page number to retrieve.
	 * 
	 * @return the next page number to retrieve
	 */
	private int getNextPageNumber() {
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(NodeIndexProvider.CONTENT_URI,
				new String[] { "count(*) AS count" },
				NodeIndexProvider.WHERE[nodeIndexType],
				NodeIndexProvider.WHERE_ARGS[nodeIndexType], null);
		c.moveToFirst();
		return c.getInt(0) / 20;
	}

	/**
	 * Iterates over each object in the JSONArray index, creating
	 * a new row for each object. Each object is iterated over its
	 * own fields, using the field names as the key in the content
	 * provider row.
	 * @param index JSONArray of node index JSONObjects
	 * @throws JSONException
	 */
	private void insertNodeIndex(JSONArray index) throws JSONException {
		ContentResolver cr = getContentResolver();
		for (int i = 0; i < index.length(); i++) {
			ContentValues cv = new ContentValues();
			JSONObject o = index.getJSONObject(i);
			Iterator<?> keys = o.keys();

			while (keys.hasNext()) {
				String key = (String) keys.next();
				cv.put(key, o.getString(key));
			}
			cr.insert(NodeIndexProvider.CONTENT_URI, cv);
		}
	}

	/**
	 * Helper method for refreshing the rows for a given node index
	 * @param nodeIndexType an int constant identifying the node index
	 * @param context used to start the IntentService
	 */
	public static void refreshNodeIndex(int nodeIndexType, Context context) {
		final Intent i = new Intent(context, NodeIndexService.class);
		i.setAction(NodeIndexService.REFRESH);
		i.putExtra(TYPE, nodeIndexType);
		context.startService(i);
	}

	/**
	 * Helper method for getting the next page of rows for a given node index
	 * @param indexType an int constant identifying the node index
	 * @param context used to start the IntentService
	 */
	public static void getNextNodeIndexPage(String indexType, Context context) {
		Intent i = new Intent(context, NodeIndexService.class);
		i.setAction(NodeIndexService.GET_NEXT_PAGE);
		i.putExtra(NodeIndexService.TYPE, indexType);
		context.startService(i);
	}
}
