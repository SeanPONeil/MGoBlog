package com.atami.mgodroid.views;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atami.mgodroid.io.NodeIndexService;
import com.atami.mgodroid.io.NodeService;
import com.atami.mgodroid.provider.NodeProvider;
import com.atami.mgodroid.util.DetachableResultReceiver;
import com.atami.mgodroid.util.DetachableResultReceiver.Receiver;
import com.atami.mgodroid.util.SherlockWebViewFragment;

public class NodeFragment extends SherlockWebViewFragment implements
		LoaderCallbacks<Cursor>, Receiver {

	// ID of the current node
	int nid;

	// Body of the node
	String body;

	// Used to receive info from NodeService
	private DetachableResultReceiver receiver;

	boolean progressBarVisibility;

	public static NodeFragment newInstance(int nid) {
		NodeFragment f = new NodeFragment();

		Bundle args = new Bundle();
		args.putInt("nid", nid);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nid = getArguments().getInt("nid");

		if (savedInstanceState == null) {
			progressBarVisibility = false;
			receiver = new DetachableResultReceiver(new Handler());
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		getWebView().getSettings().setJavaScriptEnabled(true);
		getWebView().getSettings().setDefaultFontSize(14);
		return getWebView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
				progressBarVisibility);

		setHasOptionsMenu(true);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = NodeProvider.NODES_URI;
		String where = "nid = ?";
		String whereArgs[] = { String.valueOf(nid) };

		return new CursorLoader(getActivity(), baseUri, new String[] { "title",
				"comment_count", "created", "body", "path", "link" }, where,
				whereArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() > 0) {
			data.moveToFirst();
			String body = data.getString(data.getColumnIndex("body"));
			getWebView().loadDataWithBaseURL(null, body, "text/html", "UTF-8",
					null);
		} else {
			NodeService.refreshNode(nid, getActivity(), receiver);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

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
			break;
		case NodeIndexService.STATUS_ERROR:
			Toast.makeText(getActivity(), "Error pulling content from MGoBlog",
					Toast.LENGTH_SHORT).show();
			break;
		default:

		}
	}

}
