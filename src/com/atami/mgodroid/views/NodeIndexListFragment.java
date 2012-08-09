package com.atami.mgodroid.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.atami.mgodroid.io.NodeIndexService;
import com.atami.mgodroid.io.StatusEvents.NodeIndexStatus;
import com.atami.mgodroid.io.StatusEvents.Status;
import com.atami.mgodroid.provider.NodeIndexProvider;
import com.atami.mgodroid.util.BusProvider;
import com.atami.mgodroid.util.SherlockPullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.squareup.otto.Subscribe;

public class NodeIndexListFragment extends SherlockPullToRefreshListFragment
		implements LoaderCallbacks<Cursor>, OnRefreshListener<ListView>,
		OnLastItemVisibleListener {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	int nodeIndexType;

	class NodeIndexItemClick {
		int nid;

		public NodeIndexItemClick(int nid) {
			this.nid = nid;
		}
	}

	public static NodeIndexListFragment newInstance(int type) {
		NodeIndexListFragment f = new NodeIndexListFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("NodeIndexType", type);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nodeIndexType = getArguments().getInt("NodeIndexType");

		if (savedInstanceState == null) {
			NodeIndexService.refreshNodeIndex(nodeIndexType, getActivity());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		BusProvider.getInstance().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPullToRefreshListView().onRefreshComplete();
		BusProvider.getInstance().register(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);

		getPullToRefreshListView().setOnLastItemVisibleListener(this);
		getPullToRefreshListView().setOnRefreshListener(this);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_2, null, new String[] {
						"title", "nid" }, new int[] { android.R.id.text1,
						android.R.id.text2 }, 0);
		setListAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), NodeIndexProvider.CONTENT_URI,
				new String[] { "_id", "title", "nid" },
				NodeIndexProvider.WHERE[nodeIndexType],
				NodeIndexProvider.WHERE_ARGS[nodeIndexType], null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		getPullToRefreshListView().setLastUpdatedLabel(
				DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL));

		NodeIndexService.refreshNodeIndex(nodeIndexType, getActivity());
	}

	@Override
	public void onLastItemVisible() {
		NodeIndexService.getNextNodeIndexPage(nodeIndexType, getActivity());
	}

	@Subscribe
	public void onNewStatusEvent(final NodeIndexStatus s) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (nodeIndexType == s.type) {
					switch (s.code) {
					case Status.RUNNING:
						getPullToRefreshListView().setRefreshing();
						break;
					case Status.COMPLETE:
						getPullToRefreshListView().onRefreshComplete();
						break;
					case Status.ERROR:
						Toast.makeText(getActivity(),
								"Error pulling content from MGoBlog",
								Toast.LENGTH_SHORT).show();
						getPullToRefreshListView().onRefreshComplete();
						break;
					default:

					}
				}
			}

		});
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mAdapter.getCursor().moveToPosition(position);
		int nid = mAdapter.getCursor().getInt(
				mAdapter.getCursor().getColumnIndex("nid"));
		BusProvider.getInstance().post(new NodeIndexItemClick(nid));
	}

}
