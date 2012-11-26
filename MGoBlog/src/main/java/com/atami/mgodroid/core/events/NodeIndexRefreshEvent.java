package com.atami.mgodroid.core.events;

public class NodeIndexRefreshEvent {
    public String type;
    public String page;

    public NodeIndexRefreshEvent(String type, String page) {
        this.type = type;
        this.page = page;
    }
}