package com.atami.mgodroid.io;

import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

public class NodeIndexServiceHelper {

	// Hashmap of node index types to booleans. Keeps track of
	// what Intents have been queued in NodeIndexService
	private ConcurrentHashMap<String, Boolean> threadQueue;

	// Private constructor prevents instantiation from other classes
	private NodeIndexServiceHelper() {
		threadQueue = new ConcurrentHashMap<String, Boolean>();
	}

	/**
	 * Holder is loaded on the first execution of
	 * NodeIndexServiceHelper.getInstance() or the first access to
	 * Holder.INSTANCE, not before.
	 */
	private static class Holder {
		public static final NodeIndexServiceHelper instance = new NodeIndexServiceHelper();
	}

	public static NodeIndexServiceHelper getInstance() {
		return Holder.instance;
	}

	// Utility methods for sending refresh node index intents

	public static void refreshNodeIndex(String indexType, Context context, ResultReceiver receiver) {
		Boolean alreadyQueued = NodeIndexServiceHelper.getInstance().threadQueue
				.get(indexType);
		if (alreadyQueued == null || alreadyQueued == false) {
			// Intent to refresh is not queued already
			// Launch Intent Service to refresh data
			Intent i = new Intent(context, NodeIndexService.class);
			i.setAction(NodeIndexService.REFRESH);
			i.putExtra(NodeIndexService.TYPE, indexType);
			i.putExtra(NodeIndexService.RESULT_RECEIVER, receiver);
			context.startService(i);
			NodeIndexServiceHelper.getInstance().threadQueue.put(indexType,
					true);
		} else {
			Log.d("Helper", "Intent already queued");
		}
	}

	public static void getNextNodeIndexPage(String indexType, Context context, ResultReceiver receiver) {
		Boolean alreadyQueued = NodeIndexServiceHelper.getInstance().threadQueue
				.get(indexType);
		if (alreadyQueued == null || alreadyQueued == false) {
			Intent i = new Intent(context, NodeIndexService.class);
			i.setAction(NodeIndexService.GET_NEXT_PAGE);
			i.putExtra(NodeIndexService.TYPE, indexType);
			i.putExtra(NodeIndexService.RESULT_RECEIVER, receiver);
			context.startService(i);
			NodeIndexServiceHelper.getInstance().threadQueue.put(indexType,
					true);
		} else {
			Log.d("Helper", "Intent already queued");
		}
	}

	public static void threadCompleted(String indexType) {
		NodeIndexServiceHelper.getInstance().threadQueue.put(indexType, false);
	}
}
