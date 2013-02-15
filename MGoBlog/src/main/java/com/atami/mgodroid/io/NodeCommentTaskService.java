package com.atami.mgodroid.io;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.activeandroid.ActiveAndroid;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.events.NodeCommentTaskStatus;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.util.MobileHTMLUtil;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.TaskQueue;
import retrofit.http.Callback;
import retrofit.http.RetrofitError;

import javax.inject.Inject;
import java.util.List;

public class NodeCommentTaskService extends Service implements Callback<List<NodeComment>> {

    private static final String TAG = "NodeCommentTaskService";

    @Inject
    TaskQueue<NodeCommentTask> queue;

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

        NodeCommentTask task = queue.peek();
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
    public NodeCommentTaskStatus produceStatus(boolean running, String taskTag) {
        return new NodeCommentTaskStatus(running, taskTag);
    }

    @Override
    public void success(final List<NodeComment> nodeComments) {
        Log.i(TAG, "Success!");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ActiveAndroid.beginTransaction();
                for (NodeComment nc : nodeComments) {
                	nc.setComment(MobileHTMLUtil.cleanComments(nc.getComment()));
                    nc.save();
                }
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();
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
            error.printStackTrace();
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
