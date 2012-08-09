package com.atami.mgodroid.views;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.io.NodeIndexService;
import com.atami.mgodroid.io.StatusEvents.NodeIndexStatus;
import com.atami.mgodroid.io.StatusEvents.Status;
import com.atami.mgodroid.provider.NodeIndexProvider;
import com.atami.mgodroid.util.BusProvider;
import com.squareup.otto.Subscribe;

public class NodeIndexListFragment extends SherlockListFragment implements
		LoaderCallbacks<Cursor>, OnScrollListener {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	int nodeIndexType;

	ProgressBar mProgressBar;

	OnNodeIndexItemClickListener mNodeIndexItemClickListener = null;

	// Represents a listener that will be notified of node selections
	public interface OnNodeIndexItemClickListener {
		public void onNodeIndexItemClick(int nid);
	}

	boolean getNextPageRunning = true;

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mNodeIndexItemClickListener != null) {
			mAdapter.getCursor().moveToPosition(position);
			int nid = mAdapter.getCursor().getInt(
					mAdapter.getCursor().getColumnIndex("nid"));
			mNodeIndexItemClickListener.onNodeIndexItemClick(nid);
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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mNodeIndexItemClickListener = (OnNodeIndexItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnNodeIndexItemClickListener");
		}
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View mListView = inflater.inflate(R.layout.node_index_list, container,
				false);
		mProgressBar = (ProgressBar) mListView.findViewById(R.id.progress_bar);
		return mListView;
	}

	@Override
	public void onPause() {
		super.onPause();
		BusProvider.getInstance().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		BusProvider.getInstance().register(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);

		getListView().setOnScrollListener(this);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_2, null, new String[] {
						"title", "nid" }, new int[] { android.R.id.text1,
						android.R.id.text2 }, 0);
		setListAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.node_index, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			NodeIndexService.refreshNodeIndex(nodeIndexType, getActivity());
			getListView().setSelection(0);
			break;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
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
	public void onScroll(AbsListView list, int firstVisible, int visibleCount,
			int totalCount) {

		boolean atEndOfList = firstVisible + visibleCount >= totalCount;
		boolean notAtEndOfList = firstVisible + visibleCount < totalCount;

		if (getNextPageRunning && notAtEndOfList) {
			getNextPageRunning = false;
		}

		if (!getNextPageRunning && atEndOfList && list.getChildCount() != 0) {
			// Launch Intent Service to get next page
			Log.d("test", "reached end of list");
			NodeIndexService.getNextNodeIndexPage(nodeIndexType, getActivity());
			getNextPageRunning = true;
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Subscribe
	public void onNewStatusEvent(final NodeIndexStatus s) {
		getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run() {
				if (nodeIndexType == s.type) {
					switch (s.code) {
					case Status.RUNNING:
						mProgressBar.setVisibility(View.VISIBLE);
						break;
					case Status.COMPLETE:
						mProgressBar.setVisibility(View.GONE);
						break;
					case Status.ERROR:
						Toast.makeText(getActivity(),
								"Error pulling content from MGoBlog",
								Toast.LENGTH_SHORT).show();
						mProgressBar.setVisibility(View.GONE);
						break;
					default:

					}
				}
			}
			
		});
	}
}
