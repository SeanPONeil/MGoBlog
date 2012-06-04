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

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.atami.mgodroid.R;
import com.atami.mgodroid.io.NodeIndexService;
import com.atami.mgodroid.io.NodeIndexServiceHelper;
import com.atami.mgodroid.provider.NodeIndexProvider;

public class NodeIndexListFragment extends SherlockListFragment implements
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
					break;
				case NodeIndexService.STATUS_ERROR:
					Toast.makeText(getActivity(),
							"Error pulling content from MGoBlog",
							Toast.LENGTH_SHORT).show();
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
		getListView().setOnScrollListener(this);

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
			NodeIndexServiceHelper.refreshNodeIndex(indexType, getActivity(),
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
		String where = "node_index_type = ?";
		String whereArgs[] = { indexType };

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.

		return new CursorLoader(getActivity(), baseUri, new String[] { "_id",
				"node_index_type", "node_title", "node_created", "nid",
				"is_sticky" }, where, whereArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
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
			NodeIndexServiceHelper.getNextNodeIndexPage(indexType,
					getActivity(), mReceiver);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}
