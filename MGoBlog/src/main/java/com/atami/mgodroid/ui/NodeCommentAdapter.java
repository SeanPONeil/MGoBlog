package com.atami.mgodroid.ui;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.R;

public class NodeCommentAdapter extends ArrayAdapter<NodeComment> {

	private List<NodeComment> nodeComments;

	/**
	 * Holds on to Views to avoid costly findViewById calls
	 */
	private class ViewHolder {
		public TextView timestamp;
		public TextView subject;
		public WebView comment;
		public LinearLayout container;

		private ViewHolder(TextView timestamp, TextView subject,
				WebView comment, LinearLayout container) {
			this.timestamp = timestamp;
			this.subject = subject;
			this.comment = comment;
			this.container = container;
		}
	}

	public NodeCommentAdapter(Context context, int textViewResourceId,
			List<NodeComment> nodeComments) {
		super(context, textViewResourceId, nodeComments);
		this.nodeComments = nodeComments;
	}

	@Override
	public int getCount() {
		return nodeComments.size();
	}

	@Override
	public NodeComment getItem(int position) {
		return nodeComments.get(position);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.comment_list_item, null);
			TextView subject = (TextView) view.findViewById(R.id.cmt_title);
			TextView timestamp = (TextView) view.findViewById(R.id.cmt_time);
			WebView comment = (WebView) view.findViewById(R.id.cmt_comment);
			LinearLayout container = (LinearLayout) view
					.findViewById(R.id.cmt_container);
			viewHolder = new ViewHolder(timestamp, subject, comment, container);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		NodeComment nodeComment = getItem(position);
		int rank = nodeComment.getRank() * 20;

		viewHolder.container.setPadding(10 + rank, 0, 10, 10);
		viewHolder.subject.setText(nodeComment.getSubject());
		viewHolder.timestamp.setText(nodeComment.getTimestamp());
		viewHolder.comment.loadData(nodeComment.getComment(), "text/html", null);
		viewHolder.comment.getSettings().setJavaScriptEnabled(true);
        viewHolder.comment.getSettings().setDefaultFontSize(16);
        viewHolder.comment.getSettings().setPluginState(WebSettings.PluginState.ON);
        viewHolder.comment.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

		return view;
	}

	public List<NodeComment> getNodeComments() {
		return nodeComments;
	}

	public void setNodeComments(List<NodeComment> nodeComments) {
		this.nodeComments = nodeComments;
	}
}
