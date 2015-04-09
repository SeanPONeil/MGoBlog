package com.atami.mgodroid.io;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.events.LoginTaskStatus;
import com.atami.mgodroid.models.Session;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginTaskService extends Service implements Callback<Session> {

    private static final String TAG = "LoginTaskService";

    @Inject
    TaskQueue<LoginTask> queue;

    @Inject
    Bus bus;

    @Inject
    Session session;

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

        LoginTask task = queue.peek();
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
    public LoginTaskStatus produceStatus() {
        return new LoginTaskStatus(false, false, running, taskTag);
    }

    @Override
    public void success(final Session session, Response response) {
        Log.i(TAG, "Success!");
        this.session.setSession(session);
        running = false;
        queue.remove();
        bus.post(new LoginTaskStatus(true, false, running, taskTag));
        bus.post(produceStatus());
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
        bus.post(new LoginTaskStatus(false, true, running, taskTag));
        bus.post(produceStatus());
        executeNext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
