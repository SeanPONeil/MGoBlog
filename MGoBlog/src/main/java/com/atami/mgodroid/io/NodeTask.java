package com.atami.mgodroid.io;


import com.atami.mgodroid.models.Node;
import com.squareup.tape.Task;
import retrofit.http.Callback;

import javax.inject.Inject;

import static com.atami.mgodroid.modules.MGoBlogAPIModule.MGoBlogAPI;

public class NodeTask implements Task<Callback<Node>> {

    public final static String TAG = "NodeTask";

    private int nid;
    private String tag;

    @Inject
    private MGoBlogAPI api;

    /**
     * Downloads a node from MGoBlog
     *
     * @param nid the nid of the node
     * @param tag identifier for this task
     */
    public NodeTask(int nid, String tag) {
        this.nid = nid;
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }

    @Override
    public void execute(Callback<Node> callback) {
        api.getNode(nid, callback);
    }
}
