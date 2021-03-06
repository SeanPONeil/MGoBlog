package com.atami.mgodroid.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.atami.mgodroid.models.NodeIndex;
import com.atami.mgodroid.R;
import java.util.List;

public class NodeIndexAdapter extends ArrayAdapter<NodeIndex> {

    private List<NodeIndex> nodeIndexes;

    /**
     * Holds on to Views to avoid costly findViewById calls
     */
    private class ViewHolder {
        public TextView created;
        public TextView title;

        private ViewHolder(TextView created, TextView title) {
            this.created = created;
            this.title = title;
        }
    }

    public NodeIndexAdapter(Context context, int textViewResourceId, List<NodeIndex> nodeIndexes) {
        super(context, textViewResourceId, nodeIndexes);
        this.nodeIndexes = nodeIndexes;
    }

    @Override
    public int getCount() {
        return nodeIndexes.size();
    }

    @Override
    public NodeIndex getItem(int position) {
        return nodeIndexes.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_two_line, null);
            TextView title = (TextView) view.findViewById(android.R.id.text2);
            TextView created = (TextView) view.findViewById(android.R.id.text1);

           
            viewHolder = new ViewHolder(title, created);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        NodeIndex nodeIndex = getItem(position);

        viewHolder.title.setText(nodeIndex.getTitle());
        viewHolder.created.setText(nodeIndex.getCreated());

        return view;
    }

    public List<NodeIndex> getNodeIndexes() {
        return nodeIndexes;
    }

    public void setNodeIndexes(List<NodeIndex> nodeIndexes) {
        this.nodeIndexes = nodeIndexes;
    }

}