package com.atami.mgodroid.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.atami.mgodroid.R;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.util.MobileHTMLUtil;

import java.util.List;

public class NodeCommentAdapter extends ArrayAdapter<NodeComment> {

    private List<NodeComment> nodeComments;

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
            TextView comment = (TextView) view.findViewById(R.id.cmt_comment);
            LinearLayout container = (LinearLayout) view
                    .findViewById(R.id.cmt_container);
            viewHolder = new ViewHolder(timestamp, subject, comment, container);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        NodeComment nodeComment = getItem(position);
        int rank = nodeComment.getCommentDepth() * 20;

        viewHolder.container.setPadding(10 + rank, 0, 10, 10);
        viewHolder.title.setText(nodeComment.getSubject());
        viewHolder.subtitle.setText("By " + nodeComment.getName() + " - " + nodeComment.getTimestamp());

     // Watch for button clicks.
        ImageButton reply = (ImageButton)view.findViewById(R.id.cmt_reply);
        reply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               //TODO activity.showAddComment(nodeComment.getCid());
            }
        });
        
        Spanned html = Html.fromHtml(nodeComment.getComment(),
                new Html.ImageGetter() {

                    @Override
                    public Drawable getDrawable(String source) {
                        Drawable drawable = null;
//				drawable = Drawable.createFromPath(source);
//				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//						drawable.getIntrinsicHeight());

                        return drawable;
                    }
                }, null);

        viewHolder.comment.setText(MobileHTMLUtil.trimTrailingWhitespace(html));

        return view;
    }

    public List<NodeComment> getNodeComments() {
        return nodeComments;
    }

    public void setNodeComments(List<NodeComment> nodeComments) {
        this.nodeComments = nodeComments;
    }

}
