package com.atami.mgodroid.core;


import android.util.SparseArray;
import com.atami.mgodroid.core.events.NodeUpdateEvent;
import com.squareup.otto.Bus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.atami.mgodroid.core.MGoBlogAPIModule.MGoBlogAPI;

public class NodeCache {

    Bus bus;

    MGoBlogAPI api;

    //Map of Nodes that are hashed by their
    //nid. This ISN'T synchronized to the
    //best of my knowledge.
    //TODO: make synchronized
    private SparseArray<Node> nodes;

    public SparseArray<Node> getNodes() {
        return nodes;
    }

    public NodeCache(Bus bus, MGoBlogAPI api){
        this.bus = bus;
        this.api = api;
        bus.register(this);
        nodes = new SparseArray<Node>();
        //updateNodesFromDisk();
    }

    public synchronized void refreshNode(final int nid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Node node = api.getNode(nid);
                nodes.put(nid, node);
                bus.post(new NodeUpdateEvent(node));
                //node.save();
            }
        }).start();
    }




}
