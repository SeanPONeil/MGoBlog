package com.atami.mgodroid.io;


import android.content.Context;
import android.content.Intent;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.TaskQueue;

public class NodeIndexTaskQueue extends TaskQueue<NodeIndexTask> {

    private final Context context;
    private final Bus bus;

    public NodeIndexTaskQueue(ObjectQueue<NodeIndexTask> delegate, Context context, Bus bus) {
        super(delegate);
        this.context = context;
        this.bus = bus;

        bus.register(this);

        if(size() > 0){
            startService();
        }
    }

    private void startService() {
        context.startService(new Intent(context, NodeIndexTaskService.class));
    }

    @Override public void add(NodeIndexTask entry) {
        super.add(entry);
        //bus.post(produceSizeChanged());
        startService();
    }

    @Override public void remove() {
        super.remove();
        //bus.post(produceSizeChanged());
    }

//    @SuppressWarnings("UnusedDeclaration") // Used by event bus.
//    @Produce
//    public ImageUploadQueueSizeEvent produceSizeChanged() {
//        return new ImageUploadQueueSizeEvent(size());
//    }
}
