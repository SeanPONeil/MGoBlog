package com.atami.mgodroid.io;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.events.NodeIndexTaskStatus;
import com.atami.mgodroid.events.NodeTaskStatus;
import com.atami.mgodroid.models.Node;
import com.atami.mgodroid.models.NodeIndex;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.TaskQueue;
import retrofit.http.Callback;
import retrofit.http.RetrofitError;

import javax.inject.Inject;

/**
 * Service for running NodeTasks in the background. NodeTasks are retrieved from the TaskQueue and
 * executed in a separate thread one at a time. Callbacks are run on the main thread,
 * except for writes to the database which are done in a separate thread.
 */
public class NodeTaskService extends Service implements Callback<Node> {

    private static final String TAG = "NodeTaskService";

    @Inject
    TaskQueue<NodeTask> queue;

    @Inject
    Bus bus;

    private boolean running;
    private String taskTag;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MGoBlogApplication) getApplication()).objectGraph().inject(this);
        Log.i(TAG, "Service starting!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executeNext();
        return START_STICKY;
    }

    private void executeNext() {
        if (running) return; // Only one task at a time.

        NodeTask task = queue.peek();
        if (task != null) {
            running = true;
            taskTag = task.getTag();
            bus.post(produceStatus(running, taskTag));
            task.execute(this);
        } else {
            Log.i(TAG, "Service stopping!");
            stopSelf(); // No more tasks are present. Stop.
        }
    }

    @Produce
    public NodeTaskStatus produceStatus(boolean running, String taskTag) {
        return new NodeTaskStatus(running, taskTag);
    }

    @Override
    public void success(final Node node) {
        Log.i(TAG, "Success!");
        Log.i(TAG, node.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                node.clean();
                node.save();
            }
        }).start();
        running = false;
        queue.remove();
        bus.post(produceStatus(running, taskTag));
        executeNext();
    }

    @Override
    public void failure(RetrofitError error) {
        if (error.isNetworkError()) {
            Log.i(TAG, "Network error!");
        } else {
            Log.i(TAG, "Non network error! Something is wrong!");
            Log.i(TAG, error.getBody().toString());
            Log.i(TAG, error.getUrl());
        }
        running = false;
        queue.remove();
        bus.post(produceStatus(running, taskTag));
        executeNext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
