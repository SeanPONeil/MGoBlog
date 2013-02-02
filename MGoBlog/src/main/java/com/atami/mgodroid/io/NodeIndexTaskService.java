package com.atami.mgodroid.io;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.activeandroid.query.Update;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.events.NodeIndexTaskStatus;
import com.atami.mgodroid.models.NodeIndex;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import retrofit.http.Callback;
import retrofit.http.RetrofitError;

import javax.inject.Inject;
import java.util.List;

/**
 * Service for running NodeIndexTasks in the background. NodeIndexTasks are retrieved from the NodeIndexTaskQueue and
 * executed in a separate thread one at a time. Callbacks are run on the main thread,
 * except for writes to the database which are done in a separate thread.
 */
public class NodeIndexTaskService extends Service implements Callback<List<NodeIndex>> {

    private static final String TAG = "NodeIndexTaskService";

    @Inject
    NodeIndexTaskQueue queue;
    @Inject
    Bus bus;

    private boolean running;
    private int taskId;

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

        NodeIndexTask task = queue.peek();
        if (task != null) {
            running = true;
            bus.post(produceStatus(running, taskId));

            taskId = task.getId();
            task.execute(this);
        } else {
            Log.i(TAG, "Service stopping!");
            stopSelf(); // No more tasks are present. Stop.
        }
    }

    @Produce
    public NodeIndexTaskStatus produceStatus(boolean running, int taskId) {
        return new NodeIndexTaskStatus(running, taskId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void success(final List<NodeIndex> nodeIndexes) {
        Log.i(TAG, "Success!");
        Log.d(TAG, nodeIndexes.toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(NodeIndex ni: nodeIndexes){
                    ni.save();
                }
            }
        }).start();
        running = false;
        queue.remove();
        bus.post(produceStatus(running, taskId));
        executeNext();
    }

    @Override
    public void failure(RetrofitError error) {
        if (error.isNetworkError()) {
            Log.i(TAG, "Network error!");
        } else {
            Log.i(TAG, "Non network error! Something is wrong!");
        }
        running = false;
        queue.remove();
        bus.post(produceStatus(running, taskId));
        executeNext();
    }
}
