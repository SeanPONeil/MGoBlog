package com.atami.mgodroid.provider;

import it.mmo.classcontentprovider.annotations.Authority;
import it.mmo.classcontentprovider.annotations.column.Autoincrement;
import it.mmo.classcontentprovider.annotations.column.Column;
import it.mmo.classcontentprovider.annotations.column.PrimaryKey;
import it.mmo.classcontentprovider.annotations.table.Table;

@Table(name = "nodes")
@Authority(name = "mgoblog.provider")
public class Node {

	@Column(name = "_id")
	@PrimaryKey
	@Autoincrement
	public Integer id;
	
	@Column(name = "nid")
	public String nid;
	
	@Column(name = "type")
	public String type;
	
	@Column(name = "language")
	public String language;
	
	@Column(name = "uid")
	public Integer uid;
	
	@Column(name = "status")
	public Integer status;
	
	@Column(name = "created")
	public Integer created;
	
	@Column(name = "changed")
	public Integer changed;
	
	@Column(name = "comment")
	public Integer comment;
	
	@Column(name = "promote")
	public Integer promote;
	
	@Column(name = "moderate")
	public Integer moderate;
	
	@Column(name = "sticky")
	public Integer sticky;
	
	@Column(name = "tnid")
	public Integer tnid;
	
	@Column(name = "translate")
	public Integer translate;
	
	@Column(name = "vid")
	public Integer vid;
	
	@Column(name = "revision_uid")
	public Integer revision_uid;
	
	@Column(name = "title")
	public String title;
	
	@Column(name = "body")
	public String body;
	
	@Column(name = "teaser")
	public String teaser;
	
	@Column(name = "log")
	public String log;
	
	@Column(name = "revision_timestamp")
	public Integer revision_timestamp;
	
	@Column(name = "format")
	public Integer format;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "picture")
	public String picture;
	
	@Column(name = "data")
	public String data;
	
	@Column(name = "tid")
	public Integer tid;
	
	@Column(name = "path")
	public String path;
	
	@Column(name = "last_comment_timestamp")
	public Integer last_comment_timestamp;
	
	@Column(name = "last_comment_name")
	public String last_comment_name;
	
	@Column(name = "comment_count")
	public Integer comment_count;
	
	@Column(name = "taxonomy")
	public String taxonomy;
	
	@Column(name = "files")
	public String files;
	
	@Column(name = "page_title")
	public String page_title;
	
	@Column(name = "forum_tid")
	public Integer forum_tid;
	
	@Column(name = "nodewords")
	public String nodewords;
	
	@Column(name = "uri")
	public String uri;
}
