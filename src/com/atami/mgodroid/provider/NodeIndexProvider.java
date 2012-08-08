package com.atami.mgodroid.provider;

import it.mmo.classcontentprovider.ClassContentProvider;
import android.net.Uri;

public class NodeIndexProvider extends ClassContentProvider {
	
	public static final String[] WHERE = { "type = ?", "promote = ?", "type = ?", "type = ?" };
	public static final String[][] WHERE_ARGS = { new String[] {"forum"}, new String[] {"1"}, new String[] {"blog"}, new String[] {"link"} };
	
	public static final Uri CONTENT_URI = Uri.parse("content://mgoblog.provider/node_indices");
	public NodeIndexProvider() {
		provideClass(NodeIndex.class);
	}
	
}
