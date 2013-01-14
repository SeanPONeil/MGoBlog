package com.atami.mgodroid.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "NodeIndexes")
public class NodeIndex extends Model {

    //ActiveAndroid queries
    public static List<NodeIndex> getAll(String type) {
        return new Select()
                .from(NodeIndex.class)
                .where("type = ?", type)
                .orderBy("created DESC")
                .execute();
    }

    public static List<NodeIndex> getBoard() {
        return new Select()
                .from(NodeIndex.class)
                .where("type = forum")
                .orderBy("created DESC")
                .execute();
    }

    public static List<NodeIndex> getFrontPage() {
        return new Select()
                .from(NodeIndex.class)
                .where("promoted = 1")
                .orderBy("created DESC")
                .execute();
    }

    public static List<NodeIndex> getDiaries() {
        return new Select()
                .from(NodeIndex.class)
                .where("type = blog")
                .orderBy("created DESC")
                .execute();
    }

    public static List<NodeIndex> getLinks() {
        return new Select()
                .from(NodeIndex.class)
                .where("type = link")
                .orderBy("created DESC")
                .execute();
    }

    public static void deleteAll(String type) {
        new Delete()
                .from(NodeIndex.class)
                .where("type = ?", type)
                .execute();
    }

    @Column(name = "nid")
    private int nid;

    @Column(name = "vid")
    private int vid;

    @Column(name = "type")
    private String type;

    @Column(name = "language")
    private String language;

    @Column(name = "title")
    private String title;

    @Column(name = "uid")
    private int uid;

    @Column(name = "status")
    private int status;

    @Column(name = "created")
    private long created;

    @Column(name = "changed")
    private long changed;

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

    @Column(name = "uri")
    private String uri;

    public int getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public long getCreated() {
        return created;
    }
}
