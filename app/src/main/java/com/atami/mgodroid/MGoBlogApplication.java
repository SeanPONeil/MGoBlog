package com.atami.mgodroid;


import android.app.Application;
import com.activeandroid.ActiveAndroid;
import com.atami.mgodroid.io.LoginTask;
import com.atami.mgodroid.modules.*;
import com.squareup.tape.TaskQueue;
import dagger.ObjectGraph;

public class MGoBlogApplication extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        objectGraph = ObjectGraph.create(new MGoBlogAPIModule(), new SessionModule(),
                new OttoModule(), new TaskQueueModule(this), new MGoBlogAppModule());
    }

    public ObjectGraph objectGraph() {
        return objectGraph;
    }

}
