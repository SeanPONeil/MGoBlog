package com.atami.mgodroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.core.NodeIndexCache;
import com.atami.mgodroid.core.events.NodeIndexUpdateEvent;
import com.atami.mgodroid.ui.base.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;

import javax.inject.Inject;
import java.util.List;

import static retrofit.http.RestException.NetworkException;

public class NodeIndexListFragment extends PullToRefreshListFragment
        implements OnLastItemVisibleListener, OnRefreshListener<ListView> {

    @Inject
    Bus bus;

    @Inject
    NodeIndexCache cache;

    //Type parameter used in API call
    private String type;

    // This is the Adapter being used to display the list's data.
    private NodeIndexAdapter mAdapter;

//    public static NodeIndexListFragment newInstance(String type) {
//        NodeIndexListFragment f = new NodeIndexListFragment();
//
//        Bundle args = new Bundle();
//        args.putString("type", type);
//        f.setArguments(args);
//
//        return f;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview, container);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //View footerView = getLayoutInflater(savedInstanceState).inflate(
        //		R.layout.node_index_footer, null, false);
        //footerView.setClickable(false);
        //getListView().addFooterView(footerView);

        mAdapter = new NodeIndexAdapter(getActivity(), R.layout.list_item_two_line, cache.getNodeIndexes().get(type));
        setListAdapter(mAdapter);

        getPullToRefreshListView().setOnLastItemVisibleListener(this);
        getPullToRefreshListView().setOnRefreshListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        bus.post(mAdapter.getItem(position - 1));
    }

    @Override
    public void onLastItemVisible() {
        cache.refreshNodeIndex(type, String.valueOf(mAdapter.getNodeIndexes().size() / 20));
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        getPullToRefreshListView().setLastUpdatedLabel(
                DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));
        cache.refreshNodeIndex(type, "0");
    }

    @Subscribe
    public void onNodeIndexUpdate(NodeIndexUpdateEvent event) {
        List<NodeIndex> list = cache.getNodeIndexes().get(type);
        mAdapter.setNodeIndexes(list);
        mAdapter.notifyDataSetChanged();
        getPullToRefreshListView().onRefreshComplete();
    }

    @Subscribe
    public void onNetworkError(NetworkException e) {
        Crouton.makeText(getActivity(), e.getMessage(), Style.INFO).show();
        getPullToRefreshListView().onRefreshComplete();
    }
}
