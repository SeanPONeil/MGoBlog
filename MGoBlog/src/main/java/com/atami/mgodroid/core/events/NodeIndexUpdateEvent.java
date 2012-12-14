package com.atami.mgodroid.core.events;

import com.atami.mgodroid.core.NodeIndex;

import java.util.List;

public class NodeIndexUpdateEvent {

    public String type;
    public List<NodeIndex> nodeIndexes;

    public NodeIndexUpdateEvent(String type, List<NodeIndex> nodeIndexes) {
        this.type = type;
        this.nodeIndexes = nodeIndexes;
    }
}