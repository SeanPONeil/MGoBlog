package com.atami.mgodroid.views;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.io.NodeIndexService;
import com.atami.mgodroid.provider.NodeIndexProvider;
import com.atami.mgodroid.util.SherlockPullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class NodeIndexListFragment extends SherlockPullToRefreshListFragment implements
		LoaderCallbacks<Cursor>, OnScrollListener {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	// The type of content we are displaying. Used by the CursorLoader
	// to pull the correct nodes indices out of the database.
	String indexType;

	// Used to receive info from NodeIndexService
	private ResultReceiver mReceiver;

	public static NodeIndexListFragment newInstance(String type) {
		NodeIndexListFragment f = new NodeIndexListFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("node_index_type", type);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		indexType = getArguments().getString("node_index_type");

		mReceiver = new ResultReceiver(new Handler()) {

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				switch (resultCode) {
				case NodeIndexService.STATUS_RUNNING:
					getActivity().setProgressBarIndeterminateVisibility(true);
					break;
				case NodeIndexService.STATUS_COMPLETE:
					getActivity().setProgressBarIndeterminateVisibility(false);
					getPullToRefreshListView().onRefreshComplete();
					break;
				case NodeIndexService.STATUS_ERROR:
					Toast.makeText(getActivity(),
							"Error pulling content from MGoBlog",
							Toast.LENGTH_SHORT).show();
					getPullToRefreshListView().onRefreshComplete();
					break;
				default:

				}
			}

		};
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setEmptyText("No nodes to display");
		setHasOptionsMenu(true);
		getPullToRefreshListView().setOnScrollListener(this);

		getPullToRefreshListView().setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				NodeIndexService.refreshNodeIndex(indexType, getActivity(),
						mReceiver);
			}

		});

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_2, null, new String[] {
						"node_title", "nid" }, new int[] { android.R.id.text1,
						android.R.id.text2 }, 0);
		setListAdapter(mAdapter);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("NodeIndexListFragment", "Item clicked: " + id);
		Toast.makeText(getActivity(), "Item clicked: " + id, Toast.LENGTH_SHORT)
				.show();
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
			NodeIndexService.refreshNodeIndex(indexType, getActivity(),
					mReceiver);
			getListView().setSelection(0);
			break;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		Uri baseUri = NodeIndexProvider.NODE_INDEX_URI;
		String where = "node_index_type = ? and is_sticky = ?";
		String whereArgs[] = { indexType , "0"};

		return new CursorLoader(getActivity(), baseUri, new String[] { "_id",
				"node_index_type", "node_title", "node_created", "nid",
				"is_sticky" }, where, whereArgs, null);
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

		boolean reachedEndOfList = /* maybe add a padding */
		firstVisible + visibleCount >= totalCount;

		if (reachedEndOfList && list.getChildCount() != 0) {
			// Launch Intent Service to get next page
			Log.d("test", "reached end of list");
			NodeIndexService.getNextNodeIndexPage(indexType,
					getActivity(), mReceiver);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}