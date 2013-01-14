package com.atami.mgodroid.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "node_comments")
public class NodeComment extends Model {

    public static List<NodeComment> getAll(int nid) {
        return new Select()
                .from(NodeComment.class)
                .where("nid = ?", String.valueOf(nid))
                .orderBy("thread DESC")
                .execute();
    }

    public static void deleteAll(int nid) {
        new Delete()
                .from(NodeComment.class)
                .where("nid = ?", nid)
                .execute();
    }

    @Column(name = "cid")
    private int cid;

    @Column(name = "pid")
    private int pid;

    @Column(name = "nid")
    private int nid;

    @Column(name = "uid")
    private int uid;

    @Column(name = "subject")
    private String subject;

    @Column(name = "comment")
    private String comment;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "status")
    private int status;

    @Column(name = "format")
    private int format;

    @Column(name = "thread")
    private String thread;

    @Column(name = "name")
    private String name;

    @Column(name = "mail")
    private String mail;

    @Column(name = "homepage")
    private String homepage;

    public String getSubject() {
        return subject;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
