package com.atami.mgodroid;


import android.app.Application;
import com.activeandroid.ActiveAndroid;
import com.atami.mgodroid.io.LoginTask;
import com.atami.mgodroid.modules.MGoBlogAPIModule;
import com.atami.mgodroid.modules.MGoBlogAppModule;
import com.atami.mgodroid.modules.OttoModule;
import com.atami.mgodroid.modules.TaskQueueModule;
import com.squareup.tape.TaskQueue;
import dagger.ObjectGraph;

public class MGoBlogApplication extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        objectGraph = ObjectGraph.create(new MGoBlogAPIModule(), new OttoModule(), new TaskQueueModule(this),
                new MGoBlogAppModule());
    }

    public ObjectGraph objectGraph() {
        return objectGraph;
    }

}
