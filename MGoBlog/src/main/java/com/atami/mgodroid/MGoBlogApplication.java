package com.atami.mgodroid;


import android.app.Application;
import com.atami.mgodroid.core.APIModule;
import com.atami.mgodroid.core.OttoModule;
import dagger.ObjectGraph;

public class MGoBlogApplication extends Application{

    private ObjectGraph objectGraph;

    @Override
    public void onCreate(){
        super.onCreate();

        objectGraph = ObjectGraph.create(new APIModule(), new OttoModule());
    }

    public ObjectGraph objectGraph(){
        return objectGraph;
    }
}
