package com.atami.mgodroid.views;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.atami.mgodroid.R;
import com.atami.mgodroid.io.APIIntentService;
import com.atami.mgodroid.provider.NodeIndicesProvider;

public class NodeIndexListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	// If non-null, this is the current filter the user has provided.
	String mCurFilter;

	String type;

	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
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
		type = getArguments().getString("node_index_type");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("Loading");
		setHasOptionsMenu(true);

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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.node_index, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			// Launch Intent Service to refresh data
			Intent i = new Intent(getActivity(), APIIntentService.class);
			i.setAction(APIIntentService.NODE_INDEX_DOWNLOAD);
			i.putExtra(APIIntentService.NODE_INDEX_TYPE, type);
			getActivity().startService(i);
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}

	/*
	 * 
	 * Loader Callbacks
	 */

	static final String[] NODE_INDEX_COLUMNS = new String[] { "_id",
			"node_index_type", "node_title", "node_created", "nid", "is_sticky" };

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		Uri baseUri = NodeIndicesProvider.NODE_INDICES_URI;
		String where = "node_index_type = ?";
		String whereArgs[] = { type };

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.

		return new CursorLoader(getActivity(), baseUri, NODE_INDEX_COLUMNS,
				where, whereArgs, "node_created desc");
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

}
