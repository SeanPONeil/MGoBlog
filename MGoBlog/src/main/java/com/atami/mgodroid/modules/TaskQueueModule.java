package com.atami.mgodroid.modules;

import android.content.Context;
import android.content.Intent;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.io.NodeIndexTask;
import com.atami.mgodroid.io.NodeIndexTaskService;
import com.atami.mgodroid.io.NodeTask;
import com.atami.mgodroid.io.NodeTaskService;
import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskInjector;
import com.squareup.tape.TaskQueue;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        entryPoints = {
                NodeIndexListFragment.class,
                NodeIndexTaskService.class,
                NodeTaskService.class
        }
)
public class TaskQueueModule {

    private final Context appContext;

    public TaskQueueModule(Context appContext) {
        this.appContext = appContext;
    }

    public class NodeIndexServiceStarter implements ObjectQueue.Listener<NodeIndexTask> {
        private final Context context;

        public NodeIndexServiceStarter(Context context) {
            this.context = context;
        }

        @Override
        public void onAdd(ObjectQueue<NodeIndexTask> queue, NodeIndexTask task) {
            context.startService(new Intent(context, NodeIndexTaskService.class));
        }

        @Override
        public void onRemove(ObjectQueue<NodeIndexTask> task) {
        }
    }

    public class NodeServiceStarter implements ObjectQueue.Listener<NodeTask> {
        private final Context context;

        public NodeServiceStarter(Context context) {
            this.context = context;
        }

        @Override
        public void onAdd(ObjectQueue<NodeTask> queue, NodeTask task) {
            context.startService(new Intent(context, NodeTaskService.class));
        }

        @Override
        public void onRemove(ObjectQueue<NodeTask> task) {
        }
    }


    @Provides
    @Singleton
    TaskQueue<NodeIndexTask> provideNodeIndexTaskQueue() {
        ObjectQueue<NodeIndexTask> delegate = new InMemoryObjectQueue<NodeIndexTask>();
        TaskQueue<NodeIndexTask> queue = new TaskQueue<NodeIndexTask>(delegate, new TaskInjector<NodeIndexTask>() {
            @Override
            public void injectMembers(NodeIndexTask task) {
                ((MGoBlogApplication) appContext.getApplicationContext()).objectGraph().inject(task);
            }
        });
        queue.setListener(new NodeIndexServiceStarter(appContext));
        return queue;
    }

    @Provides
    @Singleton
    TaskQueue<NodeTask> provideNodeTaskQueue() {
        ObjectQueue<NodeTask> delegate = new InMemoryObjectQueue<NodeTask>();
        TaskQueue<NodeTask> queue = new TaskQueue<NodeTask>(delegate, new TaskInjector<NodeTask>() {
            @Override
            public void injectMembers(NodeTask task) {
                ((MGoBlogApplication) appContext.getApplicationContext()).objectGraph().inject(task);
            }
        });
        queue.setListener(new NodeServiceStarter(appContext));
        return queue;
    }
}
