package com.atami.mgodroid.ui;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.atami.mgodroid.R;
import com.atami.mgodroid.models.NodeComment;

import java.util.List;

public class NodeCommentAdapter extends ArrayAdapter<NodeComment> {

    int nid;
    private List<NodeComment> nodeComments;
    private FragmentManager fragmentManager;

    public NodeCommentAdapter(Context context, int textViewResourceId,
                              List<NodeComment> nodeComments,
                              FragmentManager fragmentManager,
                              int nid) {
        super(context, textViewResourceId, nodeComments);
        this.nodeComments = nodeComments;
        this.fragmentManager = fragmentManager;
        this.nid = nid;
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
            TextView comment = (TextView) view.findViewById(R.id.cmt_comment);
            LinearLayout container = (LinearLayout) view
                    .findViewById(R.id.cmt_container);
            viewHolder = new ViewHolder(timestamp, subject, comment, container);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final NodeComment nodeComment = getItem(position);
        int rank = nodeComment.getCommentDepth() * 20;

        viewHolder.container.setPadding(10 + rank, 0, 10, 10);
        viewHolder.title.setText(nodeComment.getSubject());
        viewHolder.subtitle.setText("By " + nodeComment.getName() + " - " + nodeComment.getTimestamp());

        ImageButton reply = (ImageButton) view.findViewById(R.id.cmt_reply);
        reply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CommentDialogFragment cd = CommentDialogFragment.newInstance(nodeComment.getCid(), nid);
                cd.show(fragmentManager, "dialog");
            }
        });

        viewHolder.comment.setText(Html.fromHtml(nodeComment.getComment()).toString().trim());
        Linkify.addLinks(viewHolder.comment, Linkify.ALL);

        return view;
    }

    public void setNodeComments(List<NodeComment> nodeComments) {
        this.nodeComments = nodeComments;
    }

    /**
     * Holds on to Views to avoid costly findViewById calls
     */
    private class ViewHolder {
        public TextView subtitle;
        public TextView title;
        public TextView comment;
        public LinearLayout container;

        private ViewHolder(TextView subtitle, TextView title,
                           TextView comment, LinearLayout container) {
            this.subtitle = subtitle;
            this.title = title;
            this.comment = comment;
            this.container = container;
        }
    }

}
