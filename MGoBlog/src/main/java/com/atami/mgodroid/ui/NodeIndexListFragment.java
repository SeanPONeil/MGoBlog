package com.atami.mgodroid.ui;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import com.atami.mgodroid.R;
import com.atami.mgodroid.events.NodeIndexNextPageEvent;
import com.atami.mgodroid.events.NodeIndexRefreshEvent;
import com.atami.mgodroid.events.NodeIndexStatusEvent;
import com.atami.mgodroid.events.NodeIndexUpdateEvent;
import com.atami.mgodroid.io.NodeIndexTaskQueue;
import com.atami.mgodroid.models.NodeIndex;
import com.atami.mgodroid.modules.MGoBlogAPIModule;
import com.atami.mgodroid.ui.base.BaseFragment;
import com.atami.mgodroid.ui.base.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import de.neofonie.mobile.app.android.widget.crouton.Crouton;
import de.neofonie.mobile.app.android.widget.crouton.Style;
import retrofit.http.RestException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static retrofit.http.RestException.NetworkException;

public class NodeIndexListFragment extends PullToRefreshListFragment
        implements OnLastItemVisibleListener, OnRefreshListener<ListView> {

    //Type parameter used in API call
    private String type;

    // This is the Adapter being used to display the list's data.
    private NodeIndexAdapter mAdapter;

    @Inject
    NodeIndexTaskQueue queue;

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
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //View footerView = getLayoutInflater(savedInstanceState).inflate(
        //		R.layout.node_index_footer, null, false);
        //footerView.setClickable(false);
        //getListView().addFooterView(footerView);

        mAdapter = new NodeIndexAdapter(getActivity(), R.layout.list_item_two_line, new ArrayList<NodeIndex>());
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
        bus.post(new NodeIndexNextPageEvent(type));
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        getPullToRefreshListView().setLastUpdatedLabel(
                DateUtils.formatDateTime(getActivity(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));
        bus.post(new NodeIndexRefreshEvent(type));
    }
}
