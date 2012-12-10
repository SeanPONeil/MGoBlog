package com.atami.mgodroid.core;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Arrays;
import java.util.Map;

@Table(name = "nodes")
public class Node extends Model{

    public class Taxonomy {

        public int tid;

        public int vid;

        public String name;

        public String description;

        public int weight;

        public int v_weight_unused;
    }

    @Column(name = "nid")
    private int nid;

    @Column(name = "type")
    private String type;

    @Column(name = "langugage")
    private String language;

    @Column(name = "uid")
    private int uid;

    @Column(name = "status")
    private int status;

    @Column(name = "created")
    private int created;

    @Column(name = "changed")
    private int changed;

    @Column(name = "comment")
    private int comment;

    @Column(name = "promote")
    private int promote;

    @Column(name = "moderate")
    private int moderate;

    @Column(name = "sticky")
    private int sticky;

    @Column(name = "tnid")
    private int tnid;

    @Column(name = "translate")
    private int translate;

    @Column(name = "vid")
    private int vid;

    @Column(name = "revision_uid")
    private int revision_uid;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "teaser")
    private String teaser;

    @Column(name = "log")
    private String log;

    @Column(name = "revision_timestamp")
    private int revision_timestamp;

    @Column(name = "format")
    private int format;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @Column(name = "data")
    private String data;

    @Column(name = "path")
    private String path;

    @Column(name = "last_comment_timestamp")
    private int last_comment_timestamp;

    @Column(name = "last_comment_name")
    private String last_comment_name;

    @Column(name = "comment_count")
    private int comment_count;

    @Column(name = "taxonomy")
    private Map<Integer, Taxonomy> taxonomy;

    @Column(name = "files")
    private String[] files;

    @Column(name = "page_titles")
    private String page_title;

    @Column(name = "forum_tid")
    private String forum_tid;

    @Column(name = "nodewords")
    private String[] nodewords;

    @Column(name = "uri")
    private String uri;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public int getCommentCount() {
        return comment_count;
    }
}
