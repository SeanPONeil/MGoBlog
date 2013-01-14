package com.atami.mgodroid.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.atami.mgodroid.models.NodeComment;

import java.util.List;

public class NodeCommentAdapter extends ArrayAdapter<NodeComment> {

    private List<NodeComment> nodeComments;

    /**
     * Holds on to Views to avoid costly findViewById calls
     */
    private class ViewHolder {
        public TextView timestamp;
        public TextView subject;

        private ViewHolder(TextView timestamp, TextView subject) {
            this.timestamp = timestamp;
            this.subject = subject;
        }
    }

    public NodeCommentAdapter(Context context, int textViewResourceId, List<NodeComment> nodeComments) {
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
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_2, null);
            TextView subject = (TextView) view.findViewById(android.R.id.text2);
            TextView timestamp = (TextView) view.findViewById(android.R.id.text1);
            viewHolder = new ViewHolder(subject, timestamp);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //view.setBackground(resources.getDrawable(R.drawable.list_item_gradient));

//        if(position % 2 == 0){
//            view.setBackground(R.drawable.list_item_gradient);
//        }else{
//            view.setBackgroundColor(resources.getColor(R.color.list_item_background_alternate));
//        }

        NodeComment nodeComment = getItem(position);

        viewHolder.subject.setText(nodeComment.getSubject());
        viewHolder.timestamp.setText(String.valueOf(nodeComment.getTimestamp()));

        return view;
    }

    public List<NodeComment> getNodeComments() {
        return nodeComments;
    }

    public void setNodeComments(List<NodeComment> nodeComments) {
        this.nodeComments = nodeComments;
    }
}
