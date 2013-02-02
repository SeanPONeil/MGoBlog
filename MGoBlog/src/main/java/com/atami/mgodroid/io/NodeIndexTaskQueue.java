package com.atami.mgodroid.io;


import android.content.Context;
import android.content.Intent;
import com.atami.mgodroid.modules.MGoBlogAPIModule;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;
import dagger.Lazy;

import javax.inject.Inject;

import static com.atami.mgodroid.modules.MGoBlogAPIModule.MGoBlogAPI;

public class NodeIndexTaskQueue extends TaskQueue<NodeIndexTask> {

    private final Context context;
    private final Bus bus;
    private MGoBlogAPI api;

    public NodeIndexTaskQueue(ObjectQueue<NodeIndexTask> delegate, Context context, Bus bus, MGoBlogAPI api) {
        super(delegate);
        this.context = context;
        this.bus = bus;
        this.api = api;

        bus.register(this);

        if(size() > 0){
            startService();
        }
    }

    private void startService() {
        context.startService(new Intent(context, NodeIndexTaskService.class));
    }

    @Override public void add(NodeIndexTask entry) {
        entry.setAPI(api);
        super.add(entry);
        startService();
    }
}
