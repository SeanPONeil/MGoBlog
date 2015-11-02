package com.atami.mgodroid.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.Map;

@Table(name = "nodes")
public class Node extends Model {

    public Node() {
        super();
    }

    @Expose @Column(name = "nid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int nid;
    @Expose @Column(name = "type")
    private String type;
    @Expose @Column(name = "uid")
    private int uid;
    @Expose @Column(name = "status")
    private int status;
    @Expose @Column(name = "created")
    private int created;

    //@Column(name = "language")
    //private String language;
    @Expose @Column(name = "changed")
    private int changed;
    @Expose @Column(name = "comment")
    private int comment;
    @Expose @Column(name = "promote")
    private int promote;
    @Expose @Column(name = "moderate")
    private int moderate;
    @Expose @Column(name = "sticky")
    private int sticky;
    @Expose @Column(name = "tnid")
    private int tnid;
    @Expose @Column(name = "translate")
    private int translate;
    @Expose @Column(name = "vid")
    private int vid;
    @Expose @Column(name = "revision_uid")
    private int revision_uid;
    @Expose @Column(name = "title")
    private String title;
    @Expose @Column(name = "body")
    private String body;
    @Expose @Column(name = "teaser")
    private String teaser;
    @Expose @Column(name = "log")
    private String log;
    @Expose @Column(name = "revision_timestamp")
    private int revision_timestamp;
    @Expose @Column(name = "format")
    private int format;
    @Expose @Column(name = "name")
    private String name;
    @Expose @Column(name = "picture")
    private String picture;
    @Expose @Column(name = "data")
    private String data;
    @Expose @Column(name = "path")
    private String path;
    @Expose @Column(name = "last_comment_timestamp")
    private int last_comment_timestamp;
    @Expose @Column(name = "last_comment_name")
    private String last_comment_name;
    @Expose @Column(name = "comment_count")
    private int comment_count;
    @Expose @Column(name = "taxonomy")
    private Map<Integer, Taxonomy> taxonomy;
    @Expose @Column(name = "files")
    private String[] files;
    @Expose @Column(name = "page_titles")
    private String page_title;
    @Expose @Column(name = "forum_tid")
    private String forum_tid;
    @Expose @Column(name = "uri")
    private String uri;
    private FieldLink[] field_link;
    @Expose @Column(name = "link")
    private String link;

    //@Column(name = "nodewords")
    //private String[] nodewords;

    public static Node get(int nid) {
        return new Select()
                .from(Node.class)
                .where("nid = ?", nid)
                .executeSingle();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public FieldLink[] getField_link() {
        return field_link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public int getCommentCount() {
        return comment_count;
    }

    public int getRevisionTimestamp() {
        return revision_timestamp;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nid=" + nid +
                ", type='" + type + '\'' +
                ", uid=" + uid +
                ", status=" + status +
                ", created=" + created +
                ", changed=" + changed +
                ", comment=" + comment +
                ", promote=" + promote +
                ", moderate=" + moderate +
                ", sticky=" + sticky +
                ", tnid=" + tnid +
                ", translate=" + translate +
                ", vid=" + vid +
                ", revision_uid=" + revision_uid +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", teaser='" + teaser + '\'' +
                ", log='" + log + '\'' +
                ", revision_timestamp=" + revision_timestamp +
                ", format=" + format +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", data='" + data + '\'' +
                ", path='" + path + '\'' +
                ", last_comment_timestamp=" + last_comment_timestamp +
                ", last_comment_name='" + last_comment_name + '\'' +
                ", comment_count=" + comment_count +
                ", taxonomy=" + taxonomy +
                ", files=" + (files == null ? null : Arrays.asList(files)) +
                ", page_title='" + page_title + '\'' +
                ", forum_tid='" + forum_tid + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    public class Taxonomy {

        public int tid;
        public int vid;
        public String name;
        public String description;
        public int weight;
        public int v_weight_unused;
    }

    public class FieldLink {

        public String title;
        public String url;
    }
}
