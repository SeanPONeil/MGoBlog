package com.atami.mgodroid;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.atami.mgodroid.io.LoginTask;
import com.atami.mgodroid.modules.*;
import com.hyprmx.android.sdk.HyprMXHelper;
import com.hyprmx.android.sdk.HyprMXReward;
import com.squareup.tape.TaskQueue;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class MGoBlogApplication extends Application {

    private ObjectGraph objectGraph;

    private static MGoBlogApplication INSTANCE = null;

    @Inject TaskQueue<LoginTask> loginTaskQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        objectGraph = ObjectGraph.create(new MGoBlogAPIModule(this), new OttoModule(),
                new TaskQueueModule(this), new MGoBlogAppModule());

        INSTANCE = this;

        objectGraph.inject(this);

        // Login user if credentials available
        SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        if (username != null) {
            String password = prefs.getString("password", null);
            loginTaskQueue.add(new LoginTask(username, password, null));
        }

        HyprMXHelper.getInstance(this,
                "-45",
                "123123123123",
                "asdasdasdasd",
                true);
        HyprMXReward rewards[] = new HyprMXReward[2];
        rewards[0] = new HyprMXReward(0, 0.01f, 1, "Cent");
        rewards[1] = new HyprMXReward(1, 0.005f, 1000, "Half a Cent");
        HyprMXHelper.getInstance().setRewards(rewards);
    }

    public ObjectGraph objectGraph() {
        return objectGraph;
    }

    public static boolean isOnline() {
        if (INSTANCE == null) {
            return false;
        }

        ConnectivityManager cm =
                (ConnectivityManager) INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
