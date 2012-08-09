package com.atami.mgodroid.io;

public class StatusEvents {

	public static class Status {
		public static final int RUNNING = 0;
		public static final int COMPLETE = 1;
		public static final int ERROR = 2;

		public int code;
	}

	public static class NodeIndexStatus extends Status {
		public int type;
		
		public NodeIndexStatus(int type, int code){
			this.type = type;
			this.code = code;
		}
	}

	public static class NodeStatus extends Status {
		public int nid;
		
		public NodeStatus(int nid, int code){
			this.nid = nid;
			this.code = code;
		}
	}
}
