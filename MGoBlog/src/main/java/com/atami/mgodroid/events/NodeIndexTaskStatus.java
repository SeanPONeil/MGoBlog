package com.atami.mgodroid.events;

public class NodeIndexTaskStatus {

    public boolean running;
    public String tag;

    public NodeIndexTaskStatus(boolean running, String tag) {
        this.running = running;
        this.tag = tag;
    }
}
