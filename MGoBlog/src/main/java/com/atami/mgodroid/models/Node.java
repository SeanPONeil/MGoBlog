package com.atami.mgodroid.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.Map;

@Table(name = "nodes")
public class Node extends Model {

    public static Node get(int nid) {
        return new Select()
                .from(Node.class)
                .where("nid = ?", nid)
                .executeSingle();
    }

    public class Taxonomy {

        public int tid;

        public int vid;

        public String name;

        public String description;

        public int weight;

        public int v_weight_unused;
    }

    public class FieldLink{

        public String title;

         public String url;
    }

    @Column(name = "nid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
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

    //@Column(name = "nodewords")
    //private String[] nodewords;

    @Column(name = "uri")
    private String uri;

    private FieldLink[] field_link;

    @Column(name = "link")
    private String link;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public FieldLink[] getField_link(){
        return field_link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getName() {
        return name;
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
                ", language='" + language + '\'' +
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
}
