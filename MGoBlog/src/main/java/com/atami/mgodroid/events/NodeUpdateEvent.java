package com.atami.mgodroid.events;

import com.atami.mgodroid.models.Node;

public class NodeUpdateEvent {

    public Node node;

    public NodeUpdateEvent(Node node){
        this.node = node;
    }
}
