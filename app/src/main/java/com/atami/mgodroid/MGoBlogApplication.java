package com.atami.mgodroid;


import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.activeandroid.ActiveAndroid;
import com.atami.mgodroid.io.LoginTask;
import com.atami.mgodroid.modules.*;
import com.squareup.tape.TaskQueue;
import dagger.ObjectGraph;

public class MGoBlogApplication extends Application {

    private ObjectGraph objectGraph;

    private static MGoBlogApplication INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        objectGraph = ObjectGraph.create(new MGoBlogAPIModule(this), new SessionModule(),
                new OttoModule(), new TaskQueueModule(this), new MGoBlogAppModule());

        INSTANCE = this;
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
