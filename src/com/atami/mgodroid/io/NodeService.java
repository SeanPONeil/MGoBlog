package com.atami.mgodroid.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.atami.mgodroid.io.StatusEvents.NodeStatus;
import com.atami.mgodroid.io.StatusEvents.Status;
import com.atami.mgodroid.provider.NodeProvider;
import com.atami.mgodroid.util.BusProvider;
import com.squareup.otto.Produce;

public class NodeService extends IntentService {

	public static final String TAG = "NodeService";

	public static final String NID = "nid";

	private int nid;
	private int status;

	public NodeService() {
		super("NodeService");
		BusProvider.getInstance().register(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BusProvider.getInstance().unregister(this);
	}

	@Produce
	public NodeStatus produceStatus() {
		return new NodeStatus(nid, status);
	}

	private void insertNode(JSONObject node) throws JSONException {
		ContentResolver cr = getContentResolver();
		ContentValues cv = new ContentValues();

		Iterator<?> keys = node.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			cv.put(key, node.getString(key));
		}

		cr.insert(NodeProvider.CONTENT_URI, cv);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		nid = intent.getIntExtra(NID, -1);

		status = Status.RUNNING;
		BusProvider.getInstance().post(produceStatus());

		try {
			JSONObject node = API.getNode(nid);
			insertNode(node);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			status = Status.ERROR;
			BusProvider.getInstance().post(produceStatus());
		} catch (IOException e) {
			e.printStackTrace();
			status = Status.ERROR;
			BusProvider.getInstance().post(produceStatus());
		} catch (JSONException e) {
			e.printStackTrace();
			status = Status.ERROR;
			BusProvider.getInstance().post(produceStatus());
		}
	}

	public static void refreshNode(int nid, Context context) {
		Intent i = new Intent(context, NodeService.class);
		i.putExtra(NID, nid);
		context.startService(i);
	}

}
