package com.atami.mgodroid.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

@Table(name = "NodeIndexes")
public class NodeIndex extends Model {

    public NodeIndex() {
        super();
    }

    @Expose @Column(name = "nid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int nid;

    @Expose @Column(name = "vid")
    private int vid;

    @Expose @Column(name = "type")
    private String type;

    @Expose @Column(name = "language")
    private String language;

    @Expose @Column(name = "title")
    private String title;

    @Expose @Column(name = "uid")
    private int uid;

    @Expose @Column(name = "status")
    private int status;

    @Expose @Column(name = "created")
    private long created;

    @Expose @Column(name = "changed")
    private long changed;

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

    @Expose @Column(name = "uri")
    private String uri;

    //ActiveAndroid queries
    public static From selectFromDBWhere(String where) {
        return new Select().from(NodeIndex.class).where(where).orderBy("created DESC");
    }

    public static From deleteFromDBWhere(String where) {
        return new Delete().from(NodeIndex.class).where(where).orderBy("created DESC");
    }

    public int getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public String getCreated() {
        PrettyTime p = new PrettyTime();
        return p.format(new DateTime(created * 1000).toDate());
    }
}
