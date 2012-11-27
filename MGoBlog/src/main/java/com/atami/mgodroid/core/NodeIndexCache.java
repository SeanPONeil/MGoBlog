package com.atami.mgodroid.core;

import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.MGoBlogConstants;
import com.atami.mgodroid.core.MGoBlogAPIModule.MGoBlogAPI;
import com.atami.mgodroid.core.events.NodeIndexUpdateEvent;
import com.squareup.otto.Bus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeIndexCache implements MGoBlogConstants{

    Bus bus;

    MGoBlogAPI api;

    //Map of NodeIndex Lists that are hashed by their
    //type parameter. The Map is synchronized with
    //Collections.synchronizedMap to prevent any
    //ConcurrentModificationExceptions that might occur
    //from using multiple Threads.
    private Map<String, List<NodeIndex>> nodeIndexes;

    public Map<String, List<NodeIndex>> getNodeIndexes() {
        return nodeIndexes;
    }

    public NodeIndexCache(Bus bus, MGoBlogAPI api) {
        this.bus = bus;
        this.api = api;
        bus.register(this);
        nodeIndexes = Collections.synchronizedMap(new HashMap<String, List<NodeIndex>>());
        updateNodeIndexesFromDisk(nodeIndexTypes);
    }

    private void updateNodeIndexesFromDisk(final String[] nodeIndexTypes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String type : nodeIndexTypes) {
                    List<NodeIndex> list = NodeIndex.getAll(type);
                    nodeIndexes.put(type, list);
                    bus.post(new NodeIndexUpdateEvent(type));
                }
            }
        }).start();
    }

    public synchronized void refreshNodeIndex(final String type, final String page){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NodeIndex> list = api.getNodeIndex(type, page, "0");
                if (page.equals("0")) {
                    nodeIndexes.remove(type);
                    nodeIndexes.put(type, list);
                    bus.post(new NodeIndexUpdateEvent(type));
                    NodeIndex.deleteAll(type);
                    for(NodeIndex ni: list){
                        ni.save();
                    }
                }else{
                    List<NodeIndex> currentList = nodeIndexes.get(type);
                    currentList.addAll(list);
                    nodeIndexes.put(type, currentList);
                    bus.post(new NodeIndexUpdateEvent(type));
                    for(NodeIndex ni: currentList){
                        ni.save();
                    }
                }
            }
        }).start();
    }
}
