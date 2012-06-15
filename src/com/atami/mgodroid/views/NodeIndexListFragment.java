package com.atami.mgodroid.views;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.atami.mgodroid.util.DetachableResultReceiver;
import com.atami.mgodroid.util.DetachableResultReceiver.Receiver;
import com.atami.mgodroid.util.SherlockPullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class NodeIndexListFragment extends SherlockPullToRefreshListFragment
		implements LoaderCallbacks<Cursor>, OnScrollListener, Receiver,
		OnRefreshListener {

	// This is the Adapter being used to display the list's data.
	SimpleCursorAdapter mAdapter;

	// The type of content we are displaying. Used by the CursorLoader
	// to pull the correct nodes indices out of the database.
	String indexType;

	// Used to receive info from NodeIndexService
	DetachableResultReceiver receiver;

	OnNodeIndexItemClickListener mNodeIndexItemClickListener = null;

	boolean progressBarVisibility;

	// Represents a listener that will be notified of node selections
	public interface OnNodeIndexItemClickListener {
		public void onNodeIndexItemClick(int nid);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mNodeIndexItemClickListener != null) {
			mAdapter.getCursor().moveToPosition(position - 1);
			int nid = mAdapter.getCursor().getInt(
					mAdapter.getCursor().getColumnIndex("nid"));
			mNodeIndexItemClickListener.onNodeIndexItemClick(nid);
		}
	}

	public static NodeIndexListFragment newInstance(String type) {
		NodeIndexListFragment f = new NodeIndexListFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("node_index_type", type);
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
		indexType = getArguments().getString("node_index_type");

		if (savedInstanceState == null) {
			progressBarVisibility = false;
			receiver = new DetachableResultReceiver(new Handler());
			NodeIndexService.refreshNodeIndex(indexType, getActivity(),
					receiver);
		} else {
			progressBarVisibility = savedInstanceState
					.getBoolean("progressBar");
			receiver = savedInstanceState.getParcelable("receiver");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		receiver.clearReceiver();
	}

	@Override
	public void onResume() {
		super.onResume();
		receiver.setReceiver(this);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("progressBar", progressBarVisibility);
		savedInstanceState.putParcelable("receiver", receiver);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setEmptyText("No nodes to display");
		setHasOptionsMenu(true);

		if (progressBarVisibility == true) {
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					progressBarVisibility);
		}

		getPullToRefreshListView().setOnScrollListener(this);
		getPullToRefreshListView().setOnRefreshListener(this);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_2, null, new String[] {
						"node_title", "nid" }, new int[] { android.R.id.text1,
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
			NodeIndexService.refreshNodeIndex(indexType, getActivity(),
					receiver);
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
		String whereArgs[] = { indexType, "0" };

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
			NodeIndexService.getNextNodeIndexPage(indexType, getActivity(),
					receiver);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
		case NodeIndexService.STATUS_RUNNING:
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					true);
			progressBarVisibility = true;
			break;
		case NodeIndexService.STATUS_COMPLETE:
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					false);
			progressBarVisibility = false;
			getPullToRefreshListView().onRefreshComplete();
			break;
		case NodeIndexService.STATUS_ERROR:
			Toast.makeText(getActivity(), "Error pulling content from MGoBlog",
					Toast.LENGTH_SHORT).show();
			getPullToRefreshListView().onRefreshComplete();
			break;
		default:

		}
	}

	@Override
	public void onRefresh() {
		NodeIndexService.refreshNodeIndex(indexType, getActivity(), receiver);
	}

}