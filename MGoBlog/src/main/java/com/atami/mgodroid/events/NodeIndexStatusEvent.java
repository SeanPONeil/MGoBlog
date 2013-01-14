package com.atami.mgodroid.events;

public class NodeIndexStatusEvent {

    public boolean refreshing;
    public boolean gettingNextPage;

    public NodeIndexStatusEvent(boolean refreshing, boolean gettingNextPage){
        this.refreshing = refreshing;
        this.gettingNextPage = gettingNextPage;
    }
}
