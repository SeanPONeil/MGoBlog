package com.atami.mgodroid.modules;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.io.NodeIndexTask;
import com.atami.mgodroid.io.NodeIndexTaskService;
import com.atami.mgodroid.io.NodeTask;
import com.atami.mgodroid.io.NodeTaskService;
import com.atami.mgodroid.ui.NodeFragment;
import com.atami.mgodroid.ui.NodeIndexListFragment;
import com.squareup.tape.*;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        entryPoints = {
                NodeIndexListFragment.class,
                NodeFragment.class,
                NodeIndexTaskService.class,
                NodeTaskService.class
        }
)
public class TaskQueueModule {

    private final Context appContext;

    public TaskQueueModule(Context appContext) {
        this.appContext = appContext;
    }

    public static class IOTaskInjector<T extends Task> implements TaskInjector<T> {

        Context context;

        /**
         * Injects Dagger dependencies into Tasks added to TaskQueues
         *
         * @param context
         */
        public IOTaskInjector(Context context) {
            this.context = context;
        }

        @Override
        public void injectMembers(T task) {
            ((MGoBlogApplication) context.getApplicationContext()).objectGraph().inject(task);
        }
    }

    public static class ServiceStarter<T extends Task> implements ObjectQueue.Listener<T> {

        Context context;
        Class<? extends Service> service;

        /**
         * Starts the provided service when a Task is added to the queue
         *
         * @param context the application Context
         * @param service the Service to start
         */
        public ServiceStarter(Context context, Class<? extends Service> service) {
            this.context = context;
            this.service = service;
        }

        @Override
        public void onAdd(ObjectQueue<T> queue, T entry) {
            context.startService(new Intent(context, service));

        }

        @Override
        public void onRemove(ObjectQueue<T> queue) {
        }
    }


    @Provides
    @Singleton
    TaskQueue<NodeIndexTask> provideNodeIndexTaskQueue() {
        ObjectQueue<NodeIndexTask> delegate = new InMemoryObjectQueue<NodeIndexTask>();
        TaskQueue<NodeIndexTask> queue = new TaskQueue<NodeIndexTask>(delegate, new IOTaskInjector<NodeIndexTask>
                (appContext));
        queue.setListener(new ServiceStarter<NodeIndexTask>(appContext, NodeIndexTaskService.class));
        return queue;
    }

    @Provides
    @Singleton
    TaskQueue<NodeTask> provideNodeTaskQueue() {
        ObjectQueue<NodeTask> delegate = new InMemoryObjectQueue<NodeTask>();
        TaskQueue<NodeTask> queue = new TaskQueue<NodeTask>(delegate, new IOTaskInjector<NodeTask>(appContext));
        queue.setListener(new ServiceStarter<NodeTask>(appContext, NodeTaskService.class));
        return queue;
    }
}
