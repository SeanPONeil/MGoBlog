package com.atami.mgodroid.modules;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.io.*;
import com.atami.mgodroid.ui.*;
import com.squareup.tape.*;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = {OttoModule.class, SessionModule.class},
        injects = {
                CommentDialogFragment.class,
                LoginFragment.class,
                NodeIndexListFragment.class,
                NodeFragment.class,
                NodeCommentFragment.class,
                CommentPostTaskService.class,
                LoginTaskService.class,
                NodeIndexTaskService.class,
                NodeTaskService.class,
                NodeCommentTaskService.class
        }
)
public class TaskQueueModule {

    private final Context appContext;

    public TaskQueueModule(Context appContext) {
        this.appContext = appContext;
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

    @Provides
    @Singleton
    TaskQueue<NodeCommentTask> provideNodeCommentTaskQueue() {
        ObjectQueue<NodeCommentTask> delegate = new InMemoryObjectQueue<NodeCommentTask>();
        TaskQueue<NodeCommentTask> queue = new TaskQueue<NodeCommentTask>(delegate,
                new IOTaskInjector<NodeCommentTask>(appContext));
        queue.setListener(new ServiceStarter<NodeCommentTask>(appContext, NodeCommentTaskService.class));
        return queue;
    }

    @Provides
    @Singleton
    TaskQueue<LoginTask> provideLoginTaskQueue() {
        ObjectQueue<LoginTask> delegate = new InMemoryObjectQueue<LoginTask>();
        TaskQueue<LoginTask> queue = new TaskQueue<LoginTask>(delegate, new IOTaskInjector<LoginTask>(appContext));
        queue.setListener(new ServiceStarter<LoginTask>(appContext, LoginTaskService.class));
        return queue;
    }

    @Provides
    @Singleton
    TaskQueue<CommentPostTask> provideCommentPostTaskQueue() {
        ObjectQueue<CommentPostTask> delegate = new InMemoryObjectQueue<CommentPostTask>();
        TaskQueue<CommentPostTask> queue = new TaskQueue<CommentPostTask>(delegate, new IOTaskInjector<CommentPostTask>(appContext));
        queue.setListener(new ServiceStarter<CommentPostTask>(appContext, CommentPostTaskService.class));
        return queue;
    }

    public static class IOTaskInjector<T extends Task> implements TaskInjector<T> {

        Context context;

        /**
         * Injects Dagger dependencies into Tasks added to TaskQueues
         *
         * @param context the application Context
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
}
