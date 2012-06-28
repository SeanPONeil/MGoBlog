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
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atami.mgodroid.R;
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
	
	ProgressBar mProgressBar;

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
			receiver = new DetachableResultReceiver(new Handler());
		} else {
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
		savedInstanceState.putParcelable("receiver", receiver);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.node, container, false);
		setWebView((WebView) view.findViewById(R.id.node_webview));
		
		getWebView().getSettings().setJavaScriptEnabled(true);
		getWebView().getSettings().setDefaultFontSize(14);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
		case NodeService.STATUS_RUNNING:
			mProgressBar.setVisibility(View.VISIBLE);
			break;
		case NodeService.STATUS_COMPLETE:
			mProgressBar.setVisibility(View.GONE);
			break;
		case NodeService.STATUS_ERROR:
			Toast.makeText(getActivity(), "Error pulling content from MGoBlog",
					Toast.LENGTH_SHORT).show();
			mProgressBar.setVisibility(View.GONE);
			break;
		default:

		}
	}

}
