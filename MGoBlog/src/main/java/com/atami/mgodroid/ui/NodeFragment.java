//package com.atami.mgodroid.views;
//
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.LoaderManager.LoaderCallbacks;
//import android.support.v4.content.CursorLoader;
//import android.support.v4.content.Loader;
//import android.text.format.DateUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.Toast;
//
//import com.atami.mgodroid.io.StatusEvents.NodeStatus;
//import com.atami.mgodroid.io.StatusEvents.Status;
//import com.atami.mgodroid.util.BusProvider;
//import com.atami.mgodroid.util.SherlockWebViewFragment;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//import com.squareup.otto.Subscribe;
//
//public class NodeFragment extends SherlockWebViewFragment implements
//		LoaderCallbacks<Cursor>, OnRefreshListener<WebView> {
//
//	// ID of the current node
//	int nid;
//
//	String body;
//
//	public static NodeFragment newInstance(int nid) {
//		NodeFragment f = new NodeFragment();
//
//		Bundle args = new Bundle();
//		args.putInt("nid", nid);
//		f.setArguments(args);
//
//		return f;
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		nid = getArguments().getInt("nid");
//		body = new String();
//
//		if (savedInstanceState == null) {
//			//NodeService.refreshNode(nid, getSherlockActivity());
//		} else {
//		}
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		BusProvider.getInstance().unregister(this);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		getPullToRefreshWebView().onRefreshComplete();
//		BusProvider.getInstance().register(this);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		super.onCreateView(inflater, container, savedInstanceState);
//
//		getWebView().getSettings().setJavaScriptEnabled(true);
//		getWebView().getSettings().setDefaultFontSize(14);
//
//		getWebView()
//				.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);
//		return getPullToRefreshWebView();
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		setHasOptionsMenu(true);
//		getPullToRefreshWebView().setOnRefreshListener(this);
//		getLoaderManager().initLoader(0, null, this);
//	}
//
//	@Override
//	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//		Uri baseUri = NodeProvider.CONTENT_URI;
//		String where = "nid = ?";
//		String whereArgs[] = { String.valueOf(nid) };
//
//		return new CursorLoader(getActivity(), baseUri, new String[] { "body",
//				"title", "comment_count" }, where, whereArgs, null);
//	}
//
//	@Override
//	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//		if (data.getCount() > 0) {
//			data.moveToFirst();
//			body = data.getString(data.getColumnIndex("body"));
//			getWebView().loadDataWithBaseURL(null, body, "text/html", "UTF-8",
//					null);
//
//			// Set title and comment count in action bar
//			getSherlockActivity().getSupportActionBar().setTitle(
//					data.getString(data.getColumnIndex("title")));
//			getSherlockActivity().getSupportActionBar().setSubtitle(
//					data.getString(data.getColumnIndex("comment_count")));
//		} else {
//			// NodeService.refreshNode(nid, getActivity(), receiver);
//		}
//	}
//
//	@Override
//	public void onLoaderReset(Loader<Cursor> loader) {
//		body = null;
//	}
//
//	@Override
//	public void onRefresh(PullToRefreshBase<WebView> refreshView) {
//		getPullToRefreshWebView().setLastUpdatedLabel(
//				DateUtils.formatDateTime(getActivity(),
//						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//								| DateUtils.FORMAT_SHOW_DATE
//								| DateUtils.FORMAT_ABBREV_ALL));
//
//		NodeService.refreshNode(nid, getSherlockActivity());
//	}
//
//	@Subscribe
//	public void onNewStatusEvent(final NodeStatus s) {
//		if (nid == s.nid) {
//			switch (s.code) {
//			case Status.RUNNING:
//				Log.d("debug", "received running event");
//				getPullToRefreshWebView().setRefreshing();
//				break;
//			case Status.COMPLETE:
//				Log.d("debug", "received complete event");
//				getPullToRefreshWebView().onRefreshComplete();
//				break;
//			case Status.ERROR:
//				Toast.makeText(getActivity(),
//						"Error pulling content from MGoBlog",
//						Toast.LENGTH_SHORT).show();
//				getPullToRefreshWebView().onRefreshComplete();
//				break;
//			default:
//
//			}
//		}
//	}
//
//}
