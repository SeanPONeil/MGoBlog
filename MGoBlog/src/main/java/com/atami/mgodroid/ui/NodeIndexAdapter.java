package com.atami.mgodroid.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.atami.mgodroid.core.NodeIndex;
import com.github.kevinsawicki.wishlist.ViewFinder;
import com.atami.mgodroid.R;

import java.util.ArrayList;
import java.util.List;

public class NodeIndexAdapter extends ArrayAdapter<NodeIndex> {

    private List<NodeIndex> nodeIndexes;

    private Resources resources;

    /**
     * Holds on to Views to avoid costly findViewById calls
     */
    private class ViewHolder{
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
        resources = getContext().getResources();
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
    public View getView(int position, View view, ViewGroup parent){
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_2, null);
            TextView title = (TextView) view.findViewById(android.R.id.text2);
            TextView created = (TextView) view.findViewById(android.R.id.text1);
            viewHolder = new ViewHolder(title, created);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        //view.setBackground(resources.getDrawable(R.drawable.list_item_gradient));

//        if(position % 2 == 0){
//            view.setBackground(R.drawable.list_item_gradient);
//        }else{
//            view.setBackgroundColor(resources.getColor(R.color.list_item_background_alternate));
//        }

        NodeIndex nodeIndex = getItem(position);

        viewHolder.title.setText(nodeIndex.getTitle());
        viewHolder.created.setText(String.valueOf(nodeIndex.getCreated()));

        return view;
    }

    public List<NodeIndex> getNodeIndexes() {
        return nodeIndexes;
    }

    public void setNodeIndexes(List<NodeIndex> nodeIndexes) {
        this.nodeIndexes = nodeIndexes;
    }

}