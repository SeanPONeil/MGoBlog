package com.atami.mgodroid.views;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atami.mgodroid.provider.NodeProvider;
import com.atami.mgodroid.util.BusProvider;

public class NodeTitleFragment extends Fragment implements LoaderCallbacks<Cursor>{
	
	int nid;
	String title;
	String comment_count;
	
	TextView titleView;
	TextView commentCountView;
	
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
		title = new String();
		comment_count = new String();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(android.R.layout.simple_list_item_2, null);
		titleView = (TextView) v.findViewById(android.R.id.text1);
		commentCountView = (TextView) v.findViewById(android.R.id.text2);
		
		titleView.setText(title);
		commentCountView.setText(comment_count);
		
		return v;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		BusProvider.getInstance().unregister(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		BusProvider.getInstance().register(getActivity());
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = NodeProvider.CONTENT_URI;
		String where = "nid = ?";
		String whereArgs[] = { String.valueOf(nid) };

		return new CursorLoader(getActivity(), baseUri, new String[] { "body" }, where,
				whereArgs, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() > 0) {
			data.moveToFirst();
			title = data.getString(data.getColumnIndex("title"));
			comment_count = data.getString(data.getColumnIndex("comment_count"));
			
		} else {
			//NodeService.refreshNode(nid, getActivity(), receiver);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

}
