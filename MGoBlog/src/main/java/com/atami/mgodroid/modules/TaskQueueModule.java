package com.atami.mgodroid.modules;

import android.content.Context;
import com.atami.mgodroid.io.NodeIndexTask;
import com.atami.mgodroid.io.NodeIndexTaskQueue;
import com.atami.mgodroid.io.NodeIndexTaskService;
import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.squareup.otto.Bus;
import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        entryPoints = {
                NodeIndexListFragment.class,
                NodeIndexTaskQueue.class,
                NodeIndexTaskService.class
        }
)
public class TaskQueueModule {

    private final Context appContext;

    public TaskQueueModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    NodeIndexTaskQueue provideNodeIndexTaskQueue(Bus bus) {
        ObjectQueue<NodeIndexTask> delegate = new InMemoryObjectQueue<NodeIndexTask>();
        return new NodeIndexTaskQueue(delegate, appContext, bus);
    }
}
