package com.atami.mgodroid.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Proxy ResultReceiver that offers a listener interface that can be detached.
 * Useful for when sending callbacks to a Service where a listening Activity or
 * Fragment can be swapped out during configuration changes.
 */
public class DetachableResultReceiver extends ResultReceiver implements
		Parcelable {
	private static final String TAG = "DetachableResultReceiver";

	private Receiver mReceiver;

	public DetachableResultReceiver(Handler handler) {
		super(handler);
	}

	public void clearReceiver() {
		mReceiver = null;
	}

	public void setReceiver(Receiver receiver) {
		mReceiver = receiver;
	}

	public interface Receiver {
		public void onReceiveResult(int resultCode, Bundle resultData);
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		if (mReceiver != null) {
			mReceiver.onReceiveResult(resultCode, resultData);
		} else {
			Log.w(TAG, "Dropping result on floor for code " + resultCode + ": "
					+ resultData.toString());
		}
	}
}
