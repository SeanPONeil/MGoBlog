package com.atami.mgodroid.io;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.events.CommentPostTaskStatus;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CommentPostTaskService extends Service implements Callback {

    private final static String TAG = "CommentPostTaskService";

    @Inject
    TaskQueue<CommentPostTask> queue;

    @Inject
    Bus bus;

    private boolean running;
    private String taskTag;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MGoBlogApplication) getApplication()).objectGraph().inject(this);
        taskTag = null;
        Log.i(TAG, "Service starting!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executeNext();
        return START_STICKY;
    }

    private void executeNext() {
        if (running) return; // Only one task at a time.

        CommentPostTask task = queue.peek();
        if (task != null) {
            running = true;
            taskTag = task.getTag();
            bus.post(produceStatus());
            task.execute(this);
        } else {
            Log.i(TAG, "Service stopping!");
            stopSelf(); // No more tasks are present. Stop.
        }
    }

    @Produce
    public CommentPostTaskStatus produceStatus() {
        return new CommentPostTaskStatus(false, running, taskTag);
    }

    @Override
    public void success(Object o, Response response) {
        Log.i(TAG, "Success!");
        running = false;
        queue.remove();
        bus.post(produceStatus());
        bus.post(new CommentPostTaskStatus(true, running, taskTag));
        executeNext();
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        Log.i(TAG, "Error!");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CommentPostTaskService.this, "Something went wrong! Try posting again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        running = false;
        queue.remove();
        bus.post(produceStatus());
        executeNext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
