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
import android.util.Log;

import com.atami.mgodroid.provider.NodeIndexProvider;

public class APIIntentService extends IntentService {
	
	private static String TAG = "APIIntentService";

	public static String NODE_INDEX_DOWNLOAD = "node_index_download";
	public static String NODE_INDEX_TYPE = "node_index_type";
	public static String NODE_INDEX_PAGE = "node_index_page";
	public static String NODE_DOWNLOAD = "node_download";
	public static String NODE_NID = "node_nid";

	public APIIntentService() {
		super("MGoBlog");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();

		if (action.equals(NODE_INDEX_DOWNLOAD)) {
			Log.d(TAG, "Action = " + action);
			String type = intent.getStringExtra(NODE_INDEX_TYPE);
			String page = intent.getStringExtra(NODE_INDEX_PAGE);
			JSONArray index = new JSONArray();
			try {
				index = API.getNodeIndex(type, Integer.parseInt(page), this);
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

			ContentResolver cr = getContentResolver();
			ArrayList<ContentValues> values = new ArrayList<ContentValues>();
			for (int i = 0; i < index.length(); i++) {
				ContentValues cv = new ContentValues();
				try {
					cv.put("node_index_type",
							index.getJSONObject(i).getString("type"));
					cv.put("node_title",
							index.getJSONObject(i).getString("title"));
					cv.put("node_created",
							index.getJSONObject(i).getString("created"));
					cv.put("nid", index.getJSONObject(i).getString("nid"));
					cv.put("is_sticky",
							index.getJSONObject(i).getString("sticky"));
					values.add(cv);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			cr.bulkInsert(NodeIndexProvider.NODE_INDEX_URI,
					values.toArray(new ContentValues[0]));

		}

	}

}
