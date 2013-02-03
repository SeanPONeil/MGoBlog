package com.atami.mgodroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.models.NodeComment;
import com.atami.mgodroid.ui.base.BaseListFragment;

import java.util.ArrayList;

public class NodeCommentFragment extends BaseListFragment {

    // ID of the current node
    int nid;

    private NodeCommentAdapter mAdapter;

    private Menu nodeCommentMenu;

    public static NodeCommentFragment newInstance(int nid) {
        NodeCommentFragment f = new NodeCommentFragment();

        Bundle args = new Bundle();
        args.putInt("nid", nid);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        nid = getArguments().getInt("nid");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new NodeCommentAdapter(getActivity(), android.R.layout.simple_list_item_2,
                new ArrayList<NodeComment>());
        getListView().setAdapter(mAdapter);
    }

//    @Subscribe
//    public void onNodeCommentUpdate(NodeCommentUpdateEvent event) {
//        mAdapter.setNodeComments(event.nodeComments);
//        mAdapter.notifyDataSetChanged();
//        setListShown(true);
//    }
//
//    @Subscribe
//    public void onNodeCommentsStatusUpdate(NodeCommentStatusEvent event) {
//        if (event.refreshing) {
//            setRefreshActionItemState(true);
//        } else {
//            setRefreshActionItemState(false);
//        }
//    }

    public void setRefreshActionItemState(boolean refreshing) {
        if (nodeCommentMenu == null) {
            return;
        }
        final MenuItem refreshItem = nodeCommentMenu.findItem(R.id.refresh);
        if (refreshItem != null) {
            if (refreshing) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context
                        .LAYOUT_INFLATER_SERVICE);
                View refreshView = inflater.inflate(R.layout.refresh_menu_item, null);
                refreshItem.setActionView(refreshView);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.node_body, menu);
        nodeCommentMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                setRefreshActionItemState(true);
                //bus.post(new NodeCommentRefreshEvent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
