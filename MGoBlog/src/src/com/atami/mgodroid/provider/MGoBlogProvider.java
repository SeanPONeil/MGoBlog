package com.atami.mgodroid.provider;

import it.mmo.classcontentprovider.ClassContentProvider;
import android.net.Uri;

public class MGoBlogProvider extends ClassContentProvider {
	
	public static final String[] WHERE = { "type = ?", "promote = ?", "type = ?", "type = ?" };
	public static final String[][] WHERE_ARGS = { new String[] {"forum"}, new String[] {"1"}, new String[] {"blog"}, new String[] {"link"} };
	
	public static final Uri NODE_INDICES_CONTENT_URI = Uri.parse("content://mgoblog.provider/node_indices");
	public static final Uri NODES_CONTENT_URI = Uri.parse("content://mgoblog.provider/nodes");
	
	public MGoBlogProvider() {
		provideClass(Node.class);
		provideClass(NodeIndex.class);
	}
	
}
