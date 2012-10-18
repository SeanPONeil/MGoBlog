package com.atami.mgodroid.provider;

import android.net.Uri;
import it.mmo.classcontentprovider.ClassContentProvider;

public class NodeProvider extends ClassContentProvider {
	
	//TODO fix
	public static final String[] WHERE = { "type = ?", "promote = ?", "type = ?", "type = ?" };
	public static final String[][] WHERE_ARGS = { new String[] {"forum"}, new String[] {"1"}, new String[] {"blog"}, new String[] {"link"} };
	
	public static final Uri CONTENT_URI = Uri.parse("content://mgoblog.provider/nodes");
	public NodeProvider() {
		provideClass(Node.class);
	}
}
