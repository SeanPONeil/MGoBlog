package com.atami.mgodroid.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atami.mgodroid.R;
import com.atami.mgodroid.core.APIModule.MGoBlogAPI;
import com.atami.mgodroid.core.NodeIndex;
import com.atami.mgodroid.io.StatusEvents.NodeIndexStatus;
import com.atami.mgodroid.io.StatusEvents.Status;
import com.atami.mgodroid.util.SherlockPullToRefreshListFragment;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockListFragment;
import com.google.inject.Inject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import roboguice.inject.InjectResource;

import java.util.ArrayList;
import java.util.List;

public class NodeIndexListFragment extends RoboSherlockListFragment
		implements LoaderCallbacks<List<NodeIndex>> {

    @Inject
    MGoBlogAPI api;

    //Type parameter used in API call
    private String typeParam;

    // Whether or not we are in dual-pane mode
    @InjectResource(R.bool.has_two_panes)
    boolean mIsDualPane;

	// This is the Adapter being used to display the list's data.
	ArrayAdapter<String> mAdapter;

	public static NodeIndexListFragment newInstance(String type) {
		NodeIndexListFragment f = new NodeIndexListFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("type", type);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		typeParam = getArguments().getString("type");

		if (savedInstanceState == null) {
			//NodeIndexService.refreshNodeIndex(nodeIndexType, getActivity());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//BusProvider.getInstance().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		//getPullToRefreshListView().onRefreshComplete();
		//BusProvider.getInstance().register(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);

		//View footerView = getLayoutInflater(savedInstanceState).inflate(
		//		R.layout.node_index_footer, null, false);
		//footerView.setClickable(false);
		//getListView().addFooterView(footerView);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

		setListAdapter(mAdapter);

		//getPullToRefreshListView().setOnLastItemVisibleListener(this);
		//getPullToRefreshListView().setOnRefreshListener(this);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<NodeIndex>> onCreateLoader(int id, Bundle args) {
        return new AsyncLoader<List<NodeIndex>>(getActivity()) {
            @Override
            public List<NodeIndex> loadInBackground() {
                Log.d("test", "starting api call");
                List<NodeIndex> l = api.getNodeIndex(typeParam, "0");
                Log.d("test", "finished");
                Log.d("test", l.toString());
                return l;
            }
        };
	}

	@Override
	public void onLoadFinished(Loader<List<NodeIndex>> loader, List<NodeIndex> data) {
        for(NodeIndex node: data){
            mAdapter.add(node.getTitle());
        }
	}

	@Override
	public void onLoaderReset(Loader<List<NodeIndex>> loader) {
		mAdapter.clear();
	}

//	@Override
//	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//		getPullToRefreshListView().setLastUpdatedLabel(
//				DateUtils.formatDateTime(getActivity(),
//						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//								| DateUtils.FORMAT_SHOW_DATE
//								| DateUtils.FORMAT_ABBREV_ALL));
//
//		NodeIndexService.refreshNodeIndex(nodeIndexType, getActivity());
//	}

//	@Override
//	public void onLastItemVisible() {
		//NodeIndexService.getNextNodeIndexPage(nodeIndexType, getActivity());
//	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (v.getId() == R.id.nodeIndexFooter) {
			return;
		}
		Cursor c = (Cursor) l.getItemAtPosition(position);
		int nid = c.getInt(c.getColumnIndex("nid"));
		// String title = c.getString(c.getColumnIndex("title"));
		//BusProvider.getInstance().post(new NodeIndexItemClick(nid));
	}

}
