package com.atami.mgodroid;


import android.app.Application;
import com.activeandroid.ActiveAndroid;
import com.atami.mgodroid.core.APICacheModule;
import com.atami.mgodroid.core.MGoBlogAPIModule;
import com.atami.mgodroid.core.OttoModule;
import dagger.ObjectGraph;

public class MGoBlogApplication extends Application {

    private ObjectGraph objectGraph;

    public static final String[] nodeIndexTypes = {"forum", "story", "blog", "link"};

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        objectGraph = ObjectGraph.create(new MGoBlogAPIModule(), new OttoModule(), new APICacheModule());
    }

    public ObjectGraph objectGraph() {
        return objectGraph;
    }

}
