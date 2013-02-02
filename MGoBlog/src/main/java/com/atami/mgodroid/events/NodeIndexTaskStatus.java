package com.atami.mgodroid.events;

public class NodeIndexTaskStatus {

    public String title;

    public boolean refreshing;
    public boolean gettingNextPage;

    public NodeIndexTaskStatus(String title, boolean refreshing, boolean gettingNextPage){
        this.title = title;
        this.refreshing = refreshing;
        this.gettingNextPage = gettingNextPage;
    }
}
