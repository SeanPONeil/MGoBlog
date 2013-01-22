package com.atami.mgodroid.io;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.events.NodeIndexFailureEvent;
import com.atami.mgodroid.events.NodeIndexSuccessEvent;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class NodeIndexTaskService extends Service implements NodeIndexTask.Callback {

    private static final String TAG = "NodeIndexTaskService";

    @Inject
    NodeIndexTaskQueue queue;
    @Inject
    Bus bus;

    private boolean running;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MGoBlogApplication) getApplication()).objectGraph().inject(this);
        Log.i(TAG, "Service starting!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //executeNext();
        return START_STICKY;
    }

    private void executeNext() {
        if (running) return; // Only one task at a time.

        NodeIndexTask task = queue.peek();
        if (task != null) {
            running = true;
            task.execute(this);
        } else {
            Log.i(TAG, "Service stopping!");
            stopSelf(); // No more tasks are present. Stop.
        }
    }

    @Override
    public void onSuccess(String type) {
        running = false;
        queue.remove();
        bus.post(new NodeIndexSuccessEvent(type));
        executeNext();
    }

    @Override
    public void onFailure(String type) {
        running = false;
        queue.remove();
        bus.post(new NodeIndexFailureEvent(type));
        executeNext();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
