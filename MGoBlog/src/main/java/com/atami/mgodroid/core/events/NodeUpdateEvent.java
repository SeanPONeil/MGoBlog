package com.atami.mgodroid.core.events;

import com.atami.mgodroid.core.Node;

public class NodeUpdateEvent {

    public Node node;

    public NodeUpdateEvent(Node node){
        this.node = node;
    }
}
