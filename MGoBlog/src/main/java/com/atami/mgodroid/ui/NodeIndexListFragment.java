package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import com.atami.mgodroid.R;
import com.atami.mgodroid.core.NodeIndexCache;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.core.events.NodeIndexRefreshEvent;
import com.atami.mgodroid.core.events.NodeIndexUpdateEvent;
import com.atami.mgodroid.ui.base.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import java.util.List;

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

    //Constants differentiating different loading types
    private static final int REFRESH = 1;
    private static final int NEXT_PAGE = 2;

    public static NodeIndexListFragment newInstance(String type) {
        NodeIndexListFragment f = new NodeIndexListFragment();

        Bundle args = new Bundle();
        args.putString("type", type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPullToRefreshListView().getRefreshableView().setDivider(getResources().getDrawable(R.drawable.list_divider));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        //View footerView = getLayoutInflater(savedInstanceState).inflate(
        //		R.layout.node_index_footer, null, false);
        //footerView.setClickable(false);
        //getListView().addFooterView(footerView);

        mAdapter = new NodeIndexAdapter(getActivity(), android.R.layout.simple_list_item_2, cache.getNodeIndexes().get(type));
        setListAdapter(mAdapter);

        getPullToRefreshListView().setOnLastItemVisibleListener(this);
        getPullToRefreshListView().setOnRefreshListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        bus.post(mAdapter.getItem(position));
    }

    @Override
    public void onLastItemVisible() {
        cache.refreshNodeIndex(type, String.valueOf(mAdapter.getNodeIndexes().size()/20));
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
}
