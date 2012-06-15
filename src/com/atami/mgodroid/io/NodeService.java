package com.atami.mgodroid.io;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.atami.mgodroid.provider.NodeProvider;
import com.atami.mgodroid.util.BlockingIntentService;

public class NodeService extends BlockingIntentService {
	
	public static final String TAG = "NodeService";
	
	public static final String NID = "nid";
	
	private ResultReceiver mReceiver;
	public static String RESULT_RECEIVER = "receiver";
	public static final int STATUS_ERROR = 0;
	public static final int STATUS_COMPLETE = 1;
	public static final int STATUS_RUNNING = 2;

	public NodeService() {
		super("NodeService");
	}
	
	private void insertNode(JSONObject node) throws JSONException{
		ContentResolver cr = getContentResolver();
		ContentValues cv = new ContentValues();
		
		cv.put("nid", node.getString("nid"));
		cv.put("title", node.getString("title"));
		cv.put("comment_count", node.getString("comment_count"));
		cv.put("created", node.getString("created"));
		cv.put("body", node.getString("body"));
		cv.put("path", node.getString("path"));
		
		//mgo.licio.us nodes have a different structure
		if(node.has("field_link")){
			JSONObject field_link = (JSONObject) node.get("field_link");
			cv.put("link", field_link.getString("url"));
		}
		
		cr.insert(NodeProvider.NODES_URI, cv);
	}

	@Override
	protected void onHandleBlockingIntent(Intent intent) {
		String nid = intent.getAction();
		mReceiver = intent.getParcelableExtra(RESULT_RECEIVER);
		mReceiver.send(STATUS_RUNNING, Bundle.EMPTY);
		Log.d(TAG, "NodeService running");
		
		try {
			JSONObject node = APIUtil.getNode(nid, this);
			insertNode(node);
		} catch (Exception e) {
			e.printStackTrace();
			mReceiver.send(STATUS_ERROR, Bundle.EMPTY);
		}finally{
			Log.d(TAG, "NodeService finished");
			mReceiver.send(STATUS_COMPLETE, Bundle.EMPTY);
		}
	}
	
	public static void refreshNode(int nid, Context context,
			ResultReceiver receiver) {
		Intent i = new Intent(context, NodeService.class);
		i.setAction(String.valueOf(nid));
		i.putExtra(NodeService.RESULT_RECEIVER, receiver);
		context.startService(i);
	}

}
