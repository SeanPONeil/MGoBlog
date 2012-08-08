package com.atami.mgodroid.provider;

import it.mmo.classcontentprovider.annotations.Authority;
import it.mmo.classcontentprovider.annotations.column.Autoincrement;
import it.mmo.classcontentprovider.annotations.column.Column;
import it.mmo.classcontentprovider.annotations.column.PrimaryKey;
import it.mmo.classcontentprovider.annotations.table.MimeType;
import it.mmo.classcontentprovider.annotations.table.Table;

@Table(name = "node_indices")
@MimeType(company = "com.atami")
@Authority(name = "mgoblog.provider")
public class NodeIndex {

	@Column(name = "_id")
	@PrimaryKey
	@Autoincrement
	public Integer id;

	@Column(name = "nid")
	public Integer nid;

	@Column(name = "vid")
	public Integer vid;

	@Column(name = "type")
	public String type;

	@Column(name = "language")
	public String language;

	@Column(name = "title")
	public String title;

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

	@Column(name = "uri")
	public String uri;
}
