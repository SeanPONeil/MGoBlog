package com.atami.mgodroid.events;


public class NodeTaskStatus {

    public boolean running;
    public String tag;

    public NodeTaskStatus(boolean running, String tag) {
        this.running = running;
        this.tag = tag;
    }
}
