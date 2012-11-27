package com.atami.mgodroid.core;

import java.util.Map;

public class Node {

    public class Taxonomy {
        public int tid;
        public int vid;
        public String name;
        public String description;
        public int weight;
        public int v_weight_unused;
    }

    private int nid;
    private String type;
    private String language;
    private int uid;
    private int status;
    private int created;
    private int changed;
    private int comment;
    private int promote;
    private int moderate;
    private int sticky;
    private int tnid;
    private int translate;
    private int vid;
    private int revision_uid;
    private String title;
    private String body;
    private String teaser;
    private String log;
    private int revision_timestamp;
    private int format;
    private String name;
    private String picture;
    private String data;
    private String path;
    private int last_comment_timestamp;
    private String last_comment_name;
    private int comment_count;
    private Map<Integer, Taxonomy> taxonomy;
    private String[] files;
    private String page_title;
    private String forum_tid;
    private String[] nodewords;
    private String uri;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
