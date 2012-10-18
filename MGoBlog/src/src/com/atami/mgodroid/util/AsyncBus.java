package com.atami.mgodroid.util;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Asynchronous Otto BusProvider that always posts events on the main thread
 * @author Jake Wharton
 */
public class AsyncBus extends Bus {
	private final Handler mainThread = new Handler(Looper.getMainLooper());

	@Override
	public void post(final Object event) {
		mainThread.post(new Runnable() {
			@Override
			public void run() {
				AsyncBus.super.post(event);
			}
		});
	}
}
