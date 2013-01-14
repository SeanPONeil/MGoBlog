package com.atami.mgodroid.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.Map;

@Table(name = "nodes")
public class Node extends Model {

    public static Node get(int nid) {
        return new Select()
                .from(Node.class)
                .where("nid = ?", nid)
                .executeSingle();
    }

    //Cleans up HTML of body and makes it
    //more mobile friendly
    public void clean() {
        //Add MGoBlog css before storing
        Document doc = Jsoup.parseBodyFragment(getBody());
        Element headNode = doc.head();
        headNode.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"node_body.css\"></style>");

        //Iterate through iframes, replace Youtube embeds with
        //a thumbnail that links to Youtube
        for (Element iframe : doc.select("iframe")) {
            if(iframe.attr("src").contains("youtube")){
                Element div = new Element(Tag.valueOf("div"), "").attr("class", "video");
                Element thumbnail = new Element(Tag.valueOf("a"), "").attr("href", iframe.attr("src"));
                String videoID = iframe.attr("src").replaceFirst(".*/([^/?]+).*", "$1");
                String thumbnailURL = String.format("http://img.youtube.com/vi/%s/0.jpg", videoID);
                Element img = new Element(Tag.valueOf("img"), "")
                        .attr("src", "play_button.png")
                        .attr("style", "background:URL("+thumbnailURL+")");
                thumbnail.appendChild(img);
                div.appendChild(thumbnail);
                iframe.replaceWith(div);
            }
        }

        for(Element embed: doc.select("embed")){
            if(embed.attr("src").contains("youtube")){
                Element div = new Element(Tag.valueOf("div"), "").attr("class", "video");
                Element thumbnail = new Element(Tag.valueOf("a"), "").attr("href", embed.attr("src"));
                String src = embed.attr("src").substring(0, embed.attr("src").lastIndexOf("?"));
                String videoID = src.replaceFirst(".*/([^/?]+).*", "$1");
                String thumbnailURL = String.format("http://img.youtube.com/vi/%s/0.jpg", videoID);
                Element img = new Element(Tag.valueOf("img"), "")
                        .attr("src", "play_button.png")
                        .attr("style", "background:URL("+thumbnailURL+")");
                thumbnail.appendChild(img);
                div.appendChild(thumbnail);
                embed.replaceWith(div);
            }

        }
        setBody(doc.toString());

        //TODO: Find plain text links and wrap them in an anchor tag

    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
