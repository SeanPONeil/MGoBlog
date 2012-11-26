package com.atami.mgodroid.core;

import com.atami.mgodroid.MGoBlogApplication;
import com.atami.mgodroid.core.MGoBlogAPIModule.MGoBlogAPI;
import com.atami.mgodroid.core.events.NodeIndexRefreshEvent;
import com.atami.mgodroid.core.events.NodeIndexUpdateEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeIndexCache {

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

    //Tracker for the type of the last updated Node Index
    private String lastUpdatedNodeIndex;

    public NodeIndexCache(Bus bus, MGoBlogAPI api) {
        this.bus = bus;
        this.api = api;
        bus.register(this);
        nodeIndexes = Collections.synchronizedMap(new HashMap<String, List<NodeIndex>>());
        updateNodeIndexFromDisk(MGoBlogApplication.nodeIndexTypes);
    }

    private void updateNodeIndexFromDisk(final String[] nodeIndexTypes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String type : nodeIndexTypes) {
                    List<NodeIndex> list = NodeIndex.getAll(type);
                    nodeIndexes.put(type, list);
                    lastUpdatedNodeIndex = type;
                    bus.post(produceNodeIndexUpdateEvent());
                }
            }
        }).start();
    }

    private void updateNodeIndexFromDisk(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NodeIndex> list = NodeIndex.getAll(type);
                nodeIndexes.put(type, list);
                bus.post(produceNodeIndexUpdateEvent());
            }
        }).start();
    }

    @Produce
    public NodeIndexUpdateEvent produceNodeIndexUpdateEvent() {
        return new NodeIndexUpdateEvent(lastUpdatedNodeIndex);
    }

    public synchronized void refreshNodeIndex(final String type, final String page){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NodeIndex> list = api.getNodeIndex(type, page, "0");
                if (page == "0") {
                    nodeIndexes.remove(type);
                    nodeIndexes.put(type, list);
                    bus.post(produceNodeIndexUpdateEvent());
                    NodeIndex.deleteAll(type);
                    for(NodeIndex ni: list){
                        ni.save();
                    }
                }else{
                    List<NodeIndex> currentList = nodeIndexes.get(type);
                    currentList.addAll(list);
                    nodeIndexes.put(type, currentList);
                    bus.post(produceNodeIndexUpdateEvent());
                    for(NodeIndex ni: currentList){
                        ni.save();
                    }
                }
            }
        }).start();
    }

    @Subscribe
    public void onNodeIndexRefreshEvent(final NodeIndexRefreshEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NodeIndex> list = api.getNodeIndex(event.type, event.page, "0");
                if (event.page == "0") {
                    nodeIndexes.remove(event.type);
                    nodeIndexes.put(event.type, list);
                    bus.post(produceNodeIndexUpdateEvent());
                    NodeIndex.deleteAll(event.type);
                    for(NodeIndex ni: list){
                        ni.save();
                    }
                }else{
                    List<NodeIndex> currentList = nodeIndexes.get(event.type);
                    currentList.addAll(list);
                    nodeIndexes.put(event.type, currentList);
                    bus.post(produceNodeIndexUpdateEvent());
                    for(NodeIndex ni: currentList){
                        ni.save();
                    }
                }
            }
        }).start();
    }
}
